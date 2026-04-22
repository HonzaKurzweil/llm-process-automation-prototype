package cz.vse.kurzweil.llm_process_automation_prototype.service.execution.dto;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.List;

public record ExtractionValidationRecordResult(
        String recordId,
        String requestTypeId,
        String channel,
        String channelStyle,
        List<String> noiseTags,
        List<String> businessPerturbationTags,
        String referenceId,
        String referenceKind,
        String promptVariant,
        String modelId,
        boolean invocationSucceeded,
        boolean exactMatch,
        List<String> missingRequiredFields,
        List<String> missingRequiredPaths,
        List<String> expectedRuleViolations,
        int totalComparedPaths,
        int matchedPaths,
        double matchRate,
        List<FieldDifference> differences,
        JsonNode expectedDto,
        JsonNode actualDto,
        String errorMessage
) {
    public static ExtractionValidationRecordResult failureWithoutInvocation(
            String recordId,
            String requestTypeId,
            String channel,
            String channelStyle,
            List<String> noiseTags,
            List<String> businessPerturbationTags,
            String referenceId,
            String referenceKind,
            String promptVariant,
            String modelId,
            String errorMessage
    ) {
        return new ExtractionValidationRecordResult(
                recordId,
                requestTypeId,
                channel,
                channelStyle,
                defaultList(noiseTags),
                defaultList(businessPerturbationTags),
                referenceId,
                referenceKind,
                promptVariant,
                modelId,
                false,
                false,
                List.of(),
                List.of(),
                List.of(),
                0,
                0,
                0.0d,
                List.of(),
                null,
                null,
                errorMessage
        );
    }

    public static ExtractionValidationRecordResult failureAfterInvocation(
            String recordId,
            String requestTypeId,
            String channel,
            String channelStyle,
            List<String> noiseTags,
            List<String> businessPerturbationTags,
            String referenceId,
            String referenceKind,
            String promptVariant,
            String modelId,
            List<String> missingRequiredFields,
            List<String> missingRequiredPaths,
            List<String> expectedRuleViolations,
            JsonNode expectedDto,
            String errorMessage
    ) {
        return new ExtractionValidationRecordResult(
                recordId,
                requestTypeId,
                channel,
                channelStyle,
                defaultList(noiseTags),
                defaultList(businessPerturbationTags),
                referenceId,
                referenceKind,
                promptVariant,
                modelId,
                false,
                false,
                defaultList(missingRequiredFields),
                defaultList(missingRequiredPaths),
                defaultList(expectedRuleViolations),
                0,
                0,
                0.0d,
                List.of(),
                expectedDto,
                null,
                errorMessage
        );
    }

    public ExtractionValidationRecordResult {
        noiseTags = defaultList(noiseTags);
        businessPerturbationTags = defaultList(businessPerturbationTags);
        missingRequiredFields = defaultList(missingRequiredFields);
        missingRequiredPaths = defaultList(missingRequiredPaths);
        expectedRuleViolations = defaultList(expectedRuleViolations);
        differences = defaultList(differences);
    }

    private static <T> List<T> defaultList(List<T> input) {
        return input == null ? List.of() : List.copyOf(input);
    }
}
