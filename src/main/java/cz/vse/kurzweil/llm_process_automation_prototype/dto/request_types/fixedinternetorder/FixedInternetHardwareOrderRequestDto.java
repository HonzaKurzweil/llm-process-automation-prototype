package cz.vse.kurzweil.llm_process_automation_prototype.dto.request_types.fixedinternetorder;

import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import cz.vse.kurzweil.llm_process_automation_prototype.dto.request_types.CustomerStatus;

@JsonClassDescription("Procesní DTO pro objednávku pevného internetu s hardwarem nebo instalačním doplňkem.")
public record FixedInternetHardwareOrderRequestDto(

        CustomerStatus customerStatus,
        @JsonPropertyDescription("Jméno zákazníka.")
        String customerName,
        @JsonPropertyDescription("Hlavní kontaktní telefon zákazníka.")
        String contactPhone,
        @JsonPropertyDescription("Kontaktní e-mail zákazníka.")
        String contactEmail,
        @JsonPropertyDescription("Katalogové ID instalační adresy.")
        String installationAddressId,
        @JsonPropertyDescription("Katalogové ID objednávané internetové služby.")
        String internetServiceId,
        @JsonPropertyDescription("Katalogové ID routeru.")
        String routerProductId,
        @JsonPropertyDescription("Počet mesh jednotek.")
        Integer meshNodeQuantity,
        @JsonPropertyDescription("Indikátor, zda zákazník požaduje expresní instalaci.")
        Boolean expressInstallationRequested,
        @JsonPropertyDescription("Délka závazku v měsících.")
        Integer contractTermMonths
) {
}
