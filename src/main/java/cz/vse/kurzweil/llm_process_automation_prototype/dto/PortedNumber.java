package cz.vse.kurzweil.llm_process_automation_prototype.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * One number to be ported in. Required when porting_requested is true.
 */
public record PortedNumber(
        @JsonProperty("number") String number,
        @JsonProperty("donor_operator") String donorOperator
) {}
