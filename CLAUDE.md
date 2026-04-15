# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Commands

```bash
# Build
mvn clean package

# Run
mvn spring-boot:run

# Run tests
mvn test

# Run a single test class
mvn test -Dtest=ClassName

# Package and run the JAR
java -jar target/llm-process-automation-prototype-0.0.1-SNAPSHOT.jar
```

- REST API: `http://localhost:8080/api/structured-output/start?input={text}`
- Swagger UI: `http://localhost:8080/swagger-ui.html`

## Architecture

**Spring Boot 4.0.5 / Java 25** REST API. Entry point: `LlmProcessAutomationPrototypeApplication.java`.

### Layered structure

```
controller/ → StructuredOutputController   (REST: POST /api/structured-output/start)
service/    → StructuredOutputService      (business logic, currently scaffolding)
dto/                                        (empty — for request/response models)
```

### Domain data (YAML catalogs in `src/main/resources/`)

All telecom business knowledge lives in static YAML files, not a database:

| File | Purpose |
|------|---------|
| `domain/services.yaml` | Telecom service catalog (mobile, internet, TV tariffs) |
| `domain/products.yaml` | Hardware and add-ons (routers, set-top boxes, installation) |
| `domain/discounts.yaml` | Promotional discounts and eligibility conditions |
| `domain/rules.yaml` | Business rules: `dependency`, `incompatibility`, `eligibility`, `quantity` |
| `domain/locality_groups.yaml` | Geographic availability regions |
| `dataset/request_types.yaml` | Input request types and their required/optional fields |
| `dataset/reference_outputs.yaml` | Gold-standard extraction outputs for LLM training/validation |
| `dataset/noise_profiles.yaml` | Text perturbation definitions (typos, missing fields, contradictions) |

See `docs/FIELD_EXPLANATION.md` (in Czech) for detailed field semantics across all domain YAMLs.

### Intended data flow

1. Raw text input arrives (CRM ticket / broker email / customer service transcript).
2. LLM extracts structured order data into fields defined by `request_types.yaml`.
3. `rules.yaml` validates the extracted order (dependency checks, incompatibility checks, eligibility, quantity limits).
4. Output compared against `reference_outputs.yaml` gold standards for evaluation.

The `dataset/noise_profiles.yaml` describes synthetic noise types used to generate realistic training inputs. The `prompts/` directory is reserved for future LLM prompt templates.

### Business domain

Fictional Czech telecom operator **FuturaTel CZ**. Services include mobile (5G tariffs), fixed internet (fiber/DSL/wireless), and TV. All field names, YAML keys, and documentation are in Czech.
