package cz.vse.kurzweil.llm_process_automation_prototype.service.execution.dto;

import java.util.List;

public record ClassificationValidationRecordResult(
        String recordId,
        String channel,
        String mode,
        List<String> noiseTags,
        String promptVariant,
        String modelId,
        boolean invocationSucceeded,
        String expectedRequestTypeId,
        String actualRequestTypeId,
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
            String expectedRequestTypeId,
            String errorMessage
    ) {
        return new ClassificationValidationRecordResult(
                recordId, channel, mode, noiseTags == null ? List.of() : List.copyOf(noiseTags),
                promptVariant, modelId, false, expectedRequestTypeId, null, false, 0, 0, errorMessage
        );
    }

    public ClassificationValidationRecordResult {
        noiseTags = noiseTags == null ? List.of() : List.copyOf(noiseTags);
    }
}
