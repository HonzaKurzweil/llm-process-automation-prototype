package cz.vse.kurzweil.llm_process_automation_prototype.dto.request_types.retentiondiscount;

import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import cz.vse.kurzweil.llm_process_automation_prototype.dto.request_types.CustomerStatus;

import java.util.List;

@JsonClassDescription("Procesní DTO pro retenční požadavek stávajícího zákazníka.")
public record RetentionDiscountRequestDto(

        CustomerStatus customerStatus,
        @JsonPropertyDescription("Jméno zákazníka.")
        String customerName,
        @JsonPropertyDescription("Hlavní kontaktní telefon zákazníka.")
        String contactPhone,
        @JsonPropertyDescription("Indikátor, zda se jedná o retenční případ nebo požadavek na udržení zákazníka.")
        Boolean retentionCase,
        @JsonPropertyDescription("Seznam katalogových ID současných služeb zákazníka.")
        List<String> currentServiceIds,
        @JsonPropertyDescription("Katalogové ID služby, které se požadovaná retenční úprava nebo sleva týká.")
        String targetServiceId,
        @JsonPropertyDescription("Seznam katalogových ID retenčních nebo jiných slev požadovaných ve vstupu.")
        List<String> requestedDiscountIds
) {
}
