# Output contract

This document defines the required JSON output shape of the telecom benchmark generator.
It includes example values, rules for data population are defined within Claude skill.

Every generated file must be a JSON object with the same top-level envelope, regardless of `mode`.

## Top-level dataset envelope

The output file must use this structure:

```json
{
  "generatedAt": "2026-04-24",
  "generationRunParams": {
    "mode": "extraction",
    "requestTypeIds": [
      "rt_new_mobile_order"
    ],
    "channels": [
      "crm_ticket"
    ],
    "noiseCount": 1,
    "completenessMode": "mixed",
    "variantsPerCase": 1,
    "outputPath": "src/main/resources/dataset/inputs/example.json"
  },
  "records": [
    {
      "recordId": "ext_0001",
      "mode": "extraction",
      "channel": "crm_ticket",
      "noiseTags": [
        "shorthandInternal"
      ],
      "generationParams": {
        "requestTypeId": "rt_new_mobile_order",
        "requestTypeIds": [
          "rt_new_mobile_order"
        ],
        "scenarioKind": null,
        "completenessMode": "complete",
        "noiseCount": 1,
        "variantIndex": 0
      },
      "inputText": "Zákazník: Veronika Jarošová | Tel: +420 774 551 008 | Stav: NOVÝ | Požadavek: Start 5G, závazek 24 měs., bez portace.",
      "expectedClassification": {
        "requestTypeId": "rt_new_mobile_order",
        "unclassifiableReason": null
      },
      "expectedExtractions": [
        {
          "requestTypeId": "rt_new_mobile_order",
          "dto": {
            "customerStatus": "new",
            "customerName": "Veronika Jarošová",
            "contactPhone": "+420 774 551 008",
            "contactEmail": null,
            "requestedServices": [
              {
                "serviceId": "svc_mobile_start_5g",
                "quantity": 1
              }
            ],
            "contractTermMonths": 24,
            "portingRequested": false,
            "portedNumbers": [],
            "requestedDiscounts": []
          },
          "missingFieldPaths": []
        }
      ],
      "evidence": {
        "requestSegments": [
          {
            "requestTypeId": "rt_new_mobile_order",
            "segmentRole": "primaryKnown",
            "evidenceSnippets": [
              "Start 5G, závazek 24 měs., bez portace"
            ]
          }
        ],
        "materializedFieldPaths": [
          "customerStatus",
          "customerName",
          "contactPhone",
          "requestedServices[0].serviceId",
          "requestedServices[0].quantity",
          "contractTermMonths",
          "portingRequested"
        ],
        "intentionallyOmittedFieldPaths": [
          "contactEmail",
          "portedNumbers",
          "requestedDiscounts"
        ],
        "evidenceByFieldPath": {
          "customerStatus": {
            "normalizedValue": "new",
            "evidenceSnippets": [
              "NOVÝ"
            ]
          },
          "customerName": {
            "normalizedValue": "Veronika Jarošová",
            "evidenceSnippets": [
              "Veronika Jarošová"
            ]
          },
          "contactPhone": {
            "normalizedValue": "+420 774 551 008",
            "evidenceSnippets": [
              "+420 774 551 008"
            ]
          },
          "requestedServices[0].serviceId": {
            "normalizedValue": "svc_mobile_start_5g",
            "evidenceSnippets": [
              "Start 5G"
            ]
          },
          "requestedServices[0].quantity": {
            "normalizedValue": 1,
            "evidenceSnippets": [
              "Start 5G"
            ]
          },
          "contractTermMonths": {
            "normalizedValue": 24,
            "evidenceSnippets": [
              "24 měs."
            ]
          },
          "portingRequested": {
            "normalizedValue": false,
            "evidenceSnippets": [
              "bez portace"
            ]
          }
        },
        "distractorSnippets": [],
        "generationNote": "CRM ticket with shorthand style. Optional fields not mentioned in the text are intentionally omitted."
      }
    }
  ]
}
```

The example above is valid JSON and shows the required structure. Generated files may contain different values,
different record counts, and different modes, but they must preserve the same envelope and field names.

## Top-level fields

- `generatedAt`: date of generation in `YYYY-MM-DD` format.
- `generationRunParams`: parameters shared by the whole generated file.
- `records`: array of generated benchmark records.

`generatorVersion` must not be included.

## `generationRunParams`

Recommended fields:

