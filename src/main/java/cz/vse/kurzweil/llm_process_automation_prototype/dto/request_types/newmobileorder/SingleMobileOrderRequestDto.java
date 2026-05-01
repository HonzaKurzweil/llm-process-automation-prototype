package cz.vse.kurzweil.llm_process_automation_prototype.dto.request_types.newmobileorder;

import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import cz.vse.kurzweil.llm_process_automation_prototype.dto.request_types.CustomerStatus;
import cz.vse.kurzweil.llm_process_automation_prototype.dto.request_types.DonorOperator;

import java.util.List;

@JsonClassDescription("Procesní DTO pro objednávku jedné samostatné mobilní služby. Vyplň pouze údaje vztahující se k této jedné mobilní objednávce.")
public record SingleMobileOrderRequestDto(

        CustomerStatus customerStatus,
        @JsonPropertyDescription("Jméno zákazníka bez oslovení a titulů. Zachovej úplné jméno, pokud je ve vstupu uvedeno.")
        String customerName,
        @JsonPropertyDescription("Hlavní kontaktní telefon zákazníka normalizovaný do syntetického procesního formátu +420 XXXX XXX XXX. Pokud není uveden, vrať null.")
        String contactPhone,
        @JsonPropertyDescription("Kontaktní e-mail zákazníka normalizovaný na malá písmena. Pokud není uveden, vrať null.")
        String contactEmail,
        @JsonPropertyDescription("Katalogové ID objednávané mobilní služby podle doménového kontextu služeb. Pokud nelze jednoznačně určit, vrať null.")
        String mobileTariffId,
        @JsonPropertyDescription("Délka závazku v měsících, pokud je uvedena. Bez závazku reprezentuj hodnotou 0. Pokud údaj chybí, vrať null.")
        Integer contractTermMonths,
        @JsonPropertyDescription("True, pokud zákazník požaduje přenos čísla. False, pokud je zřejmé, že přenos nepožaduje. Pokud údaj chybí, vrať null.")
        Boolean portingRequested,
        @JsonPropertyDescription("Přenášené telefonní číslo normalizované do syntetického procesního formátu +420 XXXX XXX XXX. Vyplň jen při známém přenášeném čísle.")
        String portedNumber,
        DonorOperator donorOperator,
        @JsonPropertyDescription("Seznam katalogových ID slev explicitně požadovaných pro tuto objednávku. Pokud žádná sleva není uvedena, vrať prázdný seznam.")
        List<String> requestedDiscountIds
) {
}
