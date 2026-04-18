package cz.vse.kurzweil.llm_process_automation_prototype.service.extraction;

import cz.vse.kurzweil.llm_process_automation_prototype.service.PromptVariant;
import cz.vse.kurzweil.llm_process_automation_prototype.service.RequestType;
import org.springframework.ai.chat.client.ChatClient;

public interface ExtractionStrategy {

    PromptVariant variant();

    <T> T extract(String inputText, RequestType requestType, Class<T> dtoClass, ChatClient client);
}
