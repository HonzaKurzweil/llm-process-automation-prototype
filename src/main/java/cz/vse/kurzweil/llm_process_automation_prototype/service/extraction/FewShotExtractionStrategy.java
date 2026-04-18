package cz.vse.kurzweil.llm_process_automation_prototype.service.extraction;

import cz.vse.kurzweil.llm_process_automation_prototype.service.PromptVariant;
import cz.vse.kurzweil.llm_process_automation_prototype.service.RequestType;
import cz.vse.kurzweil.llm_process_automation_prototype.service.prompt.PromptResourceLoader;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Component;

@Component
public class FewShotExtractionStrategy implements ExtractionStrategy {

    private final PromptResourceLoader promptLoader;

    public FewShotExtractionStrategy(PromptResourceLoader promptLoader) {
        this.promptLoader = promptLoader;
    }

    @Override
    public PromptVariant variant() {
        return PromptVariant.FEW_SHOT;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T extract(String inputText, RequestType requestType, ChatClient client) {
        String dir = requestType.getPromptDirectory();
        String systemPrompt = promptLoader.load(dir + "/few-shot-system.md") + "\n\n"
                + promptLoader.load(dir + "/few-shot-examples.md");
        return client.prompt()
                .system(systemPrompt)
                .user(inputText)
                .call()
                .entity((Class<T>) requestType.getDtoClass());
    }
}
