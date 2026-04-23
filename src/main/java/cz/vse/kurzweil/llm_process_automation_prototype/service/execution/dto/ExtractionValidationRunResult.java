package cz.vse.kurzweil.llm_process_automation_prototype.service.execution.dto;

import java.util.List;

public record ExtractionValidationRunResult(
        String inputFile,
        String promptVariant,
        String modelId,
        List<ExtractionValidationRecordResult> records
) {
}
