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

    public static long elapsedMillis(long startedAtNanos) {
        return (System.nanoTime() - startedAtNanos) / 1_000_000L;
    }

    public static String sanitize(String raw) {
        return raw.replaceAll("[^a-zA-Z0-9._-]", "_");
    }

    public static @NonNull String generateOutputFileName(PromptVariant variant, ModelType model, String baseName) {
        return baseName
                + "__validation__"
                + variant.name().toLowerCase()
                + "__"
                + sanitize(model.getModelId())
                + ".json";
    }
}
