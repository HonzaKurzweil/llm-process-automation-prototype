package cz.vse.kurzweil.llm_process_automation_prototype.dto.request_types.retentiondiscount;

import cz.vse.kurzweil.llm_process_automation_prototype.dto.request_types.CurrentServiceRefDto;
import cz.vse.kurzweil.llm_process_automation_prototype.dto.request_types.CustomerStatus;
import cz.vse.kurzweil.llm_process_automation_prototype.dto.request_types.DiscountSelectionDto;

import java.util.List;

public record RetentionDiscountRequestDto(
        CustomerStatus customerStatus,
        String customerName,
        String contactPhone,
        Boolean retentionCase,
        List<CurrentServiceRefDto> currentServices,
        String targetServiceId,
        List<DiscountSelectionDto> requestedDiscounts,
        String churnReason,
        String competitorOffer
) {
}
