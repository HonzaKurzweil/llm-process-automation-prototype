package cz.vse.kurzweil.llm_process_automation_prototype.service.validation.components;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import cz.vse.kurzweil.llm_process_automation_prototype.service.validation.dto.ExtractionRecord;
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
            JsonNode root = objectMapper.readTree(inputFile.toFile());
            CollectionType listType = objectMapper.getTypeFactory()
                    .constructCollectionType(List.class, ExtractionRecord.class);

            JsonNode recordsNode = root.get("records");
            if (recordsNode != null && recordsNode.isArray()) {
                List<ExtractionRecord> records = objectMapper.convertValue(recordsNode, listType);
                log.debug("Loaded {} records from {} (wrapped format)", records.size(), inputFile.getFileName());
                return records;
            }

            throw new IllegalArgumentException(
                    "Dataset file has neither a top-level array nor a 'records' field: " + inputFile);
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to read extraction dataset: " + inputFile, e);
        }
    }
}
