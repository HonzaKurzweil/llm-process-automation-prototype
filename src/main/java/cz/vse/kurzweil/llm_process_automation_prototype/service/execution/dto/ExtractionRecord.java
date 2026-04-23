package cz.vse.kurzweil.llm_process_automation_prototype.service.execution.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ExtractionRecord(
        String recordId,
        String mode,
        String channel,
        List<String> noiseTags,
        Map<String, Object> generationParams,
        String inputText,
        ExpectedClassification expectedClassification,
        List<ExtractionExpectation> expectedExtractions,
        Map<String, Object> evidence
) {
}
