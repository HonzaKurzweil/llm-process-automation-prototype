package cz.vse.kurzweil.llm_process_automation_prototype.service.classification.impl;

import cz.vse.kurzweil.llm_process_automation_prototype.dto.PromptVariant;
import cz.vse.kurzweil.llm_process_automation_prototype.service.PromptResourceLoader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.ResponseEntity;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.stereotype.Component;

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
    public ResponseEntity<ChatResponse, ClassificationResponse> classify(String inputText, ChatClient client) {
        log.debug("Classifying using DIRECT strategy, inputLength={}", inputText.length());
        ResponseEntity<ChatResponse, ClassificationResponse> response = client.prompt()
                .system(systemPrompt)
                .user(inputText)
                .call()
                .responseEntity(ClassificationResponse.class);
        log.debug("Classification result using DIRECT strategy: {}", response.entity().requestType());
        return response;
    }
}
