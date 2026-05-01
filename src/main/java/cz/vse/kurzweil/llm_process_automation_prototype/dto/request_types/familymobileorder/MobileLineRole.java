package cz.vse.kurzweil.llm_process_automation_prototype.dto.request_types.familymobileorder;

import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@RequiredArgsConstructor
@JsonClassDescription("Procesní role mobilní linky v rodinné objednávce. Hodnotu mapuj podle doménového kontextu a podle významu textu.")
public enum MobileLineRole {

    @JsonPropertyDescription("Hlavní nebo primární linka v rodinné objednávce.")
    PRIMARY("primary"),
    @JsonPropertyDescription("Partnerská nebo druhá dospělá linka.")
    PARTNER("partner"),
    @JsonPropertyDescription("Linka určená pro dítě.")
    CHILD("child"),
    @JsonPropertyDescription("Další doplňková linka bez přesnější role.")
    ADDITIONAL("additional"),
    @JsonPropertyDescription("Role linky není ve vstupu uvedena nebo není rozpoznatelná.")
    UNKNOWN("unknown");

    private final String value;

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static MobileLineRole fromValue(String value) {
        return Arrays.stream(values())
                .filter(role -> role.value.equalsIgnoreCase(value.trim()))
                .findFirst()
                .orElse(UNKNOWN);
    }
}
