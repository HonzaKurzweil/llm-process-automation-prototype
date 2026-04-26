package cz.vse.kurzweil.llm_process_automation_prototype.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ModelType {
    GPT_5_MINI("gpt-5-mini", Provider.OPENAI),
    GPT_5_NANO("gpt-5-nano", Provider.OPENAI),
    GPT_4O_MINI("gpt-4o-mini", Provider.OPENAI),
    GPT_4O("gpt-4o", Provider.OPENAI),
    GEMMA3_1B("gemma3:1b", Provider.OLLAMA),
    GEMMA3_12B("gemma3:12b", Provider.OLLAMA),
    GEMMA3_27B("gemma3:27b", Provider.OLLAMA);

    private final String modelId;
    private final Provider provider;

    public enum Provider { OPENAI, OLLAMA }
}
