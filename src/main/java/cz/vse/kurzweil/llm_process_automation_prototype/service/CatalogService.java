package cz.vse.kurzweil.llm_process_automation_prototype.service;

import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unchecked")
@Component
public class CatalogService {

    private final Map<String, String> serviceNames = new LinkedHashMap<>();
    private final Map<String, List<String>> serviceAliases = new LinkedHashMap<>();
    private final Map<String, String> productNames = new LinkedHashMap<>();
    private final Map<String, List<String>> productAliases = new LinkedHashMap<>();
    private final Map<String, String> discountNames = new LinkedHashMap<>();
    private final Map<String, List<String>> discountAliases = new LinkedHashMap<>();

    public CatalogService(ResourceLoader resourceLoader) {
        Yaml yaml = new Yaml();
        loadServices(resourceLoader, yaml);
        loadProducts(resourceLoader, yaml);
        loadDiscounts(resourceLoader, yaml);
    }

    public String generateCatalogMappings() {
        StringBuilder sb = new StringBuilder();

        sb.append("Services:\n");
        for (Map.Entry<String, String> e : serviceNames.entrySet()) {
            appendEntry(sb, e.getKey(), e.getValue(), serviceAliases.get(e.getKey()));
        }

        sb.append("\nProducts:\n");
        for (Map.Entry<String, String> e : productNames.entrySet()) {
            appendEntry(sb, e.getKey(), e.getValue(), productAliases.get(e.getKey()));
        }

        sb.append("\nDiscounts:\n");
        for (Map.Entry<String, String> e : discountNames.entrySet()) {
            appendEntry(sb, e.getKey(), e.getValue(), discountAliases.get(e.getKey()));
        }

        return sb.toString().strip();
    }

    private void loadServices(ResourceLoader resourceLoader, Yaml yaml) {
        try (InputStream is = resourceLoader.getResource("classpath:domain/services.yaml").getInputStream()) {
            Map<String, Object> root = yaml.load(is);
            for (Map<String, Object> entry : (List<Map<String, Object>>) root.get("services")) {
                String id = (String) entry.get("serviceId");
                serviceNames.put(id, (String) entry.get("name"));
                serviceAliases.put(id, (List<String>) entry.getOrDefault("aliases", List.of()));
            }
        } catch (IOException e) {
            throw new IllegalStateException("Cannot load domain/services.yaml", e);
        }
    }

    private void loadProducts(ResourceLoader resourceLoader, Yaml yaml) {
        try (InputStream is = resourceLoader.getResource("classpath:domain/products.yaml").getInputStream()) {
            Map<String, Object> root = yaml.load(is);
            for (Map<String, Object> entry : (List<Map<String, Object>>) root.get("products")) {
                String id = (String) entry.get("productId");
                productNames.put(id, (String) entry.get("name"));
                productAliases.put(id, (List<String>) entry.getOrDefault("aliases", List.of()));
            }
        } catch (IOException e) {
            throw new IllegalStateException("Cannot load domain/products.yaml", e);
        }
    }

    private void loadDiscounts(ResourceLoader resourceLoader, Yaml yaml) {
        try (InputStream is = resourceLoader.getResource("classpath:domain/discounts.yaml").getInputStream()) {
            Map<String, Object> root = yaml.load(is);
            for (Map<String, Object> entry : (List<Map<String, Object>>) root.get("discounts")) {
                String id = (String) entry.get("discountId");
                discountNames.put(id, (String) entry.get("name"));
                discountAliases.put(id, (List<String>) entry.getOrDefault("aliases", List.of()));
            }
        } catch (IOException e) {
            throw new IllegalStateException("Cannot load domain/discounts.yaml", e);
        }
    }

    private void appendEntry(StringBuilder sb, String id, String name, List<String> aliases) {
        sb.append("- `").append(id).append("` = ").append(name);
        if (aliases != null && !aliases.isEmpty()) {
            sb.append(" (aliases: ").append(String.join(", ", aliases)).append(")");
        }
        sb.append("\n");
    }
}
