package cz.vse.kurzweil.llm_process_automation_prototype.service.execution;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import cz.vse.kurzweil.llm_process_automation_prototype.dto.ModelType;
import cz.vse.kurzweil.llm_process_automation_prototype.dto.PromptVariant;
import cz.vse.kurzweil.llm_process_automation_prototype.dto.RequestType;
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
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import static cz.vse.kurzweil.llm_process_automation_prototype.utils.Constants.EXTRACTION_TYPE;

@RequiredArgsConstructor
@Slf4j
@Service
public class ExecutionServiceImpl implements ExecutionService {

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

        ComparisonResult comparisonResult = compareTrees(expectedTree, actualTree);

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

    private ComparisonResult compareTrees(JsonNode expectedTree, JsonNode actualTree) {
        List<FieldDifference> differences = new ArrayList<>();
        compareNodes("", expectedTree, actualTree, differences);

        Set<String> comparedPaths = new LinkedHashSet<>();
        collectLeafPaths(expectedTree, "", comparedPaths);
        collectLeafPaths(actualTree, "", comparedPaths);

        Set<String> diffPaths = differences.stream()
                .map(FieldDifference::path)
                .collect(Collectors.toCollection(LinkedHashSet::new));

        int totalComparedPaths = comparedPaths.size();
        int matchedPaths = Math.max(0, totalComparedPaths - diffPaths.size());
        double matchRate = totalComparedPaths == 0 ? 1.0d : ((double) matchedPaths / (double) totalComparedPaths);

        return new ComparisonResult(differences.isEmpty(), totalComparedPaths, matchedPaths, matchRate, differences);
    }

    private void compareNodes(String path, JsonNode expected, JsonNode actual, List<FieldDifference> differences) {
        String normalizedPath = normalizePath(path);

        if (expected == null || expected.isMissingNode()) {
            if (actual != null && !actual.isMissingNode()) {
                differences.add(new FieldDifference(normalizedPath, "unexpected_field", null, actual));
            }
            return;
        }

        if (actual == null || actual.isMissingNode()) {
            differences.add(new FieldDifference(normalizedPath, "missing_field", expected, null));
            return;
        }

        if (expected.isNull() || actual.isNull()) {
            if (!expected.equals(actual)) {
                differences.add(new FieldDifference(normalizedPath, "null_mismatch", expected, actual));
            }
            return;
        }

        if (expected.getNodeType() != actual.getNodeType()) {
            differences.add(new FieldDifference(normalizedPath, "type_mismatch", expected, actual));
            return;
        }

        if (expected.isObject()) {
            Set<String> fieldNames = new TreeSet<>();
            expected.fieldNames().forEachRemaining(fieldNames::add);
            actual.fieldNames().forEachRemaining(fieldNames::add);

            for (String fieldName : fieldNames) {
                compareNodes(appendPath(normalizedPath, fieldName), expected.get(fieldName), actual.get(fieldName), differences);
            }
            return;
        }

        if (expected.isArray()) {
            int maxSize = Math.max(expected.size(), actual.size());
            for (int index = 0; index < maxSize; index++) {
                JsonNode expectedItem = index < expected.size() ? expected.get(index) : null;
                JsonNode actualItem = index < actual.size() ? actual.get(index) : null;
                compareNodes(normalizedPath + "[" + index + "]", expectedItem, actualItem, differences);
            }
            return;
        }

        if (!expected.equals(actual)) {
            differences.add(new FieldDifference(normalizedPath, "value_mismatch", expected, actual));
        }
    }

    private void collectLeafPaths(JsonNode node, String path, Set<String> output) {
        String normalizedPath = normalizePath(path);
        if (node == null || node.isMissingNode()) {
            output.add(normalizedPath);
            return;
        }

        if (node.isValueNode() || node.isNull()) {
            output.add(normalizedPath);
            return;
        }

        if (node.isObject()) {
            if (node.isEmpty()) {
                output.add(normalizedPath);
                return;
            }
            node.fieldNames().forEachRemaining(field -> collectLeafPaths(node.get(field), appendPath(normalizedPath, field), output));
            return;
        }

        if (node.isArray()) {
            if (node.isEmpty()) {
                output.add(normalizedPath);
                return;
            }
            for (int index = 0; index < node.size(); index++) {
                collectLeafPaths(node.get(index), normalizedPath + "[" + index + "]", output);
            }
        }
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

    private long elapsedMillis(long startedAtNanos) {
        return (System.nanoTime() - startedAtNanos) / 1_000_000L;
    }

    private String appendPath(String base, String next) {
        if (base == null || base.isBlank()) {
            return next;
        }
        return base + "." + next;
    }

    private String normalizePath(String path) {
        return (path == null || path.isBlank()) ? "$" : path;
    }

    private String sanitize(String raw) {
        return raw.replaceAll("[^a-zA-Z0-9._-]", "_");
    }
}
