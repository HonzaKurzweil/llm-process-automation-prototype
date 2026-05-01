package cz.vse.kurzweil.llm_process_automation_prototype.dto.request_types.newmobileorder;

import cz.vse.kurzweil.llm_process_automation_prototype.dto.request_types.CustomerStatus;
import cz.vse.kurzweil.llm_process_automation_prototype.dto.request_types.DonorOperator;

import java.util.List;

public record SingleMobileOrderRequestDto(
        CustomerStatus customerStatus,
        String customerName,
        String contactPhone,
        String contactEmail,
        String mobileTariffId,
        Integer contractTermMonths,
        Boolean portingRequested,
        String portedNumber,
        DonorOperator donorOperator,
        List<String> requestedDiscountIds
) {
}
