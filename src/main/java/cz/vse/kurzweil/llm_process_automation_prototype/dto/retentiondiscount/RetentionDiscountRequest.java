package cz.vse.kurzweil.llm_process_automation_prototype.dto.retentiondiscount;

import com.fasterxml.jackson.annotation.JsonProperty;
import cz.vse.kurzweil.llm_process_automation_prototype.dto.CustomerStatus;
import cz.vse.kurzweil.llm_process_automation_prototype.dto.DiscountRequest;
import cz.vse.kurzweil.llm_process_automation_prototype.dto.ServiceRequest;

import java.util.List;

/**
 * Request DTO for rt_retention_discount_request — negotiation with an existing customer
 * about retaining or modifying a service, with a retention discount request.
 *
 * Required fields:  customer_status, customer_name, contact_phone, retention_case,
 *                   current_services, target_service_id, requested_discounts
 * Optional fields:  churn_reason, competitor_offer, notes
 */
public record RetentionDiscountRequest(

        @JsonProperty("customer_status")
        CustomerStatus customerStatus,

        @JsonProperty("customer_name")
        String customerName,

        @JsonProperty("contact_phone")
        String contactPhone,

        @JsonProperty("retention_case")
        String retentionCase,

        @JsonProperty("current_services")
        List<ServiceRequest> currentServices,

        @JsonProperty("target_service_id")
        String targetServiceId,

        @JsonProperty("requested_discounts")
        List<DiscountRequest> requestedDiscounts,

        @JsonProperty("churn_reason")
        String churnReason,

        @JsonProperty("competitor_offer")
        String competitorOffer,

        @JsonProperty("notes")
        String notes

) {}
