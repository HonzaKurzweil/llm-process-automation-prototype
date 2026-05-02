package cz.vse.kurzweil.llm_process_automation_prototype.dto.request_types.familymobileorder;

import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import cz.vse.kurzweil.llm_process_automation_prototype.dto.request_types.DonorOperator;

@JsonClassDescription("Procesní popis jedné mobilní linky v rodinné objednávce.")
public record MobileLineDto(

        @JsonPropertyDescription("Pořadí linky v rodinné objednávce, číslováno od 1.")
        Integer lineIndex,
        MobileLineRole lineRole,
        @JsonPropertyDescription("Katalogové ID mobilní služby přiřazené této lince.")
        String planServiceId,
        @JsonPropertyDescription("Indikátor, zda je pro tuto linku požadován přenos čísla.")
        Boolean portingRequested,
        @JsonPropertyDescription("Přenášené telefonní číslo linky.")
        String portedNumber,
        DonorOperator donorOperator
) {
}
