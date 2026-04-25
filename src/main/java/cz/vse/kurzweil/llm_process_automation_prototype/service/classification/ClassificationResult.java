package cz.vse.kurzweil.llm_process_automation_prototype.service.classification;

import cz.vse.kurzweil.llm_process_automation_prototype.dto.RequestType;

public record ClassificationResult(
        RequestType requestType,
        int promptTokens,
        int completionTokens
) {
}
