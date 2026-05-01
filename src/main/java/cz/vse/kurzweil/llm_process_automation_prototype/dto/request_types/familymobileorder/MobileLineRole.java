package cz.vse.kurzweil.llm_process_automation_prototype.dto.request_types.familymobileorder;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum MobileLineRole {
    PRIMARY("primary"),
    PARTNER("partner"),
    CHILD("child"),
    ADDITIONAL("additional"),
    UNKNOWN("unknown");

    private final String value;

    MobileLineRole(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static MobileLineRole fromValue(String value) {
        if (value == null) {
            return null;
        }
        String normalized = value.trim().toLowerCase();
        for (MobileLineRole role : values()) {
            if (role.value.equals(normalized)) {
                return role;
            }
        }
        throw new IllegalArgumentException("Unknown mobileLineRole: " + value);
    }
}
