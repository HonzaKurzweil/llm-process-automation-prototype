package cz.vse.kurzweil.llm_process_automation_prototype.service.execution.components;

import com.fasterxml.jackson.databind.JsonNode;
import cz.vse.kurzweil.llm_process_automation_prototype.service.execution.dto.ComparisonResult;
import cz.vse.kurzweil.llm_process_automation_prototype.service.execution.dto.FieldDifference;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import static cz.vse.kurzweil.llm_process_automation_prototype.utils.TextUtils.appendPath;
import static cz.vse.kurzweil.llm_process_automation_prototype.utils.TextUtils.normalizePath;

@Slf4j
@Component
public class TreeComparator {

    public ComparisonResult compareTrees(JsonNode expectedTree, JsonNode actualTree) {
        List<FieldDifference> differences = new ArrayList<>();
        compareNodes("", expectedTree, actualTree, differences);

        Set<String> comparedPaths = new LinkedHashSet<>();
        collectLeafPaths(expectedTree, "", comparedPaths);
        collectLeafPaths(actualTree, "", comparedPaths);

        Set<String> diffPaths = differences.stream()
                .map(FieldDifference::path)
                .collect(Collectors.toCollection(LinkedHashSet::new));

        int totalComparedPaths = comparedPaths.size();
        int matchedPaths = Math.max(0, totalComparedPaths - diffPaths.size());
        double matchRate = totalComparedPaths == 0 ? 1.0d : ((double) matchedPaths / (double) totalComparedPaths);

        return new ComparisonResult(differences.isEmpty(), totalComparedPaths, matchedPaths, matchRate, differences);
    }

    private void compareNodes(String path, JsonNode expected, JsonNode actual, List<FieldDifference> differences) {
        String normalizedPath = normalizePath(path);

        if (expected == null || expected.isMissingNode()) {
            if (actual != null && !actual.isMissingNode()) {
                differences.add(new FieldDifference(normalizedPath, "unexpected_field", null, actual));
            }
            return;
        }

        if (actual == null || actual.isMissingNode()) {
            differences.add(new FieldDifference(normalizedPath, "missing_field", expected, null));
            return;
        }

        if (expected.isNull() || actual.isNull()) {
            if (!expected.equals(actual)) {
                differences.add(new FieldDifference(normalizedPath, "null_mismatch", expected, actual));
            }
            return;
        }

        if (expected.getNodeType() != actual.getNodeType()) {
            differences.add(new FieldDifference(normalizedPath, "type_mismatch", expected, actual));
            return;
        }

        if (expected.isObject()) {
            Set<String> fieldNames = new TreeSet<>();
            expected.fieldNames().forEachRemaining(fieldNames::add);
            actual.fieldNames().forEachRemaining(fieldNames::add);

            for (String fieldName : fieldNames) {
                compareNodes(appendPath(normalizedPath, fieldName), expected.get(fieldName), actual.get(fieldName), differences);
            }
            return;
        }

        if (expected.isArray()) {
            int maxSize = Math.max(expected.size(), actual.size());
            for (int index = 0; index < maxSize; index++) {
                JsonNode expectedItem = index < expected.size() ? expected.get(index) : null;
                JsonNode actualItem = index < actual.size() ? actual.get(index) : null;
                compareNodes(normalizedPath + "[" + index + "]", expectedItem, actualItem, differences);
            }
            return;
        }

        if (!expected.equals(actual)) {
            differences.add(new FieldDifference(normalizedPath, "value_mismatch", expected, actual));
        }
    }

    private void collectLeafPaths(JsonNode node, String path, Set<String> output) {
        String normalizedPath = normalizePath(path);
        if (node == null || node.isMissingNode()) {
            output.add(normalizedPath);
            return;
        }

        if (node.isValueNode() || node.isNull()) {
            output.add(normalizedPath);
            return;
        }

        if (node.isObject()) {
            if (node.isEmpty()) {
                output.add(normalizedPath);
                return;
            }
            node.fieldNames().forEachRemaining(field -> collectLeafPaths(node.get(field), appendPath(normalizedPath, field), output));
            return;
        }

        if (node.isArray()) {
            if (node.isEmpty()) {
                output.add(normalizedPath);
                return;
            }
            for (int index = 0; index < node.size(); index++) {
                collectLeafPaths(node.get(index), normalizedPath + "[" + index + "]", output);
            }
        }
    }
}
