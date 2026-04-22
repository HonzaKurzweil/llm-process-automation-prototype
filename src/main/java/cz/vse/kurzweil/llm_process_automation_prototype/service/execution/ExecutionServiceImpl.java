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
                .map(record -> evaluateSingleRecord(record, variant, model))
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

    private ExtractionValidationRecordResult evaluateSingleRecord(
            ExtractionRecord record,
            PromptVariant variant,
            ModelType model
    ) {
        RequestType requestType = RequestType.fromRequestTypeIdReference(record.source().requestTypeId());
        RecordExecutionContext ctx = new RecordExecutionContext(record, requestType, variant, model);

        if (requestType == RequestType.UNCLASSIFIABLE || requestType.getDtoClass() == null) {
            return generateUnknownDtoResult(ctx);
        }

        Object expectedDto = materializeExpectedDto(ctx.record(), ctx.requestType().getDtoClass());

        long startedAtNanos = System.nanoTime();
        Object actualDto;
        try {
            actualDto = structuredExtractionService.extract(ctx.record().inputText(), ctx.requestType(), ctx.variant(), ctx.model());
        } catch (Exception exception) {
            return generateExceptionResult(ctx, exception, elapsedMillis(startedAtNanos), expectedDto);
        }
        long durationMillis = elapsedMillis(startedAtNanos);
        JsonNode expectedTree = treeComparator.canonicalize(safeTree(expectedDto));
        JsonNode actualTree = treeComparator.canonicalize(safeTree(actualDto));
        ComparisonResult comparisonResult = treeComparator.compareTrees(expectedTree, actualTree);
        return generateResult(ctx, comparisonResult, durationMillis, expectedTree, actualTree);
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

    private static @NonNull ExtractionValidationRecordResult generateResult(RecordExecutionContext ctx,
                                                                            ComparisonResult comparisonResult,
                                                                            long durationMillis,
                                                                            JsonNode expectedTree,
                                                                            JsonNode actualTree) {
        return new ExtractionValidationRecordResult(
                ctx.record().recordId(),
                ctx.requestType().getRequestTypeIdReference(),
                ctx.record().channel(),
                ctx.record().channelStyle(),
                ctx.record().noiseTags(),
                ctx.record().businessPerturbationTags(),
                ctx.record().source().referenceId(),
                ctx.record().source().referenceKind(),
                ctx.variant().name(),
                ctx.model().getModelId(),
                true,
                comparisonResult.exactMatch(),
                durationMillis,
                ctx.record().goldAnnotation().extraction().missingRequiredFields(),
                ctx.record().goldAnnotation().extraction().missingRequiredPaths(),
                ctx.record().goldAnnotation().extraction().expectedRuleViolations(),
                comparisonResult.totalComparedPaths(),
                comparisonResult.matchedPaths(),
                comparisonResult.matchRate(),
                comparisonResult.differences(),
                expectedTree,
                actualTree,
                null
        );
    }

    private @NonNull ExtractionValidationRecordResult generateExceptionResult(RecordExecutionContext ctx,
                                                                              Exception exception,
                                                                              long durationMillis,
                                                                              Object expectedDto) {
        return ExtractionValidationRecordResult.failureAfterInvocation(
                ctx.record().recordId(),
                ctx.requestType().getRequestTypeIdReference(),
                ctx.record().channel(),
                ctx.record().channelStyle(),
                ctx.record().noiseTags(),
                ctx.record().businessPerturbationTags(),
                ctx.record().source().referenceId(),
                ctx.record().source().referenceKind(),
                ctx.variant().name(),
                ctx.model().getModelId(),
                durationMillis,
                ctx.record().goldAnnotation().extraction().missingRequiredFields(),
                ctx.record().goldAnnotation().extraction().missingRequiredPaths(),
                ctx.record().goldAnnotation().extraction().expectedRuleViolations(),
                safeTree(expectedDto),
                exception.getClass().getSimpleName() + ": " + exception.getMessage()
        );
    }

    private @NonNull ExtractionValidationRecordResult generateUnknownDtoResult(RecordExecutionContext ctx) {
        return ExtractionValidationRecordResult.failureWithoutInvocation(
                ctx.record().recordId(),
                ctx.requestType().name(),
                ctx.record().channel(),
                ctx.record().channelStyle(),
                ctx.record().noiseTags(),
                ctx.record().businessPerturbationTags(),
                ctx.record().source().referenceId(),
                ctx.record().source().referenceKind(),
                ctx.variant().name(),
                ctx.model().getModelId(),
                "Dataset record does not map to a concrete extraction DTO class."
        );
    }
}
