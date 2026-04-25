package cz.vse.kurzweil.llm_process_automation_prototype.service;

import cz.vse.kurzweil.llm_process_automation_prototype.dto.RequestType;
import org.jspecify.annotations.NonNull;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@SuppressWarnings("unchecked")
@Component
public class CatalogService {

    private final Map<String, String> serviceNames;
    private final Map<String, String> productNames;
    private final Map<String, String> discountNames;
    private final Map<String, Set<String>> allowedServiceIdsByRt = new HashMap<>();
    private final Map<String, Set<String>> allowedProductIdsByRt = new HashMap<>();
    private final Map<String, Set<String>> allowedDiscountIdsByRt = new HashMap<>();

    public CatalogService(ResourceLoader resourceLoader) {
        Yaml yaml = new Yaml();

        serviceNames = populateServiceNames(resourceLoader, yaml);
        productNames = populateProductNames(resourceLoader, yaml);
        discountNames = populateDiscountNames(resourceLoader, yaml);
        populateAllowedRequestedTypesItems(resourceLoader, yaml);
    }

    private void populateAllowedRequestedTypesItems(ResourceLoader resourceLoader, Yaml yaml) {
        try (InputStream is = resourceLoader.getResource("classpath:domain/request_types.yaml").getInputStream()) {
            Map<String, Object> root = yaml.load(is);
            for (Map<String, Object> rt : (List<Map<String, Object>>) root.get("requestTypes")) {
                String rtId = (String) rt.get("requestTypeId");
                Set<String> svcIds = new LinkedHashSet<>();
                Set<String> prdIds = new LinkedHashSet<>();
                Set<String> dscIds = new LinkedHashSet<>();

                List<Map<String, Object>> constraints = (List<Map<String, Object>>) rt.get("fieldConstraints");
                if (constraints != null) {
                    for (Map<String, Object> constraint : constraints) {
                        if (constraint.containsKey("allowedServiceIds")) {
                            svcIds.addAll((List<String>) constraint.get("allowedServiceIds"));
                        }
                        if (constraint.containsKey("allowedProductIds")) {
                            prdIds.addAll((List<String>) constraint.get("allowedProductIds"));
                        }
                        if (constraint.containsKey("allowedDiscountIds")) {
                            dscIds.addAll((List<String>) constraint.get("allowedDiscountIds"));
                        }
                    }
                }

                allowedServiceIdsByRt.put(rtId, svcIds);
                allowedProductIdsByRt.put(rtId, prdIds);
                allowedDiscountIdsByRt.put(rtId, dscIds);
            }
        } catch (IOException e) {
            throw new IllegalStateException("Cannot load domain/request_types.yaml", e);
        }
    }

    private @NonNull Map<String, String> populateDiscountNames(ResourceLoader resourceLoader, Yaml yaml) {
        final Map<String, String> discountNames;
        discountNames = new LinkedHashMap<>();
        try (InputStream is = resourceLoader.getResource("classpath:domain/discounts.yaml").getInputStream()) {
            Map<String, Object> root = yaml.load(is);
            for (Map<String, Object> entry : (List<Map<String, Object>>) root.get("discounts")) {
                discountNames.put((String) entry.get("discountId"), (String) entry.get("name"));
            }
        } catch (IOException e) {
            throw new IllegalStateException("Cannot load domain/discounts.yaml", e);
        }
        return discountNames;
    }

    private @NonNull Map<String, String> populateProductNames(ResourceLoader resourceLoader, Yaml yaml) {
        final Map<String, String> productNames;
        productNames = new LinkedHashMap<>();
        try (InputStream is = resourceLoader.getResource("classpath:domain/products.yaml").getInputStream()) {
            Map<String, Object> root = yaml.load(is);
            for (Map<String, Object> entry : (List<Map<String, Object>>) root.get("products")) {
                productNames.put((String) entry.get("productId"), (String) entry.get("name"));
            }
        } catch (IOException e) {
            throw new IllegalStateException("Cannot load domain/products.yaml", e);
        }
        return productNames;
    }

    private @NonNull Map<String, String> populateServiceNames(ResourceLoader resourceLoader, Yaml yaml) {
        final Map<String, String> serviceNames;
        serviceNames = new LinkedHashMap<>();
        try (InputStream is = resourceLoader.getResource("classpath:domain/services.yaml").getInputStream()) {
            Map<String, Object> root = yaml.load(is);
            for (Map<String, Object> entry : (List<Map<String, Object>>) root.get("services")) {
                serviceNames.put((String) entry.get("serviceId"), (String) entry.get("name"));
            }
        } catch (IOException e) {
            throw new IllegalStateException("Cannot load domain/services.yaml", e);
        }
        return serviceNames;
    }

    public String generateCatalogMappings(RequestType requestType) {
        String rtId = requestType.getRequestTypeIdReference();
        StringBuilder sb = new StringBuilder();

        for (String id : allowedServiceIdsByRt.getOrDefault(rtId, Set.of())) {
            sb.append("- `").append(id).append("` = ").append(serviceNames.getOrDefault(id, id)).append("\n");
        }
        for (String id : allowedProductIdsByRt.getOrDefault(rtId, Set.of())) {
            sb.append("- `").append(id).append("` = ").append(productNames.getOrDefault(id, id)).append("\n");
        }
        for (String id : allowedDiscountIdsByRt.getOrDefault(rtId, Set.of())) {
            sb.append("- `").append(id).append("` = ").append(discountNames.getOrDefault(id, id)).append("\n");
        }

        return sb.toString().strip();
    }
}
