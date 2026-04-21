# Parameter Reference

## mode

- `extraction`
- `classification`
- `both`

Default: `extraction`

## requestTypeIds

Comma-separated list of known request types, or `all`.

Examples:

- `requestTypeIds=rt_new_mobile_order`
- `requestTypeIds=rt_new_mobile_order,rt_fixed_internet_with_hardware_order`
- `requestTypeIds=all`

## extractionReferenceIds

Explicit extraction reference IDs.

Example:

- `extractionReferenceIds=ref_new_mobile_order_02,ref_internet_tv_bundle_02`

## classificationScenarioIds

Explicit classification scenario IDs.

Example:

- `classificationScenarioIds=cls_multi_known_01,cls_unknown_01`

## classifierOutcomeTypes

Subset of scenario types from `classifier_outcomes_v3.yaml`:

- `single_known_request_type`
- `known_request_type_with_unknown_tail`
- `multiple_known_request_types_present`
- `unknown_or_out_of_scope`
- `ambiguous_or_insufficient_signal`

These values filter `classificationScenarios[*].scenarioType`.

## channels

Allowed values:

- `crm_ticket`
- `broker_email`
- `call_transcript`

Default: all three.

## noiseMode

- `none`: clean baseline.
- `light`: at most one mild or channel-specific noise tag.
- `mixed`: up to two compatible noise tags.
- `custom`: use explicit `noiseTags`.

## noiseTags

Only valid with `noiseMode=custom`. Tags must exist in `noise_profiles.yaml` and apply to the selected channel.

## variantsPerBase

How many variants to generate per selected base item and channel.

Default: `1`

## includeGoldAnnotation

Default: `true`

If `false`, omit `goldAnnotation` but keep `source` metadata.

## includeObservabilityMetadata

Default: `true`

If `false`, omit the `observability` object.

## outputFormat

- `json`
- `jsonl`
- `yaml`

Default: `json`

## dryRun

Default: `false`

When `true`, validate selection and report counts, but do not write files.
