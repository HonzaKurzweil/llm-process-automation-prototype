package cz.vse.kurzweil.llm_process_automation_prototype.service.extraction.impl;

import cz.vse.kurzweil.llm_process_automation_prototype.dto.PromptVariant;
import cz.vse.kurzweil.llm_process_automation_prototype.dto.RequestType;
import cz.vse.kurzweil.llm_process_automation_prototype.service.PromptResourceLoader;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.ResponseEntity;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.stereotype.Component;

@Component
public class DirectExtractionStrategy implements ExtractionStrategy {

    private final PromptResourceLoader promptLoader;

    public DirectExtractionStrategy(PromptResourceLoader promptLoader) {
        this.promptLoader = promptLoader;
    }

    @Override
    public PromptVariant variant() {
        return PromptVariant.DIRECT;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T extract(String inputText, RequestType requestType, ChatClient client) {
        String systemPrompt = promptLoader.load(requestType.getPromptDirectory() + "/direct-system.md");
        return client.prompt()
                .system(systemPrompt)
                .user(inputText)
                .call()
                .entity((Class<T>) requestType.getDtoClass());
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> ResponseEntity<ChatResponse, T> extractResponseEntity(String inputText, RequestType requestType, ChatClient client) {
        String systemPrompt = promptLoader.load(requestType.getPromptDirectory() + "/direct-system.md");
        return client.prompt()
                .system(systemPrompt)
                .user(inputText)
                .call()
                .responseEntity((Class<T>) requestType.getDtoClass());
    }
}
