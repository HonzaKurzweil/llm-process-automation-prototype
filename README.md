# LLM Process Automation Prototype

A Spring Boot research prototype developed as part of a Master's thesis at the University of Economics in Prague (VŠE).
It evaluates how large language models (LLMs) can be integrated as deterministic tools within intelligent process
automation (IPA) workflows, applied to a synthetic Czech-language telecom domain.

> **Note:** This is a research prototype. It is not intended for production use.

---

## Table of Contents

- [Overview](#overview)
- [Tech Stack](#tech-stack)
- [Project Structure](#project-structure)
- [Setup](#setup)
- [Running the Application](#running-the-application)
- [API Reference](#api-reference)
- [Running Experiments](#running-experiments)
- [Supported Models](#supported-models)
- [Request Types](#request-types)
- [Prompt Strategies](#prompt-strategies)
- [Dataset](#dataset)
- [Validation Results](#validation-results)
- [Docs](#docs)

---

## Overview

The prototype implements two core LLM-powered tasks within a controlled evaluation framework:

- **Classification** — determines the request type from raw input text (email, call transcript, CRM entry)
- **Structured Data Extraction** — extracts typed DTOs from requests based on the classified request type

Model performance is benchmarked across:

- Three communication channels: `broker`, `call`, `crm`
- Three noise levels: `n1`, `n3`, `n5`
- Two data completeness states: `complete`, `incomplete`

---

## Tech Stack

| Layer           | Technology                       |
|-----------------|----------------------------------|
| Language        | Java 25                          |
| Framework       | Spring Boot 4.0.5                |
| Build           | Maven                            |
| LLM Abstraction | Spring AI 2.0.0-M4               |
| Cloud Models    | OpenAI (GPT-4o Mini, GPT-5 Nano) |
| Local Models    | Ollama (Gemma3 12B / 27B)        |
| API Docs        | SpringDoc OpenAPI / Swagger UI   |
| Utilities       | Lombok                           |

---

## Project Structure

```
src/main/
├── java/.../llm_process_automation_prototype/
│   ├── controller/
│   │   ├── StructuredOutputController.java   # Single-request classification & extraction
│   │   └── ValidationController.java         # Benchmark validation endpoints
│   ├── service/
│   │   ├── classification/                   # Direct & few-shot classification strategies
│   │   ├── extraction/                       # Direct & few-shot extraction strategies
│   │   └── execution/                        # Validation orchestration & result comparison
│   ├── dto/
│   │   ├── ModelType.java                    # Supported model enum
│   │   ├── PromptVariant.java                # DIRECT, FEW_SHOT
│   │   ├── RequestType.java                  # Telecom request type enum
│   │   └── request_types/                    # Per-request-type DTOs
│   ├── commons/                              # Rate limiter, prompt loader, utilities
│   └── config/
│       └── ChatClientConfig.java             # Spring AI chat client configuration
└── resources/
    ├── application.properties
    ├── read_prompts/
    │   ├── classification/                   # System prompt & few-shot examples for classification service
    │   └── extraction/                       # Per-request-type extraction prompts
    └── dataset/
        ├── inputs/                           # Benchmark input JSON datasets
        ├── results/                          # Validation output reports
        └── generator_helpers/               # YAML specs used for data generation
scripts/
├── runClassification.sh                      # Run classification benchmark
└── runExtraction.sh                          # Run extraction benchmark
```

---

## Setup

### Prerequisites

- Java 25
- Maven 3.9+
- (Optional) [Ollama](https://ollama.com) for local model inference

### Configuration

Copy the environment template and fill in your OpenAI API key:

```bash
cp .env_example .env
```

`.env`:

```
OPENAI_API_KEY=your_key_here
```

If using Ollama models, start the Ollama server before running the application:

```bash
ollama serve
ollama pull gemma3:12b   # or whichever model you need
```

---

## Running the Application

```bash
mvn spring-boot:run
```

The application starts on `http://localhost:8080`.

Swagger UI is available at: `http://localhost:8080/swagger-ui.html`

---

## API Reference

### Single Request Endpoints

| Method | Path                              | Description                            |
|--------|-----------------------------------|----------------------------------------|
| POST   | `/api/structured-output/classify` | Classify a single request text         |
| POST   | `/api/structured-output/extract`  | Extract structured data from a request |

### Validation / Benchmark Endpoints

| Method | Path                                        | Description                            |
|--------|---------------------------------------------|----------------------------------------|
| POST   | `/api/validation/classification-validation` | Run classification over a dataset file |
| POST   | `/api/validation/extraction-validation`     | Run extraction over a dataset file     |

**Common query parameters for validation endpoints:**

| Parameter   | Description                         | Example                                                                |
|-------------|-------------------------------------|------------------------------------------------------------------------|
| `inputPath` | Absolute path to input JSON dataset | `.../dataset/inputs/cls_broker_n1.json`                                |
| `variant`   | Prompt strategy                     | `DIRECT` or `FEW_SHOT`                                                 |
| `model`     | Model identifier                    | `GPT_5_NANO`, `GEMMA3_12B` (see [Supported Models](#supported-models)) |

Validation results are written to `src/main/resources/dataset/results/`.

---

## Running Experiments

The scripts automate benchmark runs across all dataset files. The application must be running before executing them.

**Classification benchmark** (9 datasets: 3 channels × 3 noise levels):

```bash
./scripts/runClassification.sh [VARIANT] [MODEL]
# Default: DIRECT GPT_5_NANO
```

**Extraction benchmark** (18 datasets: 3 channels × 3 noise levels × 2 completeness states):

```bash
./scripts/runExtraction.sh [VARIANT] [MODEL]
# Default: DIRECT GPT_5_NANO
```

Example — run few-shot extraction with GPT-4o:

```bash
./scripts/runExtraction.sh FEW_SHOT GPT_4O
```

---

## Supported Models

| Enum Value    | Model ID      | Provider       |
|---------------|---------------|----------------|
| `GPT_5_MINI`  | `gpt-5-mini`  | OpenAI         |
| `GPT_5_NANO`  | `gpt-5-nano`  | OpenAI         |
| `GPT_4O_MINI` | `gpt-4o-mini` | OpenAI         |
| `GPT_4O`      | `gpt-4o`      | OpenAI         |
| `GEMMA3_1B`   | `gemma3:1b`   | Ollama (local) |
| `GEMMA3_12B`  | `gemma3:12b`  | Ollama (local) |
| `GEMMA3_27B`  | `gemma3:27b`  | Ollama (local) |

---

## Request Types

The prototype covers five telecom business request types:

| Enum Value                              | Description                                |
|-----------------------------------------|--------------------------------------------|
| `RT_NEW_MOBILE_ORDER`                   | New single mobile line order               |
| `RT_FAMILY_MOBILE_ORDER`                | Family mobile bundle order                 |
| `RT_FIXED_INTERNET_WITH_HARDWARE_ORDER` | Fixed internet order with hardware         |
| `RT_INTERNET_TV_BUNDLE_ORDER`           | Internet and TV bundle order               |
| `RT_RETENTION_DISCOUNT_REQUEST`         | Customer retention discount request        |
| `UNCLASSIFIABLE`                        | Fallback — request could not be classified |

---

## Prompt Strategies

| Variant    | Description                                       |
|------------|---------------------------------------------------|
| `DIRECT`   | Baseline — system prompt only, no examples        |
| `FEW_SHOT` | Enhanced — system prompt with in-context examples |

Prompt templates are stored in `src/main/resources/read_prompts/`.

---

## Dataset

All input data is synthetically generated in Czech language and stored as JSON files in
`src/main/resources/dataset/inputs/`.

Each record contains the Czech input text, the gold-standard expected outcome, and an evidence block
that links every expected field value back to the source snippet in the text.
Datasets are generated via a Claude skill using domain catalogs and channel/noise configuration files.
See [docs/dataset-generation.md](docs/dataset-generation.md) for the full generation setup and record structure.

**Naming convention:**

```
{task}_{channel}_{noise}[_{completeness}].json

cls_broker_n1.json                  # classification, broker channel, noise level 1
ext_call_n3_incomplete.json         # extraction, call channel, noise level 3, incomplete data
```

| Dimension                      | Values                                     |
|--------------------------------|--------------------------------------------|
| Task                           | `cls` (classification), `ext` (extraction) |
| Channel                        | `broker`, `call`, `crm`                    |
| Noise level                    | `n1`, `n3`, `n5`                           |
| Completeness (extraction only) | `complete`, `incomplete`                   |

---

## Validation Results

After each benchmark run the validation service writes results to `src/main/resources/dataset/results/`.
Each run produces a JSON file comparing expected vs. actual model output per record, including field-level
differences and token counts. Aggregate rows are also appended to `summary.csv`.
See [docs/validation-results.md](docs/validation-results.md) for the full result schema.

---

## Docs

| File                                                     | Contents                                                    |
|----------------------------------------------------------|-------------------------------------------------------------|
| [docs/dataset-generation.md](docs/dataset-generation.md) | Dataset generation skill, source files, record structure    |
| [docs/validation-results.md](docs/validation-results.md) | Result file schema, field descriptions, summary.csv columns |
