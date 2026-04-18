package cz.vse.kurzweil.llm_process_automation_prototype.service.extraction;

import cz.vse.kurzweil.llm_process_automation_prototype.service.ModelType;
import cz.vse.kurzweil.llm_process_automation_prototype.service.PromptVariant;
import cz.vse.kurzweil.llm_process_automation_prototype.service.RequestType;
import cz.vse.kurzweil.llm_process_automation_prototype.service.registry.RequestTypeRegistry;
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
public class StructuredExtractionServiceImpl implements StructuredExtractionService {

    private final Map<PromptVariant, ExtractionStrategy> strategies;
    private final RequestTypeRegistry registry;
    private final Map<ModelType, ChatClient> clients;

    public StructuredExtractionServiceImpl(
            List<ExtractionStrategy> strategies,
            RequestTypeRegistry registry,
            ChatClient.Builder builder) {
        this.strategies = strategies.stream()
                .collect(Collectors.toMap(ExtractionStrategy::variant, s -> s));
        this.registry = registry;
        this.clients = Arrays.stream(ModelType.values()).collect(Collectors.toMap(
                m -> m,
                m -> builder.defaultOptions(OpenAiChatOptions.builder().model(m.getModelId()).build()).build()
        ));
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T extract(String inputText, RequestType requestType, PromptVariant variant, ModelType model) {
        log.info("Extracting with requestType={}, variant={}, model={}", requestType, variant, model);
        return strategies.get(variant).extract(
                inputText, requestType,
                (Class<T>) registry.get(requestType).dtoClass(),
                clients.get(model)
        );
    }
}
