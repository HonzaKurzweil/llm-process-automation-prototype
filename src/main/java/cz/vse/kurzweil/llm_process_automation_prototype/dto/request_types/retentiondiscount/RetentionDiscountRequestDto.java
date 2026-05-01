package cz.vse.kurzweil.llm_process_automation_prototype.dto.request_types.retentiondiscount;

import cz.vse.kurzweil.llm_process_automation_prototype.dto.request_types.CustomerStatus;

import java.util.List;

public record RetentionDiscountRequestDto(
        CustomerStatus customerStatus,
        String customerName,
        String contactPhone,
        Boolean retentionCase,
        List<String> currentServiceIds,
        String targetServiceId,
        List<String> requestedDiscountIds
) {
}
