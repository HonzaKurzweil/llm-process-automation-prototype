package cz.vse.kurzweil.llm_process_automation_prototype.service.classification.impl;

import cz.vse.kurzweil.llm_process_automation_prototype.service.ModelType;
import cz.vse.kurzweil.llm_process_automation_prototype.service.PromptVariant;
import cz.vse.kurzweil.llm_process_automation_prototype.service.RequestType;
import cz.vse.kurzweil.llm_process_automation_prototype.service.classification.ClassificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ClassificationServiceImpl implements ClassificationService {

    private final Map<PromptVariant, ClassificationStrategy> strategies;
    private final Map<ModelType, ChatClient> clients;

    public ClassificationServiceImpl(
            List<ClassificationStrategy> strategies,
            ChatClient.Builder builder) {
        this.strategies = strategies.stream()
                .collect(Collectors.toMap(ClassificationStrategy::variant, s -> s));
        this.clients = Arrays.stream(ModelType.values()).collect(Collectors.toMap(
                m -> m,
                m -> builder.defaultOptions(OpenAiChatOptions.builder().model(m.getModelId()).build()).build()
        ));
    }

    @Override
    public RequestType classify(String inputText, PromptVariant variant, ModelType model) {
        log.info("Classifying with prompt variant={}, model={}", variant, model);
        return strategies.get(variant).classify(inputText, clients.get(model));
    }
}
