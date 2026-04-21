package cz.vse.kurzweil.llm_process_automation_prototype.service.execution.dto;

import java.util.List;

public record ExtractionValidationRunResult(
        String executedAtUtc,
        String inputFile,
        Integer datasetVersion,
        String promptVariant,
        String modelId,
        RunSummary summary,
        List<ExtractionValidationRecordResult> records
) {
}
