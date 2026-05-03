package cz.vse.kurzweil.llm_process_automation_prototype.dto.request_types;

import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@RequiredArgsConstructor
@JsonClassDescription("Stav zákazníka ve vztahu k FuturaTel CZ.")
public enum CustomerStatus {

    @JsonPropertyDescription("Nový zákazník.")
    NEW("new"),
    @JsonPropertyDescription("Stávající zákazník.")
    EXISTING("existing");

    private final String value;

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static CustomerStatus fromValue(String value) {
        if (value == null) {
            return null;
        }
        return Arrays.stream(values())
                .filter(status -> status.value.equalsIgnoreCase(value.trim()))
                .findFirst()
                .orElse(null);
    }
}
