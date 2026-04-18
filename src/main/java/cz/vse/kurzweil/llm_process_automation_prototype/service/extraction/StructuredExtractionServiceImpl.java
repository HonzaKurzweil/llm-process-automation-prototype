package cz.vse.kurzweil.llm_process_automation_prototype.service.extraction;

import cz.vse.kurzweil.llm_process_automation_prototype.service.ModelType;
import cz.vse.kurzweil.llm_process_automation_prototype.service.PromptVariant;
import cz.vse.kurzweil.llm_process_automation_prototype.service.RequestType;
import cz.vse.kurzweil.llm_process_automation_prototype.service.registry.RequestTypeMetadata;
import cz.vse.kurzweil.llm_process_automation_prototype.service.registry.RequestTypeRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class StructuredExtractionServiceImpl implements StructuredExtractionService {

    private final Map<PromptVariant, ExtractionStrategy> strategies;
    private final RequestTypeRegistry registry;
    private final Map<ModelType, ChatClient> clients;

    public StructuredExtractionServiceImpl(
            List<ExtractionStrategy> strategies,
            RequestTypeRegistry registry,
            @Qualifier("gpt4oMini") ChatClient gpt4oMiniClient,
            @Qualifier("gpt4o") ChatClient gpt4oClient) {
        this.strategies = strategies.stream()
                .collect(Collectors.toMap(ExtractionStrategy::variant, s -> s));
        this.registry = registry;
        this.clients = Map.of(
                ModelType.GPT_4O_MINI, gpt4oMiniClient,
                ModelType.GPT_4O, gpt4oClient
        );
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T extract(String inputText, RequestType requestType, PromptVariant variant, ModelType model) {
        log.info("Extracting with requestType={}, variant={}, model={}", requestType, variant, model);
        RequestTypeMetadata metadata = registry.get(requestType);
        ExtractionStrategy strategy = strategies.get(variant);
        ChatClient client = clients.get(model);
        return strategy.extract(inputText, requestType, (Class<T>) metadata.dtoClass(), client);
    }
}
