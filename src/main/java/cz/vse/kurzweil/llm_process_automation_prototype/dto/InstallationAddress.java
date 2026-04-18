package cz.vse.kurzweil.llm_process_automation_prototype.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record InstallationAddress(
        @JsonProperty("street") String street,
        @JsonProperty("city") String city,
        @JsonProperty("zip_code") String zipCode
) {}
