package cz.vse.kurzweil.llm_process_automation_prototype.controller;

import cz.vse.kurzweil.llm_process_automation_prototype.dto.newmobileorder.NewMobileOrderRequest;
import cz.vse.kurzweil.llm_process_automation_prototype.service.ExtractionMode;
import cz.vse.kurzweil.llm_process_automation_prototype.service.ModelType;
import cz.vse.kurzweil.llm_process_automation_prototype.service.StructuredOutputService;
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

    private final StructuredOutputService structuredOutputService;

    @PostMapping("/extract")
    public ResponseEntity<NewMobileOrderRequest> extract(
            @RequestParam String input,
            @RequestParam(defaultValue = "DIRECT") ExtractionMode mode,
            @RequestParam(defaultValue = "GPT_4O_MINI") ModelType model) {
        return ResponseEntity.ok(structuredOutputService.extract(input, mode, model));
    }
}
