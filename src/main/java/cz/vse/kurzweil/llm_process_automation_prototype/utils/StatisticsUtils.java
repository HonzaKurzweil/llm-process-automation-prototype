package cz.vse.kurzweil.llm_process_automation_prototype.utils;

import cz.vse.kurzweil.llm_process_automation_prototype.service.execution.dto.ExtractionValidationRecordResult;
import cz.vse.kurzweil.llm_process_automation_prototype.service.execution.dto.OutcomeCounter;
import cz.vse.kurzweil.llm_process_automation_prototype.service.execution.dto.RunSummary;
import lombok.experimental.UtilityClass;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@UtilityClass
public class StatisticsUtils {

    public static RunSummary summarize(List<ExtractionValidationRecordResult> recordResults) {
        int totalRecords = recordResults.size();
        long invocationFailures = recordResults.stream().filter(result -> !result.invocationSucceeded()).count();
        long exactMatches = recordResults.stream().filter(ExtractionValidationRecordResult::exactMatch).count();

        Map<String, OutcomeCounter> byRequestType = aggregateBy(recordResults, ExtractionValidationRecordResult::requestTypeId);
        Map<String, OutcomeCounter> byChannel = aggregateBy(recordResults, ExtractionValidationRecordResult::channel);
        Map<String, OutcomeCounter> byReferenceKind = aggregateBy(recordResults, ExtractionValidationRecordResult::referenceKind);
        Map<String, OutcomeCounter> byPromptVariant = aggregateBy(recordResults, ExtractionValidationRecordResult::promptVariant);
        Map<String, OutcomeCounter> byModel = aggregateBy(recordResults, ExtractionValidationRecordResult::modelId);

        return new RunSummary(totalRecords, exactMatches, invocationFailures, byRequestType, byChannel, byReferenceKind, byPromptVariant, byModel);
    }

    private static Map<String, OutcomeCounter> aggregateBy(
            List<ExtractionValidationRecordResult> results,
            java.util.function.Function<ExtractionValidationRecordResult, String> classifier
    ) {
        return results.stream().collect(Collectors.groupingBy(
                classifier,
                LinkedHashMap::new,
                Collectors.collectingAndThen(Collectors.toList(), group -> new OutcomeCounter(
                        group.size(),
                        group.stream().filter(ExtractionValidationRecordResult::exactMatch).count(),
                        group.stream().filter(result -> !result.invocationSucceeded()).count()
                ))
        ));
    }
}
