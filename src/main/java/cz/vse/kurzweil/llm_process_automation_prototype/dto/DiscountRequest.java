package cz.vse.kurzweil.llm_process_automation_prototype.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/** One requested discount by catalog ID. */
public record DiscountRequest(
        @JsonProperty("discount_id") String discountId
) {}
