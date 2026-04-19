# Parameter Reference

## mode
- `extraction`
- `classification`
- `both`

Default: `extraction`

## request_type_ids
Comma-separated list of known request types, or `all`.

## extraction_reference_ids
Explicit extraction reference IDs.

## classification_scenario_ids
Explicit classification scenario IDs.

## classifier_outcome_types
Subset of classifier outcome types defined in `classifier_outcomes_v1.yaml`.

## channels
Allowed values:
- `crm_ticket`
- `broker_email`
- `call_transcript`

Default: all three.

## noise_mode
- `none`
- `light`
- `mixed`
- `custom`

## noise_tags
Only valid with `noise_mode=custom`. Tags must exist in `noise_profiles.yaml`.

## variants_per_base
How many variants to generate per selected base item and channel.

Default: `1`

## include_gold_annotation
Default: `true`

## include_observability_metadata
Default: `true`

## output_format
- `json`
- `jsonl`
- `yaml`

Default: `json`

## dry_run
Default: `false`
