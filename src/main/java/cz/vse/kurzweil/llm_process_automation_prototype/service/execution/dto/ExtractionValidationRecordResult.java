package cz.vse.kurzweil.llm_process_automation_prototype.service.execution.dto;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.List;

public record ExtractionValidationRecordResult(
        String recordId,
        String requestTypeId,
        String channel,
        List<String> noiseTags,
        String promptVariant,
        String modelId,
        boolean invocationSucceeded,
        boolean exactMatch,
        List<String> missingFieldPaths,
        int totalComparedPaths,
        int matchedPaths,
        double matchRate,
        List<FieldDifference> differences,
        JsonNode expectedDto,
        JsonNode actualDto,
        int promptTokens,
        int completionTokens,
        String errorMessage
) {
    public static ExtractionValidationRecordResult failureWithoutInvocation(
            String recordId,
            String requestTypeId,
            String channel,
            List<String> noiseTags,
            String promptVariant,
            String modelId,
            String errorMessage
    ) {
        return new ExtractionValidationRecordResult(
                recordId, requestTypeId, channel, defaultList(noiseTags),
                promptVariant, modelId,
                false, false, List.of(), 0, 0, 0.0d, List.of(), null, null, 0, 0, errorMessage
        );
    }

    public static ExtractionValidationRecordResult failureAfterInvocation(
            String recordId,
            String requestTypeId,
            String channel,
            List<String> noiseTags,
            String promptVariant,
            String modelId,
            List<String> missingFieldPaths,
            JsonNode expectedDto,
            String errorMessage
    ) {
        return new ExtractionValidationRecordResult(
                recordId, requestTypeId, channel, defaultList(noiseTags),
                promptVariant, modelId,
                false, false, defaultList(missingFieldPaths), 0, 0, 0.0d, List.of(), expectedDto, null, 0, 0, errorMessage
        );
    }

    public ExtractionValidationRecordResult {
        noiseTags = defaultList(noiseTags);
        missingFieldPaths = defaultList(missingFieldPaths);
        differences = defaultList(differences);
    }

    private static <T> List<T> defaultList(List<T> input) {
        return input == null ? List.of() : List.copyOf(input);
    }
}
