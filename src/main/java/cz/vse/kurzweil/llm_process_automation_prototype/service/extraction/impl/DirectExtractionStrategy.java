package cz.vse.kurzweil.llm_process_automation_prototype.service.extraction.impl;

import cz.vse.kurzweil.llm_process_automation_prototype.commons.PromptResourceLoader;
import cz.vse.kurzweil.llm_process_automation_prototype.dto.PromptVariant;
import cz.vse.kurzweil.llm_process_automation_prototype.dto.RequestType;
import cz.vse.kurzweil.llm_process_automation_prototype.service.extraction.components.CatalogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.ResponseEntity;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.stereotype.Component;

@Slf4j
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
        log.debug("Extracting using DIRECT strategy for requestType={}, inputLength={}", requestType, inputText.length());
        String resolvedSystem = promptLoader.load(requestType.getPromptDirectory() + "/direct-system.md")
                + "\n\n" + catalogService.generateCatalogMappings();
        ResponseEntity<ChatResponse, T> response = client.prompt()
                .system(resolvedSystem)
                .user(inputText)
                .call()
                .responseEntity((Class<T>) requestType.getDtoClass());
        log.debug("Extraction complete using DIRECT strategy for requestType={}", requestType);
        return response;
    }
}
