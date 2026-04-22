package cz.vse.kurzweil.llm_process_automation_prototype.service.execution;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import cz.vse.kurzweil.llm_process_automation_prototype.dto.ModelType;
import cz.vse.kurzweil.llm_process_automation_prototype.dto.PromptVariant;
import cz.vse.kurzweil.llm_process_automation_prototype.dto.RequestType;
import cz.vse.kurzweil.llm_process_automation_prototype.service.execution.components.ResultExporter;
import cz.vse.kurzweil.llm_process_automation_prototype.service.execution.components.TreeComparator;
import cz.vse.kurzweil.llm_process_automation_prototype.service.execution.dto.*;
import cz.vse.kurzweil.llm_process_automation_prototype.service.extraction.StructuredExtractionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Path;
import java.time.Instant;
import java.util.List;
import java.util.Map;

import static cz.vse.kurzweil.llm_process_automation_prototype.utils.Constants.EXTRACTION_TYPE;
import static cz.vse.kurzweil.llm_process_automation_prototype.utils.TextUtils.elapsedMillis;

@RequiredArgsConstructor
@Slf4j
@Service
public class ExecutionServiceImpl implements ExecutionService {

    private final TreeComparator treeComparator;
    private final ResultExporter resultExporter;
    private final StructuredExtractionService structuredExtractionService;
    private final ObjectMapper objectMapper = new ObjectMapper();


    @Override
    public void validateExtractionService(Path inputFile, PromptVariant variant, ModelType model) {
        try {
            ExtractionValidationRunResult runResult = runExtractionValidation(inputFile, variant, model);
            resultExporter.exportResults(inputFile, variant, model, runResult);
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
            return generateUnknownDtoResult(record, variant, model, requestType);
        }

        Object expectedDto = materializeExpectedDto(record, requestType.getDtoClass());

        long startedAtNanos = System.nanoTime();
        Object actualDto;
        try {
            actualDto = invokeExtraction(record.inputText(), requestType, variant, model);
        } catch (Exception exception) {
            return generateExceptionResult(record, variant, model, exception, requestType, elapsedMillis(startedAtNanos), expectedDto);
        }
        long durationMillis = elapsedMillis(startedAtNanos);
        JsonNode expectedTree = treeComparator.canonicalize(safeTree(expectedDto));
        JsonNode actualTree = treeComparator.canonicalize(safeTree(actualDto));
        ComparisonResult comparisonResult = treeComparator.compareTrees(expectedTree, actualTree);
        return generateResult(record, variant, model, requestType, comparisonResult, durationMillis, expectedTree, actualTree);
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

    private static @NonNull ExtractionValidationRecordResult generateResult(ExtractionRecord record,
                                                                            PromptVariant variant,
                                                                            ModelType model,
                                                                            RequestType requestType,
                                                                            ComparisonResult comparisonResult,
                                                                            long durationMillis,
                                                                            JsonNode expectedTree,
                                                                            JsonNode actualTree) {
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

    private @NonNull ExtractionValidationRecordResult generateExceptionResult(ExtractionRecord record,
                                                                              PromptVariant variant,
                                                                              ModelType model, Exception exception,
                                                                              RequestType requestType,
                                                                              long durationMillis,
                                                                              Object expectedDto) {
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

    private @NonNull ExtractionValidationRecordResult generateUnknownDtoResult(ExtractionRecord record,
                                                                               PromptVariant variant,
                                                                               ModelType model,
                                                                               RequestType requestType) {
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
}
