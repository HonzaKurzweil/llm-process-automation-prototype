package cz.vse.kurzweil.llm_process_automation_prototype.dto.fixedinternetorder;

import cz.vse.kurzweil.llm_process_automation_prototype.dto.CustomerStatus;
import cz.vse.kurzweil.llm_process_automation_prototype.dto.ProductSelectionDto;
import cz.vse.kurzweil.llm_process_automation_prototype.dto.ServiceSelectionDto;

import java.util.List;

public record FixedInternetHardwareOrderRequestDto(
        CustomerStatus customerStatus,
        String customerName,
        String contactPhone,
        String contactEmail,
        String installationAddress,
        List<ServiceSelectionDto> requestedServices,
        List<ProductSelectionDto> requestedProducts,
        Integer contractTermMonths
) {}
