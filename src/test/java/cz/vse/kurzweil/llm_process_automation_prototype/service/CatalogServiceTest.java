package cz.vse.kurzweil.llm_process_automation_prototype.service;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.DefaultResourceLoader;

import static org.assertj.core.api.Assertions.assertThat;

class CatalogServiceTest {

    private static String output;

    @BeforeAll
    static void setUp() {
        output = new CatalogService(new DefaultResourceLoader()).generateCatalogMappings();
    }

    // --- Section structure ---

    @Test
    void containsAllThreeSectionHeaders() {
        assertThat(output)
                .contains("Services:\n")
                .contains("Products:\n")
                .contains("Discounts:\n");
    }

    @Test
    void sectionsAppearInCorrectOrder() {
        int servicesIdx  = output.indexOf("Services:");
        int productsIdx  = output.indexOf("Products:");
        int discountsIdx = output.indexOf("Discounts:");
        assertThat(servicesIdx).isLessThan(productsIdx);
        assertThat(productsIdx).isLessThan(discountsIdx);
    }

    @Test
    void outputHasNoLeadingOrTrailingWhitespace() {
        assertThat(output).isEqualTo(output.strip());
    }

    // --- Services ---

    @Test
    void containsAllServiceIds() {
        assertThat(output).contains(
                "svc_mobile_start_5g",
                "svc_mobile_unlimited_5g",
                "svc_mobile_family_plus",
                "svc_internet_fiber_300",
                "svc_internet_fiber_1000",
                "svc_internet_dsl_100",
                "svc_internet_wireless_50",
                "svc_tv_basic",
                "svc_tv_family"
        );
    }

    @Test
    void serviceEntryFormatIsCorrect() {
        assertThat(output).contains("- `svc_mobile_start_5g` = Mobil Start 5G");
    }

    @Test
    void serviceEntryIncludesAliases() {
        assertThat(output).contains("- `svc_mobile_start_5g` = Mobil Start 5G (aliases: Start 5G, mobil start, start 5g)");
    }

    @Test
    void serviceIdsAreInServicesSection() {
        int servicesIdx  = output.indexOf("Services:");
        int productsIdx  = output.indexOf("Products:");
        String servicesSection = output.substring(servicesIdx, productsIdx);
        assertThat(servicesSection)
                .contains("svc_mobile_start_5g")
                .doesNotContain("prod_")
                .doesNotContain("disc_");
    }

    // --- Products ---

    @Test
    void containsAllProductIds() {
        assertThat(output).contains(
                "prod_router_standard",
                "prod_router_pro",
                "prod_mesh_node",
                "prod_set_top_box",
                "prod_installation_express",
                "prod_tv_sports_pack"
        );
    }

    @Test
    void productEntryFormatIsCorrect() {
        assertThat(output).contains("- `prod_router_standard` = Wi‑Fi Router Standard");
    }

    @Test
    void productEntryIncludesAliases() {
        assertThat(output).contains("(aliases: router standard, standardní router)");
    }

    @Test
    void productIdsAreInProductsSection() {
        int productsIdx  = output.indexOf("Products:");
        int discountsIdx = output.indexOf("Discounts:");
        String productsSection = output.substring(productsIdx, discountsIdx);
        assertThat(productsSection)
                .contains("prod_router_standard")
                .doesNotContain("svc_")
                .doesNotContain("disc_");
    }

    // --- Discounts ---

    @Test
    void containsAllDiscountIds() {
        assertThat(output).contains(
                "disc_bundle_10_new",
                "disc_porting_mobile_200",
                "disc_retention_15_existing",
                "disc_family_line_100"
        );
    }

    @Test
    void discountEntryFormatIsCorrect() {
        assertThat(output).contains("- `disc_bundle_10_new` = Balíček 10 % pro nové zákazníky");
    }

    @Test
    void discountEntryIncludesAliases() {
        assertThat(output).contains("(aliases: balíček 10 %, bundle sleva)");
    }

    @Test
    void discountIdsAreInDiscountsSection() {
        int discountsIdx = output.indexOf("Discounts:");
        String discountsSection = output.substring(discountsIdx);
        assertThat(discountsSection)
                .contains("disc_bundle_10_new")
                .doesNotContain("svc_")
                .doesNotContain("prod_");
    }
}
