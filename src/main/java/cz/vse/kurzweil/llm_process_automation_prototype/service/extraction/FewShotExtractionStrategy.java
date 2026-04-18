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
    public <T> T extract(String inputText, RequestType requestType, Class<T> dtoClass, ChatClient client) {
        String systemPrompt = promptLoader.load(
                "prompts/extraction/" + requestType.getValue() + "/few-shot-system.md");
        String examples = promptLoader.load(
                "prompts/extraction/" + requestType.getValue() + "/few-shot-examples.md");
        return client.prompt()
                .system(systemPrompt + "\n\n" + examples)
                .user(inputText)
                .call()
                .entity(dtoClass);
    }
}
