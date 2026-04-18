package cz.vse.kurzweil.llm_process_automation_prototype.service.classification;

import com.fasterxml.jackson.annotation.JsonProperty;
import cz.vse.kurzweil.llm_process_automation_prototype.service.ExtractionMode;
import cz.vse.kurzweil.llm_process_automation_prototype.service.RequestType;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class DirectClassificationStrategy implements ClassificationStrategy {

    private static final String SYSTEM_PROMPT = """
            You are a request classifier for FuturaTel CZ, a Czech telecom operator.
            Classify the provided input text into exactly one of the following request types:

            - rt_new_mobile_order: A single mobile service order for a new or existing customer,
              optionally with number porting. Involves one mobile line only.

            - rt_family_mobile_order: A family mobile tariff order with multiple lines (svc_mobile_family_plus).

            - rt_fixed_internet_with_hardware_order: Fixed internet service order (fiber, DSL, or wireless)
              that includes hardware (router, mesh node, or installation add-on).

            - rt_internet_tv_bundle_order: Combined order of fixed internet and TV service,
              optionally with TV add-ons and a bundle discount.

            - rt_retention_discount_request: Negotiation with an existing customer about retaining
              or modifying a service, with a retention discount request.

            - unclassifiable: The input does not clearly match any of the above types.

            Respond only with a JSON object containing the key "request_type" and one of the values above.
            """;

    @Override
    public ExtractionMode mode() {
        return ExtractionMode.DIRECT;
    }

    @Override
    public RequestType classify(String inputText, ChatClient client) {
        return Objects.requireNonNull(client.prompt()
                        .system(SYSTEM_PROMPT)
                        .user(inputText)
                        .call()
                        .entity(ClassificationResponse.class))
                .requestType();
    }

    private record ClassificationResponse(
            @JsonProperty("request_type") RequestType requestType
    ) {}
}
