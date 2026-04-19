# Parameter Reference

## mode
- `extraction`
- `classification`
- `both`

Default: `extraction`

## request_type_ids
Comma-separated list of known request types, or `all`.

Examples:
- `request_type_ids=rt_new_mobile_order`
- `request_type_ids=rt_new_mobile_order,rt_fixed_internet_with_hardware_order`
- `request_type_ids=all`

## extraction_reference_ids
Explicit extraction reference IDs.

Example:
- `extraction_reference_ids=ref_new_mobile_order_02,ref_internet_tv_bundle_02`

## classification_scenario_ids
Explicit classification scenario IDs.

Example:
- `classification_scenario_ids=cls_multi_known_01,cls_unknown_01`

## classifier_outcome_types
Subset of:
- `single_known_request_type`
- `single_known_request_type_with_unknown_tail`
- `multi_intent_known_request_types`
- `unknown_or_out_of_scope`
- `ambiguous_or_insufficient_signal`

## channels
Allowed values:
- `crm_ticket`
- `broker_email`
- `call_transcript`

Default: all three.

## noise_mode
- `none`: clean baseline.
- `light`: at most one mild or channel-specific noise tag.
- `mixed`: up to two compatible noise tags.
- `custom`: use explicit `noise_tags`.

## noise_tags
Only valid with `noise_mode=custom`. Tags must exist in `noise_profiles.yaml` and apply to the selected channel.

## variants_per_base
How many variants to generate per selected base item and channel.

Default: `1`

## include_gold_annotation
Default: `true`

If `false`, omit `gold_annotation` but keep `source` metadata.

## include_observability_metadata
Default: `true`

If `false`, omit the `observability` object.

## output_format
- `json`
- `jsonl`
- `yaml`

Default: `json`

## dry_run
Default: `false`

When `true`, validate selection and report counts, but do not write files.
