package cz.vse.kurzweil.llm_process_automation_prototype.controller;

import cz.vse.kurzweil.llm_process_automation_prototype.dto.ModelType;
import cz.vse.kurzweil.llm_process_automation_prototype.dto.PromptVariant;
import cz.vse.kurzweil.llm_process_automation_prototype.service.validation.ClassificationValidationService;
import cz.vse.kurzweil.llm_process_automation_prototype.service.validation.ExtractionValidationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.Path;

@RestController
@RequestMapping("/api/validation")
@RequiredArgsConstructor
public class ValidationController {

    private final ExtractionValidationService extractionValidationService;
    private final ClassificationValidationService classificationValidationService;

    @PostMapping("/extraction-validation")
    public ResponseEntity<Void> validateExtraction(
            @RequestParam String inputPath,
            @RequestParam(defaultValue = "DIRECT") PromptVariant variant,
            @RequestParam(defaultValue = "GPT_4O_MINI") ModelType model) {
        extractionValidationService.validateExtractionService(Path.of(inputPath), variant, model);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/classification-validation")
    public ResponseEntity<Void> validateClassification(
            @RequestParam String inputPath,
            @RequestParam(defaultValue = "DIRECT") PromptVariant variant,
            @RequestParam(defaultValue = "GPT_4O_MINI") ModelType model) {
        classificationValidationService.validateClassificationService(Path.of(inputPath), variant, model);
        return ResponseEntity.ok().build();
    }
}
