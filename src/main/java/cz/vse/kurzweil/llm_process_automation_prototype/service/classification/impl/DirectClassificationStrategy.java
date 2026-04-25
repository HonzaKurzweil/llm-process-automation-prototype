package cz.vse.kurzweil.llm_process_automation_prototype.service.classification.impl;

import com.fasterxml.jackson.annotation.JsonProperty;
import cz.vse.kurzweil.llm_process_automation_prototype.dto.PromptVariant;
import cz.vse.kurzweil.llm_process_automation_prototype.dto.RequestType;
import cz.vse.kurzweil.llm_process_automation_prototype.service.PromptResourceLoader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Slf4j
@Component
public class DirectClassificationStrategy implements ClassificationStrategy {

    private final String systemPrompt;

    public DirectClassificationStrategy(PromptResourceLoader promptLoader) {
        this.systemPrompt = promptLoader.load("read_prompts/classification/direct-system.md");
    }

    @Override
    public PromptVariant variant() {
        return PromptVariant.DIRECT;
    }

    @Override
    public RequestType classify(String inputText, ChatClient client) {
        log.debug("Classifying using DIRECT strategy, inputLength={}", inputText.length());
        RequestType result = Objects.requireNonNull(client.prompt()
                        .system(systemPrompt)
                        .user(inputText)
                        .call()
                        .entity(ClassificationResponse.class))
                .requestType();
        log.debug("Classification result using DIRECT strategy: {}", result);
        return result;
    }

    private record ClassificationResponse(
            @JsonProperty("request_type") RequestType requestType
    ) {
    }
}
