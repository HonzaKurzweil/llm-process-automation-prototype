package cz.vse.kurzweil.llm_process_automation_prototype.service.validation;

import cz.vse.kurzweil.llm_process_automation_prototype.dto.ModelType;
import cz.vse.kurzweil.llm_process_automation_prototype.dto.PromptVariant;

import java.nio.file.Path;

public interface ClassificationValidationService {
    void validateClassificationService(Path inputFile, PromptVariant variant, ModelType model);
}
