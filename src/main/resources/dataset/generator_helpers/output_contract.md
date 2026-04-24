# Output contract v2

This document defines the required output shape of the telecom benchmark generator.

## Top-level dataset envelope

Every generated file must be a JSON object with this shape, regardless of `mode`.

```json
{
  "generatedAt": "YYYY-MM-DD",
  "generationRunParams": {
    "mode": "extraction | classification | combined",
    "requestTypeIds": ["rt_*"],
    "channels": ["crm_ticket", "broker_email", "call_transcript"],
    "noiseProfile": "clean | light | medium",
    "variantsPerCase": 1
  },
  "records": [
    {
      "recordId": "...",
      "mode": "extraction | classification | combined",
      "channel": "crm_ticket | broker_email | call_transcript",
      "noiseTags": ["..."],
      "generationParams": {
        "requestTypeId": "rt_*",
        "requestTypeIds": ["rt_*"],
        "scenarioKind": "...",
        "scenarioTemplateId": "...",
        "templateId": "...",
        "templateIds": {"rt_*": "..."},
        "completeness": "complete | incomplete | mixed",
        "perRequestCompleteness": "complete | incomplete | mixed",
        "noiseProfile": "clean | light | medium",
        "variantIndex": 0
      },
      "inputText": "...",
      "expectedClassification": {
        "requestTypeId": "rt_* | unclassifiable",
        "unclassifiableReason": null
      },
      "expectedExtractions": [
        {
          "requestTypeId": "rt_*",
          "templateId": "...",
          "dto": {"...": "..."},
          "missingFieldPaths": ["..."]
        }
      ],
      "evidence": {
        "requestSegments": [
          {
            "requestTypeId": "rt_* | unknown",
            "segmentRole": "primaryKnown | secondaryKnown | unknownTail",
            "evidenceSnippets": ["..."]
          }
        ],
        "materializedFieldPaths": ["customerName", "requestedServices[0].serviceId"],
        "intentionallyOmittedFieldPaths": ["contactEmail"],
        "evidenceByFieldPath": {
          "customerName": {
            "normalizedValue": "Veronika Jarošová",
            "evidenceSnippets": ["Veronika Jarošová"]
          }
        },
        "distractorSnippets": ["..."],
        "generationNote": "..."
      }
    }
  ]
}
```

## Mandatory rules

1. The top-level value must always be a JSON object.
2. The top-level object must always contain:
   - `generatedAt`
   - `generationRunParams`
   - `records`
3. The top-level object must never be replaced by a raw array.
4. `generatorVersion` must not be included.
5. `records` must always be an array, even if it contains only one record.
6. `generationRunParams.mode` and each `record.mode` must match the requested mode.
7. Key names must remain in camelCase.
8. `expectedClassification.requestTypeId` must contain exactly one value:
   - one known `rt_*`
   - or `unclassifiable`
9. `expectedExtractions`:
   - `extraction` mode -> exactly 1 item per record
   - `classification` mode -> empty array
   - `combined` mode -> one item for every known request present in the text

## Interpretation

- `generatedAt` describes the generation run, not the scenario itself.
- `generationRunParams` contains parameters shared by the whole file.
- `generationParams` contains parameters specific to a single record.
- `evidence` is audit metadata only and must not be used as the primary expected output.

## Evaluation guidance

- Extraction evaluation compares `expectedExtractions[*].dto` and `missingFieldPaths`.
- Classification evaluation compares `expectedClassification.requestTypeId` and optionally `unclassifiableReason`.
- `evidence` and `generationParams` do not enter the score.
