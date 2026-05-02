package cz.vse.kurzweil.llm_process_automation_prototype.dto.request_types.familymobileorder;

import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import cz.vse.kurzweil.llm_process_automation_prototype.dto.request_types.CustomerStatus;

import java.util.List;

@JsonClassDescription("Procesní DTO pro rodinnou mobilní objednávku s více mobilními linkami ve společném rodinném tarifu.")
public record FamilyMobileOrderRequestDto(
        CustomerStatus customerStatus,
        @JsonPropertyDescription("Jméno zákazníka.")
        String customerName,
        @JsonPropertyDescription("Hlavní kontaktní telefon zákazníka.")
        String contactPhone,
        @JsonPropertyDescription("Kontaktní e-mail zákazníka.")
        String contactEmail,
        @JsonPropertyDescription("Katalogové ID rodinné mobilní služby.")
        String familyTariffId,
        @JsonPropertyDescription("Počet mobilních linek v rodinné objednávce.")
        Integer mobileLinesCount,
        @JsonPropertyDescription("Seznam linek v rodinné objednávce.")
        List<MobileLineDto> mobileLines,
        @JsonPropertyDescription("Délka závazku v měsících.")
        Integer contractTermMonths,
        @JsonPropertyDescription("Seznam katalogových ID slev požadovaných pro rodinnou objednávku.")
        List<String> requestedDiscountIds
) {
}
