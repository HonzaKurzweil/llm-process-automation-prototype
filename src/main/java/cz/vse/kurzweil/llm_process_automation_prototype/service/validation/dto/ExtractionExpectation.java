package cz.vse.kurzweil.llm_process_automation_prototype.service.validation.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ExtractionExpectation(
        String requestTypeId,
        String templateId,
        Map<String, Object> dto,
        List<String> missingFieldPaths
) {
}
