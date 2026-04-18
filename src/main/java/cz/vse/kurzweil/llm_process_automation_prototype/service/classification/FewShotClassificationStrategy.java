package cz.vse.kurzweil.llm_process_automation_prototype.service.classification;

import com.fasterxml.jackson.annotation.JsonProperty;
import cz.vse.kurzweil.llm_process_automation_prototype.service.ExtractionMode;
import cz.vse.kurzweil.llm_process_automation_prototype.service.RequestType;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class FewShotClassificationStrategy implements ClassificationStrategy {

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

            --- EXAMPLE 1 ---
            INPUT:
            Zákazník Jan Novák chce novou SIM kartu s tarifem Mobil Start 5G. Bude portovat číslo od O2.
            OUTPUT:
            {"request_type":"rt_new_mobile_order"}

            --- EXAMPLE 2 ---
            INPUT:
            Rodina Procházkových — tatínek, maminka a dvě děti. Zájem o rodinný tarif, 4 linky.
            OUTPUT:
            {"request_type":"rt_family_mobile_order"}

            --- EXAMPLE 3 ---
            INPUT:
            Zákaznice chce fiber internet 1 Gbit/s a k tomu zapůjčit router Pro. Instalace na adrese Praha 2.
            OUTPUT:
            {"request_type":"rt_fixed_internet_with_hardware_order"}

            --- EXAMPLE 4 ---
            INPUT:
            Pan Kovář má zájem o balíček internet + TV Family, případně přidat sportovní balíček.
            OUTPUT:
            {"request_type":"rt_internet_tv_bundle_order"}

            --- EXAMPLE 5 ---
            INPUT:
            Stávající zákazník chce zrušit smlouvu kvůli nabídce Vodafone. Snažíme se ho udržet slevou.
            OUTPUT:
            {"request_type":"rt_retention_discount_request"}
            """;

    @Override
    public ExtractionMode mode() {
        return ExtractionMode.FEW_SHOT;
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
