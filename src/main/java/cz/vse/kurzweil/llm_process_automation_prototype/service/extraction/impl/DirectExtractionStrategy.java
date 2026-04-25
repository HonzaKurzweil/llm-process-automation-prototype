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
public class DirectExtractionStrategy implements ExtractionStrategy {

    private final PromptResourceLoader promptLoader;
    private final CatalogService catalogService;

    public DirectExtractionStrategy(PromptResourceLoader promptLoader, CatalogService catalogService) {
        this.promptLoader = promptLoader;
        this.catalogService = catalogService;
    }

    @Override
    public PromptVariant variant() {
        return PromptVariant.DIRECT;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> ResponseEntity<ChatResponse, T> extractResponseEntity(String inputText, RequestType requestType, ChatClient client) {
        String template = promptLoader.load(requestType.getPromptDirectory() + "/direct-system.md");
        String catalogMappings = catalogService.generateCatalogMappings(requestType);
        return client.prompt()
                .system(s -> s.text(template).param("catalog_mappings", catalogMappings))
                .user(inputText)
                .call()
                .responseEntity((Class<T>) requestType.getDtoClass());
    }
}
