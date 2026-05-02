package cz.vse.kurzweil.llm_process_automation_prototype.dto.request_types;

import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@RequiredArgsConstructor
@JsonClassDescription("Původní operátor přenášeného telefonního čísla.")
public enum DonorOperator {

    @JsonPropertyDescription("Operátor O2.")
    O2("o2"),
    @JsonPropertyDescription("Operátor Vodafone.")
    VODAFONE("vodafone"),
    @JsonPropertyDescription("Operátor T-Mobile.")
    T_MOBILE("t_mobile"),
    @JsonPropertyDescription("Původní operátor uveden nebo není rozpoznatelný.")
    UNKNOWN("unknown");

    private final String value;

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static DonorOperator fromValue(String value) {
        if (value == null) {
            return UNKNOWN;
        }
        return Arrays.stream(values())
                .filter(operator -> operator.value.equalsIgnoreCase(value.trim()))
                .findFirst()
                .orElse(UNKNOWN);
    }
}
