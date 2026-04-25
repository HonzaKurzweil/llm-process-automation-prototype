package cz.vse.kurzweil.llm_process_automation_prototype.service.execution;

import cz.vse.kurzweil.llm_process_automation_prototype.dto.ModelType;
import cz.vse.kurzweil.llm_process_automation_prototype.dto.PromptVariant;
import cz.vse.kurzweil.llm_process_automation_prototype.dto.RequestType;
import cz.vse.kurzweil.llm_process_automation_prototype.service.classification.ClassificationService;
import cz.vse.kurzweil.llm_process_automation_prototype.service.execution.components.ExtractionDataSetBundleReader;
import cz.vse.kurzweil.llm_process_automation_prototype.service.execution.components.ResultExporter;
import cz.vse.kurzweil.llm_process_automation_prototype.service.execution.dto.ClassificationValidationRecordResult;
import cz.vse.kurzweil.llm_process_automation_prototype.service.execution.dto.ClassificationValidationRunResult;
import cz.vse.kurzweil.llm_process_automation_prototype.service.execution.dto.ExtractionRecord;
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
import java.util.Optional;

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
        RequestType expectedRequestType = RequestType.fromRequestTypeIdReference(record.expectedClassification().requestTypeId());
        ResponseEntity<ChatResponse, RequestType> responseEntity;
        try {
            responseEntity = classificationService.classify(record.inputText(), variant, model);
        } catch (Exception exception) {
            return ClassificationValidationRecordResult.failure(
                    record.recordId(),
                    record.channel(),
                    record.mode(),
                    record.noiseTags(),
                    variant.name(),
                    model.getModelId(),
                    expectedRequestType,
                    exception.getClass().getSimpleName() + ": " + exception.getMessage()
            );
        }
        RequestType actualRequestType = responseEntity.entity();
        boolean correct = expectedRequestType == actualRequestType;
        Optional<Usage> usage = Optional.ofNullable(responseEntity.response())
                .map(ChatResponse::getMetadata)
                .map(ChatResponseMetadata::getUsage);
        return new ClassificationValidationRecordResult(
                record.recordId(),
                record.channel(),
                record.mode(),
                record.noiseTags(),
                variant.name(),
                model.getModelId(),
                true,
                expectedRequestType,
                actualRequestType,
                correct,
                usage.map(Usage::getPromptTokens).orElse(0),
                usage.map(Usage::getCompletionTokens).orElse(0),
                null
        );
    }
}
