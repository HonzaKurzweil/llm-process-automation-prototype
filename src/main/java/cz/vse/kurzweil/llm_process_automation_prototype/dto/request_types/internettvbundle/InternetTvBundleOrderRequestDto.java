package cz.vse.kurzweil.llm_process_automation_prototype.dto.request_types.internettvbundle;

import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import cz.vse.kurzweil.llm_process_automation_prototype.dto.request_types.CustomerStatus;

import java.util.List;

@JsonClassDescription("Procesní DTO pro kombinovanou objednávku pevného internetu a televizní služby.")
public record InternetTvBundleOrderRequestDto(

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
        @JsonPropertyDescription("Katalogové ID objednávané televizní služby podle doménového kontextu služeb. Pokud nelze jednoznačně určit, vrať null.")
        String tvServiceId,
        @JsonPropertyDescription("Katalogové ID routeru podle doménového kontextu produktů. Pokud router není uveden nebo nelze jednoznačně určit, vrať null.")
        String routerProductId,
        @JsonPropertyDescription("Počet set-top boxů. Pokud set-top box není požadován, vrať 0. Pokud údaj chybí, vrať null.")
        Integer setTopBoxQuantity,
        @JsonPropertyDescription("True, pokud zákazník explicitně požaduje sportovní televizní balíček. False, pokud je zřejmé, že jej nepožaduje. Pokud údaj chybí, vrať null.")
        Boolean sportsPackRequested,
        @JsonPropertyDescription("Délka závazku v měsících, pokud je uvedena. Bez závazku reprezentuj hodnotou 0. Pokud údaj chybí, vrať null.")
        Integer contractTermMonths,
        @JsonPropertyDescription("Seznam katalogových ID slev explicitně požadovaných pro balíček internet a televize. Pokud žádná sleva není uvedena, vrať prázdný seznam.")
        List<String> requestedDiscountIds
) {
}
