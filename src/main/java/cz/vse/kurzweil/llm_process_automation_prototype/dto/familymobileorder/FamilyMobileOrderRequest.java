package cz.vse.kurzweil.llm_process_automation_prototype.dto.familymobileorder;

import com.fasterxml.jackson.annotation.JsonProperty;
import cz.vse.kurzweil.llm_process_automation_prototype.dto.CustomerStatus;
import cz.vse.kurzweil.llm_process_automation_prototype.dto.DiscountRequest;
import cz.vse.kurzweil.llm_process_automation_prototype.dto.PortedNumber;
import cz.vse.kurzweil.llm_process_automation_prototype.dto.ServiceRequest;

import java.util.List;

/**
 * Request DTO for rt_family_mobile_order — a family mobile tariff with multiple lines.
 *
 * Required fields:        customer_status, customer_name, contact_phone,
 *                         requested_services, mobile_lines_count, mobile_lines, contract_term_months
 * Conditional required:   ported_numbers  (when any mobile_lines[].porting_requested == true)
 * Optional fields:        contact_email, requested_discounts, notes
 */
public record FamilyMobileOrderRequest(

        @JsonProperty("customer_status")
        CustomerStatus customerStatus,

        @JsonProperty("customer_name")
        String customerName,

        @JsonProperty("contact_phone")
        String contactPhone,

        @JsonProperty("requested_services")
        List<ServiceRequest> requestedServices,

        @JsonProperty("mobile_lines_count")
        Integer mobileLinesCount,

        @JsonProperty("mobile_lines")
        List<MobileLine> mobileLines,

        @JsonProperty("contract_term_months")
        Integer contractTermMonths,

        // conditional required (when any mobileLines[].portingRequested == true)
        @JsonProperty("ported_numbers")
        List<PortedNumber> portedNumbers,

        @JsonProperty("contact_email")
        String contactEmail,

        @JsonProperty("requested_discounts")
        List<DiscountRequest> requestedDiscounts,

        @JsonProperty("notes")
        String notes

) {}
