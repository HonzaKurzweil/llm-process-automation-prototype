package cz.vse.kurzweil.llm_process_automation_prototype.service.execution;

import cz.vse.kurzweil.llm_process_automation_prototype.service.ModelType;
import cz.vse.kurzweil.llm_process_automation_prototype.service.PromptVariant;

import java.nio.file.Path;

public interface ExecutionService {
    void validateExtractionService(Path inputFile, PromptVariant variant, ModelType model);
}
