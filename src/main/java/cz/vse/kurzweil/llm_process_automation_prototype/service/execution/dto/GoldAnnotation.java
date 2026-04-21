package cz.vse.kurzweil.llm_process_automation_prototype.service.execution.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record GoldAnnotation(
        ExtractionGold extraction
) {
}
