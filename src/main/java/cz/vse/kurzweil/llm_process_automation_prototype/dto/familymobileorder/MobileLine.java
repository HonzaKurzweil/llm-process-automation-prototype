package cz.vse.kurzweil.llm_process_automation_prototype.dto.familymobileorder;

import com.fasterxml.jackson.annotation.JsonProperty;

public record MobileLine(
        @JsonProperty("porting_requested") Boolean portingRequested
) {}
