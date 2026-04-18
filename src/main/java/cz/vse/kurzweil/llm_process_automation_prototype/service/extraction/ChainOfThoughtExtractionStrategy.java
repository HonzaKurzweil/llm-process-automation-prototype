package cz.vse.kurzweil.llm_process_automation_prototype.service.extraction;

// NOT WIRED — first-iteration placeholder, reserved for a future chain-of-thought experiment.
// To activate: add @Component and ensure PromptVariant.CHAIN_OF_THOUGHT is accepted in the controller.

import cz.vse.kurzweil.llm_process_automation_prototype.service.PromptVariant;
import cz.vse.kurzweil.llm_process_automation_prototype.service.RequestType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;

@Slf4j
public class ChainOfThoughtExtractionStrategy implements ExtractionStrategy {

    private static final String REASONING_SYSTEM_PROMPT = """
            You are a data extraction assistant for FuturaTel CZ, a Czech telecom operator.
            Analyse the provided Czech telecom input text step by step.
            Work through each field and state clearly what you can determine:

            1. Customer name, status (new / existing), phone number, email address
            2. Requested service ID and quantity
            3. Contract term in months (0 = no commitment, 24 = two-year contract)
            4. Is number porting requested? If yes: the number to port and the donor operator
            5. Discounts requested
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

    @Override
    public PromptVariant variant() {
        return PromptVariant.CHAIN_OF_THOUGHT;
    }

    @Override
    public <T> T extract(String inputText, RequestType requestType, Class<T> dtoClass, ChatClient client) {
        String reasoning = client.prompt()
                .system(REASONING_SYSTEM_PROMPT)
                .user(inputText)
                .call()
                .content();

        log.debug("CoT extraction reasoning:\n{}", reasoning);

        String extractionPrompt = """
                Original input:
                %s

                Step-by-step analysis:
                %s

                Now extract the structured data.
                """.formatted(inputText, reasoning);

        return client.prompt()
                .system(EXTRACTION_SYSTEM_PROMPT)
                .user(extractionPrompt)
                .call()
                .entity(dtoClass);
    }
}
