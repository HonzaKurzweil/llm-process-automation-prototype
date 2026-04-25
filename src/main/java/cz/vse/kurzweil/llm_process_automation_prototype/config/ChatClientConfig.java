package cz.vse.kurzweil.llm_process_automation_prototype.config;

import cz.vse.kurzweil.llm_process_automation_prototype.dto.ModelType;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
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
    public Map<ModelType, ChatClient> chatClients(ChatClient.Builder builder) {
        return Arrays.stream(ModelType.values()).collect(Collectors.toMap(
                m -> m,
                m -> {
                    ChatClient.Builder b = builder.defaultOptions(
                            OpenAiChatOptions.builder()
                                    .model(m.getModelId())
                                    .build()
                    );
                    if (logAdvisorEnabled) {
                        b = b.defaultAdvisors(new SimpleLoggerAdvisor());
                    }
                    return b.build();
                }
        ));
    }
}
