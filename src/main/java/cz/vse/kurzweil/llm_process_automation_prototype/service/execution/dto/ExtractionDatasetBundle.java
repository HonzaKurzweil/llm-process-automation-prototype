package cz.vse.kurzweil.llm_process_automation_prototype.service.execution.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ExtractionDatasetBundle(
        Integer datasetVersion,
        Map<String, Object> sourceFiles,
        Map<String, Object> generationParameters,
        List<ExtractionRecord> records
) {
}
