package cz.vse.kurzweil.llm_process_automation_prototype.controller;

import cz.vse.kurzweil.llm_process_automation_prototype.dto.ModelType;
import cz.vse.kurzweil.llm_process_automation_prototype.dto.PromptVariant;
import cz.vse.kurzweil.llm_process_automation_prototype.dto.RequestType;
import cz.vse.kurzweil.llm_process_automation_prototype.service.classification.ClassificationService;
import cz.vse.kurzweil.llm_process_automation_prototype.service.extraction.ExtractionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/structured-output")
@RequiredArgsConstructor
public class StructuredOutputController {

    private final ClassificationService classificationService;
    private final ExtractionService extractionService;

    @PostMapping("/classify")
    public ResponseEntity<RequestType> classify(
            @RequestParam String input,
            @RequestParam(defaultValue = "DIRECT") PromptVariant variant,
            @RequestParam(defaultValue = "GPT_4O_MINI") ModelType model) {
        return ResponseEntity.ok(classificationService.classify(input, variant, model).entity());
    }

    @PostMapping("/extract")
    public ResponseEntity<Object> extract(
            @RequestParam String input,
            @RequestParam(defaultValue = "RT_NEW_MOBILE_ORDER") RequestType requestType,
            @RequestParam(defaultValue = "DIRECT") PromptVariant variant,
            @RequestParam(defaultValue = "GPT_4O_MINI") ModelType model) {
        return ResponseEntity.ok(extractionService.extract(input, requestType, variant, model));
    }
}
