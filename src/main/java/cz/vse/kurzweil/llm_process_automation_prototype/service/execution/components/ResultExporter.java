package cz.vse.kurzweil.llm_process_automation_prototype.service.execution.components;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import cz.vse.kurzweil.llm_process_automation_prototype.dto.ModelType;
import cz.vse.kurzweil.llm_process_automation_prototype.dto.PromptVariant;
import cz.vse.kurzweil.llm_process_automation_prototype.service.execution.dto.ExtractionValidationRecordResult;
import cz.vse.kurzweil.llm_process_automation_prototype.service.execution.dto.ExtractionValidationRunResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

import static cz.vse.kurzweil.llm_process_automation_prototype.utils.Constants.RESULTS_DIR_NAME;
import static cz.vse.kurzweil.llm_process_automation_prototype.utils.Constants.SUFFIX_JSON;
import static cz.vse.kurzweil.llm_process_automation_prototype.utils.TextUtils.generateOutputFileName;
import static cz.vse.kurzweil.llm_process_automation_prototype.utils.TextUtils.quote;

@Slf4j
@Component
public class ResultExporter {

    private static final String SUMMARY_CSV = "summary.csv";
    private static final String CSV_HEADER = "resultFileName,mode,channel,noiseCount,completenessMode,promptVariant,modelType,matchRate,promptTokens,completionTokens";

    private final ObjectMapper objectMapper = new ObjectMapper();

    public void exportResults(Path inputFile,
                              PromptVariant variant,
                              ModelType model,
                              ExtractionValidationRunResult runResult) {
        Path outputFile = buildOutputPath(inputFile, variant, model);
        writeRunResult(outputFile, runResult);
        appendSummaryRows(outputFile.getParent(), outputFile.getFileName().toString(), runResult);
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

    private void appendSummaryRows(Path resultsDirectory, String resultFileName, ExtractionValidationRunResult runResult) {
        Path summaryFile = resultsDirectory.resolve(SUMMARY_CSV);
        boolean isNewFile = Files.notExists(summaryFile);
        try (BufferedWriter writer = Files.newBufferedWriter(summaryFile, StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
            if (isNewFile) {
                writer.write(CSV_HEADER);
                writer.newLine();
            }
            for (ExtractionValidationRecordResult record : runResult.records()) {
                writer.write(buildCsvRow(resultFileName, record));
                writer.newLine();
            }
        } catch (IOException exception) {
            throw new UncheckedIOException("Failed to write summary CSV at " + summaryFile, exception);
        }
    }

    private String buildCsvRow(String resultFileName, ExtractionValidationRecordResult record) {
        String completenessMode = record.missingFieldPaths().isEmpty() ? "FULL" : "PARTIAL";
        return String.join(",",
                quote(resultFileName),
                quote(record.mode()),
                quote(record.channel()),
                String.valueOf(record.noiseTags().size()),
                quote(completenessMode),
                quote(record.promptVariant()),
                quote(record.modelId()),
                String.valueOf(record.matchRate()),
                String.valueOf(record.promptTokens()),
                String.valueOf(record.completionTokens())
        );
    }
}
