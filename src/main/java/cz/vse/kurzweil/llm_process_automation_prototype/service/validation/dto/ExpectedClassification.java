package cz.vse.kurzweil.llm_process_automation_prototype.service.validation.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ExpectedClassification(
        String requestTypeId,
        String unclassifiableReason
) {
}
