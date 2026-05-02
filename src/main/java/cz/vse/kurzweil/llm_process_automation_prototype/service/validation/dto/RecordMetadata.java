package cz.vse.kurzweil.llm_process_automation_prototype.service.validation.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public record RecordMetadata(
        String mode,
        String channel,
        List<String> requestTypeIds,
        String requestTypeId,
        String scenarioKind,
        String completenessMode,
        Integer requestedNoiseCount,
        List<String> noiseTags,
        Integer variantIndex
) {
    public RecordMetadata {
        requestTypeIds = requestTypeIds == null ? List.of() : List.copyOf(requestTypeIds);
        noiseTags = noiseTags == null ? List.of() : List.copyOf(noiseTags);
    }

    public Map<String, Object> toGenerationParams() {
        Map<String, Object> params = new LinkedHashMap<>();
        putIfNotNull(params, "requestTypeIds", requestTypeIds);
        putIfNotNull(params, "requestTypeId", requestTypeId);
        putIfNotNull(params, "scenarioKind", scenarioKind);
        putIfNotNull(params, "completenessMode", completenessMode);
        putIfNotNull(params, "noiseCount", requestedNoiseCount);
        putIfNotNull(params, "variantIndex", variantIndex);
        return params;
    }

    private static void putIfNotNull(Map<String, Object> output, String key, Object value) {
        if (value != null) {
            output.put(key, value);
        }
    }
}
