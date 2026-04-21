package cz.vse.kurzweil.llm_process_automation_prototype.service.execution;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import cz.vse.kurzweil.llm_process_automation_prototype.dto.ModelType;
import cz.vse.kurzweil.llm_process_automation_prototype.dto.PromptVariant;
import cz.vse.kurzweil.llm_process_automation_prototype.dto.RequestType;
import cz.vse.kurzweil.llm_process_automation_prototype.service.execution.components.TreeComparator;
import cz.vse.kurzweil.llm_process_automation_prototype.service.execution.dto.*;
import cz.vse.kurzweil.llm_process_automation_prototype.service.extraction.StructuredExtractionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import static cz.vse.kurzweil.llm_process_automation_prototype.utils.Constants.EXTRACTION_TYPE;
import static cz.vse.kurzweil.llm_process_automation_prototype.utils.TextUtils.elapsedMillis;
import static cz.vse.kurzweil.llm_process_automation_prototype.utils.TextUtils.sanitize;

@RequiredArgsConstructor
@Slf4j
@Service
public class ExecutionServiceImpl implements ExecutionService {

    private final TreeComparator treeComparator;

    private static final List<String> ARRAY_SORT_KEYS = List.of(
            "serviceId",
            "productId",
            "discountId",
            "targetServiceId",
            "label",
            "number",
            "donorOperator"
    );

    private final StructuredExtractionService structuredExtractionService;
    private final ObjectMapper objectMapper = new ObjectMapper();


    @Override
    public void validateExtractionService(Path inputFile, PromptVariant variant, ModelType model) {
        try {
            ExtractionValidationRunResult runResult = runExtractionValidation(inputFile, variant, model);
            Path outputFile = buildOutputPath(inputFile, variant, model);
            writeRunResult(outputFile, runResult);
        } catch (Exception e) {
            log.error("Extraction validation failed", e);
            throw new RuntimeException(e);
        }
    }

    private ExtractionValidationRunResult runExtractionValidation(Path inputFile, PromptVariant variant, ModelType model) {
        ExtractionDatasetBundle bundle = readBundle(inputFile);
        List<ExtractionValidationRecordResult> recordResults = getBundleRecordsInExtractionMode(bundle).stream()
                .map(record -> validateSingleRecord(record, variant, model))
                .toList();

        return new ExtractionValidationRunResult(
                Instant.now().toString(),
                inputFile.toAbsolutePath().toString(),
                bundle.datasetVersion(),
                variant.name(),
                model.getModelId(),
                recordResults
        );
    }

    private @NonNull List<ExtractionRecord> getBundleRecordsInExtractionMode(ExtractionDatasetBundle bundle) {
        return bundle.records().stream()
                .filter(record -> EXTRACTION_TYPE.equalsIgnoreCase(record.mode()))
                .toList();
    }

    private ExtractionValidationRecordResult validateSingleRecord(
            ExtractionRecord record,
            PromptVariant variant,
            ModelType model
    ) {
        RequestType requestType = RequestType.fromRequestTypeIdReference(record.source().requestTypeId());

        if (requestType == RequestType.UNCLASSIFIABLE || requestType.getDtoClass() == null) {
            return ExtractionValidationRecordResult.failureWithoutInvocation(
                    record.recordId(),
                    requestType.name(),
                    record.channel(),
                    record.channelStyle(),
                    record.noiseTags(),
                    record.businessPerturbationTags(),
                    record.source().referenceId(),
                    record.source().referenceKind(),
                    variant.name(),
                    model.getModelId(),
                    "Dataset record does not map to a concrete extraction DTO class."
            );
        }

        Object expectedDto = materializeExpectedDto(record, requestType.getDtoClass());

        long startedAtNanos = System.nanoTime();
        Object actualDto;
        try {
            actualDto = invokeExtraction(record.inputText(), requestType, variant, model);
        } catch (Exception exception) {
            long durationMillis = elapsedMillis(startedAtNanos);
            return ExtractionValidationRecordResult.failureAfterInvocation(
                    record.recordId(),
                    requestType.getRequestTypeIdReference(),
                    record.channel(),
                    record.channelStyle(),
                    record.noiseTags(),
                    record.businessPerturbationTags(),
                    record.source().referenceId(),
                    record.source().referenceKind(),
                    variant.name(),
                    model.getModelId(),
                    durationMillis,
                    record.goldAnnotation().extraction().missingRequiredFields(),
                    record.goldAnnotation().extraction().missingRequiredPaths(),
                    record.goldAnnotation().extraction().expectedRuleViolations(),
                    safeTree(expectedDto),
                    exception.getClass().getSimpleName() + ": " + exception.getMessage()
            );
        }

        long durationMillis = elapsedMillis(startedAtNanos);

        JsonNode expectedTree = canonicalize(safeTree(expectedDto));
        JsonNode actualTree = canonicalize(safeTree(actualDto));

        ComparisonResult comparisonResult = treeComparator.compareTrees(expectedTree, actualTree);

        return new ExtractionValidationRecordResult(
                record.recordId(),
                requestType.getRequestTypeIdReference(),
                record.channel(),
                record.channelStyle(),
                record.noiseTags(),
                record.businessPerturbationTags(),
                record.source().referenceId(),
                record.source().referenceKind(),
                variant.name(),
                model.getModelId(),
                true,
                comparisonResult.exactMatch(),
                durationMillis,
                record.goldAnnotation().extraction().missingRequiredFields(),
                record.goldAnnotation().extraction().missingRequiredPaths(),
                record.goldAnnotation().extraction().expectedRuleViolations(),
                comparisonResult.totalComparedPaths(),
                comparisonResult.matchedPaths(),
                comparisonResult.matchRate(),
                comparisonResult.differences(),
                expectedTree,
                actualTree,
                null
        );
    }

