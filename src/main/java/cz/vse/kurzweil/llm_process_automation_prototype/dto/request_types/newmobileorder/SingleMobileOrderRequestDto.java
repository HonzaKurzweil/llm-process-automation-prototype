package cz.vse.kurzweil.llm_process_automation_prototype.dto.request_types.newmobileorder;

import cz.vse.kurzweil.llm_process_automation_prototype.dto.request_types.CustomerStatus;
import cz.vse.kurzweil.llm_process_automation_prototype.dto.request_types.DiscountSelectionDto;
import cz.vse.kurzweil.llm_process_automation_prototype.dto.request_types.PortedNumberDto;
import cz.vse.kurzweil.llm_process_automation_prototype.dto.request_types.ServiceSelectionDto;

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
