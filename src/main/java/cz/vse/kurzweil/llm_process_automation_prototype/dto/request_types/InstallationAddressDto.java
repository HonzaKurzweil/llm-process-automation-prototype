package cz.vse.kurzweil.llm_process_automation_prototype.dto.request_types;

import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;

@JsonClassDescription("Instalační adresa.")
public record InstallationAddressDto(
        @JsonPropertyDescription("PSČ.")
        String psc,
        @JsonPropertyDescription("Město.")
        String city,
        @JsonPropertyDescription("Název ulice.")
        String street,
        @JsonPropertyDescription("Číslo popisné.")
        String houseNumber
) {
}
