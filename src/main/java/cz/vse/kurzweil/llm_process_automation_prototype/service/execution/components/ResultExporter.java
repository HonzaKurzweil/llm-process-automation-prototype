package cz.vse.kurzweil.llm_process_automation_prototype.service.execution.components;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import cz.vse.kurzweil.llm_process_automation_prototype.dto.ModelType;
import cz.vse.kurzweil.llm_process_automation_prototype.dto.PromptVariant;
import cz.vse.kurzweil.llm_process_automation_prototype.service.execution.dto.ExtractionValidationRunResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static cz.vse.kurzweil.llm_process_automation_prototype.utils.Constants.RESULTS_DIR_NAME;
import static cz.vse.kurzweil.llm_process_automation_prototype.utils.Constants.SUFFIX_JSON;
import static cz.vse.kurzweil.llm_process_automation_prototype.utils.TextUtils.generateOutputFileName;

@Slf4j
@Component
public class ResultExporter {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public void exportResults(Path inputFile,
                              PromptVariant variant,
                              ModelType model,
                              ExtractionValidationRunResult runResult) {
        Path outputFile = buildOutputPath(inputFile, variant, model);
        writeRunResult(outputFile, runResult);
    }

    private Path buildOutputPath(Path inputFile, PromptVariant variant, ModelType model) {
        String inputFileName = inputFile.getFileName().toString();
        String baseName = inputFileName.substring(0, inputFileName.length() - SUFFIX_JSON.length());
        Path resultsDirectory = inputFile.getParent().resolveSibling(RESULTS_DIR_NAME);

        try {
            Files.createDirectories(resultsDirectory);
        } catch (IOException exception) {
            throw new UncheckedIOException("Failed to create results directory: " + resultsDirectory, exception);
        }
        return resultsDirectory.resolve(generateOutputFileName(variant, model, baseName));
    }

    private void writeRunResult(Path outputFile, ExtractionValidationRunResult runResult) {
        try {
            objectMapper.copy()
                    .enable(SerializationFeature.INDENT_OUTPUT)
                    .writeValue(outputFile.toFile(), runResult);
        } catch (IOException exception) {
            throw new UncheckedIOException("Failed to write validation result to " + outputFile, exception);
        }
    }
}
