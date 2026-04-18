package cz.vse.kurzweil.llm_process_automation_prototype.service.registry;

import cz.vse.kurzweil.llm_process_automation_prototype.dto.familymobileorder.FamilyMobileOrderRequest;
import cz.vse.kurzweil.llm_process_automation_prototype.dto.fixedinternetorder.FixedInternetWithHardwareOrderRequest;
import cz.vse.kurzweil.llm_process_automation_prototype.dto.internettvbundle.InternetTvBundleOrderRequest;
import cz.vse.kurzweil.llm_process_automation_prototype.dto.newmobileorder.NewMobileOrderRequest;
import cz.vse.kurzweil.llm_process_automation_prototype.dto.retentiondiscount.RetentionDiscountRequest;
import cz.vse.kurzweil.llm_process_automation_prototype.service.RequestType;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;

import static cz.vse.kurzweil.llm_process_automation_prototype.service.RequestType.RT_FAMILY_MOBILE_ORDER;
import static cz.vse.kurzweil.llm_process_automation_prototype.service.RequestType.RT_FIXED_INTERNET_WITH_HARDWARE_ORDER;
import static cz.vse.kurzweil.llm_process_automation_prototype.service.RequestType.RT_INTERNET_TV_BUNDLE_ORDER;
import static cz.vse.kurzweil.llm_process_automation_prototype.service.RequestType.RT_NEW_MOBILE_ORDER;
import static cz.vse.kurzweil.llm_process_automation_prototype.service.RequestType.RT_RETENTION_DISCOUNT_REQUEST;

@Component
public class RequestTypeRegistry {

    private final Map<RequestType, RequestTypeMetadata> registry = new EnumMap<>(RequestType.class);

    public RequestTypeRegistry() {

        register(RT_NEW_MOBILE_ORDER,
                NewMobileOrderRequest.class,
                "prompts/extraction/rt_new_mobile_order");
        register(RT_FAMILY_MOBILE_ORDER,
                FamilyMobileOrderRequest.class,
                "prompts/extraction/rt_family_mobile_order");
        register(RT_FIXED_INTERNET_WITH_HARDWARE_ORDER,
                FixedInternetWithHardwareOrderRequest.class,
                "prompts/extraction/rt_fixed_internet_with_hardware_order");
        register(RT_INTERNET_TV_BUNDLE_ORDER,
                InternetTvBundleOrderRequest.class,
                "prompts/extraction/rt_internet_tv_bundle_order");
        register(RT_RETENTION_DISCOUNT_REQUEST,
                RetentionDiscountRequest.class,
                "prompts/extraction/rt_retention_discount_request");
    }

    private void register(RequestType type, Class<?> dtoClass, String promptDirectory) {
        registry.put(type, new RequestTypeMetadata(type, dtoClass, promptDirectory));
    }

    public RequestTypeMetadata get(RequestType type) {
        return Objects.requireNonNull(registry.get(type), "No registry entry for RequestType: " + type);
    }
}
