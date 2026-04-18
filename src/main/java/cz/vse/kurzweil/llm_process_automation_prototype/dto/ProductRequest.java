package cz.vse.kurzweil.llm_process_automation_prototype.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ProductRequest(
        @JsonProperty("product_id") String productId,
        @JsonProperty("quantity") Integer quantity
) {}
