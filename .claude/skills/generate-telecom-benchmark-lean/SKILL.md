---
name: generate-telecom-benchmark-lean
description: Generate Czech telecom benchmark datasets directly from request type templates and entity pools, without reference outputs.
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

# Generate Telecom Benchmark Lean

Generate Czech telecom benchmark inputs directly from `request_types.yaml`, `classification_targets.yaml`, `entity_pools.yaml`, and `noise_profiles.yaml`.

## Default paths

- `src/main/resources/domain/services.yaml`
- `src/main/resources/domain/products.yaml`
- `src/main/resources/domain/discounts.yaml`
- `src/main/resources/domain/entity_pools.yaml`
- `src/main/resources/dataset/generator_helpers/request_types.yaml`
- `src/main/resources/dataset/generator_helpers/classification_targets.yaml`
- `src/main/resources/dataset/generator_helpers/noise_profiles.yaml`
- output directory: `src/main/resources/dataset/inputs/`

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
- `outputFormat`: `json`, `jsonl`, or `yaml`, default `json`
- `outputPath`: explicit output file path
- `overwrite`: `true` or `false`, default `false`
- `dryRun`: `true` or `false`, default `false`

### Extraction-only parameters

- `completenessMode`: `complete`, `incomplete`, or `mixed`, default `mixed`
- `missingFieldPaths`: optional explicit comma-separated list of field paths to omit from every record

### Classification-only parameters

- `scenarioKinds`: subset of `singleKnownExact`, `singleKnownPartial`, `singleKnownWithUnknownTail`, `multiKnownMixed`, `unknownOnly`, `ambiguousInsufficient`
- `knownRequestCount`: integer or range such as `1` or `2-3`

### Combined-only parameters

- `scenarioKinds`: subset of `singleKnownExact`, `singleKnownPartial`, `singleKnownWithUnknownTail`, `multiKnownMixed`
- `knownRequestCount`: integer or range such as `1` or `2-3`
- `perRequestCompletenessMode`: `complete`, `incomplete`, or `mixed`, default `mixed`

## Generation workflow

1. Load request types, classification targets, noise profiles, domain catalogs, and entity pools.
2. Validate all requested IDs and channels.
3. Resolve base generation cases:
   - extraction: request type × scenario template
   - classification: scenario kind × request type count
   - combined: scenario kind × request type count × scenario templates
4. Materialize concrete values from entity pools.
5. Apply completeness rules or explicit omissions.
6. Render `inputText` in the chosen channel style.
7. Build `expectedClassification`, `expectedExtractions`, and `evidence`.
8. Write the dataset unless `dryRun=true`.

## Hard constraints

1. All text must be in Czech.
2. Never expose internal helper labels such as `templateId`, `scenarioKind`, or entity pool names in the user-facing text.
3. Use only service/product/discount IDs available in the loaded catalogs.
4. In extraction mode, generate exactly one known request type per record.
5. In classification mode, leave `expectedExtractions` empty.
6. In combined mode, every known request present in the text must have one entry in `expectedExtractions`.
7. `evidenceByFieldPath` must use literal snippets from `inputText` whenever possible.
8. `materializedFieldPaths` must contain only paths that are actually supported by the rendered text.
9. `intentionallyOmittedFieldPaths` must list only fields intentionally absent from the text.
10. Keep the contract camelCase in all keys and field-path strings.
