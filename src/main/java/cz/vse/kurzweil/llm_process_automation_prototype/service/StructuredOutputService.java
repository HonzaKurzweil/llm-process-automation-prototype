package cz.vse.kurzweil.llm_process_automation_prototype.service;

import cz.vse.kurzweil.llm_process_automation_prototype.dto.NewMobileOrderRequest;
import cz.vse.kurzweil.llm_process_automation_prototype.service.extraction.ExtractionStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class StructuredOutputService {

    private final Map<ExtractionMode, ExtractionStrategy> strategies;

    public StructuredOutputService(List<ExtractionStrategy> strategies) {
        this.strategies = strategies.stream()
                .collect(Collectors.toMap(ExtractionStrategy::mode, strategy -> strategy));
    }

    public NewMobileOrderRequest extract(String inputText, ExtractionMode mode) {
        log.info("Extracting with mode={}", mode);
        return strategies.get(mode).extract(inputText);
    }
}
