package cz.vse.kurzweil.llm_process_automation_prototype.service;

public enum ModelType {
    GPT_4O_MINI("gpt-4o-mini"),
    GPT_4O("gpt-4o");

    public final String modelId;

    ModelType(String modelId) {
        this.modelId = modelId;
    }
}
