package cz.vse.kurzweil.llm_process_automation_prototype.service.classification.impl;

import cz.vse.kurzweil.llm_process_automation_prototype.dto.PromptVariant;
import cz.vse.kurzweil.llm_process_automation_prototype.dto.RequestType;
import cz.vse.kurzweil.llm_process_automation_prototype.commons.PromptResourceLoader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.ResponseEntity;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class FewShotClassificationStrategy implements ClassificationStrategy {

    private final String systemPrompt;

    public FewShotClassificationStrategy(PromptResourceLoader promptLoader) {
        String base = promptLoader.load("read_prompts/classification/direct-system.md");
        String examples = promptLoader.load("read_prompts/classification/few-shot-examples.md");
        this.systemPrompt = base + "\n\n" + examples;
    }

    @Override
    public PromptVariant variant() {
        return PromptVariant.FEW_SHOT;
    }

    @Override
    public ResponseEntity<ChatResponse, RequestType> classify(String inputText, ChatClient client) {
        log.debug("Classifying using FEW_SHOT strategy, inputLength={}", inputText.length());
        ResponseEntity<ChatResponse, RequestType> response = client.prompt()
                .system(systemPrompt)
                .user(inputText)
                .call()
                .responseEntity(RequestType.class);
        log.debug("Classification result using FEW_SHOT strategy: {}", response.entity());
        return response;
    }
}
