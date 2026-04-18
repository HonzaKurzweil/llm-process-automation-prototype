package cz.vse.kurzweil.llm_process_automation_prototype.dto.fixedinternetorder;

import com.fasterxml.jackson.annotation.JsonProperty;
import cz.vse.kurzweil.llm_process_automation_prototype.dto.CustomerStatus;
import cz.vse.kurzweil.llm_process_automation_prototype.dto.DiscountRequest;
import cz.vse.kurzweil.llm_process_automation_prototype.dto.InstallationAddress;
import cz.vse.kurzweil.llm_process_automation_prototype.dto.ProductRequest;
import cz.vse.kurzweil.llm_process_automation_prototype.dto.ServiceRequest;

import java.util.List;

/**
 * Request DTO for rt_fixed_internet_with_hardware_order — fixed internet service including router,
 * mesh node, or installation add-on.
 *
 * Required fields:  customer_status, customer_name, contact_phone, installation_address,
 *                   requested_services, requested_products, contract_term_months
 * Optional fields:  contact_email, requested_discounts, notes
 */
public record FixedInternetWithHardwareOrderRequest(

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

        @JsonProperty("requested_products")
        List<ProductRequest> requestedProducts,

        @JsonProperty("contract_term_months")
        Integer contractTermMonths,

        @JsonProperty("contact_email")
        String contactEmail,

        @JsonProperty("requested_discounts")
        List<DiscountRequest> requestedDiscounts,

        @JsonProperty("notes")
        String notes

) {}
