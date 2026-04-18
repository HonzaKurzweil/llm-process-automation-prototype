package cz.vse.kurzweil.llm_process_automation_prototype.service.classification;

import cz.vse.kurzweil.llm_process_automation_prototype.service.ModelType;
import cz.vse.kurzweil.llm_process_automation_prototype.service.PromptVariant;
import cz.vse.kurzweil.llm_process_automation_prototype.service.RequestType;

public interface ClassificationService {

    RequestType classify(String inputText, PromptVariant variant, ModelType model);
}
