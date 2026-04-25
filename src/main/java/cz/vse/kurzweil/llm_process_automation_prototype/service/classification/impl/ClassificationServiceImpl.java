package cz.vse.kurzweil.llm_process_automation_prototype.service.classification.impl;

import cz.vse.kurzweil.llm_process_automation_prototype.dto.ModelType;
import cz.vse.kurzweil.llm_process_automation_prototype.dto.PromptVariant;
import cz.vse.kurzweil.llm_process_automation_prototype.dto.RequestType;
import cz.vse.kurzweil.llm_process_automation_prototype.service.LlmRateLimiter;
import cz.vse.kurzweil.llm_process_automation_prototype.service.classification.ClassificationService;
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
public class ClassificationServiceImpl implements ClassificationService {

    private final Map<PromptVariant, ClassificationStrategy> strategies;
    private final Map<ModelType, ChatClient> clients;
    private final LlmRateLimiter rateLimiter;

    public ClassificationServiceImpl(
            List<ClassificationStrategy> strategies,
            Map<ModelType, ChatClient> clients,
            LlmRateLimiter rateLimiter) {
        this.strategies = strategies.stream()
                .collect(Collectors.toMap(ClassificationStrategy::variant, s -> s));
        this.clients = clients;
        this.rateLimiter = rateLimiter;
    }

    @Override
    public ResponseEntity<ChatResponse, RequestType> classify(String inputText, PromptVariant variant, ModelType model) {
        log.info("Classifying with prompt variant={}, model={}", variant, model);
        ResponseEntity<ChatResponse, RequestType> response =
                rateLimiter.execute(() -> strategies.get(variant).classify(inputText, clients.get(model)));
        RequestType requestType = response.entity() != null ? response.entity() : RequestType.UNCLASSIFIABLE;
        return new ResponseEntity<>(response.response(), requestType);
    }
}
