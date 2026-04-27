package cz.vse.kurzweil.llm_process_automation_prototype.service.execution.components;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import cz.vse.kurzweil.llm_process_automation_prototype.service.validation.components.TreeComparator;
import cz.vse.kurzweil.llm_process_automation_prototype.service.validation.dto.ComparisonResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TreeComparatorTest {

    private TreeComparator comparator;
    private ObjectMapper mapper;

    @BeforeEach
    void setUp() {
        comparator = new TreeComparator();
        mapper = new ObjectMapper();
    }

    // --- exactMatch: identical trees ---

    @Test
    void compareTrees_identicalSimpleObjects_exactMatch() throws Exception {
        JsonNode tree = json("""
                {"name": "Alice", "age": 30}
                """);
        ComparisonResult result = comparator.compareTrees(tree, tree);

        assertThat(result.exactMatch()).isTrue();
        assertThat(result.matchRate()).isEqualTo(1.0);
        assertThat(result.differences()).isEmpty();
    }

    @Test
    void compareTrees_emptyObjects_exactMatch() throws Exception {
        JsonNode empty = json("{}");
        ComparisonResult result = comparator.compareTrees(empty, empty);

        assertThat(result.exactMatch()).isTrue();
        assertThat(result.totalComparedPaths()).isEqualTo(1);
    }

    @Test
    void compareTrees_identicalNestedObjects_exactMatch() throws Exception {
        JsonNode tree = json("""
                {"address": {"city": "Prague", "zip": "110 00"}, "name": "Jan"}
                """);
        ComparisonResult result = comparator.compareTrees(tree, tree);

        assertThat(result.exactMatch()).isTrue();
    }

    // --- missing / unexpected fields ---

    @Test
    void compareTrees_actualMissingField_reportsMissingField() throws Exception {
        JsonNode expected = json("""
                {"name": "Alice", "age": 30}
                """);
        JsonNode actual = json("""
                {"name": "Alice"}
                """);

        ComparisonResult result = comparator.compareTrees(expected, actual);

        assertThat(result.exactMatch()).isFalse();
        assertThat(result.differences()).hasSize(1);
        assertThat(result.differences().getFirst().differenceType()).isEqualTo("missing_field");
        assertThat(result.differences().getFirst().path()).contains("age");
    }

    @Test
    void compareTrees_actualHasExtraField_reportsUnexpectedField() throws Exception {
        JsonNode expected = json("""
                {"name": "Alice"}
                """);
        JsonNode actual = json("""
                {"name": "Alice", "age": 30}
                """);

        ComparisonResult result = comparator.compareTrees(expected, actual);

        assertThat(result.exactMatch()).isFalse();
        assertThat(result.differences()).hasSize(1);
        assertThat(result.differences().getFirst().differenceType()).isEqualTo("unexpected_field");
    }

    // --- value mismatch ---

    @Test
    void compareTrees_differentStringValues_reportsValueMismatch() throws Exception {
        JsonNode expected = json("""
                {"name": "Alice"}
                """);
        JsonNode actual = json("""
                {"name": "Bob"}
                """);

        ComparisonResult result = comparator.compareTrees(expected, actual);

        assertThat(result.exactMatch()).isFalse();
        assertThat(result.differences()).hasSize(1);
        assertThat(result.differences().getFirst().differenceType()).isEqualTo("value_mismatch");
    }

    @Test
    void compareTrees_differentNumericValues_reportsValueMismatch() throws Exception {
        JsonNode expected = json("""
                {"count": 5}
                """);
        JsonNode actual = json("""
                {"count": 9}
                """);

        ComparisonResult result = comparator.compareTrees(expected, actual);

        assertThat(result.differences().getFirst().differenceType()).isEqualTo("value_mismatch");
    }

    // --- type mismatch ---

    @Test
    void compareTrees_stringVsNumber_reportsTypeMismatch() throws Exception {
        JsonNode expected = json("""
                {"field": "text"}
                """);
        JsonNode actual = json("""
                {"field": 123}
                """);

        ComparisonResult result = comparator.compareTrees(expected, actual);

        assertThat(result.exactMatch()).isFalse();
        assertThat(result.differences().getFirst().differenceType()).isEqualTo("type_mismatch");
    }

    // --- null handling ---

    @Test
    void compareTrees_nullVsEmptyArray_treatedAsMatch() throws Exception {
        JsonNode expected = json("""
                {"items": null}
                """);
        JsonNode actual = json("""
                {"items": []}
                """);

        ComparisonResult result = comparator.compareTrees(expected, actual);

        assertThat(result.exactMatch()).isTrue();
        assertThat(result.differences()).isEmpty();
    }

    @Test
    void compareTrees_nullVsNonNullValue_reportsNullMismatch() throws Exception {
        JsonNode expected = json("""
                {"field": null}
                """);
        JsonNode actual = json("""
                {"field": "value"}
                """);

        ComparisonResult result = comparator.compareTrees(expected, actual);

        assertThat(result.exactMatch()).isFalse();
        assertThat(result.differences().getFirst().differenceType()).isEqualTo("null_mismatch");
    }

    // --- match rate ---

    @Test
    void compareTrees_oneDiffOutOfTwo_halfMatchRate() throws Exception {
        JsonNode expected = json("""
                {"name": "Alice", "age": 30}
                """);
        JsonNode actual = json("""
                {"name": "Bob", "age": 30}
                """);

        ComparisonResult result = comparator.compareTrees(expected, actual);

        assertThat(result.totalComparedPaths()).isEqualTo(2);
        assertThat(result.matchedPaths()).isEqualTo(1);
        assertThat(result.matchRate()).isEqualTo(0.5);
    }

    // --- array ordering (canonicalize) ---

    @Test
    void compareTrees_arraysInDifferentOrder_treatedAsMatch() throws Exception {
        JsonNode expected = json("""
                {"items": [{"serviceId": "svc_a"}, {"serviceId": "svc_b"}]}
                """);
        JsonNode actual = json("""
                {"items": [{"serviceId": "svc_b"}, {"serviceId": "svc_a"}]}
                """);

        ComparisonResult result = comparator.compareTrees(expected, actual);

        assertThat(result.exactMatch()).isTrue();
    }

    @Test
    void compareTrees_primitiveArraysDifferentOrder_treatedAsMatch() throws Exception {
        JsonNode expected = json("""
                {"tags": ["c", "a", "b"]}
                """);
        JsonNode actual = json("""
                {"tags": ["a", "b", "c"]}
                """);

        ComparisonResult result = comparator.compareTrees(expected, actual);

        assertThat(result.exactMatch()).isTrue();
    }

    // --- canonicalize ---

    @Test
    void canonicalize_objectFieldsSortedAlphabetically() throws Exception {
        JsonNode node = json("""
                {"z": 1, "a": 2, "m": 3}
                """);
        JsonNode canonical = comparator.canonicalize(node);

        assertThat(canonical.fieldNames()).toIterable().containsExactly("a", "m", "z");
    }

    @Test
    void canonicalize_nullNode_returnsNull() {
        assertThat(comparator.canonicalize(null)).isNull();
    }

    @Test
    void canonicalize_valueNode_returnsSameNode() throws Exception {
        JsonNode node = mapper.readTree("\"hello\"");
        assertThat(comparator.canonicalize(node)).isSameAs(node);
    }

    @Test
    void canonicalize_primitiveArraySorted() throws Exception {
        JsonNode node = json("""
                ["banana", "apple", "cherry"]
                """);
        JsonNode canonical = comparator.canonicalize(node);

        assertThat(canonical.get(0).asText()).isEqualTo("apple");
        assertThat(canonical.get(1).asText()).isEqualTo("banana");
        assertThat(canonical.get(2).asText()).isEqualTo("cherry");
    }

    @Test
    void canonicalize_objectArraySortedByKnownKey() throws Exception {
        JsonNode node = json("""
                [{"serviceId": "svc_z", "name": "Z"}, {"serviceId": "svc_a", "name": "A"}]
                """);
        JsonNode canonical = comparator.canonicalize(node);

        assertThat(canonical.get(0).path("serviceId").asText()).isEqualTo("svc_a");
        assertThat(canonical.get(1).path("serviceId").asText()).isEqualTo("svc_z");
    }

    // --- helpers ---

    private JsonNode json(String raw) throws Exception {
        return mapper.readTree(raw);
    }
}
