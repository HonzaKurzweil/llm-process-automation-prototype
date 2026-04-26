package cz.vse.kurzweil.llm_process_automation_prototype.commons;

import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Component
public class PromptResourceLoader {

    private final ResourceLoader resourceLoader;

    public PromptResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    public String load(String classpathPath) {
        var resource = resourceLoader.getResource("classpath:" + classpathPath);
        try (InputStream is = resource.getInputStream()) {
            return new String(is.readAllBytes(), StandardCharsets.UTF_8).strip();
        } catch (IOException e) {
            throw new IllegalStateException("Cannot load prompt resource: " + classpathPath, e);
        }
    }
}
