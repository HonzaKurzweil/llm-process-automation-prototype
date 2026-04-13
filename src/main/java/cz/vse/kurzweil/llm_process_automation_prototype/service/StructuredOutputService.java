package cz.vse.kurzweil.llm_process_automation_prototype.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class StructuredOutputService {

    public void sayHello(String input) {
        log.info("Generated greeting: {}", input);
    }
}
