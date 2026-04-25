package cz.vse.kurzweil.llm_process_automation_prototype.service.extraction.impl;

import cz.vse.kurzweil.llm_process_automation_prototype.dto.PromptVariant;
import cz.vse.kurzweil.llm_process_automation_prototype.dto.RequestType;
import cz.vse.kurzweil.llm_process_automation_prototype.service.CatalogService;
import cz.vse.kurzweil.llm_process_automation_prototype.service.PromptResourceLoader;
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
        String template = promptLoader.load(dir + "/direct-system.md") + "\n\n" + promptLoader.load(dir + "/few-shot-examples.md");
        String catalogMappings = catalogService.generateCatalogMappings();
        ResponseEntity<ChatResponse, T> response = client.prompt()
                .system(s -> s.text(template).param("catalog_mappings", catalogMappings))
                .user(inputText)
                .call()
                .responseEntity((Class<T>) requestType.getDtoClass());
        log.debug("Extraction complete using FEW_SHOT strategy for requestType={}", requestType);
        return response;
    }
}
