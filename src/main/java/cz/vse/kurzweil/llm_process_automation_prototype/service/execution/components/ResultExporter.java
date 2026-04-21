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

import static cz.vse.kurzweil.llm_process_automation_prototype.utils.TextUtils.sanitize;

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
        String baseName = inputFileName.endsWith(".json")
                ? inputFileName.substring(0, inputFileName.length() - 5)
                : inputFileName;

        Path resultsDirectory = inputFile.getParent() == null
                ? Path.of("results")
                : inputFile.getParent().resolveSibling("results");

        try {
            Files.createDirectories(resultsDirectory);
        } catch (IOException exception) {
            throw new UncheckedIOException("Failed to create results directory: " + resultsDirectory, exception);
        }

        String outputFileName = baseName
                + "__validation__"
                + variant.name().toLowerCase()
                + "__"
                + sanitize(model.getModelId())
                + ".json";

        return resultsDirectory.resolve(outputFileName);
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
