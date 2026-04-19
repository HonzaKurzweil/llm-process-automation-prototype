package cz.vse.kurzweil.llm_process_automation_prototype.dto.newmobileorder;

import cz.vse.kurzweil.llm_process_automation_prototype.dto.CustomerStatus;
import cz.vse.kurzweil.llm_process_automation_prototype.dto.DiscountSelectionDto;
import cz.vse.kurzweil.llm_process_automation_prototype.dto.PortedNumberDto;
import cz.vse.kurzweil.llm_process_automation_prototype.dto.ServiceSelectionDto;

import java.util.List;

public record SingleMobileOrderRequestDto(
        CustomerStatus customerStatus,
        String customerName,
        String contactPhone,
        String contactEmail,
        List<ServiceSelectionDto> requestedServices,
        Integer contractTermMonths,
        Boolean portingRequested,
        List<PortedNumberDto> portedNumbers,
        List<DiscountSelectionDto> requestedDiscounts
) {}
