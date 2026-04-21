package cz.vse.kurzweil.llm_process_automation_prototype.service.execution.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ExtractionRecord(
        String recordId,
        String mode,
        String channel,
        String channelStyle,
        List<String> noiseTags,
        List<String> businessPerturbationTags,
        RecordSource source,
        String inputText,
        GoldAnnotation goldAnnotation,
        Map<String, Object> observability
) {
}
