package cz.vse.kurzweil.llm_process_automation_prototype.service;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum RequestType {
    RT_NEW_MOBILE_ORDER("rt_new_mobile_order"),
    RT_FAMILY_MOBILE_ORDER("rt_family_mobile_order"),
    RT_FIXED_INTERNET_WITH_HARDWARE_ORDER("rt_fixed_internet_with_hardware_order"),
    RT_INTERNET_TV_BUNDLE_ORDER("rt_internet_tv_bundle_order"),
    RT_RETENTION_DISCOUNT_REQUEST("rt_retention_discount_request"),
    UNCLASSIFIABLE("unclassifiable");

    private final String value;

    RequestType(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static RequestType fromValue(String value) {
        for (RequestType type : values()) {
            if (type.value.equalsIgnoreCase(value)) {
                return type;
            }
        }
        return UNCLASSIFIABLE;
    }
}
