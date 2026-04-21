package cz.vse.kurzweil.llm_process_automation_prototype.service.classification.impl;

import cz.vse.kurzweil.llm_process_automation_prototype.dto.PromptVariant;
import cz.vse.kurzweil.llm_process_automation_prototype.dto.RequestType;
import org.springframework.ai.chat.client.ChatClient;

public interface ClassificationStrategy {

    PromptVariant variant();

    RequestType classify(String inputText, ChatClient client);
}
