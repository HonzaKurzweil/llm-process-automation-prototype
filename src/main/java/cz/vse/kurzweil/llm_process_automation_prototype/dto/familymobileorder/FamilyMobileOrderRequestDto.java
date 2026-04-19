package cz.vse.kurzweil.llm_process_automation_prototype.dto.familymobileorder;

import cz.vse.kurzweil.llm_process_automation_prototype.dto.CustomerStatus;
import cz.vse.kurzweil.llm_process_automation_prototype.dto.DiscountSelectionDto;
import cz.vse.kurzweil.llm_process_automation_prototype.dto.PortedNumberDto;
import cz.vse.kurzweil.llm_process_automation_prototype.dto.ServiceSelectionDto;

import java.util.List;

public record FamilyMobileOrderRequestDto(
        CustomerStatus customerStatus,
        String customerName,
        String contactPhone,
        String contactEmail,
        List<ServiceSelectionDto> requestedServices,
        Integer mobileLinesCount,
        List<MobileLineDto> mobileLines,
        List<PortedNumberDto> portedNumbers,
        Integer contractTermMonths,
        List<DiscountSelectionDto> requestedDiscounts
) {}
