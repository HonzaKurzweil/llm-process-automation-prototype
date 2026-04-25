package cz.vse.kurzweil.llm_process_automation_prototype.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ModelType {
    GPT_5_MINI("gpt-5-mini"),
    GPT_5_NANO("gpt-5-nano"),
    GPT_4O_MINI("gpt-4o-mini"),
    GPT_4O("gpt-4o");

    private final String modelId;
}
