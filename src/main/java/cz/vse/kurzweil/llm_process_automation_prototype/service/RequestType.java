package cz.vse.kurzweil.llm_process_automation_prototype.service;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import cz.vse.kurzweil.llm_process_automation_prototype.dto.familymobileorder.FamilyMobileOrderRequestDto;
import cz.vse.kurzweil.llm_process_automation_prototype.dto.fixedinternetorder.FixedInternetHardwareOrderRequestDto;
import cz.vse.kurzweil.llm_process_automation_prototype.dto.internettvbundle.InternetTvBundleOrderRequestDto;
import cz.vse.kurzweil.llm_process_automation_prototype.dto.newmobileorder.SingleMobileOrderRequestDto;
import cz.vse.kurzweil.llm_process_automation_prototype.dto.retentiondiscount.RetentionDiscountRequestDto;
import lombok.Getter;

public enum RequestType {
    RT_NEW_MOBILE_ORDER("rt_new_mobile_order", SingleMobileOrderRequestDto.class, "read_prompts/extraction/rt_new_mobile_order"),
    RT_FAMILY_MOBILE_ORDER("rt_family_mobile_order", FamilyMobileOrderRequestDto.class, "read_prompts/extraction/rt_family_mobile_order"),
    RT_FIXED_INTERNET_WITH_HARDWARE_ORDER("rt_fixed_internet_with_hardware_order", FixedInternetHardwareOrderRequestDto.class, "read_prompts/extraction/rt_fixed_internet_with_hardware_order"),
    RT_INTERNET_TV_BUNDLE_ORDER("rt_internet_tv_bundle_order", InternetTvBundleOrderRequestDto.class, "read_prompts/extraction/rt_internet_tv_bundle_order"),
    RT_RETENTION_DISCOUNT_REQUEST("rt_retention_discount_request", RetentionDiscountRequestDto.class, "read_prompts/extraction/rt_retention_discount_request"),
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
