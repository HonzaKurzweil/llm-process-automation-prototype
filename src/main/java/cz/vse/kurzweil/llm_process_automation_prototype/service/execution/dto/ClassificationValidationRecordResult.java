package cz.vse.kurzweil.llm_process_automation_prototype.service.execution.dto;

import cz.vse.kurzweil.llm_process_automation_prototype.dto.RequestType;

import java.util.List;

public record ClassificationValidationRecordResult(
        String recordId,
        String channel,
        String mode,
        List<String> noiseTags,
        String promptVariant,
        String modelId,
        boolean invocationSucceeded,
        RequestType expectedRequestType,
        RequestType actualRequestType,
        boolean correct,
        int promptTokens,
        int completionTokens,
        String errorMessage
) {
    public static ClassificationValidationRecordResult failure(
            String recordId,
            String channel,
            String mode,
            List<String> noiseTags,
            String promptVariant,
            String modelId,
            RequestType expectedRequestType,
            String errorMessage
    ) {
        return new ClassificationValidationRecordResult(
                recordId, channel, mode, noiseTags == null ? List.of() : List.copyOf(noiseTags),
                promptVariant, modelId, false, expectedRequestType, null, false, 0, 0, errorMessage
        );
    }

    public ClassificationValidationRecordResult {
        noiseTags = noiseTags == null ? List.of() : List.copyOf(noiseTags);
    }
}
