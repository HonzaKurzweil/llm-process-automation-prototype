package cz.vse.kurzweil.llm_process_automation_prototype.service;

public enum PromptVariant {
    DIRECT,
    FEW_SHOT,
    /** Not wired in the first iteration — reserved for a future chain-of-thought experiment. */
    CHAIN_OF_THOUGHT
}
