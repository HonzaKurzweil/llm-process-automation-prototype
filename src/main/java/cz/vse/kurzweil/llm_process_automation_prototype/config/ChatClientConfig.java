package cz.vse.kurzweil.llm_process_automation_prototype.config;

import cz.vse.kurzweil.llm_process_automation_prototype.dto.ModelType;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.ollama.api.OllamaChatOptions;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Configuration
public class ChatClientConfig {

    @Value("${app.chat.log-advisor.enabled:false}")
    private boolean logAdvisorEnabled;

    @Bean
    public ChatClient.Builder chatClientBuilder(OpenAiChatModel openAiChatModel) {
        return ChatClient.builder(openAiChatModel);
    }

    @Bean
    @Qualifier("openAiChatClients")
    public Map<ModelType, ChatClient> openAiChatClients(ChatClient.Builder openAiBuilder) {
        return Arrays.stream(ModelType.values())
                .filter(m -> m.getProvider() == ModelType.Provider.OPENAI)
                .collect(Collectors.toMap(m -> m, m -> {
                    ChatClient.Builder b = openAiBuilder.defaultOptions(
                            OpenAiChatOptions.builder()
                                    .model(m.getModelId())
                                    .temperature(0.1)
                                    .build());
                    if (logAdvisorEnabled) b = b.defaultAdvisors(new SimpleLoggerAdvisor());
                    return b.build();
                }));
    }

    @Bean
    @Qualifier("ollamaChatClients")
    public Map<ModelType, ChatClient> ollamaChatClients(OllamaChatModel ollamaChatModel) {
        return Arrays.stream(ModelType.values())
                .filter(m -> m.getProvider() == ModelType.Provider.OLLAMA)
                .collect(Collectors.toMap(m -> m, m -> {
                    ChatClient.Builder b = ChatClient.builder(ollamaChatModel)
                            .defaultOptions(OllamaChatOptions.builder()
                                    .model(m.getModelId())
                                    .temperature(0.1)
                                    .build());
                    if (logAdvisorEnabled) b = b.defaultAdvisors(new SimpleLoggerAdvisor());
                    return b.build();
                }));
    }

    @Bean
    @Primary
    public Map<ModelType, ChatClient> chatClients(
            @Qualifier("openAiChatClients") Map<ModelType, ChatClient> openAiClients,
            @Qualifier("ollamaChatClients") Map<ModelType, ChatClient> ollamaClients) {
        Map<ModelType, ChatClient> all = new HashMap<>(openAiClients);
        all.putAll(ollamaClients);
        return all;
    }
}
