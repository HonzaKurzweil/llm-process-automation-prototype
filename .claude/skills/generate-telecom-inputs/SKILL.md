---
name: generate-telecom-inputs
description: Generate Czech telecom intake input records from request type definitions, reference outputs, noise profiles, and domain YAML catalogs. Use when creating synthetic CRM tickets, broker emails, or call transcripts for LLM extraction evaluation.
when_to_use: Use this skill when the user asks to generate telecom input data, input variants, CRM ticket text, broker email text, call transcript text, or dataset input files from request_types.yaml, reference_outputs.yaml, noise_profiles.yaml, and domain YAML files.
argument-hint: "request_type_id=<id|all> channels=crm_ticket,broker_email,call_transcript output_path=<path> noise_mode=<none|light|mixed|custom>"
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

Generate synthetic Czech telecom intake input files from existing structured reference files. This skill creates **input text records only**; it must not invent new request types, product/service/discount IDs, or processing/validation rules.

## Default project paths

Unless the user provides overrides, use these files:

- Domain catalogs: `src/main/resources/domain/*.yaml`
- Request type definitions: `src/main/resources/dataset/request_types.yaml`
- Reference outputs: `src/main/resources/dataset/reference_outputs.yaml`
- Noise profiles: `src/main/resources/dataset/noise_profiles.yaml`
- Output directory: `src/main/resources/dataset/inputs/`

If a required file is missing, report the missing path and stop.

## Parameters

Parse parameters from the user request. Use defaults when safe.

Required unless `reference_ids` is provided:

- `request_type_id`: one request type ID, or `all`

Optional:

- `reference_ids`: comma-separated explicit reference IDs. If present, generate only these references.
- `channels`: comma-separated subset of `crm_ticket`, `broker_email`, `call_transcript`. Default: all three.
- `noise_mode`: `none`, `light`, `mixed`, or `custom`. Default: `light`.
- `noise_tags`: comma-separated noise tags, used only when `noise_mode=custom`.
- `variants_per_reference_per_channel`: integer. Default: `1`.
- `output_format`: `json`, `jsonl`, or `yaml`. Default: `json`.
- `output_path`: explicit output file path. Default: `src/main/resources/dataset/inputs/<request_type_id>/inputs_<request_type_id>.json`.
- `include_reference_output`: `true` or `false`. Default: `true`.
- `overwrite`: `true` or `false`. Default: `false`.
- `dry_run`: `true` or `false`. Default: `false`.
- `allow_reference_mutation`: `true` or `false`. Default: `false`.
- `batch_id`: optional stable suffix for generated `input_id` values.

## Core constraints

1. Generate only **Czech** input texts in a realistic Czech telecom context.
2. Use only product, service, discount, rule, and locality IDs present in the loaded source files.
3. Do not add facts to `input_text` that are not present in the selected `reference_output`, unless they are irrelevant noise and are not extractable fields.
4. Do not remove extractable facts from `input_text` when `allow_reference_mutation=false`.
5. Preserve the semantic kind of each reference:
   - `incomplete_reference`: generated input must remain incomplete in exactly the same material way.
   - `complete_valid_reference`: generated input must express all reference fields and must not introduce rule violations.
   - `complete_invalid_reference`: generated input must express all reference fields and must preserve the intended business-rule violation.
6. Do not create OCR, invoice, banking, healthcare, insurance, or non-telecom examples.
7. Do not generate processing instructions, prompts for extraction, guardrails, or evaluation code unless explicitly requested.
8. Keep text realistic but compact. Avoid theatrical dialogue, absurd prices, fake operators outside common Czech context, or impossible addresses.

## Workflow

1. Read and understand the source files listed in Default project paths.
2. Validate requested IDs:
   - `request_type_id` must exist in `request_types.yaml` unless `all`.
   - every `reference_id` must exist in `reference_outputs.yaml`.
   - every requested `channel` must exist in `noise_profiles.yaml`.
   - every requested `noise_tag` must exist and must apply to the selected channel.
3. Select references:
   - if `reference_ids` is present, select those references;
   - else select all references under `request_type_id`;
   - else if `request_type_id=all`, select all references.
4. Choose noise:
   - `none`: use only clean channel style; no additional noise tags.
   - `light`: use at most one mild noise tag per record, preferably channel-specific style or minor surface noise.
   - `mixed`: use at most two compatible noise tags per record. Do not combine contradictory, omission, and invalid-entity noise in one record unless explicitly requested.
   - `custom`: use `noise_tags`, but reject incompatible tags.
5. Generate input records. For each selected reference and channel, create `variants_per_reference_per_channel` records.
6. Write the output file unless `dry_run=true`.
7. Report a compact summary: number of generated records, output path, request types, channels, and any skipped references.

## Output record schema

Each generated record must use this shape:

```json
{
  "input_id": "rt_internet_tv_bundle_order__ref_internet_tv_bundle_02__broker_email__v01",
  "request_type_id": "rt_internet_tv_bundle_order",
  "reference_id": "ref_internet_tv_bundle_02",
  "reference_kind": "complete_valid_reference",
  "channel": "broker_email",
  "channel_style": "broker_email",
  "noise_tags": ["greeting_signature_noise"],
  "business_perturbation_tags": [],
  "input_text": "Dobrý den, posílám poptávku...",
  "reference_output": {},
  "missing_required_fields": [],
  "expected_rule_violations": [],
  "generation_note": "Complete valid reference expressed as broker email with greeting/signature noise."
}
```

If `include_reference_output=false`, omit `reference_output`, `missing_required_fields`, and `expected_rule_violations`, but keep `reference_id` and `reference_kind`.

## Output file schema for JSON

```json
{
  "dataset_version": 1,
  "source_files": [
    "src/main/resources/domain/*.yaml",
    "src/main/resources/dataset/request_types.yaml",
    "src/main/resources/dataset/reference_outputs.yaml",
    "src/main/resources/dataset/noise_profiles.yaml"
  ],
  "generation_parameters": {
    "request_type_id": "rt_internet_tv_bundle_order",
    "channels": ["crm_ticket", "broker_email", "call_transcript"],
    "noise_mode": "light",
    "variants_per_reference_per_channel": 1,
    "allow_reference_mutation": false
  },
  "records": []
}
```

## Channel style guidance

Use these distinctions strictly:

- `crm_ticket`: internal, terse, semi-structured, abbreviations, bullet-like fragments, minimal politeness.
- `broker_email`: coherent email from a broker/salesperson, greeting/signature, contextual sentences, sometimes redundant information.
- `call_transcript`: spoken Czech, hesitations, self-corrections, interruptions, partial sentences. Still keep it readable.

## Reference mutation policy

Default is `allow_reference_mutation=false`. Under this policy:

- Do not apply `missing_critical` or `missing_noncritical` to a complete reference.
- Do not add a nonexistent product or discount unless the selected reference already contains an invalid or unresolved item.
- Do not introduce contradictions that change the expected reference output.
- Surface noise, duplication, email thread noise, and irrelevant information are safe because they do not change extractable facts.

If the user explicitly sets `allow_reference_mutation=true`, create a new derived reference output inside the generated record and clearly mark it with `derived_reference_output: true`. Do not modify source reference files.

## Additional resources

- `references/parameter-reference.md`: detailed parameter behavior.
- `references/output-contract.md`: exact input record contract and consistency rules.
- `examples/example-invocations.md`: example slash-command usage.
