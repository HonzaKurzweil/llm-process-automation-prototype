---
name: generate-telecom-benchmark-final
description: Generate Czech telecom benchmark datasets from request types, domain catalogs, entity pools, channel profiles, and noise profiles.
when_to_use: Use this skill when the user asks to generate benchmark datasets for extraction, classification, or combined evaluation in the telecom prototype.
argument-hint: "mode=<extraction|classification|combined> requestTypeIds=<id|all> channels=crm_ticket,broker_email,call_transcript noiseProfile=<clean|light|medium> variantsPerCase=1 outputPath=<path>"
disable-model-invocation: true
allowed-tools:
  - Read
  - Write
  - Edit
  - MultiEdit
  - Glob
  - Grep
---

# Generate Telecom Benchmark Final

Generate benchmark records directly from:

- `domain/services.yaml`
- `domain/products.yaml`
- `domain/discounts.yaml`
- `domain/request_types.yaml`
- `dataset/generator_helpers/classification_targets.yaml`
- `dataset/generator_helpers/entity_pools.yaml`
- `dataset/generator_helpers/channel_profiles.yaml`
- `dataset/generator_helpers/noise_profiles.yaml`

Output directory default: `src/main/resources/dataset/inputs/`

Stop if any required file is missing.

## Global output rule

For **every** mode (`extraction`, `classification`, `combined`), write one JSON object with the following top-level envelope:

```json
{
  "generatedAt": "YYYY-MM-DD",
  "generationRunParams": { ... },
  "records": [ ... ]
}
```

Never output a raw top-level array.
Never include `generatorVersion`.

The per-record structure must follow `output_contract.md`.

## Supported modes

### 1. `extraction`
Generate one known request type per record.

- Choose one request type.
- Choose one scenario template of that request type.
- Materialize concrete values from entity pools.
- Render text in the requested channel.
- If `completeness=incomplete`, intentionally omit some fields in the rendered text.
- Produce one gold DTO in `expectedExtractions`.
- Set `expectedClassification.requestTypeId` to that request type.

### 2. `classification`
Generate text meant only for classification.

- Use `classification_targets.yaml` to decide the scenario shape.
- The text may contain one known request type, multiple mixed request types, a known request plus unknown tail, or only unknown content.
- `expectedExtractions` must be empty.
- `expectedClassification.requestTypeId` must contain exactly one value:
  - one known `rt_*`
  - or `unclassifiable`

### 3. `combined`
Generate classification input that also contains extraction gold objects for every known request present in the text.

- The text may contain one or more known request types.
- Every known request present in the text must have one entry in `expectedExtractions`.
- If multiple known requests are mixed in one text, `expectedClassification.requestTypeId` must be `unclassifiable`.
- The output file must still use the same top-level envelope with `generatedAt`, `generationRunParams`, and `records`.

## Parameters

Required:

- `mode`: `extraction`, `classification`, or `combined`

Optional:

- `requestTypeIds`: comma-separated list or `all`
- `channels`: comma-separated subset of `crm_ticket`, `broker_email`, `call_transcript`
- `noiseProfile`: `clean`, `light`, or `medium`
- `variantsPerCase`: integer, default `1`
- `outputPath`: explicit output file path

Mode-specific:

- `extraction`
  - `completeness`: `complete`, `incomplete`, or `mixed`, default `mixed`

- `classification`
  - `scenarioKinds`: subset of `singleKnownExact`, `singleKnownPartial`, `singleKnownWithUnknownTail`, `multiKnownMixed`, `unknownOnly`, `ambiguousInsufficient`
  - `knownRequestCount`: integer or range such as `1` or `2-3`

- `combined`
  - `scenarioKinds`: subset of `singleKnownExact`, `singleKnownPartial`, `singleKnownWithUnknownTail`, `multiKnownMixed`
  - `knownRequestCount`: integer or range such as `1` or `2-3`
  - `perRequestCompleteness`: `complete`, `incomplete`, or `mixed`, default `mixed`

## Generation workflow

1. Load all domain files and generator helpers.
2. Validate requested IDs and channels.
3. Resolve base cases:
   - extraction: request type × scenario template
   - classification: scenario kind × request type count
   - combined: scenario kind × request type count × scenario templates
4. Materialize values from entity pools.
5. Render text according to `channel_profiles.yaml`.
6. Apply requested noise and optional incompleteness.
7. Build the common top-level envelope:
   - `generatedAt`
   - `generationRunParams`
   - `records`
8. For each record build:
   - `expectedClassification`
   - `expectedExtractions`
   - `evidence`

## Hard constraints

1. All generated text must be in Czech.
2. Keys and field-path strings must stay in camelCase.
3. Use only IDs available in the loaded domain catalogs.
4. In extraction mode, generate exactly one known request type per record.
5. In classification mode, keep `expectedExtractions` empty.
6. In combined mode, include one extraction gold object for every known request present in the text.
7. `expectedClassification.requestTypeId` must always contain exactly one value.
8. If the classification result is not safely one known request type, use `unclassifiable`.
9. `call_transcript` must always contain both `Operátor:` and `Zákazník:` turns.
10. `crm_ticket` must not contain greeting or signature.
11. `broker_email` may contain greeting and signature.
12. For incomplete cases, omit only selected field values from the rendered text; do not violate the DTO schema of the gold object.
13. `evidenceByFieldPath` should use literal snippets from `inputText` whenever possible.
14. `materializedFieldPaths` must list only fields actually supported by the rendered text.
15. `intentionallyOmittedFieldPaths` must list only fields intentionally absent from the rendered text.
16. The output file must never be a bare array, even in `combined` mode.
