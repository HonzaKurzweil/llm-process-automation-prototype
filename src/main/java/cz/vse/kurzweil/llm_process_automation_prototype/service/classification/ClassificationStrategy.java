package cz.vse.kurzweil.llm_process_automation_prototype.service.classification;

import cz.vse.kurzweil.llm_process_automation_prototype.service.ExtractionMode;
import cz.vse.kurzweil.llm_process_automation_prototype.service.RequestType;
import org.springframework.ai.chat.client.ChatClient;

public interface ClassificationStrategy {

    ExtractionMode mode();

    RequestType classify(String inputText, ChatClient client);
}
