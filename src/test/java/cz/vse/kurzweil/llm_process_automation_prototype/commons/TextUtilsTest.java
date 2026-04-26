package cz.vse.kurzweil.llm_process_automation_prototype.commons;

import cz.vse.kurzweil.llm_process_automation_prototype.dto.ModelType;
import cz.vse.kurzweil.llm_process_automation_prototype.dto.PromptVariant;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TextUtilsTest {

    // --- appendPath ---

    @Test
    void appendPath_nullBase_returnsNext() {
        assertThat(TextUtils.appendPath(null, "next")).isEqualTo("next");
    }

    @Test
    void appendPath_emptyBase_returnsNext() {
        assertThat(TextUtils.appendPath("", "next")).isEqualTo("next");
    }

    @Test
    void appendPath_blankBase_returnsNext() {
        assertThat(TextUtils.appendPath("   ", "next")).isEqualTo("next");
    }

    @Test
    void appendPath_nonEmptyBase_joinsWithDot() {
        assertThat(TextUtils.appendPath("base", "child")).isEqualTo("base.child");
    }

    @Test
    void appendPath_nestedBase_joinsWithDot() {
        assertThat(TextUtils.appendPath("$.root.parent", "leaf")).isEqualTo("$.root.parent.leaf");
    }

    // --- normalizePath ---

    @Test
    void normalizePath_null_returnsDollarSign() {
        assertThat(TextUtils.normalizePath(null)).isEqualTo("$");
    }

    @Test
    void normalizePath_empty_returnsDollarSign() {
        assertThat(TextUtils.normalizePath("")).isEqualTo("$");
    }

    @Test
    void normalizePath_blank_returnsDollarSign() {
        assertThat(TextUtils.normalizePath("   ")).isEqualTo("$");
    }

    @Test
    void normalizePath_nonBlank_returnsSame() {
        assertThat(TextUtils.normalizePath("$.name")).isEqualTo("$.name");
    }

    // --- sanitize ---

    @Test
    void sanitize_allowedCharsUnchanged() {
        assertThat(TextUtils.sanitize("model-name_v1.0")).isEqualTo("model-name_v1.0");
    }

    @Test
    void sanitize_spaceReplacedWithUnderscore() {
        assertThat(TextUtils.sanitize("hello world")).isEqualTo("hello_world");
    }

    @Test
    void sanitize_colonReplacedWithUnderscore() {
        assertThat(TextUtils.sanitize("gemma3:12b")).isEqualTo("gemma3_12b");
    }

    @Test
    void sanitize_slashReplacedWithUnderscore() {
        assertThat(TextUtils.sanitize("path/to/model")).isEqualTo("path_to_model");
    }

    @Test
    void sanitize_multipleSpecialChars_allReplaced() {
        assertThat(TextUtils.sanitize("a@b#c$d")).isEqualTo("a_b_c_d");
    }

    // --- generateOutputFileName ---

    @Test
    void generateOutputFileName_directVariant_opensCorrectFormat() {
        String result = TextUtils.generateOutputFileName("extraction", PromptVariant.DIRECT, ModelType.GPT_4O, "run_001");
        assertThat(result).isEqualTo("run_001__extraction__direct__gpt-4o.json");
    }

    @Test
    void generateOutputFileName_fewShotVariant_lowercasedWithUnderscore() {
        String result = TextUtils.generateOutputFileName("extraction", PromptVariant.FEW_SHOT, ModelType.GPT_4O_MINI, "run");
        assertThat(result).isEqualTo("run__extraction__few_shot__gpt-4o-mini.json");
    }

    @Test
    void generateOutputFileName_modelWithColonSanitized() {
        String result = TextUtils.generateOutputFileName("classification", PromptVariant.DIRECT, ModelType.GEMMA3_12B, "test");
        assertThat(result).isEqualTo("test__classification__direct__gemma3_12b.json");
    }

    @Test
    void generateOutputFileName_alwaysEndsWithJsonExtension() {
        String result = TextUtils.generateOutputFileName("extraction", PromptVariant.DIRECT, ModelType.GPT_4O, "base");
        assertThat(result).endsWith(".json");
    }

    // --- quote ---

    @Test
    void quote_nonNullValue_wrappedInDoubleQuotes() {
        assertThat(TextUtils.quote("hello")).isEqualTo("\"hello\"");
    }

    @Test
    void quote_null_wrapsEmpty() {
        assertThat(TextUtils.quote(null)).isEqualTo("\"\"");
    }

    @Test
    void quote_emptyString_wrapsEmpty() {
        assertThat(TextUtils.quote("")).isEqualTo("\"\"");
    }
}
