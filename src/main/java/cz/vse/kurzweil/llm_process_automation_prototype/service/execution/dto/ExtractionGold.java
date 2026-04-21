package cz.vse.kurzweil.llm_process_automation_prototype.service.execution.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ExtractionGold(
        String requestTypeId,
        String referenceId,
        String referenceKind,
        Map<String, Object> extractedFields,
        List<String> missingRequiredFields,
        List<String> missingRequiredPaths,
        List<String> expectedRuleViolations
) {
}
