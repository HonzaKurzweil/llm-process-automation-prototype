package cz.vse.kurzweil.llm_process_automation_prototype.dto.internettvbundle;

import com.fasterxml.jackson.annotation.JsonProperty;
import cz.vse.kurzweil.llm_process_automation_prototype.dto.CustomerStatus;
import cz.vse.kurzweil.llm_process_automation_prototype.dto.DiscountRequest;
import cz.vse.kurzweil.llm_process_automation_prototype.dto.InstallationAddress;
import cz.vse.kurzweil.llm_process_automation_prototype.dto.ProductRequest;
import cz.vse.kurzweil.llm_process_automation_prototype.dto.ServiceRequest;

import java.util.List;

/**
 * Request DTO for rt_internet_tv_bundle_order — combined fixed internet and TV service,
 * optionally with TV add-ons and a bundle discount.
 *
 * Required fields:  customer_status, customer_name, contact_phone, installation_address,
 *                   requested_services, contract_term_months
 * Optional fields:  requested_products, requested_discounts, contact_email, notes
 */
public record InternetTvBundleOrderRequest(

        @JsonProperty("customer_status")
        CustomerStatus customerStatus,

        @JsonProperty("customer_name")
        String customerName,

        @JsonProperty("contact_phone")
        String contactPhone,

        @JsonProperty("installation_address")
        InstallationAddress installationAddress,

        @JsonProperty("requested_services")
        List<ServiceRequest> requestedServices,

        @JsonProperty("contract_term_months")
        Integer contractTermMonths,

        @JsonProperty("requested_products")
        List<ProductRequest> requestedProducts,

        @JsonProperty("requested_discounts")
        List<DiscountRequest> requestedDiscounts,

        @JsonProperty("contact_email")
        String contactEmail,

        @JsonProperty("notes")
        String notes

) {}
