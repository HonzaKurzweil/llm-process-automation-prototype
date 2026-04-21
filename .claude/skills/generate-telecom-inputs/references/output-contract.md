# Output Contract

Generated records are benchmark inputs. They must be precise enough to support later comparison against gold annotations
and simple observability-based audits.

## File-level shape (JSON)

```json
{
  "datasetVersion": 6,
  "sourceFiles": {
    "domain": "src/main/resources/domain/*.yaml",
    "requestTypes": "src/main/resources/dataset/generator_helpers/request_types_v3.yaml",
    "classifierOutcomes": "src/main/resources/dataset/generator_helpers/classifier_outcomes_v3.yaml",
    "noiseProfiles": "src/main/resources/dataset/generator_helpers/noise_profiles.yaml",
    "referenceOutputs": "src/main/resources/dataset/generator_helpers/reference_outputs_v4.yaml"
  },
  "generationParameters": {},
  "records": []
}
```

## Common required fields per record

- `recordId`
- `mode`
- `channel`
- `channelStyle`
- `noiseTags`
- `businessPerturbationTags`
- `source`
- `inputText`

## Extraction record shape

```json
{
  "recordId": "ext__ref_new_mobile_order_02__crm_ticket__v01",
  "mode": "extraction",
  "channel": "crm_ticket",
  "channelStyle": "crm_ticket",
  "noiseTags": [
    "shorthand_internal"
  ],
  "businessPerturbationTags": [],
  "source": {
    "requestTypeId": "rt_new_mobile_order",
    "referenceId": "ref_new_mobile_order_02",
    "referenceKind": "complete_valid_reference"
  },
  "inputText": "...",
  "goldAnnotation": {
    "extraction": {
      "requestTypeId": "rt_new_mobile_order",
      "referenceId": "ref_new_mobile_order_02",
      "referenceKind": "complete_valid_reference",
      "extractedFields": {},
      "missingRequiredFields": [],
      "missingRequiredPaths": [],
      "expectedRuleViolations": []
    }
  },
  "observability": {
    "materializedFieldPaths": [
      "customerName",
      "requestedServices[0].serviceId"
    ],
    "intentionallyOmittedFieldPaths": [],
    "evidenceByFieldPath": {
      "customerName": {
        "normalizedValue": "Klára Horáková",
        "evidenceSnippets": [
          "Klára Horáková"
        ]
      },
      "requestedServices[0].serviceId": {
        "normalizedValue": "svc_mobile_start_5g",
        "evidenceSnippets": [
          "Mobil Start 5G"
        ]
      }
    },
    "componentSegments": [
      {
        "componentRole": "primary_known",
        "requestTypeId": "rt_new_mobile_order",
        "referenceId": "ref_new_mobile_order_02",
        "evidenceSnippets": [
          "Mobil Start 5G",
          "přenos čísla"
        ]
      }
    ],
    "distractorSnippets": [],
    "generationNote": "Complete valid extraction record rendered as CRM ticket."
  }
}
```

## Classification record shape

```json
{
  "recordId": "cls__cls_multi_known_01__broker_email__v01",
  "mode": "classification",
  "channel": "broker_email",
  "channelStyle": "broker_email",
  "noiseTags": [
    "greeting_signature_noise"
  ],
  "businessPerturbationTags": [],
  "source": {
    "scenarioId": "cls_multi_known_01",
    "scenarioType": "multiple_known_request_types_present"
  },
  "inputText": "...",
  "goldAnnotation": {
    "classification": {
      "scenarioId": "cls_multi_known_01",
      "scenarioType": "multiple_known_request_types_present",
      "expectedRequestTypeIdReference": "unclassifiable",
      "unclassifiableReasonId": "multiple_known_request_types_present",
      "linkedReferenceIds": [
        "ref_new_mobile_order_02",
        "ref_fixed_internet_hw_02"
      ],
      "linkedRequestTypeIds": [
        "rt_new_mobile_order",
        "rt_fixed_internet_with_hardware_order"
      ]
    }
  },
  "observability": {
    "materializedFieldPaths": [
      "requestedServices[0].serviceId",
      "installationAddress"
    ],
    "intentionallyOmittedFieldPaths": [],
    "evidenceByFieldPath": {},
    "componentSegments": [
      {
        "componentRole": "known_intent_1",
        "requestTypeId": "rt_new_mobile_order",
        "referenceId": "ref_new_mobile_order_02",
        "evidenceSnippets": [
          "Mobil Start 5G",
          "přenos čísla"
        ]
      },
      {
        "componentRole": "known_intent_2",
        "requestTypeId": "rt_fixed_internet_with_hardware_order",
        "referenceId": "ref_fixed_internet_hw_02",
        "evidenceSnippets": [
          "Optika Domů 300",
          "Purkyňova 85"
        ]
      }
    ],
    "distractorSnippets": [],
    "generationNote": "Two separate known intents kept distinct inside one broker email."
  }
}
```

## Consistency requirements

1. Every evidence snippet should be a literal substring of `inputText` whenever feasible.
2. A path listed in `intentionallyOmittedFieldPaths` must not appear in `evidenceByFieldPath`.
3. `componentSegments` must align with the intended classification scenario.
4. `distractorSnippets` may add noise, but must not introduce accidental extra known intents.
5. Extraction records must preserve the exact reference-kind semantics:
    - incomplete stays incomplete,
    - complete valid stays valid,
    - complete invalid stays invalid for the same rule reason.
6. Classification records must preserve the intended classifier result from the scenario definition.
7. Only records with `expectedRequestTypeIdReference` different from `unclassifiable` are eligible for direct downstream
   extraction.
