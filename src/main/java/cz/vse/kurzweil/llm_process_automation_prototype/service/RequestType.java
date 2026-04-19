package cz.vse.kurzweil.llm_process_automation_prototype.service;

import cz.vse.kurzweil.llm_process_automation_prototype.dto.familymobileorder.FamilyMobileOrderRequestDto;
import cz.vse.kurzweil.llm_process_automation_prototype.dto.fixedinternetorder.FixedInternetHardwareOrderRequestDto;
import cz.vse.kurzweil.llm_process_automation_prototype.dto.internettvbundle.InternetTvBundleOrderRequestDto;
import cz.vse.kurzweil.llm_process_automation_prototype.dto.newmobileorder.SingleMobileOrderRequestDto;
import cz.vse.kurzweil.llm_process_automation_prototype.dto.retentiondiscount.RetentionDiscountRequestDto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RequestType {
    RT_NEW_MOBILE_ORDER("rt_new_mobile_order", SingleMobileOrderRequestDto.class, "read_prompts/extraction/rt_new_mobile_order"),
    RT_FAMILY_MOBILE_ORDER("rt_family_mobile_order", FamilyMobileOrderRequestDto.class, "read_prompts/extraction/rt_family_mobile_order"),
    RT_FIXED_INTERNET_WITH_HARDWARE_ORDER("rt_fixed_internet_with_hardware_order", FixedInternetHardwareOrderRequestDto.class, "read_prompts/extraction/rt_fixed_internet_with_hardware_order"),
    RT_INTERNET_TV_BUNDLE_ORDER("rt_internet_tv_bundle_order", InternetTvBundleOrderRequestDto.class, "read_prompts/extraction/rt_internet_tv_bundle_order"),
    RT_RETENTION_DISCOUNT_REQUEST("rt_retention_discount_request", RetentionDiscountRequestDto.class, "read_prompts/extraction/rt_retention_discount_request"),
    UNCLASSIFIABLE("unclassifiable", null, null);

    private final String requestTypeIdReference;
    private final Class<?> dtoClass;
    private final String promptDirectory;


    public static RequestType fromRequestTypeIdReference(String requestTypeIdReference) {
        for (RequestType type : values()) {
            if (type.requestTypeIdReference.equalsIgnoreCase(requestTypeIdReference)) {
                return type;
            }
        }
        return UNCLASSIFIABLE;
    }
}
