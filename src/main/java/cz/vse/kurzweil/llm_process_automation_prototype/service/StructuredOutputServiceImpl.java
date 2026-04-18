package cz.vse.kurzweil.llm_process_automation_prototype.service;

import cz.vse.kurzweil.llm_process_automation_prototype.dto.newmobileorder.NewMobileOrderRequest;
import cz.vse.kurzweil.llm_process_automation_prototype.service.classification.ClassificationStrategy;
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

    private final Map<ExtractionMode, PromptStrategy> extractionStrategies;
    private final Map<ExtractionMode, ClassificationStrategy> classificationStrategies;
    private final Map<ModelType, ChatClient> modelClients;

    public StructuredOutputServiceImpl(
            List<PromptStrategy> extractionStrategies,
            List<ClassificationStrategy> classificationStrategies,
            @Qualifier("gpt4oMini") ChatClient gpt4oMiniClient,
            @Qualifier("gpt4o") ChatClient gpt4oClient) {
        this.extractionStrategies = extractionStrategies.stream()
                .collect(Collectors.toMap(PromptStrategy::mode, s -> s));
        this.classificationStrategies = classificationStrategies.stream()
                .collect(Collectors.toMap(ClassificationStrategy::mode, s -> s));
        this.modelClients = Map.of(
                ModelType.GPT_4O_MINI, gpt4oMiniClient,
                ModelType.GPT_4O, gpt4oClient
        );
    }

    @Override
    public NewMobileOrderRequest extract(String inputText, ExtractionMode mode, ModelType model) {
        log.info("Extracting with mode={}, model={}", mode, model);
        return extractionStrategies.get(mode).extract(inputText, modelClients.get(model));
    }

    @Override
    public RequestType classify(String inputText, ExtractionMode mode, ModelType model) {
        log.info("Classifying with mode={}, model={}", mode, model);
        return classificationStrategies.get(mode).classify(inputText, modelClients.get(model));
    }
}
