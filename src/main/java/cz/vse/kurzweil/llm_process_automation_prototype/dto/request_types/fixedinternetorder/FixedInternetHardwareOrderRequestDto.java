package cz.vse.kurzweil.llm_process_automation_prototype.dto.request_types.fixedinternetorder;

import cz.vse.kurzweil.llm_process_automation_prototype.dto.request_types.CustomerStatus;
import cz.vse.kurzweil.llm_process_automation_prototype.dto.request_types.ProductSelectionDto;
import cz.vse.kurzweil.llm_process_automation_prototype.dto.request_types.ServiceSelectionDto;

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
) {
}
