package cz.vse.kurzweil.llm_process_automation_prototype.service.classification;

import cz.vse.kurzweil.llm_process_automation_prototype.dto.ModelType;
import cz.vse.kurzweil.llm_process_automation_prototype.dto.PromptVariant;

public interface ClassificationService {

    ClassificationResult classify(String inputText, PromptVariant variant, ModelType model);
}
