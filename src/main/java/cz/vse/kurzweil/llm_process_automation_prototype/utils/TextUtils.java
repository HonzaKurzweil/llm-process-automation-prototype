package cz.vse.kurzweil.llm_process_automation_prototype.utils;

import cz.vse.kurzweil.llm_process_automation_prototype.dto.ModelType;
import cz.vse.kurzweil.llm_process_automation_prototype.dto.PromptVariant;
import lombok.experimental.UtilityClass;
import org.jspecify.annotations.NonNull;

@UtilityClass
public class TextUtils {

    public static String appendPath(String base, String next) {
        if (base == null || base.isBlank()) {
            return next;
        }
        return base + "." + next;
    }

    public static String normalizePath(String path) {
        return (path == null || path.isBlank()) ? "$" : path;
    }

    public static String sanitize(String raw) {
        return raw.replaceAll("[^a-zA-Z0-9._-]", "_");
    }

    public static @NonNull String generateOutputFileName(String type, PromptVariant variant, ModelType model, String baseName) {
        return baseName
                + "__" + type + "__"
                + variant.name().toLowerCase()
                + "__"
                + sanitize(model.getModelId())
                + ".json";
    }

    public static String quote(String value) {
        return "\"" + (value == null ? "" : value) + "\"";
    }
}
