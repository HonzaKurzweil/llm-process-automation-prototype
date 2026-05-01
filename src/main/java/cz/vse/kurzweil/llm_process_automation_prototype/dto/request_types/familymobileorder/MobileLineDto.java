package cz.vse.kurzweil.llm_process_automation_prototype.dto.request_types.familymobileorder;

import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import cz.vse.kurzweil.llm_process_automation_prototype.dto.request_types.DonorOperator;

@JsonClassDescription("Procesní popis jedné mobilní linky v rodinné objednávce.")
public record MobileLineDto(

        @JsonPropertyDescription("Pořadí linky v rodinné objednávce od 1 podle pořadí, ve kterém je linka ve vstupu popsána.")
        Integer lineIndex,
        MobileLineRole lineRole,
        @JsonPropertyDescription("Katalogové ID mobilní služby přiřazené této lince podle doménového kontextu služeb. Pokud není samostatně uvedeno, použij rodinný tarif z rodičovské objednávky.")
        String planServiceId,
        @JsonPropertyDescription("True, pokud se pro tuto linku požaduje přenos čísla. False, pokud je zřejmé, že se přenos nepožaduje. Pokud údaj chybí, vrať null.")
        Boolean portingRequested,
        @JsonPropertyDescription("Přenášené telefonní číslo linky normalizované do syntetického procesního formátu +420 XXXX XXX XXX. Vyplň jen při známém přenášeném čísle.")
        String portedNumber,
        DonorOperator donorOperator
) {
}
