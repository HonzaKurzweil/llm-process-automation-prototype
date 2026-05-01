package cz.vse.kurzweil.llm_process_automation_prototype.dto.request_types.retentiondiscount;

import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import cz.vse.kurzweil.llm_process_automation_prototype.dto.request_types.CustomerStatus;

import java.util.List;

@JsonClassDescription("Procesní DTO pro retenční požadavek stávajícího zákazníka. DTO neobsahuje volný důvod odchodu ani text konkurenční nabídky; extrahuje pouze procesně použitelné hodnoty.")
public record RetentionDiscountRequestDto(

        CustomerStatus customerStatus,
        @JsonPropertyDescription("Jméno zákazníka bez oslovení a titulů. Zachovej úplné jméno, pokud je ve vstupu uvedeno.")
        String customerName,
        @JsonPropertyDescription("Hlavní kontaktní telefon zákazníka normalizovaný do syntetického procesního formátu +420 XXXX XXX XXX. Pokud není uveden, vrať null.")
        String contactPhone,
        @JsonPropertyDescription("True, pokud jde o retenční případ nebo požadavek na udržení zákazníka. Pokud to ze vstupu nevyplývá, vrať null.")
        Boolean retentionCase,
        @JsonPropertyDescription("Seznam katalogových ID současných služeb zákazníka uvedených ve vstupu podle doménového kontextu služeb. Pokud žádná současná služba není uvedena, vrať prázdný seznam.")
        List<String> currentServiceIds,
        @JsonPropertyDescription("Katalogové ID služby, které se požadovaná retenční úprava nebo sleva týká. Pokud nelze jednoznačně určit, vrať null.")
        String targetServiceId,
        @JsonPropertyDescription("Seznam katalogových ID retenčních nebo jiných slev explicitně požadovaných ve vstupu. Pokud žádná sleva není uvedena, vrať prázdný seznam.")
        List<String> requestedDiscountIds
) {
}
