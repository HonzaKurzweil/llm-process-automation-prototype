package cz.vse.kurzweil.llm_process_automation_prototype.service.extraction;

import cz.vse.kurzweil.llm_process_automation_prototype.dto.NewMobileOrderRequest;
import cz.vse.kurzweil.llm_process_automation_prototype.service.ExtractionMode;

public interface ExtractionStrategy {

    ExtractionMode mode();

    NewMobileOrderRequest extract(String inputText);
}