    private Object invokeExtraction(String inputText, RequestType requestType, PromptVariant variant, ModelType model) {
        return structuredExtractionService.extract(inputText, requestType, variant, model);
    }

    private Object materializeExpectedDto(ExtractionRecord record, Class<?> dtoClass) {
        Map<String, Object> extractedFields = record.goldAnnotation().extraction().extractedFields();
        try {
            return objectMapper.convertValue(extractedFields, dtoClass);
        } catch (IllegalArgumentException exception) {
            throw new IllegalStateException(
                    "Failed to materialize expected DTO for record " + record.recordId() + " into " + dtoClass.getSimpleName(),
                    exception
            );
        }
    }

    private ExtractionDatasetBundle readBundle(Path inputFile) {
        try {
            return objectMapper.readValue(inputFile.toFile(), ExtractionDatasetBundle.class);
        } catch (IOException exception) {
            throw new UncheckedIOException("Failed to read extraction bundle: " + inputFile, exception);
        }
    }

    private JsonNode safeTree(Object value) {
        return objectMapper.valueToTree(value);
    }


    private JsonNode canonicalize(JsonNode node) {
        if (node == null || node.isNull() || node.isValueNode()) {
            return node;
        }

        if (node.isObject()) {
            ObjectNode objectNode = objectMapper.createObjectNode();
            List<String> fieldNames = new ArrayList<>();
            node.fieldNames().forEachRemaining(fieldNames::add);
            fieldNames.stream().sorted().forEach(field -> objectNode.set(field, canonicalize(node.get(field))));
            return objectNode;
        }

        if (node.isArray()) {
            List<JsonNode> items = new ArrayList<>();
            node.forEach(item -> items.add(canonicalize(item)));

            Comparator<JsonNode> comparator = buildArrayComparator(items);
            if (comparator != null) {
                items.sort(comparator);
            }

            ArrayNode arrayNode = objectMapper.createArrayNode();
            items.forEach(arrayNode::add);
            return arrayNode;
        }

        return node;
    }

    private Comparator<JsonNode> buildArrayComparator(List<JsonNode> items) {
        if (items.isEmpty()) {
            return null;
        }

        if (items.stream().allMatch(JsonNode::isValueNode)) {
            return Comparator.comparing(JsonNode::asText, Comparator.nullsFirst(String::compareTo));
        }

        if (!items.stream().allMatch(JsonNode::isObject)) {
            return null;
        }

        String sortKey = ARRAY_SORT_KEYS.stream()
                .filter(candidate -> items.stream().allMatch(item -> item.has(candidate)))
                .findFirst()
                .orElse(null);

        if (sortKey == null) {
            return Comparator.comparing(JsonNode::toString);
        }

        return Comparator.comparing(item -> item.path(sortKey).asText(""), Comparator.nullsFirst(String::compareTo));
    }

    private Path buildOutputPath(Path inputFile, PromptVariant variant, ModelType model) {
        String inputFileName = inputFile.getFileName().toString();
        String baseName = inputFileName.endsWith(".json")
                ? inputFileName.substring(0, inputFileName.length() - 5)
                : inputFileName;

        Path resultsDirectory = inputFile.getParent() == null
                ? Path.of("results")
                : inputFile.getParent().resolveSibling("results");

        try {
            Files.createDirectories(resultsDirectory);
        } catch (IOException exception) {
            throw new UncheckedIOException("Failed to create results directory: " + resultsDirectory, exception);
        }

        String outputFileName = baseName
                + "__validation__"
                + variant.name().toLowerCase()
                + "__"
                + sanitize(model.getModelId())
                + ".json";

        return resultsDirectory.resolve(outputFileName);
    }

    private void writeRunResult(Path outputFile, ExtractionValidationRunResult runResult) {
        try {
            objectMapper.copy()
                    .enable(SerializationFeature.INDENT_OUTPUT)
                    .writeValue(outputFile.toFile(), runResult);
        } catch (IOException exception) {
            throw new UncheckedIOException("Failed to write validation result to " + outputFile, exception);
        }
    }
}
