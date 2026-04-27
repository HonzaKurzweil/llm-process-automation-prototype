package cz.vse.kurzweil.llm_process_automation_prototype.service.validation.dto;

import cz.vse.kurzweil.llm_process_automation_prototype.dto.ModelType;
import cz.vse.kurzweil.llm_process_automation_prototype.dto.PromptVariant;
import cz.vse.kurzweil.llm_process_automation_prototype.dto.RequestType;

public record RecordExecutionContext(
        ExtractionRecord record,
        ExtractionExpectation expectation,
        RequestType requestType,
        PromptVariant variant,
        ModelType model
) {
}
