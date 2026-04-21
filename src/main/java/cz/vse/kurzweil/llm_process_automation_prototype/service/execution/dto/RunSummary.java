package cz.vse.kurzweil.llm_process_automation_prototype.service.execution.dto;

import java.util.Map;

public record RunSummary(
        int totalRecords,
        long exactMatches,
        long invocationFailures,
        Map<String, OutcomeCounter> byRequestType,
        Map<String, OutcomeCounter> byChannel,
        Map<String, OutcomeCounter> byReferenceKind,
        Map<String, OutcomeCounter> byPromptVariant,
        Map<String, OutcomeCounter> byModel
) {
}
