package cz.vse.kurzweil.llm_process_automation_prototype.service.validation.dto;

import java.util.List;

public record ComparisonResult(
        boolean exactMatch,
        int totalComparedPaths,
        int matchedPaths,
        double matchRate,
        List<FieldDifference> differences
) {
}
