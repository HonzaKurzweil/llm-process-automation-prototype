package cz.vse.kurzweil.llm_process_automation_prototype.service.extraction.impl;

import cz.vse.kurzweil.llm_process_automation_prototype.dto.ModelType;
import cz.vse.kurzweil.llm_process_automation_prototype.dto.PromptVariant;
import cz.vse.kurzweil.llm_process_automation_prototype.dto.RequestType;
import cz.vse.kurzweil.llm_process_automation_prototype.service.LlmRateLimiter;
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
    private final LlmRateLimiter rateLimiter;

    public StructuredExtractionServiceImpl(
            List<ExtractionStrategy> strategies,
            Map<ModelType, ChatClient> clients,
            LlmRateLimiter rateLimiter) {
        this.strategies = strategies.stream()
                .collect(Collectors.toMap(ExtractionStrategy::variant, s -> s));
        this.clients = clients;
        this.rateLimiter = rateLimiter;
    }

    @Override
    public <T> T extractJustDto(String inputText, RequestType requestType, PromptVariant variant, ModelType model) {
        log.info("Extracting with requestType={}, variant={}, model={}", requestType, variant, model);
        return rateLimiter.execute(() -> strategies.get(variant).extract(inputText, requestType, clients.get(model)));
    }

    @Override
    public <T> ResponseEntity<ChatResponse, T> extractResponseEntity(String inputText, RequestType requestType, PromptVariant variant, ModelType model) {
        log.info("Extracting with responseEntity, requestType={}, variant={}, model={}", requestType, variant, model);
        return rateLimiter.execute(() -> strategies.get(variant).extractResponseEntity(inputText, requestType, clients.get(model)));
    }
}
