package cz.vse.kurzweil.llm_process_automation_prototype.service.execution;

import cz.vse.kurzweil.llm_process_automation_prototype.dto.ModelType;
import cz.vse.kurzweil.llm_process_automation_prototype.dto.PromptVariant;

import java.nio.file.Path;

public interface ExecutionService {
    void validateExtractionService(Path inputFile, PromptVariant variant, ModelType model);
}
