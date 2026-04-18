package cz.vse.kurzweil.llm_process_automation_prototype.service;

import cz.vse.kurzweil.llm_process_automation_prototype.dto.newmobileorder.NewMobileOrderRequest;

public interface StructuredOutputService {

    NewMobileOrderRequest extract(String inputText, ExtractionMode mode, ModelType model);

    RequestType classify(String inputText, ExtractionMode mode, ModelType model);
}
