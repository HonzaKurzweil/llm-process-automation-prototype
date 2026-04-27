package cz.vse.kurzweil.llm_process_automation_prototype.service.validation.dto;

import java.util.List;

public record ClassificationValidationRunResult(
        String inputFile,
        String promptVariant,
        String modelId,
        List<ClassificationValidationRecordResult> records
) {
}
