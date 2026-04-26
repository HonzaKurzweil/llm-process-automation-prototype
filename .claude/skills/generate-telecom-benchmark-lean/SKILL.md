---
name: generate-telecom-benchmark-final
description: Generate Czech telecom benchmark datasets from request types, domain catalogs, entity pools, channel profiles, and noise profiles.
when_to_use: Use this skill when the user asks to generate benchmark datasets for extraction or classification evaluation in the telecom prototype.
argument-hint: "mode=<extraction|classification> requestTypeIds=<id|all> channels=crm_ticket,broker_email,call_transcript noiseCount=<0..N> completenessMode=<complete|incomplete|mixed> variantsPerCase=1 outputPath=<path>"
disable-model-invocation: true
allowed-tools:
  - Read
  - Write
  - Edit
  - MultiEdit
  - Glob
  - Grep
---

# Generate Telecom Benchmark Final

Generate Czech benchmark datasets for the telecom prototype. The generator creates structured benchmark records for
two modes: extraction and classification.

## Required input files

Load these files from the project resources:

- `src/main/resources/domain/services.yaml`
- `src/main/resources/domain/products.yaml`
- `src/main/resources/domain/discounts.yaml`
- `src/main/resources/domain/request_types.yaml`
- `src/main/resources/dataset/generator_helpers/classification_targets.yaml`
- `src/main/resources/dataset/generator_helpers/entity_pools.yaml`
- `src/main/resources/dataset/generator_helpers/channels.yaml`
- `src/main/resources/dataset/generator_helpers/noise_profiles.yaml`
- `src/main/resources/dataset/generator_helpers/output_contract.md`

Stop if any required file is missing.

The generator must create benchmark instances directly from `request_types.yaml`, the domain catalogs, the entity pools,
the channel definitions, and the noise definitions.

Default output directory: `src/main/resources/dataset/inputs/`.

## Meaning of input files

Use the files as follows:

- `request_types.yaml` is the canonical definition of known request types. It defines the DTO class name, allowed
  fields, field meanings, component types, and field constraints. Do not generate fields that are not defined there.
- `services.yaml` defines allowed service IDs, service names, aliases, allowed contract terms, and request types where
  services may be used.
- `products.yaml` defines allowed product IDs, product names, aliases, and request types where products may be used.
- `discounts.yaml` defines allowed discount IDs, discount names, aliases, and request types where discounts may be used.
- `entity_pools.yaml` provides concrete synthetic values such as customers, phone numbers, emails, installation
  addresses, churn reasons, competitor offers, broker signatures, and unknown request snippets.
- `channels.yaml` defines how each channel should be rendered. CRM tickets are short and internal, broker emails are
  prose-like and may contain greeting/signature, and call transcripts must be a dialogue between customer and operator.
- `noise_profiles.yaml` defines available noise tags and the channels where each tag may be applied.
- `classification_targets.yaml` defines classification scenario kinds and the allowed unclassifiable reasons.
- `output_contract.md` defines the required JSON output shape.

## Global output rule

