package cz.vse.kurzweil.llm_process_automation_prototype.service;

import cz.vse.kurzweil.llm_process_automation_prototype.dto.newmobileorder.NewMobileOrderRequest;
import cz.vse.kurzweil.llm_process_automation_prototype.service.extraction.PromptStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class StructuredOutputServiceImpl implements StructuredOutputService {

    private final Map<ExtractionMode, PromptStrategy> promptStrategies;
    private final Map<ModelType, ChatClient> modelClients;

    public StructuredOutputServiceImpl(
            List<PromptStrategy> strategies,
            @Qualifier("gpt4oMini") ChatClient gpt4oMiniClient,
            @Qualifier("gpt4o") ChatClient gpt4oClient) {
        this.promptStrategies = strategies.stream()
                .collect(Collectors.toMap(PromptStrategy::mode, s -> s));
        this.modelClients = Map.of(
                ModelType.GPT_4O_MINI, gpt4oMiniClient,
                ModelType.GPT_4O, gpt4oClient
        );
    }

    @Override
    public NewMobileOrderRequest extract(String inputText, ExtractionMode mode, ModelType model) {
        log.info("Extracting with mode={}, model={}", mode, model);
        return promptStrategies.get(mode).extract(inputText, modelClients.get(model));
    }
}
