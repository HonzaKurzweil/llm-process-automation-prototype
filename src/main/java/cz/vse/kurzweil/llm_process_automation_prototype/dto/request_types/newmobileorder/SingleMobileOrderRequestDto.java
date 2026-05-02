package cz.vse.kurzweil.llm_process_automation_prototype.dto.request_types.newmobileorder;

import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import cz.vse.kurzweil.llm_process_automation_prototype.dto.request_types.CustomerStatus;
import cz.vse.kurzweil.llm_process_automation_prototype.dto.request_types.DonorOperator;

import java.util.List;

@JsonClassDescription("Procesní DTO pro objednávku jedné samostatné mobilní služby.")
public record SingleMobileOrderRequestDto(

        CustomerStatus customerStatus,
        @JsonPropertyDescription("Jméno zákazníka.")
        String customerName,
        @JsonPropertyDescription("Hlavní kontaktní telefon zákazníka.")
        String contactPhone,
        @JsonPropertyDescription("Kontaktní e-mail zákazníka.")
        String contactEmail,
        @JsonPropertyDescription("Katalogové ID objednávané mobilní služby.")
        String mobileTariffId,
        @JsonPropertyDescription("Délka závazku v měsících.")
        Integer contractTermMonths,
        @JsonPropertyDescription("Indikátor, zda zákazník požaduje přenos čísla.")
        Boolean portingRequested,
        @JsonPropertyDescription("Přenášené telefonní číslo.")
        String portedNumber,
        DonorOperator donorOperator,
        @JsonPropertyDescription("Seznam katalogových ID slev požadovaných pro tuto objednávku.")
        List<String> requestedDiscountIds
) {
}
