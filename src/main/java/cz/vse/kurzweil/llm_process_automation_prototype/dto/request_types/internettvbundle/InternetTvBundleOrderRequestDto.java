package cz.vse.kurzweil.llm_process_automation_prototype.dto.request_types.internettvbundle;

import cz.vse.kurzweil.llm_process_automation_prototype.dto.request_types.CustomerStatus;

import java.util.List;

public record InternetTvBundleOrderRequestDto(
        CustomerStatus customerStatus,
        String customerName,
        String contactPhone,
        String contactEmail,
        String installationAddressId,
        String internetServiceId,
        String tvServiceId,
        String routerProductId,
        Integer setTopBoxQuantity,
        Boolean sportsPackRequested,
        Integer contractTermMonths,
        List<String> requestedDiscountIds
) {
}
