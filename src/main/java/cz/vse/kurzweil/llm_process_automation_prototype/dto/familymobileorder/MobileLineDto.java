package cz.vse.kurzweil.llm_process_automation_prototype.dto.familymobileorder;

public record MobileLineDto(
        String label,
        String planServiceId,
        Boolean portingRequested
) {}
