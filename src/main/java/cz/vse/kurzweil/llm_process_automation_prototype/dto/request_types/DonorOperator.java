package cz.vse.kurzweil.llm_process_automation_prototype.dto.request_types;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum DonorOperator {
    O2("o2"),
    VODAFONE("vodafone"),
    T_MOBILE("t_mobile"),
    UNKNOWN("unknown");

    private final String value;

    DonorOperator(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static DonorOperator fromValue(String value) {
        if (value == null) {
            return null;
        }
        String normalized = value.trim().toLowerCase();
        for (DonorOperator operator : values()) {
            if (operator.value.equals(normalized)) {
                return operator;
            }
        }
        throw new IllegalArgumentException("Unknown donorOperator: " + value);
    }
}
