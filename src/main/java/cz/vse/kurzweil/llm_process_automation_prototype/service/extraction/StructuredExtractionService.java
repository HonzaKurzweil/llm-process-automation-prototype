package cz.vse.kurzweil.llm_process_automation_prototype.service.extraction;

import cz.vse.kurzweil.llm_process_automation_prototype.service.ModelType;
import cz.vse.kurzweil.llm_process_automation_prototype.service.PromptVariant;
import cz.vse.kurzweil.llm_process_automation_prototype.service.RequestType;

public interface StructuredExtractionService {

    <T> T extract(String inputText, RequestType requestType, PromptVariant variant, ModelType model);
}
