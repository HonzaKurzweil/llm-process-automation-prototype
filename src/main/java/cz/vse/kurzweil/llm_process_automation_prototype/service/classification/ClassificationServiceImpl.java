package cz.vse.kurzweil.llm_process_automation_prototype.service.classification;

import cz.vse.kurzweil.llm_process_automation_prototype.service.ModelType;
import cz.vse.kurzweil.llm_process_automation_prototype.service.PromptVariant;
import cz.vse.kurzweil.llm_process_automation_prototype.service.RequestType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

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
            @Qualifier("gpt4oMini") ChatClient gpt4oMiniClient,
            @Qualifier("gpt4o") ChatClient gpt4oClient) {
        this.strategies = strategies.stream()
                .collect(Collectors.toMap(ClassificationStrategy::variant, s -> s));
        this.clients = Map.of(
                ModelType.GPT_4O_MINI, gpt4oMiniClient,
                ModelType.GPT_4O, gpt4oClient
        );
    }

    @Override
    public RequestType classify(String inputText, PromptVariant variant, ModelType model) {
        log.info("Classifying with prompt variant={}, model={}", variant, model);
        return strategies.get(variant).classify(inputText, clients.get(model));
    }
}
