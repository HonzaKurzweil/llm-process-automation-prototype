# Output Contract

Generated records are benchmark inputs. They must be precise enough to support later comparison against gold annotations and simple observability-based audits.

## File-level shape (JSON)

```json
{
  "dataset_version": 5,
  "source_files": {
    "domain": "src/main/resources/domain/*.yaml",
    "request_types": "src/main/resources/dataset/generator_helpers/request_types_v3.yaml",
    "classifier_outcomes": "src/main/resources/dataset/generator_helpers/classifier_outcomes_v3.yaml",
    "noise_profiles": "src/main/resources/dataset/generator_helpers/noise_profiles.yaml",
    "reference_outputs": "src/main/resources/dataset/generator_helpers/reference_outputs_v4.yaml"
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

## Extraction record shape

```json
{
  "record_id": "ext__ref_new_mobile_order_02__crm_ticket__v01",
  "mode": "extraction",
  "channel": "crm_ticket",
  "channel_style": "crm_ticket",
  "noise_tags": ["shorthand_internal"],
  "business_perturbation_tags": [],
  "source": {
    "request_type_id": "rt_new_mobile_order",
    "reference_id": "ref_new_mobile_order_02",
    "reference_kind": "complete_valid_reference"
  },
  "input_text": "...",
  "gold_annotation": {
    "extraction": {
      "request_type_id": "rt_new_mobile_order",
      "reference_id": "ref_new_mobile_order_02",
      "reference_kind": "complete_valid_reference",
      "extracted_fields": {},
      "missing_required_fields": [],
      "missing_required_paths": [],
      "expected_rule_violations": []
    }
  },
  "observability": {
    "materialized_field_paths": ["customer_name", "requested_services[0].service_id"],
    "intentionally_omitted_field_paths": [],
    "evidence_by_field_path": {
      "customer_name": {
        "normalized_value": "Klára Horáková",
        "evidence_snippets": ["Klára Horáková"]
      },
      "requested_services[0].service_id": {
        "normalized_value": "svc_mobile_start_5g",
        "evidence_snippets": ["Mobil Start 5G"]
      }
    },
    "component_segments": [
      {
        "component_role": "primary_known",
        "request_type_id": "rt_new_mobile_order",
        "reference_id": "ref_new_mobile_order_02",
        "evidence_snippets": ["Mobil Start 5G", "přenos čísla"]
      }
    ],
    "distractor_snippets": [],
    "generation_note": "Complete valid extraction record rendered as CRM ticket."
  }
}
```

## Classification record shape

```json
{
  "record_id": "cls__cls_multi_known_01__broker_email__v01",
  "mode": "classification",
  "channel": "broker_email",
  "channel_style": "broker_email",
  "noise_tags": ["greeting_signature_noise"],
  "business_perturbation_tags": [],
  "source": {
    "scenario_id": "cls_multi_known_01",
    "scenario_type": "multiple_known_request_types_present"
  },
  "input_text": "...",
  "gold_annotation": {
    "classification": {
      "scenario_id": "cls_multi_known_01",
      "scenario_type": "multiple_known_request_types_present",
      "expected_request_type_id_reference": "unclassifiable",
      "unclassifiable_reason_id": "multiple_known_request_types_present",
      "linked_reference_ids": ["ref_new_mobile_order_02", "ref_fixed_internet_hw_02"],
      "linked_request_type_ids": ["rt_new_mobile_order", "rt_fixed_internet_with_hardware_order"]
    }
  },
  "observability": {
    "materialized_field_paths": [
      "requested_services[0].service_id",
      "installation_address"
    ],
    "intentionally_omitted_field_paths": [],
    "evidence_by_field_path": {},
    "component_segments": [
      {
        "component_role": "known_intent_1",
        "request_type_id": "rt_new_mobile_order",
        "reference_id": "ref_new_mobile_order_02",
        "evidence_snippets": ["Mobil Start 5G", "přenos čísla"]
      },
      {
        "component_role": "known_intent_2",
        "request_type_id": "rt_fixed_internet_with_hardware_order",
        "reference_id": "ref_fixed_internet_hw_02",
        "evidence_snippets": ["Optika Domů 300", "Purkyňova 85"]
      }
    ],
    "distractor_snippets": [],
    "generation_note": "Two separate known intents kept distinct inside one broker email."
  }
}
```

## Consistency requirements

1. Every evidence snippet should be a literal substring of `input_text` whenever feasible.
2. A path listed in `intentionally_omitted_field_paths` must not appear in `evidence_by_field_path`.
3. `component_segments` must align with the intended classification scenario.
4. `distractor_snippets` may add noise, but must not introduce accidental extra known intents.
5. Extraction records must preserve the exact reference kind semantics:
   - incomplete stays incomplete,
   - complete valid stays valid,
   - complete invalid stays invalid for the same rule reason.
6. Classification records must preserve the intended classifier result from the scenario definition.
7. Only records with `expected_request_type_id_reference` different from `unclassifiable` are eligible for direct downstream extraction.
