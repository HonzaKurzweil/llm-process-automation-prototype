package cz.vse.kurzweil.llm_process_automation_prototype.service.validation.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import cz.vse.kurzweil.llm_process_automation_prototype.dto.ModelType;
import cz.vse.kurzweil.llm_process_automation_prototype.dto.PromptVariant;
import cz.vse.kurzweil.llm_process_automation_prototype.dto.RequestType;
import cz.vse.kurzweil.llm_process_automation_prototype.service.validation.ExtractionValidationService;
import cz.vse.kurzweil.llm_process_automation_prototype.service.validation.components.ExtractionDataSetBundleReader;
import cz.vse.kurzweil.llm_process_automation_prototype.service.validation.components.ResultExporter;
import cz.vse.kurzweil.llm_process_automation_prototype.service.validation.components.TreeComparator;
import cz.vse.kurzweil.llm_process_automation_prototype.service.execution.dto.*;
import cz.vse.kurzweil.llm_process_automation_prototype.service.extraction.ExtractionService;
import cz.vse.kurzweil.llm_process_automation_prototype.service.validation.dto.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.ai.chat.client.ResponseEntity;
import org.springframework.ai.chat.metadata.ChatResponseMetadata;
import org.springframework.ai.chat.metadata.Usage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
@Service
public class ExtractionValidationServiceImpl implements ExtractionValidationService {

    private final ExtractionDataSetBundleReader bundleReader;
    private final TreeComparator treeComparator;
    private final ResultExporter resultExporter;
    private final ExtractionService extractionService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void validateExtractionService(Path inputFile, PromptVariant variant, ModelType model) {
        try {
            ExtractionValidationRunResult runResult = runExtractionValidation(inputFile, variant, model);
            resultExporter.exportExtractionResults(inputFile, variant, model, runResult);
        } catch (Exception e) {
            log.error("Extraction validation failed", e);
            throw new RuntimeException(e);
        }
    }

    private ExtractionValidationRunResult runExtractionValidation(Path inputFile, PromptVariant variant, ModelType model) {
        List<ExtractionRecord> records = bundleReader.read(inputFile);
        List<ExtractionValidationRecordResult> recordResults = getExtractableRecords(records).stream()
                .flatMap(record -> evaluateRecord(record, variant, model).stream())
                .toList();
        return new ExtractionValidationRunResult(
                inputFile.toAbsolutePath().toString(),
                variant.name(),
                model.getModelId(),
                recordResults
        );
    }

    private @NonNull List<ExtractionRecord> getExtractableRecords(List<ExtractionRecord> records) {
        return records.stream()
                .filter(r -> r.expectedExtractions() != null && !r.expectedExtractions().isEmpty())
                .toList();
    }

    private List<ExtractionValidationRecordResult> evaluateRecord(ExtractionRecord record, PromptVariant variant, ModelType model) {
        return record.expectedExtractions().stream()
                .map(expectation -> evaluateSingleExpectation(record, expectation, variant, model))
                .toList();
    }

    private ExtractionValidationRecordResult evaluateSingleExpectation(
            ExtractionRecord record,
            ExtractionExpectation expectation,
            PromptVariant variant,
            ModelType model
    ) {
        RequestType requestType = RequestType.fromRequestTypeIdReference(expectation.requestTypeId());
        RecordExecutionContext ctx = new RecordExecutionContext(record, expectation, requestType, variant, model);
        if (requestType == RequestType.UNCLASSIFIABLE || requestType.getDtoClass() == null) {
            return generateUnknownDtoResult(ctx);
        }
        Object expectedDto = deserializeExpectedDto(ctx);
        ResponseEntity<ChatResponse, ?> responseEntity;
        try {
            responseEntity = extractionService.extract(record.inputText(), requestType, variant, model);
        } catch (Exception exception) {
            return generateExceptionResult(ctx, expectedDto, exception);
        }
        Object actualDto = responseEntity.entity();
        Optional<Usage> usage = Optional.ofNullable(responseEntity.response())
                .map(ChatResponse::getMetadata)
                .map(ChatResponseMetadata::getUsage);
        int promptTokens = usage.map(Usage::getPromptTokens).orElse(0);
        int completionTokens = usage.map(Usage::getCompletionTokens).orElse(0);
        JsonNode expectedTree = treeComparator.canonicalize(objectMapper.valueToTree(expectedDto));
        JsonNode actualTree = treeComparator.canonicalize(objectMapper.valueToTree(actualDto));
        ComparisonResult comparisonResult = treeComparator.compareTrees(expectedTree, actualTree);
        return generateResult(ctx, comparisonResult, expectedTree, actualTree, promptTokens, completionTokens);
    }

    private Object deserializeExpectedDto(RecordExecutionContext ctx) {
        Map<String, Object> dtoFields = ctx.expectation().dto();
        try {
            return objectMapper.convertValue(dtoFields, ctx.requestType().getDtoClass());
        } catch (IllegalArgumentException exception) {
            throw new IllegalStateException(
                    "Failed to materialize expected DTO for record " + ctx.record().recordId()
                            + " into " + ctx.requestType().getDtoClass().getSimpleName(),
                    exception
            );
        }
    }

    private @NonNull ExtractionValidationRecordResult generateResult(
            RecordExecutionContext ctx,
            ComparisonResult comparison,
            JsonNode expectedTree,
            JsonNode actualTree,
            int promptTokens,
            int completionTokens
    ) {
        return new ExtractionValidationRecordResult(
                ctx.record().recordId(),
                ctx.requestType().getRequestTypeIdReference(),
                ctx.record().channel(),
                ctx.record().mode(),
                ctx.record().noiseTags(),
                ctx.variant().name(),
                ctx.model().getModelId(),
                true,
                comparison.exactMatch(),
                ctx.expectation().missingFieldPaths(),
                comparison.totalComparedPaths(),
                comparison.matchedPaths(),
                comparison.matchRate(),
                comparison.differences(),
                expectedTree,
                actualTree,
                promptTokens,
                completionTokens,
                null
        );
    }

    private @NonNull ExtractionValidationRecordResult generateExceptionResult(
            RecordExecutionContext ctx,
            Object expectedDto,
            Exception exception
    ) {
        return ExtractionValidationRecordResult.failureAfterInvocation(
                ctx.record().recordId(),
                ctx.requestType().getRequestTypeIdReference(),
                ctx.record().channel(),
                ctx.record().mode(),
                ctx.record().noiseTags(),
                ctx.variant().name(),
                ctx.model().getModelId(),
                ctx.expectation().missingFieldPaths(),
                objectMapper.valueToTree(expectedDto),
                exception.getClass().getSimpleName() + ": " + exception.getMessage()
        );
    }

    private @NonNull ExtractionValidationRecordResult generateUnknownDtoResult(RecordExecutionContext ctx) {
        return ExtractionValidationRecordResult.failureWithoutInvocation(
                ctx.record().recordId(),
                ctx.expectation().requestTypeId(),
                ctx.record().channel(),
                ctx.record().mode(),
                ctx.record().noiseTags(),
                ctx.variant().name(),
                ctx.model().getModelId(),
                "Dataset record does not map to a concrete extraction DTO class."
        );
    }
}
