package cz.vse.kurzweil.llm_process_automation_prototype.service.classification.impl;

import com.fasterxml.jackson.annotation.JsonProperty;
import cz.vse.kurzweil.llm_process_automation_prototype.dto.RequestType;

record ClassificationResponse(
        @JsonProperty("request_type") RequestType requestType
) {
}
