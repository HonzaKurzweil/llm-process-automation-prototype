package cz.vse.kurzweil.llm_process_automation_prototype.service.extraction.impl;

import cz.vse.kurzweil.llm_process_automation_prototype.service.PromptVariant;
import cz.vse.kurzweil.llm_process_automation_prototype.service.RequestType;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.ResponseEntity;
import org.springframework.ai.chat.model.ChatResponse;

public interface ExtractionStrategy {

    PromptVariant variant();

    <T> T extract(String inputText, RequestType requestType, ChatClient client);

    <T> ResponseEntity<ChatResponse, T> extractResponseEntity(String inputText, RequestType requestType, ChatClient client);
}
