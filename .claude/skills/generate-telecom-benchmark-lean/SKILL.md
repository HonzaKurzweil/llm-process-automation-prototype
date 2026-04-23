---
name: generate-telecom-benchmark-final
description: Generate Czech telecom benchmark datasets from domain request types, generator scenario templates, channels, noise profiles, and entity pools.
when_to_use: Use this skill when the user asks to create extraction, classification, or combined telecom benchmark datasets aligned to Java DTOs.
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

Generate Czech telecom benchmark inputs from a small set of explicit source files.

## Default paths

Domain:
- `src/main/resources/domain/services.yaml`
- `src/main/resources/domain/products.yaml`
- `src/main/resources/domain/discounts.yaml`
- `src/main/resources/domain/request_types.yaml`

Generator helpers:
- `src/main/resources/dataset/generator_helpers/request_type_generation.yaml`
- `src/main/resources/dataset/generator_helpers/classification_targets.yaml`
- `src/main/resources/dataset/generator_helpers/channels.yaml`
- `src/main/resources/dataset/generator_helpers/noise_profiles.yaml`
- `src/main/resources/dataset/generator_helpers/entity_pools.yaml`
- `src/main/resources/dataset/generator_helpers/dataset_contract.md`

Output directory:
- `src/main/resources/dataset/inputs/`

Stop if any required file is missing.

## Supported modes

- `extraction`
  - exactly one known request type per record
  - produces `expectedClassification` and one `expectedExtractions` entry
- `classification`
  - produces only `expectedClassification`
  - `expectedExtractions` must be an empty array
- `combined`
  - text may contain one or more known request types
  - produces `expectedClassification` and `expectedExtractions` for every known request present in the text

## Parameters

Required:
- `mode`: `extraction`, `classification`, or `combined`

Optional:
- `requestTypeIds`: comma-separated list or `all`
- `channels`: comma-separated subset of `crm_ticket`, `broker_email`, `call_transcript`
- `noiseProfile`: `clean`, `light`, or `medium`
- `variantsPerCase`: integer, default `1`
- `outputPath`: explicit output file path
- `overwrite`: `true` or `false`, default `false`
- `dryRun`: `true` or `false`, default `false`

Extraction-specific:
- `completeness`: `complete`, `incomplete`, or `mixed`, default `mixed`

Classification-specific:
- `scenarioKinds`: subset of `singleKnownExact`, `singleKnownPartial`, `singleKnownWithUnknownTail`, `multiKnownMixed`, `unknownOnly`, `ambiguousInsufficient`
- `knownRequestCount`: integer or range such as `1` or `2-3`

Combined-specific:
- `scenarioKinds`: subset of `singleKnownExact`, `singleKnownPartial`, `singleKnownWithUnknownTail`, `multiKnownMixed`
- `knownRequestCount`: integer or range such as `1` or `2-3`
- `perRequestCompleteness`: `complete`, `incomplete`, or `mixed`, default `mixed`

## Generation workflow

1. Load domain files and generator helpers.
2. Validate all requested IDs and channels.
3. Resolve base generation cases:
   - extraction: request type × scenario template
   - classification: scenario kind × request type count
   - combined: scenario kind × request type count × scenario templates
4. Materialize concrete values from entity pools.
5. If completeness is `incomplete`, intentionally omit a small number of fields from the rendered text and record them in metadata.
6. Render `inputText` in the selected channel style.
   - `crm_ticket`: short internal record
   - `broker_email`: prose email from broker/partner
   - `call_transcript`: dialogue that must contain both `Zákazník` and `Operátor`
7. Build `expectedClassification`, `expectedExtractions`, and `evidence`.
8. Write the dataset unless `dryRun=true`.

## Hard constraints

1. All text must be in Czech.
2. Use only service/product/discount IDs from loaded domain catalogs.
3. Keep the dataset contract camelCase in all keys and field-path strings.
4. In extraction mode, generate exactly one known request type per record.
5. In classification mode, leave `expectedExtractions` empty.
6. In combined mode, every known request present in the text must have one entry in `expectedExtractions`.
7. `expectedClassification` must contain only:
   - `requestTypeId`
   - `unclassifiableReason`
8. `evidenceByFieldPath` must use literal snippets from `inputText` whenever possible.
9. `materializedFieldPaths` must contain only paths supported by the rendered text.
10. `call_transcript` records must always contain both customer and operator turns.
11. Never expose helper names such as `scenarioKind`, entity pool names, or internal generation labels in the user-facing text.
