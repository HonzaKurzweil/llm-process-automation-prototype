package cz.vse.kurzweil.llm_process_automation_prototype.service.extraction.impl;

import cz.vse.kurzweil.llm_process_automation_prototype.dto.ModelType;
import cz.vse.kurzweil.llm_process_automation_prototype.dto.PromptVariant;
import cz.vse.kurzweil.llm_process_automation_prototype.dto.RequestType;
import cz.vse.kurzweil.llm_process_automation_prototype.commons.LlmRateLimiter;
import cz.vse.kurzweil.llm_process_automation_prototype.service.extraction.ExtractionService;
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
public class ExtractionServiceImpl implements ExtractionService {

    private final Map<PromptVariant, ExtractionStrategy> strategies;
    private final Map<ModelType, ChatClient> clients;
    private final LlmRateLimiter rateLimiter;

    public ExtractionServiceImpl(
            List<ExtractionStrategy> strategies,
            Map<ModelType, ChatClient> clients,
            LlmRateLimiter rateLimiter) {
        this.strategies = strategies.stream()
                .collect(Collectors.toMap(ExtractionStrategy::variant, s -> s));
        this.clients = clients;
        this.rateLimiter = rateLimiter;
    }

    @Override
    public <T> ResponseEntity<ChatResponse, T> extract(String inputText, RequestType requestType, PromptVariant variant, ModelType model) {
        log.info("Extracting with responseEntity, requestType={}, variant={}, model={}", requestType, variant, model);
        return rateLimiter.execute(model, () -> strategies.get(variant).extractResponseEntity(inputText, requestType, clients.get(model)));
    }
}
