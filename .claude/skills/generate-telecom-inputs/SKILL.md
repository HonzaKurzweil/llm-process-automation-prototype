---
name: generate-telecom-inputs
description: Generate Czech telecom intake datasets for either extraction (clear request type) or classification (unclear, multi-intent, ambiguous, or out-of-scope request type) from request_types_v3.yaml, reference_outputs_v2.yaml, classifier_outcomes_v1.yaml, noise_profiles.yaml, and domain YAML catalogs.
when_to_use: Use this skill when the user asks to generate synthetic telecom CRM tickets, broker emails, call transcripts, extraction benchmarks, classification benchmarks, or mixed telecom intake datasets.
argument-hint: "mode=<extraction|classification|both> request_type_ids=<id|all> channels=crm_ticket,broker_email,call_transcript variants_per_base=1 noise_mode=<none|light|mixed|custom> output_path=<path>"
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

Generate synthetic Czech telecom intake input records from structured references and scenario definitions. This skill produces benchmark-style input datasets with gold annotations and observability metadata suitable for later back-checking.

## Default project paths

Unless the user provides overrides, use these files:

- Domain catalogs: `src/main/resources/domain/*.yaml`
- Request types: `src/main/resources/dataset/generator_helpers/request_types_v3.yaml`
- Classifier outcomes: `src/main/resources/dataset/generator_helpers/classifier_outcomes_v1.yaml`
- Noise profiles: `src/main/resources/dataset/generator_helpers/noise_profiles.yaml`
- Reference outputs and classification scenarios: `src/main/resources/dataset/generator_helpers/reference_outputs_v2.yaml`
- Output directory: `src/main/resources/dataset/inputs/`

Do **not** read from `src/main/resources/read_prompts/` when generating dataset inputs. Those prompt files belong to later extraction/classification experiments, not to dataset generation.

If a required file is missing, report the missing path and stop.

## Supported modes

- `extraction`: generate records for extraction from inputs with a clear known request type.
- `classification`: generate records where the input must first be classified.
- `both`: generate both kinds of records in one output file.

## Parameters

Required unless explicit IDs are provided:

- `mode`: `extraction`, `classification`, or `both`. Default: `extraction`.

Optional:

- `request_type_ids`: comma-separated list or `all`. Used mainly for `extraction`, and as a filter for linked known components in `classification`.
- `extraction_reference_ids`: explicit extraction reference IDs.
- `classification_scenario_ids`: explicit classification scenario IDs.
- `classifier_outcome_types`: comma-separated subset of outcome types from `classifier_outcomes_v1.yaml`.
- `channels`: comma-separated subset of `crm_ticket`, `broker_email`, `call_transcript`. Default: all three.
- `noise_mode`: `none`, `light`, `mixed`, or `custom`. Default: `light`.
- `noise_tags`: comma-separated explicit noise tags, valid only with `noise_mode=custom`.
- `variants_per_base`: integer. Default: `1`.
- `output_format`: `json`, `jsonl`, or `yaml`. Default: `json`.
- `output_path`: explicit output file path.
- `include_gold_annotation`: `true` or `false`. Default: `true`.
- `include_observability_metadata`: `true` or `false`. Default: `true`.
- `overwrite`: `true` or `false`. Default: `false`.
- `dry_run`: `true` or `false`. Default: `false`.
- `batch_id`: optional suffix for stable record IDs.

## Core constraints

1. Generate only **Czech** input texts in a realistic Czech telecom context.
2. Use only IDs present in the loaded domain and helper files.
3. Do not expose hidden technical labels such as `request_type_id`, `reference_id`, `scenario_id`, or classifier outcome names inside `input_text`.
4. For `extraction` mode, each generated input must preserve the semantic content of exactly one extraction reference.
5. For `classification` mode, each generated input must preserve the intended classifier outcome of the selected scenario.
6. Irrelevant noise must not introduce accidental extra known intents.
7. Observability metadata must use literal snippets from the generated input text whenever possible.
8. Do not invent new services, products, discounts, or business rules.
9. Keep texts realistic and compact; avoid theatrical dialogue or absurd details.

## Workflow

1. Read the helper files and domain files.
2. Validate requested IDs and requested channels.
3. Resolve base items:
   - `extraction`: select records from `extraction_reference_sets`.
   - `classification`: select records from `classification_scenarios`.
4. Choose noise according to `noise_mode` and `noise_profiles.yaml`.
5. Generate `variants_per_base` records per selected base item and channel.
6. Build gold annotations and observability metadata.
7. Write the output unless `dry_run=true`.
8. Report a compact summary with counts, selected modes, output path, and any skipped items.

## Output record contract

Use the schema documented in `references/output-contract.md`.

Every record contains:

- base metadata (`record_id`, `mode`, `channel`, `noise_tags`),
- `input_text`,
- `gold_annotation` for extraction or classification,
- `observability` metadata for later back-checking.

## Selection behavior

### Extraction mode

Selection priority:

1. `extraction_reference_ids`
2. `request_type_ids`
3. all extraction references

### Classification mode

Selection priority:

1. `classification_scenario_ids`
2. `classifier_outcome_types`
3. linked request type filter via `request_type_ids`
4. all classification scenarios

If `request_type_ids` is used together with `classification` mode, keep only scenarios whose known linked components all fall into the requested request types. This prevents accidental mixing with unrelated scenarios.

## Channel guidance

- `crm_ticket`: terse, semi-structured, internal shorthand is acceptable.
- `broker_email`: coherent email style, explanatory sentences, optional greeting/signature.
- `call_transcript`: spoken Czech, hesitations and repairs allowed, but keep readable.

## Gold-annotation policy

### Extraction mode

Gold annotation must contain:

- `request_type_id`
- `reference_id`
- `reference_kind`
- `extracted_fields`
- `missing_required_fields`
- `missing_required_paths`
- `expected_rule_violations`

### Classification mode

Gold annotation must contain:

- `scenario_id`
- `expected_classifier_result`
- `linked_reference_ids`
- `linked_request_type_ids`

## Observability policy

If `include_observability_metadata=true`, include at least:

- `materialized_field_paths`
- `intentionally_omitted_field_paths`
- `evidence_by_field_path`
- `component_segments`
- `distractor_snippets`
- `generation_note`
