package cz.vse.kurzweil.llm_process_automation_prototype.dto.request_types.internettvbundle;

import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import cz.vse.kurzweil.llm_process_automation_prototype.dto.request_types.CustomerStatus;

import java.util.List;

@JsonClassDescription("Procesní DTO pro kombinovanou objednávku pevného internetu a televizní služby.")
public record InternetTvBundleOrderRequestDto(

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
        @JsonPropertyDescription("Katalogové ID objednávané televizní služby.")
        String tvServiceId,
        @JsonPropertyDescription("Katalogové ID routeru.")
        String routerProductId,
        @JsonPropertyDescription("Počet set-top boxů.")
        Integer setTopBoxQuantity,
        @JsonPropertyDescription("Indikátor, zda zákazník požaduje sportovní televizní balíček.")
        Boolean sportsPackRequested,
        @JsonPropertyDescription("Délka závazku v měsících.")
        Integer contractTermMonths,
        @JsonPropertyDescription("Seznam katalogových ID slev požadovaných pro balíček internet a televize.")
        List<String> requestedDiscountIds
) {
}
