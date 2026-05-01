package cz.vse.kurzweil.llm_process_automation_prototype.dto.request_types.familymobileorder;

import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import cz.vse.kurzweil.llm_process_automation_prototype.dto.request_types.CustomerStatus;

import java.util.List;

@JsonClassDescription("Procesní DTO pro rodinnou mobilní objednávku s více mobilními linkami ve společném rodinném tarifu.")
public record FamilyMobileOrderRequestDto(
        CustomerStatus customerStatus,
        @JsonPropertyDescription("Jméno zákazníka bez oslovení a titulů. Zachovej úplné jméno, pokud je ve vstupu uvedeno.")
        String customerName,
        @JsonPropertyDescription("Hlavní kontaktní telefon zákazníka normalizovaný do syntetického procesního formátu +420 XXXX XXX XXX. Pokud není uveden, vrať null.")
        String contactPhone,
        @JsonPropertyDescription("Kontaktní e-mail zákazníka normalizovaný na malá písmena. Pokud není uveden, vrať null.")
        String contactEmail,
        @JsonPropertyDescription("Katalogové ID rodinné mobilní služby podle doménového kontextu služeb. Pokud nelze jednoznačně určit, vrať null.")
        String familyTariffId,
        @JsonPropertyDescription("Počet mobilních linek v rodinné objednávce. Vyplň jako celé číslo, pokud je počet ve vstupu uveden nebo jednoznačně vyplývá z výčtu linek.")
        Integer mobileLinesCount,
        @JsonPropertyDescription("Strukturovaný seznam linek v rodinné objednávce. Každá položka popisuje jednu linku.")
        List<MobileLineDto> mobileLines,
        @JsonPropertyDescription("Délka závazku v měsících, pokud je uvedena. Bez závazku reprezentuj hodnotou 0. Pokud údaj chybí, vrať null.")
        Integer contractTermMonths,
        @JsonPropertyDescription("Seznam katalogových ID slev explicitně požadovaných pro rodinnou objednávku. Pokud žádná sleva není uvedena, vrať prázdný seznam.")
        List<String> requestedDiscountIds
) {
}
