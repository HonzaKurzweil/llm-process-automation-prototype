package cz.vse.kurzweil.llm_process_automation_prototype.dto.retentiondiscount;

import cz.vse.kurzweil.llm_process_automation_prototype.dto.CurrentServiceRefDto;
import cz.vse.kurzweil.llm_process_automation_prototype.dto.CustomerStatus;
import cz.vse.kurzweil.llm_process_automation_prototype.dto.DiscountSelectionDto;

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
) {}
