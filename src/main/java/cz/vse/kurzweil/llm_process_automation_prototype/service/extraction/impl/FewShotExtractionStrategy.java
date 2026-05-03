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
public class FewShotExtractionStrategy implements ExtractionStrategy {

    private static final String DIRECT_EXTRACTION_PROMPT = "read_prompts/extraction/direct-system.md";
    private static final String EXTRACTION_FEW_SHOT_PROMPT = "read_prompts/extraction/few-shot-examples.md";

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

        String resolvedSystem = promptLoader.load(DIRECT_EXTRACTION_PROMPT)
                + "\n\n"
                + catalogService.generateCatalogMappings()
                + "\n\n"
                + promptLoader.load(EXTRACTION_FEW_SHOT_PROMPT);

        ResponseEntity<ChatResponse, T> response = client.prompt()
                .system(resolvedSystem)
                .user(inputText)
                .call()
                .responseEntity((Class<T>) requestType.getDtoClass());
        log.debug("Extraction complete using FEW_SHOT strategy for requestType={}", requestType);
        return response;
    }
}
