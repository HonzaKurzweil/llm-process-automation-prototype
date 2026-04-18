package cz.vse.kurzweil.llm_process_automation_prototype.service.extraction;

import cz.vse.kurzweil.llm_process_automation_prototype.dto.newmobileorder.NewMobileOrderRequest;
import cz.vse.kurzweil.llm_process_automation_prototype.service.ExtractionMode;
import org.springframework.ai.chat.client.ChatClient;

public interface PromptStrategy {

    ExtractionMode mode();

    NewMobileOrderRequest extract(String inputText, ChatClient client);
}
