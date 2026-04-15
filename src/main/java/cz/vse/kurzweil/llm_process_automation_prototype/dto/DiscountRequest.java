package cz.vse.kurzweil.llm_process_automation_prototype.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * One requested discount by catalog ID.
 * Allowed discount_id value for rt_new_mobile_order: disc_porting_mobile_200.
 */
public record DiscountRequest(
        @JsonProperty("discount_id") String discountId
) {}
