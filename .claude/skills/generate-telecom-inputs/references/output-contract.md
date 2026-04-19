# Output Contract

Generated records are benchmark inputs. They must be precise enough to support later comparison against gold annotations and simple observability-based audits.

## File-level shape (JSON)

```json
{
  "dataset_version": 3,
  "source_files": {
    "domain": "src/main/resources/domain/*.yaml",
    "request_types": "src/main/resources/dataset/generator_helpers/request_types_v3.yaml",
    "classifier_outcomes": "src/main/resources/dataset/generator_helpers/classifier_outcomes_v1.yaml",
    "noise_profiles": "src/main/resources/dataset/generator_helpers/noise_profiles.yaml",
    "reference_outputs": "src/main/resources/dataset/generator_helpers/reference_outputs_v2.yaml"
  },
  "generation_parameters": {},
  "records": []
}
```

## Common required fields per record

- `record_id`
- `mode`
- `channel`
- `channel_style`
- `noise_tags`
- `business_perturbation_tags`
- `source`
- `input_text`

## Extraction record essentials

Each extraction record must contain:

- clear `source.request_type_id`
- clear `source.reference_id`
- `gold_annotation.extraction`
- `observability` metadata unless disabled

## Classification record essentials

Each classification record must contain:

- clear `source.scenario_id`
- clear `source.scenario_type`
- `gold_annotation.classification`
- `observability` metadata unless disabled

## Consistency requirements

1. Every evidence snippet should be a literal substring of `input_text` whenever feasible.
2. A path listed in `intentionally_omitted_field_paths` must not appear in `evidence_by_field_path`.
3. `component_segments` must align with the intended classifier outcome.
4. `distractor_snippets` may add noise, but must not introduce accidental extra known intents.
5. Extraction records must preserve the exact reference kind semantics.
6. Classification records must preserve the intended outcome type from the scenario definition.
