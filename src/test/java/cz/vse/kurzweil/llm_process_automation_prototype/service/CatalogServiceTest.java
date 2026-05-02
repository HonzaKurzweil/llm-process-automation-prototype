package cz.vse.kurzweil.llm_process_automation_prototype.service;

import cz.vse.kurzweil.llm_process_automation_prototype.service.extraction.components.CatalogService;
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
    void containsAllSectionHeaders() {
        assertThat(output)
                .contains("Services:\n")
                .contains("Products:\n")
                .contains("Discounts:\n")
                .contains("Operators:\n")
                .contains("Enums:\n");
    }

    @Test
    void sectionsAppearInCorrectOrder() {
        int servicesIdx = output.indexOf("Services:");
        int productsIdx = output.indexOf("Products:");
        int discountsIdx = output.indexOf("Discounts:");
        int operatorsIdx = output.indexOf("Operators:");
        int enumsIdx = output.indexOf("Enums:");
        assertThat(servicesIdx).isLessThan(productsIdx);
        assertThat(productsIdx).isLessThan(discountsIdx);
        assertThat(discountsIdx).isLessThan(operatorsIdx);
        assertThat(operatorsIdx).isLessThan(enumsIdx);
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
        int servicesIdx = output.indexOf("Services:");
        int productsIdx = output.indexOf("Products:");
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
        assertThat(output).contains("- `prod_router_standard` = Wi-Fi Router Standard");
    }

    @Test
    void productEntryIncludesAliases() {
        assertThat(output).contains("- `prod_router_standard` = Wi-Fi Router Standard (aliases: router standard, standardní router)");
    }

    @Test
    void productIdsAreInProductsSection() {
        int productsIdx = output.indexOf("Products:");
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
        assertThat(output).contains("- `disc_bundle_10_new` = Balíček 10 % pro nové zákazníky (aliases: balíček 10 %, bundle sleva, sleva na balíček)");
    }

    @Test
    void discountIdsAreInDiscountsSection() {
        int discountsIdx = output.indexOf("Discounts:");
        int operatorsIdx = output.indexOf("Operators:");
        String discountsSection = output.substring(discountsIdx, operatorsIdx);
        assertThat(discountsSection)
                .contains("disc_bundle_10_new")
                .doesNotContain("svc_")
                .doesNotContain("prod_");
    }

    // --- Operators ---

    @Test
    void containsAllOperatorIds() {
        assertThat(output).contains("`o2`", "`vodafone`", "`t_mobile`", "`unknown`");
    }

    @Test
    void operatorEntryFormatIsCorrect() {
        assertThat(output).contains("- `o2` = O2");
    }

    @Test
    void operatorEntryIncludesAliases() {
        assertThat(output).contains("- `t_mobile` = T-Mobile (aliases: T-Mobile, T Mobile, t-mobile, t mobile)");
    }

    @Test
    void operatorIdsAreInOperatorsSection() {
        int operatorsIdx = output.indexOf("Operators:");
        int enumsIdx = output.indexOf("Enums:");
        String operatorsSection = output.substring(operatorsIdx, enumsIdx);
        assertThat(operatorsSection)
                .contains("vodafone")
                .doesNotContain("svc_")
                .doesNotContain("disc_")
                .doesNotContain("prod_");
    }

    // --- Enums ---

    @Test
    void containsAllEnumSectionNames() {
        int enumsIdx = output.indexOf("Enums:");
        String enumsSection = output.substring(enumsIdx);
        assertThat(enumsSection)
                .contains("RequestType:\n")
                .contains("CustomerStatus:\n")
                .contains("MobileLineRole:\n");
    }

    @Test
    void enumEntryFormatIsCorrect() {
        assertThat(output).contains("- `new` = NEW");
    }

    @Test
    void enumEntryIncludesAliases() {
        assertThat(output).contains("- `new` = NEW (aliases: nový zákazník, nový klient)");
    }

    @Test
    void enumValuesAreInEnumsSection() {
        int enumsIdx = output.indexOf("Enums:");
        String enumsSection = output.substring(enumsIdx);
        assertThat(enumsSection)
                .contains("rt_new_mobile_order")
                .doesNotContain("svc_")
                .doesNotContain("disc_")
                .doesNotContain("prod_")
                .doesNotContain("addr_");
    }
}
