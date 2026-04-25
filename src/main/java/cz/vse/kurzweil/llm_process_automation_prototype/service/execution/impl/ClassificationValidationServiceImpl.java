package cz.vse.kurzweil.llm_process_automation_prototype.service.execution.impl;

import cz.vse.kurzweil.llm_process_automation_prototype.dto.ModelType;
import cz.vse.kurzweil.llm_process_automation_prototype.dto.PromptVariant;
import cz.vse.kurzweil.llm_process_automation_prototype.dto.RequestType;
import cz.vse.kurzweil.llm_process_automation_prototype.service.classification.ClassificationResult;
import cz.vse.kurzweil.llm_process_automation_prototype.service.classification.ClassificationService;
import cz.vse.kurzweil.llm_process_automation_prototype.service.execution.ClassificationValidationService;
import cz.vse.kurzweil.llm_process_automation_prototype.service.execution.components.ExtractionDataSetBundleReader;
import cz.vse.kurzweil.llm_process_automation_prototype.service.execution.components.ResultExporter;
import cz.vse.kurzweil.llm_process_automation_prototype.service.execution.dto.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Service
public class ClassificationValidationServiceImpl implements ClassificationValidationService {

    private final ExtractionDataSetBundleReader bundleReader;
    private final ResultExporter resultExporter;
    private final ClassificationService classificationService;

    @Override
    public void validateClassificationService(Path inputFile, PromptVariant variant, ModelType model) {
        try {
            ClassificationValidationRunResult runResult = runClassificationValidation(inputFile, variant, model);
            resultExporter.exportClassificationResults(inputFile, variant, model, runResult);
        } catch (Exception e) {
            log.error("Classification validation failed", e);
            throw new RuntimeException(e);
        }
    }

    private ClassificationValidationRunResult runClassificationValidation(Path inputFile, PromptVariant variant, ModelType model) {
        List<ExtractionRecord> records = bundleReader.read(inputFile);
        List<ClassificationValidationRecordResult> recordResults = getClassifiableRecords(records).stream()
                .map(record -> evaluateRecord(record, variant, model))
                .toList();
        return new ClassificationValidationRunResult(
                inputFile.toAbsolutePath().toString(),
                variant.name(),
                model.getModelId(),
                recordResults
        );
    }

    private @NonNull List<ExtractionRecord> getClassifiableRecords(List<ExtractionRecord> records) {
        return records.stream()
                .filter(r -> r.expectedClassification() != null)
                .toList();
    }

    private ClassificationValidationRecordResult evaluateRecord(ExtractionRecord record, PromptVariant variant, ModelType model) {
        ExpectedClassification expected = record.expectedClassification();
        String expectedRequestTypeId = expected.requestTypeId();
        ClassificationResult result;
        try {
            result = classificationService.classify(record.inputText(), variant, model);
        } catch (Exception exception) {
            return ClassificationValidationRecordResult.failure(
                    record.recordId(),
                    record.channel(),
                    record.mode(),
                    record.noiseTags(),
                    variant.name(),
                    model.getModelId(),
                    expectedRequestTypeId,
                    exception.getClass().getSimpleName() + ": " + exception.getMessage()
            );
        }
        RequestType actualRequestType = result.requestType();
        String actualRequestTypeId = actualRequestType == RequestType.UNCLASSIFIABLE
                ? null : actualRequestType.getRequestTypeIdReference();
        boolean correct = expectedRequestTypeId != null && expectedRequestTypeId.equals(actualRequestTypeId);
        return new ClassificationValidationRecordResult(
                record.recordId(),
                record.channel(),
                record.mode(),
                record.noiseTags(),
                variant.name(),
                model.getModelId(),
                true,
                expectedRequestTypeId,
                actualRequestTypeId,
                correct,
                result.promptTokens(),
                result.completionTokens(),
                null
        );
    }
}
