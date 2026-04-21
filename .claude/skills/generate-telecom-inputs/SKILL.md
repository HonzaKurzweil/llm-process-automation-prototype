---
name: generate-telecom-inputs
description: Generate Czech telecom intake datasets for either extraction (clear request type) or classification (unclear, mixed, ambiguous, or out-of-scope request type) from camelCase YAML resources and emit camelCase output records aligned for direct Java mapping.
when_to_use: Use this skill when the user asks to generate synthetic telecom CRM tickets, broker emails, call transcripts, extraction benchmarks, classification benchmarks, or mixed telecom intake datasets.
argument-hint: "mode=<extraction|classification|both> requestTypeIds=<id|all> channels=crm_ticket,broker_email,call_transcript variantsPerBase=1 noiseMode=<none|light|mixed|custom> outputPath=<path>"
disable-model-invocation: true
allowed-tools:
  - Read
  - Write
  - Edit
  - MultiEdit
  - Glob
  - Grep
---

# Generate Telecom Inputs Skill

Generate synthetic Czech telecom intake input records from structured references and scenario definitions. This
camelCase variant is designed so that generated JSON or YAML can be mapped directly to Java record-like data objects
without extra Jackson naming strategies.

## Default project paths

Unless the user provides overrides, use these files:

- Domain catalogs: `src/main/resources/domain/*.yaml`
- Request types: `src/main/resources/dataset/generator_helpers/request_types_v3.yaml`
- Classifier outcomes: `src/main/resources/dataset/generator_helpers/classifier_outcomes_v3.yaml`
- Noise profiles: `src/main/resources/dataset/generator_helpers/noise_profiles.yaml`
- Reference outputs and classification scenarios:
  `src/main/resources/dataset/generator_helpers/reference_outputs_v4.yaml`
- Output directory: `src/main/resources/dataset/inputs/`

If a required file is missing, report the missing path and stop.

## Supported modes

- `extraction`: generate records for extraction from inputs with a clear known request type.
- `classification`: generate records where the input must first be classified.
- `both`: generate both kinds of records in one output file.

## Parameters

Required unless explicit IDs are provided:

- `mode`: `extraction`, `classification`, or `both`. Default: `extraction`.

Optional:

- `requestTypeIds`: comma-separated list or `all`. Used mainly for `extraction`, and as a filter for linked known
  components in `classification`.
- `extractionReferenceIds`: explicit extraction reference IDs.
- `classificationScenarioIds`: explicit classification scenario IDs.
- `classifierOutcomeTypes`: comma-separated subset of scenario types from `classifier_outcomes_v3.yaml`.
- `channels`: comma-separated subset of `crm_ticket`, `broker_email`, `call_transcript`. Default: all three.
- `noiseMode`: `none`, `light`, `mixed`, or `custom`. Default: `light`.
- `noiseTags`: comma-separated explicit noise tags, valid only with `noiseMode=custom`.
- `variantsPerBase`: integer. Default: `1`.
- `outputFormat`: `json`, `jsonl`, or `yaml`. Default: `json`.
- `outputPath`: explicit output file path.
- `includeGoldAnnotation`: `true` or `false`. Default: `true`.
- `includeObservabilityMetadata`: `true` or `false`. Default: `true`.
- `overwrite`: `true` or `false`. Default: `false`.
- `dryRun`: `true` or `false`. Default: `false`.
- `batchId`: optional suffix for stable record IDs.

## Core constraints

1. Generate only **Czech** input texts in a realistic Czech telecom context.
2. Use only IDs present in the loaded domain and helper files.
3. Do not expose hidden technical labels such as `requestTypeId`, `referenceId`, `scenarioId`, or classifier taxonomy
   labels inside `inputText`.
4. For `extraction` mode, each generated input must preserve the semantic content of exactly one extraction reference.
5. For `classification` mode, the generated input must preserve the intended classification scenario and the expected
   runtime outcome:
    - one known request type, or
    - `unclassifiable` because the input is mixed, contains an unknown tail, is out of scope, or is too ambiguous.
6. Irrelevant noise must not introduce accidental extra known intents.
7. Observability metadata must use **literal snippets from the generated input text** whenever possible.
8. Do not invent new services, products, discounts, or business rules.
9. Keep texts realistic and compact; avoid theatrical dialogue or absurd details.
10. All generated envelope keys, gold-annotation keys, extracted-field keys, and field-path strings must use camelCase.

## Workflow

1. Read the helper files and domain files.
2. Validate requested IDs and requested channels.
3. Resolve base items:
    - `extraction`: select records from `extractionReferenceSets`.
    - `classification`: select records from `classificationScenarios`.
4. Choose noise according to `noiseProfiles.yaml`.
5. Generate `variantsPerBase` records per selected base item and channel.
6. Build gold annotations and observability metadata.
7. Write the output unless `dryRun=true`.
8. Report a compact summary with counts, selected modes, output path, and any skipped items.

## Output record contract

Use the schema documented in `references/output-contract.md`.

In brief, every record contains:

- base metadata (`recordId`, `mode`, `channel`, `noiseTags`),
- `inputText`,
- `goldAnnotation` for extraction or classification,
- `observability` metadata for later back-checking.

## Selection behavior

### Extraction mode

Selection priority:

1. `extractionReferenceIds`
2. `requestTypeIds`
3. all extraction references

### Classification mode

Selection priority:

1. `classificationScenarioIds`
2. `classifierOutcomeTypes` (matched against `scenarioType`)
3. linked request type filter via `requestTypeIds`
4. all classification scenarios

If `requestTypeIds` is used together with `classification` mode, keep only scenarios whose known linked components all
fall into the requested request types. This prevents accidental mixing with unrelated scenarios.

## Channel guidance

- `crm_ticket`: terse, semi-structured, internal shorthand is acceptable.
- `broker_email`: coherent email style, explanatory sentences, optional greeting or signature.
- `call_transcript`: spoken Czech, hesitations and repairs allowed, but keep readable.

## Gold-annotation policy

### Extraction mode

Gold annotation must contain:

- `requestTypeId`
- `referenceId`
- `referenceKind`
- `extractedFields`
- `missingRequiredFields`
- `missingRequiredPaths`
- `expectedRuleViolations`

### Classification mode

Gold annotation must contain:

- `scenarioId`
- `scenarioType`
- `expectedRequestTypeIdReference`
- `unclassifiableReasonId`
- `linkedReferenceIds`
- `linkedRequestTypeIds`

Optional:

- `possibleRequestTypeIds` for ambiguous scenarios only.

## Observability policy

If `includeObservabilityMetadata=true`, include at least:

- `materializedFieldPaths`
- `intentionallyOmittedFieldPaths`
- `evidenceByFieldPath`
- `componentSegments`
- `distractorSnippets`
- `generationNote`

## Additional resources

- `references/parameter-reference.md`: parameter behavior and selection rules.
- `references/output-contract.md`: exact record schemas and consistency requirements.
- `examples/example-invocations.md`: sample slash-command usage.
