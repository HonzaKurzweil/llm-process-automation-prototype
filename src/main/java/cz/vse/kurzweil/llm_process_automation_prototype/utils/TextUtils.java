package cz.vse.kurzweil.llm_process_automation_prototype.utils;

import lombok.experimental.UtilityClass;

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
}
