package cz.vse.kurzweil.llm_process_automation_prototype.service.extraction.impl;

import cz.vse.kurzweil.llm_process_automation_prototype.dto.PromptVariant;
import cz.vse.kurzweil.llm_process_automation_prototype.dto.RequestType;
import cz.vse.kurzweil.llm_process_automation_prototype.service.extraction.components.CatalogService;
import cz.vse.kurzweil.llm_process_automation_prototype.commons.PromptResourceLoader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.ResponseEntity;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.stereotype.Component;

@Slf4j
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
    public <T> ResponseEntity<ChatResponse, T> extractResponseEntity(String inputText, RequestType requestType, ChatClient client) {
        log.debug("Extracting using FEW_SHOT strategy for requestType={}, inputLength={}", requestType, inputText.length());
        String dir = requestType.getPromptDirectory();
        String resolvedSystem = promptLoader.load(dir + "/direct-system.md")
                + "\n\n" + catalogService.generateCatalogMappings()
                + "\n\n" + promptLoader.load(dir + "/few-shot-examples.md");
        ResponseEntity<ChatResponse, T> response = client.prompt()
                .system(resolvedSystem)
                .user(inputText)
                .call()
                .responseEntity((Class<T>) requestType.getDtoClass());
        log.debug("Extraction complete using FEW_SHOT strategy for requestType={}", requestType);
        return response;
    }
}
