package cz.vse.kurzweil.llm_process_automation_prototype.service.validation.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

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

}
