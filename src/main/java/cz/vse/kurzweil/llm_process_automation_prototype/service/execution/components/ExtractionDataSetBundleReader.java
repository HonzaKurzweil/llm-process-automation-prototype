package cz.vse.kurzweil.llm_process_automation_prototype.service.execution.components;

import com.fasterxml.jackson.databind.ObjectMapper;
import cz.vse.kurzweil.llm_process_automation_prototype.service.execution.dto.ExtractionRecord;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Path;
import java.util.List;

@Slf4j
@Component
public class ExtractionDataSetBundleReader {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public List<ExtractionRecord> read(Path inputFile) {
        try {
            var listType = objectMapper.getTypeFactory()
                    .constructCollectionType(List.class, ExtractionRecord.class);
            List<ExtractionRecord> records = objectMapper.readValue(inputFile.toFile(), listType);
            log.debug("Loaded {} records from {}", records.size(), inputFile.getFileName());
            return records;
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to read extraction dataset: " + inputFile, e);
        }
    }
}
