package cz.vse.kurzweil.llm_process_automation_prototype.service.execution.components;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import cz.vse.kurzweil.llm_process_automation_prototype.dto.ModelType;
import cz.vse.kurzweil.llm_process_automation_prototype.dto.PromptVariant;
import cz.vse.kurzweil.llm_process_automation_prototype.service.execution.dto.ClassificationValidationRecordResult;
import cz.vse.kurzweil.llm_process_automation_prototype.service.execution.dto.ClassificationValidationRunResult;
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

import static cz.vse.kurzweil.llm_process_automation_prototype.commons.Constants.*;
import static cz.vse.kurzweil.llm_process_automation_prototype.commons.TextUtils.generateOutputFileName;
import static cz.vse.kurzweil.llm_process_automation_prototype.commons.TextUtils.quote;

@Slf4j
@Component
public class ResultExporter {

    private static final String SUMMARY_CSV = "summary.csv";
    private static final String CSV_HEADER = "validationType,resultFileName,mode,channel,noiseCount,promptVariant,modelType,matchRate,promptTokens,completionTokens";

    private final ObjectMapper objectMapper = new ObjectMapper();

    public void exportExtractionResults(Path inputFile,
                                        PromptVariant variant,
                                        ModelType model,
                                        ExtractionValidationRunResult runResult) {
        Path outputFile = buildOutputPath(inputFile, EXTRACTION_TYPE, variant, model);
        writeResult(outputFile, runResult);
        try (BufferedWriter writer = openSummaryWriter(outputFile.getParent())) {
            for (ExtractionValidationRecordResult record : runResult.records()) {
                writer.write(buildExtractionCsvRow(outputFile.getFileName().toString(), record));
                writer.newLine();
            }
        } catch (IOException exception) {
            throw new UncheckedIOException("Failed to write summary CSV", exception);
        }
    }

    public void exportClassificationResults(Path inputFile,
                                            PromptVariant variant,
                                            ModelType model,
                                            ClassificationValidationRunResult runResult) {
        Path outputFile = buildOutputPath(inputFile, CLASSIFICATION_TYPE, variant, model);
        writeResult(outputFile, runResult);
        try (BufferedWriter writer = openSummaryWriter(outputFile.getParent())) {
            for (ClassificationValidationRecordResult record : runResult.records()) {
                writer.write(buildClassificationCsvRow(outputFile.getFileName().toString(), record));
                writer.newLine();
            }
        } catch (IOException exception) {
            throw new UncheckedIOException("Failed to write summary CSV", exception);
        }
    }

    private Path buildOutputPath(Path inputFile, String type, PromptVariant variant, ModelType model) {
        String inputFileName = inputFile.getFileName().toString();
        String baseName = inputFileName.substring(0, inputFileName.length() - SUFFIX_JSON.length());
        Path resultsDirectory = inputFile.getParent().resolveSibling(RESULTS_DIR_NAME);
        try {
            Files.createDirectories(resultsDirectory);
        } catch (IOException exception) {
            throw new UncheckedIOException("Failed to create results directory: " + resultsDirectory, exception);
        }
        return resultsDirectory.resolve(generateOutputFileName(type, variant, model, baseName));
    }

    private void writeResult(Path outputFile, Object result) {
        try {
            objectMapper.copy()
                    .enable(SerializationFeature.INDENT_OUTPUT)
                    .writeValue(outputFile.toFile(), result);
        } catch (IOException exception) {
            throw new UncheckedIOException("Failed to write validation result to " + outputFile, exception);
        }
    }

    private BufferedWriter openSummaryWriter(Path resultsDirectory) throws IOException {
        Path summaryFile = resultsDirectory.resolve(SUMMARY_CSV);
        boolean isNewFile = Files.notExists(summaryFile);
        BufferedWriter writer = Files.newBufferedWriter(summaryFile, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        if (isNewFile) {
            writer.write(CSV_HEADER);
            writer.newLine();
        }
        return writer;
    }

    private String buildExtractionCsvRow(String resultFileName, ExtractionValidationRecordResult record) {
        return String.join(",",
                quote(EXTRACTION_TYPE.toUpperCase()),
                quote(resultFileName),
                quote(record.mode()),
                quote(record.channel()),
                String.valueOf(record.noiseTags().size()),
                quote(record.promptVariant()),
                quote(record.modelId()),
                String.valueOf(record.matchRate()),
                String.valueOf(record.promptTokens()),
                String.valueOf(record.completionTokens())
        );
    }

    private String buildClassificationCsvRow(String resultFileName, ClassificationValidationRecordResult record) {
        double matchRate = record.correct() ? 1.0 : 0.0;
        return String.join(",",
                quote(resultFileName),
                quote(record.mode()),
                quote(record.channel()),
                String.valueOf(record.noiseTags().size()),
                quote(record.promptVariant()),
                quote(record.modelId()),
                String.valueOf(matchRate),
                String.valueOf(record.promptTokens()),
                String.valueOf(record.completionTokens())
        );
    }
}
