package cz.vse.kurzweil.llm_process_automation_prototype.service.extraction;

import cz.vse.kurzweil.llm_process_automation_prototype.dto.NewMobileOrderRequest;
import cz.vse.kurzweil.llm_process_automation_prototype.service.ExtractionMode;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Component;

/**
 * Zero-shot extraction: sends only a system role description and the raw input.
 * The model must infer all fields solely from its training and the JSON schema
 * injected by Spring AI's BeanOutputConverter.
 */
@Component
public class DirectExtractionStrategy implements PromptStrategy {

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
            """;

    @Override
    public ExtractionMode mode() {
        return ExtractionMode.DIRECT;
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
