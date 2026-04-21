package cz.vse.kurzweil.llm_process_automation_prototype.service.execution.components;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import cz.vse.kurzweil.llm_process_automation_prototype.service.execution.dto.ComparisonResult;
import cz.vse.kurzweil.llm_process_automation_prototype.service.execution.dto.FieldDifference;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

import static cz.vse.kurzweil.llm_process_automation_prototype.utils.TextUtils.appendPath;
import static cz.vse.kurzweil.llm_process_automation_prototype.utils.TextUtils.normalizePath;

@Slf4j
@Component
public class TreeComparator {

    private static final List<String> ARRAY_SORT_KEYS = List.of(
            "serviceId",
            "productId",
            "discountId",
            "targetServiceId",
            "label",
            "number",
            "donorOperator"
    );

    private final ObjectMapper objectMapper = new ObjectMapper();

    public ComparisonResult compareTrees(JsonNode expectedTree, JsonNode actualTree) {
        JsonNode canonicalExpected = canonicalize(expectedTree);
        JsonNode canonicalActual = canonicalize(actualTree);

        List<FieldDifference> differences = new ArrayList<>();
        compareNodes("", canonicalExpected, canonicalActual, differences);

        Set<String> comparedPaths = new LinkedHashSet<>();
        collectLeafPaths(canonicalExpected, "", comparedPaths);
        collectLeafPaths(canonicalActual, "", comparedPaths);

        Set<String> diffPaths = differences.stream()
                .map(FieldDifference::path)
                .collect(Collectors.toCollection(LinkedHashSet::new));

        int totalComparedPaths = comparedPaths.size();
        int matchedPaths = Math.max(0, totalComparedPaths - diffPaths.size());
        double matchRate = totalComparedPaths == 0 ? 1.0d : ((double) matchedPaths / (double) totalComparedPaths);

        return new ComparisonResult(differences.isEmpty(), totalComparedPaths, matchedPaths, matchRate, differences);
    }


    /**
     * Normalizes a JSON tree before comparison: sorts object fields alphabetically and array elements
     * by a stable key (e.g. "id" or "kod"), so that LLM output differing only in ordering does not
     * produce false field-level differences when evaluated against reference_outputs.yaml gold standards.
     */
    public JsonNode canonicalize(JsonNode node) {
        if (node == null || node.isNull() || node.isValueNode()) {
            return node;
        }

        if (node.isObject()) {
            ObjectNode objectNode = objectMapper.createObjectNode();
            List<String> fieldNames = new ArrayList<>();
            node.fieldNames().forEachRemaining(fieldNames::add);
            fieldNames.stream().sorted().forEach(field -> objectNode.set(field, canonicalize(node.get(field))));
            return objectNode;
        }

        if (node.isArray()) {
            List<JsonNode> items = new ArrayList<>();
            node.forEach(item -> items.add(canonicalize(item)));

            Comparator<JsonNode> comparator = buildArrayComparator(items);
            if (comparator != null) {
                items.sort(comparator);
            }

            ArrayNode arrayNode = objectMapper.createArrayNode();
            items.forEach(arrayNode::add);
            return arrayNode;
        }

        return node;
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

    private Comparator<JsonNode> buildArrayComparator(List<JsonNode> items) {
        if (items.isEmpty()) {
            return null;
        }

        if (items.stream().allMatch(JsonNode::isValueNode)) {
            return Comparator.comparing(JsonNode::asText, Comparator.nullsFirst(String::compareTo));
        }

        if (!items.stream().allMatch(JsonNode::isObject)) {
            return null;
        }

        String sortKey = ARRAY_SORT_KEYS.stream()
                .filter(candidate -> items.stream().allMatch(item -> item.has(candidate)))
                .findFirst()
                .orElse(null);

        if (sortKey == null) {
            return Comparator.comparing(JsonNode::toString);
        }

        return Comparator.comparing(item -> item.path(sortKey).asText(""), Comparator.nullsFirst(String::compareTo));
    }
}
