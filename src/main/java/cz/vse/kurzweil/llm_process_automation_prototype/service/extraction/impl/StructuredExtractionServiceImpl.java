package cz.vse.kurzweil.llm_process_automation_prototype.service.extraction.impl;

import cz.vse.kurzweil.llm_process_automation_prototype.service.ModelType;
import cz.vse.kurzweil.llm_process_automation_prototype.service.PromptVariant;
import cz.vse.kurzweil.llm_process_automation_prototype.service.RequestType;
import cz.vse.kurzweil.llm_process_automation_prototype.service.extraction.StructuredExtractionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.ResponseEntity;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class StructuredExtractionServiceImpl implements StructuredExtractionService {

    private final Map<PromptVariant, ExtractionStrategy> strategies;
    private final Map<ModelType, ChatClient> clients;

    public StructuredExtractionServiceImpl(
            List<ExtractionStrategy> strategies,
            Map<ModelType, ChatClient> clients) {
        this.strategies = strategies.stream()
                .collect(Collectors.toMap(ExtractionStrategy::variant, s -> s));
        this.clients = clients;
    }

    @Override
    public <T> T extract(String inputText, RequestType requestType, PromptVariant variant, ModelType model) {
        log.info("Extracting with requestType={}, variant={}, model={}", requestType, variant, model);
        Object extract = strategies.get(variant).extract(inputText, requestType, clients.get(model));
        return (T) extract;
    }

    @Override
    public <T> ResponseEntity<ChatResponse, T> extractResponseEntity(String inputText, RequestType requestType, PromptVariant variant, ModelType model) {
        log.info("Extracting with responseEntity, requestType={}, variant={}, model={}", requestType, variant, model);
        return strategies.get(variant).extractResponseEntity(inputText, requestType, clients.get(model));
    }
}
