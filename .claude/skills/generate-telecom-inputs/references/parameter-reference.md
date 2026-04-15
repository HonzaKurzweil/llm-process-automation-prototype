# Parameter Reference

## request_type_id
Controls which request type to generate. Use `all` to generate all request types.

Examples:
- `request_type_id=rt_new_mobile_order`
- `request_type_id=all`

## reference_ids
Overrides `request_type_id` selection and generates only explicit references.

Example:
- `reference_ids=ref_new_mobile_order_01,ref_internet_tv_bundle_02`

## channels
Allowed values:
- `crm_ticket`
- `broker_email`
- `call_transcript`

Default: all three.

## noise_mode
- `none`: clean baseline.
- `light`: one mild tag; safe for first dataset generation.
- `mixed`: up to two compatible tags; useful for harder benchmark inputs.
- `custom`: use explicit `noise_tags`.

## noise_tags
Only valid with `noise_mode=custom`. Tags must exist in `noise_profiles.yaml` and apply to the selected channel.

## allow_reference_mutation
Default: `false`.

Keep it `false` for the main benchmark, because generated input must match existing `reference_output`. Use `true` only when deliberately creating derived examples with a different reference output.
