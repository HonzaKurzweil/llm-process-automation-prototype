package cz.vse.kurzweil.llm_process_automation_prototype.service.registry;

import cz.vse.kurzweil.llm_process_automation_prototype.service.RequestType;

public record RequestTypeMetadata(
        RequestType requestType,
        Class<?> dtoClass,
        String promptDirectory
) {}
