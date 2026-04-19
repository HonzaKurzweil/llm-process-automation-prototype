package cz.vse.kurzweil.llm_process_automation_prototype.service;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import cz.vse.kurzweil.llm_process_automation_prototype.dto.familymobileorder.FamilyMobileOrderRequest;
import cz.vse.kurzweil.llm_process_automation_prototype.dto.fixedinternetorder.FixedInternetWithHardwareOrderRequest;
import cz.vse.kurzweil.llm_process_automation_prototype.dto.internettvbundle.InternetTvBundleOrderRequest;
import cz.vse.kurzweil.llm_process_automation_prototype.dto.newmobileorder.NewMobileOrderRequest;
import cz.vse.kurzweil.llm_process_automation_prototype.dto.retentiondiscount.RetentionDiscountRequest;
import lombok.Getter;

public enum RequestType {
    RT_NEW_MOBILE_ORDER("rt_new_mobile_order", NewMobileOrderRequest.class, "read_prompts/extraction/rt_new_mobile_order"),
    RT_FAMILY_MOBILE_ORDER("rt_family_mobile_order", FamilyMobileOrderRequest.class, "read_prompts/extraction/rt_family_mobile_order"),
    RT_FIXED_INTERNET_WITH_HARDWARE_ORDER("rt_fixed_internet_with_hardware_order", FixedInternetWithHardwareOrderRequest.class, "read_prompts/extraction/rt_fixed_internet_with_hardware_order"),
    RT_INTERNET_TV_BUNDLE_ORDER("rt_internet_tv_bundle_order", InternetTvBundleOrderRequest.class, "read_prompts/extraction/rt_internet_tv_bundle_order"),
    RT_RETENTION_DISCOUNT_REQUEST("rt_retention_discount_request", RetentionDiscountRequest.class, "read_prompts/extraction/rt_retention_discount_request"),
    UNCLASSIFIABLE("unclassifiable", null, null);

    private final String value;
    @Getter private final Class<?> dtoClass;
    @Getter private final String promptDirectory;

    RequestType(String value, Class<?> dtoClass, String promptDirectory) {
        this.value = value;
        this.dtoClass = dtoClass;
        this.promptDirectory = promptDirectory;
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
