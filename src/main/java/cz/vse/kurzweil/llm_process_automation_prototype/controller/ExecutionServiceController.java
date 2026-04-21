package cz.vse.kurzweil.llm_process_automation_prototype.controller;

import cz.vse.kurzweil.llm_process_automation_prototype.dto.ModelType;
import cz.vse.kurzweil.llm_process_automation_prototype.dto.PromptVariant;
import cz.vse.kurzweil.llm_process_automation_prototype.service.execution.ExecutionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.Path;

@RestController
@RequestMapping("/api/execution")
@RequiredArgsConstructor
public class ExecutionServiceController {

    private final ExecutionService executionService;

    @PostMapping("/extraction-validation")
    public ResponseEntity<Void> classify(
            @RequestParam String inputPath,
            @RequestParam(defaultValue = "DIRECT") PromptVariant variant,
            @RequestParam(defaultValue = "GPT_4O_MINI") ModelType model) {
        executionService.validateExtractionService(Path.of(inputPath), variant, model);
        return ResponseEntity.ok().build();
    }
}
