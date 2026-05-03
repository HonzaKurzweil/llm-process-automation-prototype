package cz.vse.kurzweil.llm_process_automation_prototype.dto;

import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import cz.vse.kurzweil.llm_process_automation_prototype.dto.request_types.familymobileorder.FamilyMobileOrderRequestDto;
import cz.vse.kurzweil.llm_process_automation_prototype.dto.request_types.fixedinternetorder.FixedInternetHardwareOrderRequestDto;
import cz.vse.kurzweil.llm_process_automation_prototype.dto.request_types.internettvbundle.InternetTvBundleOrderRequestDto;
import cz.vse.kurzweil.llm_process_automation_prototype.dto.request_types.newmobileorder.SingleMobileOrderRequestDto;
import cz.vse.kurzweil.llm_process_automation_prototype.dto.request_types.retentiondiscount.RetentionDiscountRequestDto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
@JsonClassDescription("Výsledek klasifikace zákaznického požadavku FuturaTel CZ.")
public enum RequestType {

    @JsonPropertyDescription("Samostatná mobilní objednávka jedné mobilní linky pro nového nebo stávajícího zákazníka.")
    RT_NEW_MOBILE_ORDER("rt_new_mobile_order", SingleMobileOrderRequestDto.class),
    @JsonPropertyDescription("Rodinná mobilní objednávka zahrnující více mobilních linek ve společném rodinném tarifu.")
    RT_FAMILY_MOBILE_ORDER("rt_family_mobile_order", FamilyMobileOrderRequestDto.class),
    @JsonPropertyDescription("Objednávka pevného internetu, která zahrnuje také hardware nebo instalační doplněk.")
    RT_FIXED_INTERNET_WITH_HARDWARE_ORDER("rt_fixed_internet_with_hardware_order", FixedInternetHardwareOrderRequestDto.class),
    @JsonPropertyDescription("Objednávka kombinovaného balíčku pevného internetu a televizní služby.")
    RT_INTERNET_TV_BUNDLE_ORDER("rt_internet_tv_bundle_order", InternetTvBundleOrderRequestDto.class),
    @JsonPropertyDescription("Požadavek stávajícího zákazníka na retenční slevu nebo udržení služby.")
    RT_RETENTION_DISCOUNT_REQUEST("rt_retention_discount_request", RetentionDiscountRequestDto.class),
    @JsonPropertyDescription("Text nelze jednoznačně zařadit do právě jednoho podporovaného request type.")
    UNCLASSIFIABLE("unclassifiable", null);


    private final String requestTypeIdReference;
    private final Class<?> dtoClass;

    @JsonCreator
    public static RequestType fromRequestTypeIdReference(String requestTypeIdReference) {
        if (requestTypeIdReference == null) {
            return UNCLASSIFIABLE;
        }
        return Arrays.stream(values())
                .filter(type -> type.requestTypeIdReference.equalsIgnoreCase(requestTypeIdReference.trim()))
                .findFirst()
                .orElse(UNCLASSIFIABLE);
    }
}
