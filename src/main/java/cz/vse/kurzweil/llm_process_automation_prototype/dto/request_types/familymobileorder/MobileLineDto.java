package cz.vse.kurzweil.llm_process_automation_prototype.dto.request_types.familymobileorder;

import cz.vse.kurzweil.llm_process_automation_prototype.dto.request_types.DonorOperator;

public record MobileLineDto(
        Integer lineIndex,
        MobileLineRole lineRole,
        String planServiceId,
        Boolean portingRequested,
        String portedNumber,
        DonorOperator donorOperator
) {
}
