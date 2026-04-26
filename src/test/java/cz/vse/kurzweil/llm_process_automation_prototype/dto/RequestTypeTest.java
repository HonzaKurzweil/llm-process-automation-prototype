package cz.vse.kurzweil.llm_process_automation_prototype.dto;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

class RequestTypeTest {

    @ParameterizedTest(name = "fromRequestTypeIdReference(\"{0}\") == {1}")
    @CsvSource({
            "rt_new_mobile_order,           RT_NEW_MOBILE_ORDER",
            "rt_family_mobile_order,        RT_FAMILY_MOBILE_ORDER",
            "rt_fixed_internet_with_hardware_order, RT_FIXED_INTERNET_WITH_HARDWARE_ORDER",
            "rt_internet_tv_bundle_order,   RT_INTERNET_TV_BUNDLE_ORDER",
            "rt_retention_discount_request, RT_RETENTION_DISCOUNT_REQUEST",
            "unclassifiable,                UNCLASSIFIABLE",
    })
    void fromRequestTypeIdReference_knownIds_resolveCorrectly(String input, String expectedName) {
        RequestType type = RequestType.fromRequestTypeIdReference(input);
        assertThat(type.name()).isEqualTo(expectedName);
    }

    @ParameterizedTest(name = "case-insensitive lookup for \"{0}\"")
    @CsvSource({
            "RT_NEW_MOBILE_ORDER",
            "Rt_New_Mobile_Order",
            "RT_NEW_MOBILE_ORDER",
    })
    void fromRequestTypeIdReference_caseInsensitive_resolves(String input) {
        assertThat(RequestType.fromRequestTypeIdReference(input)).isEqualTo(RequestType.RT_NEW_MOBILE_ORDER);
    }

    @Test
    void fromRequestTypeIdReference_unknownId_returnsUnclassifiable() {
        assertThat(RequestType.fromRequestTypeIdReference("completely_unknown")).isEqualTo(RequestType.UNCLASSIFIABLE);
    }

    @Test
    void fromRequestTypeIdReference_null_returnsUnclassifiable() {
        assertThat(RequestType.fromRequestTypeIdReference(null)).isEqualTo(RequestType.UNCLASSIFIABLE);
    }

    @Test
    void fromRequestTypeIdReference_emptyString_returnsUnclassifiable() {
        assertThat(RequestType.fromRequestTypeIdReference("")).isEqualTo(RequestType.UNCLASSIFIABLE);
    }

    @Test
    void allEnumValues_haveNonBlankRequestTypeIdReference() {
        for (RequestType type : RequestType.values()) {
            assertThat(type.getRequestTypeIdReference())
                    .as("requestTypeIdReference for %s", type)
                    .isNotBlank();
        }
    }

    @Test
    void nonUnclassifiableValues_haveAssociatedDtoClass() {
        for (RequestType type : RequestType.values()) {
            if (type == RequestType.UNCLASSIFIABLE) continue;
            assertThat(type.getDtoClass())
                    .as("dtoClass for %s", type)
                    .isNotNull();
            assertThat(type.getPromptDirectory())
                    .as("promptDirectory for %s", type)
                    .isNotBlank();
        }
    }
}
