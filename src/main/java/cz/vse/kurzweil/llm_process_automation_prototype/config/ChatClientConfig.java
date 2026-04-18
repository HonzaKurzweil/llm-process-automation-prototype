package cz.vse.kurzweil.llm_process_automation_prototype.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChatClientConfig {

    @Bean
    @Qualifier("gpt4oMini")
    ChatClient gpt4oMiniClient(ChatClient.Builder builder) {
        return builder
                .defaultOptions(OpenAiChatOptions.builder().model("gpt-4o-mini").build())
                .build();
    }

    @Bean
    @Qualifier("gpt4o")
    ChatClient gpt4oClient(ChatClient.Builder builder) {
        return builder
                .defaultOptions(OpenAiChatOptions.builder().model("gpt-4o").build())
                .build();
    }
}
