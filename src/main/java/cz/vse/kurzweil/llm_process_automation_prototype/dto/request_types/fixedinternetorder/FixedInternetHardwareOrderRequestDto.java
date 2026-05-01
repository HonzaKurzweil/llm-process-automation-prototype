package cz.vse.kurzweil.llm_process_automation_prototype.dto.request_types.fixedinternetorder;

import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import cz.vse.kurzweil.llm_process_automation_prototype.dto.request_types.CustomerStatus;

@JsonClassDescription("Procesní DTO pro objednávku pevného internetu s hardwarem nebo instalačním doplňkem.")
public record FixedInternetHardwareOrderRequestDto(

        CustomerStatus customerStatus,
        @JsonPropertyDescription("Jméno zákazníka bez oslovení a titulů. Zachovej úplné jméno, pokud je ve vstupu uvedeno.")
        String customerName,
        @JsonPropertyDescription("Hlavní kontaktní telefon zákazníka normalizovaný do syntetického procesního formátu +420 XXXX XXX XXX. Pokud není uveden, vrať null.")
        String contactPhone,
        @JsonPropertyDescription("Kontaktní e-mail zákazníka normalizovaný na malá písmena. Pokud není uveden, vrať null.")
        String contactEmail,
        @JsonPropertyDescription("Katalogové ID instalační adresy podle doménového kontextu adres. Pokud adresa není uvedena nebo nelze jednoznačně určit, vrať null.")
        String installationAddressId,
        @JsonPropertyDescription("Katalogové ID objednávané internetové služby podle doménového kontextu služeb. Pokud nelze jednoznačně určit, vrať null.")
        String internetServiceId,
        @JsonPropertyDescription("Katalogové ID routeru podle doménového kontextu produktů. Pokud router není uveden nebo nelze jednoznačně určit, vrať null.")
        String routerProductId,
        @JsonPropertyDescription("Počet mesh jednotek. Pokud mesh jednotky nejsou požadovány, vrať 0. Pokud údaj chybí, vrať null.")
        Integer meshNodeQuantity,
        @JsonPropertyDescription("True, pokud zákazník explicitně požaduje expresní nebo rychlou instalaci. False, pokud je zřejmé, že ji nepožaduje. Pokud údaj chybí, vrať null.")
        Boolean expressInstallationRequested,
        @JsonPropertyDescription("Délka závazku v měsících, pokud je uvedena. Bez závazku reprezentuj hodnotou 0. Pokud údaj chybí, vrať null.")
        Integer contractTermMonths
) {
}
