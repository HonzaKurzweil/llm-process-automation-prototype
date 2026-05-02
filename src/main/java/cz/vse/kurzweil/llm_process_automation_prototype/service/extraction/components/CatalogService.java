package cz.vse.kurzweil.llm_process_automation_prototype.service.extraction.components;

import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unchecked")
@Component
public class CatalogService {

    private final Map<String, CatalogEntry> services = new LinkedHashMap<>();
    private final Map<String, CatalogEntry> products = new LinkedHashMap<>();
    private final Map<String, CatalogEntry> discounts = new LinkedHashMap<>();
    private final Map<String, CatalogEntry> operators = new LinkedHashMap<>();
    private final List<AddressEntry> addresses = new ArrayList<>();
    private final Map<String, List<CatalogEntry>> enums = new LinkedHashMap<>();

    public CatalogService(ResourceLoader resourceLoader) {
        Yaml yaml = new Yaml();
        loadServices(resourceLoader, yaml);
        loadProducts(resourceLoader, yaml);
        loadDiscounts(resourceLoader, yaml);
        loadOperators(resourceLoader, yaml);
        loadAddresses(resourceLoader, yaml);
        loadEnums(resourceLoader, yaml);
    }

    public String generateCatalogMappings() {
        StringBuilder sb = new StringBuilder();
        sb.append("Doménový kontext FuturaTel CZ:\n\n");

        appendSection(sb, "Services", services);
        appendSection(sb, "Products", products);
        appendSection(sb, "Discounts", discounts);
        appendSection(sb, "Operators", operators);
        appendAddressSection(sb);
        appendEnumSection(sb);

        return sb.toString().strip();
    }

    private void loadServices(ResourceLoader resourceLoader, Yaml yaml) {
        Map<String, Object> root = loadYaml(resourceLoader, yaml, "classpath:domain/services.yaml");
        for (Map<String, Object> entry : (List<Map<String, Object>>) root.get("services")) {
            String id = (String) entry.get("serviceId");
            services.put(id, new CatalogEntry(id, (String) entry.get("name"), (List<String>) entry.getOrDefault("aliases", List.of())));
        }
    }

    private void loadProducts(ResourceLoader resourceLoader, Yaml yaml) {
        Map<String, Object> root = loadYaml(resourceLoader, yaml, "classpath:domain/products.yaml");
        for (Map<String, Object> entry : (List<Map<String, Object>>) root.get("products")) {
            String id = (String) entry.get("productId");
            products.put(id, new CatalogEntry(id, (String) entry.get("name"), (List<String>) entry.getOrDefault("aliases", List.of())));
        }
    }

    private void loadDiscounts(ResourceLoader resourceLoader, Yaml yaml) {
        Map<String, Object> root = loadYaml(resourceLoader, yaml, "classpath:domain/discounts.yaml");
        for (Map<String, Object> entry : (List<Map<String, Object>>) root.get("discounts")) {
            String id = (String) entry.get("discountId");
            discounts.put(id, new CatalogEntry(id, (String) entry.get("name"), (List<String>) entry.getOrDefault("aliases", List.of())));
        }
    }

    private void loadOperators(ResourceLoader resourceLoader, Yaml yaml) {
        Map<String, Object> root = loadYaml(resourceLoader, yaml, "classpath:domain/operators.yaml");
        for (Map<String, Object> entry : (List<Map<String, Object>>) root.get("operators")) {
            String id = (String) entry.get("operatorId");
            operators.put(id, new CatalogEntry(id, (String) entry.get("name"), (List<String>) entry.getOrDefault("aliases", List.of())));
        }
    }

    private void loadAddresses(ResourceLoader resourceLoader, Yaml yaml) {
        Map<String, Object> root = loadYaml(resourceLoader, yaml, "classpath:domain/addresses.yaml");
        for (Map<String, Object> entry : (List<Map<String, Object>>) root.get("addresses")) {
            addresses.add(new AddressEntry(
                    (String) entry.get("psc"),
                    (String) entry.get("city"),
                    (String) entry.get("street"),
                    (String) entry.get("houseNumber")
            ));
        }
    }

    private void loadEnums(ResourceLoader resourceLoader, Yaml yaml) {
        Map<String, Object> root = loadYaml(resourceLoader, yaml, "classpath:domain/enums.yaml");
        for (Map<String, Object> enumEntry : (List<Map<String, Object>>) root.get("enums")) {
            String enumName = (String) enumEntry.get("enumName");
            List<CatalogEntry> values = ((List<Map<String, Object>>) enumEntry.get("values"))
                    .stream()
                    .map(value -> new CatalogEntry(
                            (String) value.get("value"),
                            (String) value.get("javaEnumConstant"),
                            (List<String>) value.getOrDefault("aliases", List.of())))
                    .toList();
            enums.put(enumName, values);
        }
    }

    private Map<String, Object> loadYaml(ResourceLoader resourceLoader, Yaml yaml, String path) {
        try (InputStream is = resourceLoader.getResource(path).getInputStream()) {
            return yaml.load(is);
        }
        catch (IOException e) {
            throw new IllegalStateException("Cannot load " + path, e);
        }
    }

    private void appendSection(StringBuilder sb, String title, Map<String, CatalogEntry> entries) {
        sb.append(title).append(":\n");
        for (CatalogEntry entry : entries.values()) {
            appendEntry(sb, entry.id(), entry.label(), entry.aliases());
        }
        sb.append("\n");
    }

    private void appendAddressSection(StringBuilder sb) {
        sb.append("Addresses:\n");
        for (AddressEntry a : addresses) {
            sb.append("- psc: ").append(a.psc())
              .append(", city: ").append(a.city())
              .append(", street: ").append(a.street())
              .append(", houseNumber: ").append(a.houseNumber())
              .append("\n");
        }
        sb.append("\n");
    }

    private void appendEnumSection(StringBuilder sb) {
        sb.append("Enums:\n");
        for (Map.Entry<String, List<CatalogEntry>> enumEntry : enums.entrySet()) {
            sb.append(enumEntry.getKey()).append(":\n");
            for (CatalogEntry value : enumEntry.getValue()) {
                appendEntry(sb, value.id(), value.label(), value.aliases());
            }
        }
    }

    private void appendEntry(StringBuilder sb, String id, String label, List<String> aliases) {
        sb.append("- `").append(id).append("` = ").append(label);
        if (aliases != null && !aliases.isEmpty()) {
            sb.append(" (aliases: ").append(String.join(", ", aliases)).append(")");
        }
        sb.append("\n");
    }

    private record CatalogEntry(String id, String label, List<String> aliases) {
    }

    private record AddressEntry(String psc, String city, String street, String houseNumber) {
    }
}
