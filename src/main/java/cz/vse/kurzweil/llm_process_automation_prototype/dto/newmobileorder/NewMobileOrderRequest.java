package cz.vse.kurzweil.llm_process_automation_prototype.dto.newmobileorder;

import com.fasterxml.jackson.annotation.JsonProperty;
import cz.vse.kurzweil.llm_process_automation_prototype.dto.CustomerStatus;
import cz.vse.kurzweil.llm_process_automation_prototype.dto.DiscountRequest;
import cz.vse.kurzweil.llm_process_automation_prototype.dto.PortedNumber;
import cz.vse.kurzweil.llm_process_automation_prototype.dto.ServiceRequest;

import java.util.List;

/**
 * Request DTO for rt_new_mobile_order — a single mobile service order for a new or existing customer,
 * optionally with number porting.
 *
 * Required fields:        customer_status, customer_name, contact_phone,
 *                         requested_services, contract_term_months, porting_requested
 * Conditional required:   ported_numbers  (when porting_requested == true)
 * Optional fields:        contact_email, requested_discounts, notes
 */
public record NewMobileOrderRequest(

        CustomerStatus customerStatus,

        String customerName,

        String contactPhone,

        List<ServiceRequest> requestedServices,

        Integer contractTermMonths,

        Boolean portingRequested,

        List<PortedNumber> portedNumbers,

        String contactEmail,

        List<DiscountRequest> requestedDiscounts,

        String notes

) {}
