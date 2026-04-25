package cz.vse.kurzweil.llm_process_automation_prototype.service.classification.impl;

import cz.vse.kurzweil.llm_process_automation_prototype.dto.PromptVariant;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.ResponseEntity;
import org.springframework.ai.chat.model.ChatResponse;

public interface ClassificationStrategy {

    PromptVariant variant();

    ResponseEntity<ChatResponse, ClassificationResponse> classify(String inputText, ChatClient client);
}
