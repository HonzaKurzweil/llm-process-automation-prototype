package cz.vse.kurzweil.llm_process_automation_prototype.service.extraction;

import cz.vse.kurzweil.llm_process_automation_prototype.dto.ModelType;
import cz.vse.kurzweil.llm_process_automation_prototype.dto.PromptVariant;
import cz.vse.kurzweil.llm_process_automation_prototype.dto.RequestType;
import org.springframework.ai.chat.client.ResponseEntity;
import org.springframework.ai.chat.model.ChatResponse;

public interface StructuredExtractionService {

    <T> T extract(String inputText, RequestType requestType, PromptVariant variant, ModelType model);

    <T> ResponseEntity<ChatResponse, T> extractResponseEntity(String inputText, RequestType requestType, PromptVariant variant, ModelType model);
}
