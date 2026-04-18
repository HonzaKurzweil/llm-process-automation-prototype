package cz.vse.kurzweil.llm_process_automation_prototype.service.extraction;

import cz.vse.kurzweil.llm_process_automation_prototype.dto.NewMobileOrderRequest;
import cz.vse.kurzweil.llm_process_automation_prototype.service.ExtractionMode;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Component;

/**
 * Few-shot extraction: the system prompt includes two labeled input/output examples
 * drawn from the rt_new_mobile_order reference dataset before the actual input is sent.
 * Examples demonstrate both an incomplete case (ported number missing) and a complete valid case.
 */
@Component
public class FewShotExtractionStrategy implements PromptStrategy {

    private static final String SYSTEM_PROMPT = """
            You are a data extraction assistant for FuturaTel CZ, a Czech telecom operator.
            Extract a new mobile order request from the provided input text.
            The input is written in Czech and may be a CRM ticket, broker email, or call transcript.

            Service catalog IDs:
            - svc_mobile_start_5g  → Mobil Start 5G (449 CZK/month, 10 GB data)
            - svc_mobile_unlimited_5g → Mobil Neomezeně 5G (749 CZK/month, unlimited data)

            Discount catalog IDs:
            - disc_porting_mobile_200 → porting discount, 200 CZK/month for 6 months

            Set any field to null when it is not present in the input.

            --- EXAMPLE 1 (CRM ticket — ported number not yet provided) ---
            INPUT:
            Nový zákazník – Petr Veselý
            Tel.: 603 112 445
            Tarif: Mobil Neomezeně 5G – 1x
            Závazek: 24 měs.
            Portace: ANO – číslo zatím neposkytnuto
            Sleva: Přenos čísla 200 Kč / 6 měs.

            OUTPUT:
            {"customer_status":"new","customer_name":"Petr Veselý","contact_phone":"+420 603 112 445","requested_services":[{"service_id":"svc_mobile_unlimited_5g","quantity":1}],"contract_term_months":24,"porting_requested":true,"ported_numbers":null,"contact_email":null,"requested_discounts":[{"discount_id":"disc_porting_mobile_200"}],"notes":null}

            --- EXAMPLE 2 (broker email — complete valid order with porting) ---
            INPUT:
            Dobrý den,
            zasílám objednávku nové mobilní služby pro zákaznici Kláru Horákovou.
            Zákaznice: Klára Horáková (nová zákaznice)
            Telefon: +420 777 210 884
            E-mail: klara.horakova@email.cz
            Tarif: Mobil Start 5G, 1 linka
            Délka závazku: 24 měsíců
            Portace čísla: ano – přenášené číslo +420 731 555 888, dosavadní operátor O2
            Sleva: Přenos čísla 200 Kč na 6 měsíců

            OUTPUT:
            {"customer_status":"new","customer_name":"Klára Horáková","contact_phone":"+420 777 210 884","requested_services":[{"service_id":"svc_mobile_start_5g","quantity":1}],"contract_term_months":24,"porting_requested":true,"ported_numbers":[{"number":"+420 731 555 888","donor_operator":"O2"}],"contact_email":"klara.horakova@email.cz","requested_discounts":[{"discount_id":"disc_porting_mobile_200"}],"notes":null}
            """;

    @Override
    public ExtractionMode mode() {
        return ExtractionMode.FEW_SHOT;
    }

    @Override
    public NewMobileOrderRequest extract(String inputText, ChatClient client) {
        return client.prompt()
                .system(SYSTEM_PROMPT)
                .user(inputText)
                .call()
                .entity(NewMobileOrderRequest.class);
    }
}