- `mode`: `extraction` or `classification`.
- `requestTypeIds`: list of requested known request type IDs, or `all` if the invocation used `all`.
- `channels`: list of channels used in the generation run.
- `noiseCount`: numeric number of noise tags requested.
- `completenessMode`: `complete`, `incomplete`, or `mixed`, when relevant.
- `scenarioKinds`: list of classification scenario kinds, when relevant.
- `knownRequestCount`: number or range of known requests, when relevant.
- `variantsPerCase`: requested number of variants per generated case.
- `outputPath`: output file path.

## Record fields

Each record must contain:

- `recordId`: stable record ID unique inside the file.
- `mode`: same mode as `generationRunParams.mode`.
- `channel`: one of `crm_ticket`, `broker_email`, `call_transcript`.
- `noiseTags`: list of applied noise tags.
- `generationParams`: record-specific parameters.
- `inputText`: generated Czech input text.
- `expectedClassification`: expected classification result.
- `expectedExtractions`: expected extraction DTOs.
- `evidence`: audit metadata for generated text and gold values.

## `generationParams`

Allowed fields:

- `requestTypeId`: known request type ID for single-request records.
- `requestTypeIds`: list of known request type IDs for multi-request records.
- `scenarioKind`: classification scenario kind, or `null` if not applicable.
- `completenessMode`: completeness used for the record.
- `noiseCount`: numeric number of requested noise tags.
- `variantIndex`: zero-based variant index.

Do not include `templateId`, `templateIds`, or `scenarioTemplateId`.

## `expectedClassification`

Shape:

```json
{
  "requestTypeId": "rt_new_mobile_order",
  "unclassifiableReason": null
}
```

Rules:

- `requestTypeId` must contain exactly one value.
- It must be one known `rt_*` request type ID or `unclassifiable`.
- If `requestTypeId` is a known `rt_*`, `unclassifiableReason` must be `null`.
- If `requestTypeId` is `unclassifiable`, `unclassifiableReason` must be one of the reason IDs from
  `classification_targets.yaml`.

## `expectedExtractions`

Mode rules:

- `extraction`: exactly one item per record.
- `classification`: empty array.

Each item must contain:

- `requestTypeId`: known request type ID.
- `dto`: expected DTO object using fields from `request_types.yaml`.
- `missingFieldPaths`: field paths intentionally missing from the input and expected DTO because the case is incomplete.

Do not include `templateId`.

## `evidence`

`evidence` is audit metadata only. It is not the primary expected output used for scoring.

Fields:

- `requestSegments`: list of known or unknown request segments found in `inputText`.
- `materializedFieldPaths`: field paths supported by the rendered text.
- `intentionallyOmittedFieldPaths`: field paths intentionally absent from the rendered text.
- `evidenceByFieldPath`: mapping from materialized field path to normalized value and text snippets.
- `distractorSnippets`: snippets that are intentionally irrelevant or outside the primary task.
- `generationNote`: short human-readable note about the generated record.

## Evidence coverage rule

For every expected extraction object:

1. Compute the expected DTO field paths from `request_types.yaml` and the generated DTO object.
2. Include every materialized field path in `materializedFieldPaths`.
3. Include every intentionally absent field path in `intentionallyOmittedFieldPaths`.
4. The union of `materializedFieldPaths` and `intentionallyOmittedFieldPaths` must cover all field paths of the expected
   DTO object.
5. A field path must not appear in both lists.
6. `evidenceByFieldPath` keys must correspond to materialized field paths.
7. For nested list values, use indexed paths such as `requestedServices[0].serviceId`.
8. `normalizedValue` should use the same JSON type as the DTO value whenever possible.

## Mandatory rules

1. The top-level value must always be a JSON object.
2. The top-level object must always contain `generatedAt`, `generationRunParams`, and `records`.
3. The top-level object must never be replaced by a raw array.
4. `records` must always be an array, even if it contains only one record.
5. Key names must remain in camelCase.
6. The output JSON must be valid RFC8259 JSON.
7. Do not use placeholders such as `...`.
8. Do not include `generatorVersion`.
9. Do not include `templateId`, `templateIds`, or `scenarioTemplateId`.

## Evaluation guidance

- Extraction evaluation compares `expectedExtractions[*].dto` and `missingFieldPaths`.
- Classification evaluation compares `expectedClassification.requestTypeId` and optionally `unclassifiableReason`.
- `evidence` and `generationParams` are for audit, debugging, and manual validation. They do not enter the primary score
  unless an additional dataset-quality check is implemented.
