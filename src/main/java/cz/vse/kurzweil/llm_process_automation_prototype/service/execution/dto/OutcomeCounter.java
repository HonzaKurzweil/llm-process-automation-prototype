package cz.vse.kurzweil.llm_process_automation_prototype.service.execution.dto;

public record OutcomeCounter(
        long total,
        long exactMatches,
        long invocationFailures
) {
}
