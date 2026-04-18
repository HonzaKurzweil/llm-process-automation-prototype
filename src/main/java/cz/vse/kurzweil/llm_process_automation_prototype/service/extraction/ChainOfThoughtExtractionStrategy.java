package cz.vse.kurzweil.llm_process_automation_prototype.service.extraction;

import cz.vse.kurzweil.llm_process_automation_prototype.dto.NewMobileOrderRequest;
import cz.vse.kurzweil.llm_process_automation_prototype.service.ExtractionMode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Component;

/**
 * Chain-of-thought extraction: two sequential model calls.
 *
 * <p>Call 1 — reasoning: the model analyses the input field-by-field in free text,
 * explicitly noting what is present, absent, or ambiguous.
 *
 * <p>Call 2 — extraction: the original input and the reasoning are sent together;
 * Spring AI's BeanOutputConverter injects the JSON schema and the model produces
 * the final structured result grounded in the prior analysis.
 *
 * <p>This approach trades latency for accuracy on noisy or ambiguous inputs.
 */
@Slf4j
@Component
public class ChainOfThoughtExtractionStrategy implements ExtractionStrategy {

    private static final String REASONING_SYSTEM_PROMPT = """
            You are a data extraction assistant for FuturaTel CZ, a Czech telecom operator.
            Analyse the provided Czech telecom input text step by step.
            Work through each field below and state clearly what you can determine:

            1. Customer name, status (new / existing), phone number, email address
            2. Requested mobile service:
               - svc_mobile_start_5g  = Mobil Start 5G (449 CZK/month, 10 GB)
               - svc_mobile_unlimited_5g = Mobil Neomezeně 5G (749 CZK/month, unlimited)
            3. Contract term in months (0 = no commitment, 24 = two-year contract)
            4. Is number porting requested? If yes: the number to port and the donor operator
            5. Discounts requested:
               - disc_porting_mobile_200 = porting bonus, 200 CZK/month for 6 months
            6. Any free-text notes

            For each field, state whether it is present, absent, or ambiguous.
            Do not produce JSON yet — only the analysis.
            """;

    private static final String EXTRACTION_SYSTEM_PROMPT = """
            You are a data extraction assistant for FuturaTel CZ, a Czech telecom operator.
            You will receive the original input text and a step-by-step analysis of its fields.
            Use the analysis to produce the final structured extraction.
            Set any field to null when the analysis confirms it is absent.
            """;

    private final ChatClient chatClient;

    public ChainOfThoughtExtractionStrategy(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    @Override
    public ExtractionMode mode() {
        return ExtractionMode.CHAIN_OF_THOUGHT;
    }

    @Override
    public NewMobileOrderRequest extract(String inputText) {
        String reasoning = chatClient.prompt()
                .system(REASONING_SYSTEM_PROMPT)
                .user(inputText)
                .call()
                .content();

        log.debug("CoT reasoning:\n{}", reasoning);

        String extractionPrompt = """
                Original input:
                %s

                Step-by-step analysis:
                %s

                Now extract the structured data.
                """.formatted(inputText, reasoning);

        return chatClient.prompt()
                .system(EXTRACTION_SYSTEM_PROMPT)
                .user(extractionPrompt)
                .call()
                .entity(NewMobileOrderRequest.class);
    }
}
