package cz.vse.kurzweil.llm_process_automation_prototype.dto.request_types.internettvbundle;

import cz.vse.kurzweil.llm_process_automation_prototype.dto.request_types.CustomerStatus;
import cz.vse.kurzweil.llm_process_automation_prototype.dto.request_types.DiscountSelectionDto;
import cz.vse.kurzweil.llm_process_automation_prototype.dto.request_types.ProductSelectionDto;
import cz.vse.kurzweil.llm_process_automation_prototype.dto.request_types.ServiceSelectionDto;

import java.util.List;

public record InternetTvBundleOrderRequestDto(
        CustomerStatus customerStatus,
        String customerName,
        String contactPhone,
        String contactEmail,
        String installationAddress,
        List<ServiceSelectionDto> requestedServices,
        List<ProductSelectionDto> requestedProducts,
        Integer contractTermMonths,
        List<DiscountSelectionDto> requestedDiscounts
) {
}