For every mode (`extraction`, `classification`), write one JSON object with this top-level envelope:

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
  "records": []
}
```

Never output a raw top-level array. Never include `generatorVersion`. The full record structure must follow
`output_contract.md`.

## Parameters

Required:

- `mode`: one of `extraction`, `classification`

Optional:

- `requestTypeIds`: comma-separated list of known request type IDs, or `all`. Default: `all`.
- `channels`: comma-separated subset of `crm_ticket`, `broker_email`, `call_transcript`. Default: all channels.
- `noiseCount`: integer from `0` to `N`, where `N` is the number of applicable noise tags for the current channel.
  Default: `0`.
- `completenessMode`: `complete`, `incomplete`, or `mixed`. Default: `mixed`.
- `variantsPerCase`: positive integer. Default: `1`.
- `outputPath`: explicit output file path. If omitted, write to the default output directory.

Mode-specific optional parameters:

- `classification`:
    - `scenarioKinds`: comma-separated subset of `singleKnownExact`, `singleKnownPartial`, `singleKnownWithUnknownTail`,
      `multiKnownMixed`, `unknownOnly`, `ambiguousInsufficient`.
    - `knownRequestCount`: integer or range such as `1` or `2-3`.

## Supported modes

### 1. `extraction`

Generate records for data extraction from a known request type.

For each record:

- choose exactly one known request type from `request_types.yaml`;
- create one DTO instance following only the fields and constraints from that request type;
- choose values from the relevant domain catalogs and entity pools;
- render the DTO values as Czech input text in the selected channel;
- if `completenessMode=incomplete`, omit selected field values from the rendered text and set the corresponding DTO
  values to `null` or an empty array as appropriate;
- if `completenessMode=mixed`, generate both complete and incomplete cases;
- set `expectedClassification.requestTypeId` to the selected request type;
- include exactly one item in `expectedExtractions`.

### 2. `classification`

Generate records for request-type classification only.

For each record:

- use `classification_targets.yaml` to choose the scenario kind;
- render Czech input text that matches the selected scenario kind;
- the text may contain one known request type, multiple known request types, one known request type with an unknown
  tail, only unknown content, or an ambiguous insufficient signal;
- keep `expectedExtractions` as an empty array;
- set `expectedClassification.requestTypeId` to exactly one value: one known `rt_*` ID or `unclassifiable`;
- if the result is `unclassifiable`, set `unclassifiableReason` using the IDs from `classification_targets.yaml`.

## Generation workflow

1. Load all required files.
2. Validate requested IDs, channels, scenario kinds, and numeric parameters.
3. Resolve generation cases:
    - extraction: request type × channel × variant;
    - classification: scenario kind × channel × variant.
4. For every known request, build a DTO instance directly from `request_types.yaml`.
5. Use `fieldConstraints` to select valid service, product, and discount IDs from the domain catalogs.
6. Use `entity_pools.yaml` for names, phones, emails, addresses, churn reasons, competitor offers, signatures, and
   unknown snippets.
7. Render `inputText` according to `channels.yaml`.
8. Apply noise by selecting up to `noiseCount` distinct tags from `noise_profiles.yaml` whose `appliesTo` contains the
   current channel. Record the selected tags in `noiseTags`.
9. Apply completeness rules only by omitting values from the rendered text and the expected DTO. Do not add fields
   outside the DTO definition.
10. Build `expectedClassification`, `expectedExtractions`, and `evidence` according to `output_contract.md`.
11. Write one JSON object containing `generatedAt`, `generationRunParams`, and `records`.

## Hard constraints

1. All generated `inputText` values must be in Czech.
2. JSON keys and field-path strings must stay in camelCase.
3. Use only IDs available in the loaded domain catalogs and allowed by the selected request type constraints.
4. In extraction mode, generate exactly one known request type per record.
5. In classification mode, keep `expectedExtractions` empty.
6. `expectedClassification.requestTypeId` must always contain exactly one value.
8. If the classification result is not safely one known request type, use `unclassifiable`.
9. If `expectedClassification.requestTypeId` is `unclassifiable`, `unclassifiableReason` must be one of the reason IDs
   from `classification_targets.yaml`.
10. `call_transcript` must always contain both `Operátor:` and `Zákazník:` turns.
11. `crm_ticket` must not contain greeting or signature.
12. `broker_email` may contain greeting and signature.
13. For incomplete cases, omit selected field values from the rendered text and represent them as `null` or empty arrays
    in the expected DTO. Do not violate the DTO schema.
14. `evidenceByFieldPath` should use literal snippets from `inputText` whenever possible.
15. `materializedFieldPaths` must list only fields actually supported by the rendered text.
16. `intentionallyOmittedFieldPaths` must list only fields intentionally absent from the rendered text.
17. For every expected extraction object, the union of `materializedFieldPaths` and `intentionallyOmittedFieldPaths`
    must cover all field paths of that expected DTO object. A field path must not appear in both lists.
18. For nested list objects, use indexed field paths such as `requestedServices[0].serviceId` and
    `portedNumbers[0].donorOperator`.
19. `evidenceByFieldPath` keys must correspond to materialized field paths.
20. `normalizedValue` in `evidenceByFieldPath` must use the same JSON value type as the DTO value whenever possible, for
    example number for `24` and boolean for `true`.
21. The output file must never be a bare array.
22. The output JSON must be valid RFC8259 JSON. Do not use placeholders such as `...`.
