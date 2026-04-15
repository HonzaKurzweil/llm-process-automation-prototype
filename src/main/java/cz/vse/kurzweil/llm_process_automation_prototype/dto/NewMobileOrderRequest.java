package cz.vse.kurzweil.llm_process_automation_prototype.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

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

        // ── required ─────────────────────────────────────────────────────────────
        @JsonProperty("customer_status")
        CustomerStatus customerStatus,

        @JsonProperty("customer_name")
        String customerName,

        @JsonProperty("contact_phone")
        String contactPhone,

        @JsonProperty("requested_services")
        List<ServiceRequest> requestedServices,

        /** Allowed values per service catalog: 0 (no commitment) or 24 months. */
        @JsonProperty("contract_term_months")
        int contractTermMonths,

        @JsonProperty("porting_requested")
        boolean portingRequested,

        // ── conditional required (when portingRequested == true) ─────────────────
        @JsonProperty("ported_numbers")
        List<PortedNumber> portedNumbers,

        // ── optional ─────────────────────────────────────────────────────────────
        @JsonProperty("contact_email")
        String contactEmail,

        @JsonProperty("requested_discounts")
        List<DiscountRequest> requestedDiscounts,

        @JsonProperty("notes")
        String notes

) {}
