package cz.vse.kurzweil.llm_process_automation_prototype.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * One entry in requested_services: a service catalog ID and the desired quantity.
 * Allowed service_id values for rt_new_mobile_order: svc_mobile_start_5g, svc_mobile_unlimited_5g.
 */
public record ServiceRequest(
        @JsonProperty("service_id") String serviceId,
        @JsonProperty("quantity") Integer quantity
) {}
