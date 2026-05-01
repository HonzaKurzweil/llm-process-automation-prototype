package cz.vse.kurzweil.llm_process_automation_prototype.dto.request_types.fixedinternetorder;

import cz.vse.kurzweil.llm_process_automation_prototype.dto.request_types.CustomerStatus;

public record FixedInternetHardwareOrderRequestDto(
        CustomerStatus customerStatus,
        String customerName,
        String contactPhone,
        String contactEmail,
        String installationAddressId,
        String internetServiceId,
        String routerProductId,
        Integer meshNodeQuantity,
        Boolean expressInstallationRequested,
        Integer contractTermMonths
) {
}
