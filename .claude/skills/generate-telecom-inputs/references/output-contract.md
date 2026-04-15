# Output Contract

Generated input records are evaluation data. They must be deterministic enough to compare an LLM extraction result against `reference_output`.

## Required fields per record

- `input_id`: stable unique ID.
- `request_type_id`: source request type.
- `reference_id`: source reference output.
- `reference_kind`: one of `incomplete_reference`, `complete_valid_reference`, `complete_invalid_reference`.
- `channel`: one of `crm_ticket`, `broker_email`, `call_transcript`.
- `channel_style`: same as channel unless a future style taxonomy is introduced.
- `noise_tags`: list of applied text-noise tags.
- `business_perturbation_tags`: list of applied business perturbation tags, usually empty unless the reference is invalid.
- `input_text`: generated Czech text.
- `reference_output`: copied source reference output when `include_reference_output=true`.
- `missing_required_fields`: copied from source reference.
- `expected_rule_violations`: copied from source reference.
- `generation_note`: short note explaining the input variant.

## Consistency rules

1. If a field appears in `reference_output`, it should be clearly recoverable from `input_text` unless the field is a normalized ID derived from a natural-language product name.
2. If `missing_required_fields` contains a field, the generated text must not reveal that missing value.
3. If `expected_rule_violations` is non-empty, the generated text must include the facts that cause the violation.
4. Irrelevant noise must not create additional extractable telecom facts.
5. Never include hidden answers outside `reference_output`.
