package cz.vse.kurzweil.llm_process_automation_prototype.config;

import cz.vse.kurzweil.llm_process_automation_prototype.dto.ModelType;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.ollama.api.OllamaChatOptions;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@Configuration
public class ChatClientConfig {

    @Value("${app.chat.log-advisor.enabled:false}")
    private boolean logAdvisorEnabled;

    @Bean
    public Map<ModelType, ChatClient> chatClients(OpenAiChatModel openAiChatModel, OllamaChatModel ollamaChatModel) {
        return Arrays.stream(ModelType.values()).collect(Collectors.toMap(
                m -> m,
                m -> buildClient(m, openAiChatModel, ollamaChatModel)
        ));
    }

    private ChatClient buildClient(ModelType m, OpenAiChatModel openAiChatModel, OllamaChatModel ollamaChatModel) {
        ChatClient.Builder b = switch (m.getProvider()) {
            case OPENAI -> ChatClient.builder(openAiChatModel)
                    .defaultOptions(OpenAiChatOptions.builder()
                            .model(m.getModelId())
                            .temperature(0.1)
                            .build());
            case OLLAMA -> ChatClient.builder(ollamaChatModel)
                    .defaultOptions(OllamaChatOptions.builder()
                            .model(m.getModelId())
                            .temperature(0.1)
                            .build());
        };
        if (logAdvisorEnabled) {
            b = b.defaultAdvisors(new SimpleLoggerAdvisor());
        }
        return b.build();
    }
}
