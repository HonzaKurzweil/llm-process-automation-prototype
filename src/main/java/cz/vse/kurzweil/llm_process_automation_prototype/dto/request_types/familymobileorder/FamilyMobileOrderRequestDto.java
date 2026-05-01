package cz.vse.kurzweil.llm_process_automation_prototype.dto.request_types.familymobileorder;

import cz.vse.kurzweil.llm_process_automation_prototype.dto.request_types.CustomerStatus;

import java.util.List;

public record FamilyMobileOrderRequestDto(
        CustomerStatus customerStatus,
        String customerName,
        String contactPhone,
        String contactEmail,
        String familyTariffId,
        Integer mobileLinesCount,
        List<MobileLineDto> mobileLines,
        Integer contractTermMonths,
        List<String> requestedDiscountIds
) {
}
