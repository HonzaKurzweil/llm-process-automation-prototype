package cz.vse.kurzweil.llm_process_automation_prototype.service.classification;

// NOT WIRED — first-iteration placeholder, reserved for a future chain-of-thought experiment.
// To activate: add @Component and ensure PromptVariant.CHAIN_OF_THOUGHT is accepted in the controller.

import com.fasterxml.jackson.annotation.JsonProperty;
import cz.vse.kurzweil.llm_process_automation_prototype.service.PromptVariant;
import cz.vse.kurzweil.llm_process_automation_prototype.service.RequestType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;

import java.util.Objects;

@Slf4j
public class ChainOfThoughtClassificationStrategy implements ClassificationStrategy {

    private static final String REASONING_SYSTEM_PROMPT = """
            You are a request analyst for FuturaTel CZ, a Czech telecom operator.
            Analyse the provided Czech telecom input text step by step.

            Consider the following signals:
            1. Is the customer new or existing?
            2. Is this about mobile service, fixed internet, TV, or a combination?
            3. How many mobile lines are involved? Is a family tariff mentioned?
            4. Is hardware (router, mesh, installation) mentioned alongside internet?
            5. Is both internet and TV ordered together?
            6. Is the customer at risk of churning, or is a retention discount being negotiated?

            State clearly which signals are present and which are absent.
            Do not classify yet — only provide the analysis.
            """;

    private static final String CLASSIFICATION_SYSTEM_PROMPT = """
            You are a request classifier for FuturaTel CZ, a Czech telecom operator.
            You will receive the original input text and a step-by-step analysis of its signals.
            Use the analysis to classify the request into exactly one of:

            - rt_new_mobile_order
            - rt_family_mobile_order
            - rt_fixed_internet_with_hardware_order
            - rt_internet_tv_bundle_order
            - rt_retention_discount_request
            - unclassifiable

            Respond only with a JSON object containing the key "request_type".
            """;

    @Override
    public PromptVariant variant() {
        return PromptVariant.CHAIN_OF_THOUGHT;
    }

    @Override
    public RequestType classify(String inputText, ChatClient client) {
        String reasoning = client.prompt()
                .system(REASONING_SYSTEM_PROMPT)
                .user(inputText)
                .call()
                .content();

        log.debug("CoT classification reasoning:\n{}", reasoning);

        String classificationPrompt = """
                Original input:
                %s

                Step-by-step analysis:
                %s

                Now classify the request type.
                """.formatted(inputText, reasoning);

        return Objects.requireNonNull(client.prompt()
                        .system(CLASSIFICATION_SYSTEM_PROMPT)
                        .user(classificationPrompt)
                        .call()
                        .entity(ClassificationResponse.class))
                .requestType();
    }

    private record ClassificationResponse(
            @JsonProperty("request_type") RequestType requestType
    ) {}
}
