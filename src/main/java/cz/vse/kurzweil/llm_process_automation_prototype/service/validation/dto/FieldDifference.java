package cz.vse.kurzweil.llm_process_automation_prototype.service.validation.dto;

import com.fasterxml.jackson.databind.JsonNode;

public record FieldDifference(
        String path,
        String differenceType,
        JsonNode expected,
        JsonNode actual
) {
}
