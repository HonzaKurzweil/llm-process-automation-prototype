package cz.vse.kurzweil.llm_process_automation_prototype.service.extraction.impl;

import cz.vse.kurzweil.llm_process_automation_prototype.dto.PromptVariant;
import cz.vse.kurzweil.llm_process_automation_prototype.dto.RequestType;
import cz.vse.kurzweil.llm_process_automation_prototype.service.CatalogService;
import cz.vse.kurzweil.llm_process_automation_prototype.service.PromptResourceLoader;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.ResponseEntity;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.stereotype.Component;

@Component
public class FewShotExtractionStrategy implements ExtractionStrategy {

    private final PromptResourceLoader promptLoader;
    private final CatalogService catalogService;

    public FewShotExtractionStrategy(PromptResourceLoader promptLoader, CatalogService catalogService) {
        this.promptLoader = promptLoader;
        this.catalogService = catalogService;
    }

    @Override
    public PromptVariant variant() {
        return PromptVariant.FEW_SHOT;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T extract(String inputText, RequestType requestType, ChatClient client) {
        String dir = requestType.getPromptDirectory();
        String template = promptLoader.load(dir + "/direct-system.md") + "\n\n" + promptLoader.load(dir + "/few-shot-examples.md");
        String catalogMappings = catalogService.generateCatalogMappings(requestType);
        return client.prompt()
                .system(s -> s.text(template).param("catalog_mappings", catalogMappings))
                .user(inputText)
                .call()
                .entity((Class<T>) requestType.getDtoClass());
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> ResponseEntity<ChatResponse, T> extractResponseEntity(String inputText, RequestType requestType, ChatClient client) {
        String dir = requestType.getPromptDirectory();
        String template = promptLoader.load(dir + "/direct-system.md") + "\n\n" + promptLoader.load(dir + "/few-shot-examples.md");
        String catalogMappings = catalogService.generateCatalogMappings(requestType);
        return client.prompt()
                .system(s -> s.text(template).param("catalog_mappings", catalogMappings))
                .user(inputText)
                .call()
                .responseEntity((Class<T>) requestType.getDtoClass());
    }
}
