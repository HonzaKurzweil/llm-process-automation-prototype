# Final dataset contract

Tento kontrakt popisuje výstup generátoru benchmark dat. Patří do `generator_helpers`, protože je součástí specifikace generovaného datasetu, nikoli pouze interní dokumentace skillu.

## Společná struktura záznamu

```json
{
  "recordId": "...",
  "mode": "extraction | classification | combined",
  "channel": "crm_ticket | broker_email | call_transcript",
  "noiseTags": ["..."],
  "generationParams": { "...": "..." },
  "inputText": "...",
  "expectedClassification": {
    "requestTypeId": "rt_* | unclassifiable",
    "unclassifiableReason": null
  },
  "expectedExtractions": [
    {
      "requestTypeId": "rt_*",
      "dto": { "...": "..." },
      "missingFieldPaths": ["..."],
      "templateId": "..."
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
    "intentionallyOmittedFieldPaths": ["installationAddress"],
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
```

## Význam

- `expectedClassification` je cílový výstup klasifikační služby.
- `expectedExtractions` je seznam extrakčních gold objektů.
  - v režimu `extraction` obsahuje přesně 1 položku
  - v režimu `classification` je prázdný
  - v režimu `combined` obsahuje 1 až N známých požadavků přítomných ve vstupu
- `evidence` slouží pouze pro audit správnosti vygenerovaného vstupu.

## Doporučené vyhodnocení

- Extrakce: porovnávat pouze `expectedExtractions[*].dto` a `missingFieldPaths`.
- Klasifikace: porovnávat pouze `expectedClassification.requestTypeId` a případně `unclassifiableReason`.
- `evidence` a `generationParams` nevstupují do skóre, slouží pro audit a stratifikaci datasetu.
