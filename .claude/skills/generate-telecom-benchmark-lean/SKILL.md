---
name: generate-telecom-benchmark-final
description: Generate Czech telecom benchmark datasets from request type schemas, domain catalogs, entity pools, channel profiles, and noise tags.
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

Generate Czech benchmark datasets for two modes: extraction and classification.

General generation rules are defined in this file. Treat YAML files as structured data sources. YAML files may contain domain values, enum values, descriptive metadata, channel style characteristics, required speaker metadata, and noise-tag identifiers. Do not add prompt instructions, migration notes, or experiment explanations into domain YAML files or generator helper YAML files.

## Required input files

Load these files from the project resources:

- `src/main/resources/domain/services.yaml`
- `src/main/resources/domain/products.yaml`
- `src/main/resources/domain/discounts.yaml`
- `src/main/resources/domain/operators.yaml`
- `src/main/resources/domain/enums.yaml`
- `src/main/resources/domain/component_types.yaml`
- `src/main/resources/domain/request_types.yaml`
- `src/main/resources/dataset/generator_helpers/classification_targets.yaml`
- `src/main/resources/dataset/generator_helpers/entity_pools.yaml`
- `src/main/resources/dataset/generator_helpers/channels.yaml`
- `src/main/resources/dataset/generator_helpers/noise_profiles.yaml`

Stop if any required file is missing.

Default output directory: `src/main/resources/dataset/inputs/`.

## Meaning of input files

- `request_types.yaml` defines known request types and their target DTO field shapes.
- `component_types.yaml` defines reusable nested DTO components.
- `services.yaml`, `products.yaml`, `discounts.yaml`, `operators.yaml`, and `enums.yaml` define domain values that may be used in generated inputs and expected DTOs.
- `entity_pools.yaml` provides concrete synthetic customers, phone numbers, emails, broker signatures, retention-context phrases, out-of-scope snippets, and smalltalk snippets.
- `channels.yaml` defines supported input channels and their style metadata.
- `noise_profiles.yaml` lists supported noise tags and their applicable channels.
- `classification_targets.yaml` lists classification scenario kinds, runtime alignment with `RequestType`, and unclassifiable reason IDs.

## Parameters

Required:

- `mode`: one of `extraction`, `classification`

Optional:

- `requestTypeIds`: comma-separated list of known request type IDs, or `all`. Default: `all`.
- `channels`: comma-separated subset of `crm_ticket`, `broker_email`, `call_transcript`. Default: all channels.
- `noiseCount`: integer from `0` to `N`, where `N` is the number of noise tags applicable to the selected channel. Default: `0`.
- `completenessMode`: `complete`, `incomplete`, or `mixed`. Default: `mixed`.
- `variantsPerCase`: positive integer. Default: `1`.
- `outputPath`: explicit output file path. If omitted, write to the default output directory.

Mode-specific optional parameters:

- `classification.scenarioKinds`: comma-separated subset of `singleKnownExact`, `singleKnownPartial`, `singleKnownWithUnknownTail`, `multiKnownMixed`, `unknownOnly`, `ambiguousInsufficient`.
- `classification.knownRequestCount`: integer or range such as `1` or `2-3`.

## Output envelope

For every mode, write one JSON object with this top-level envelope:

```json
{
  "generatedAt": "2026-05-01",
  "generationRunParams": {
    "mode": "extraction",
    "requestTypeIds": "all",
    "channels": ["crm_ticket"],
    "noiseCount": 1,
    "completenessMode": "complete",
    "variantsPerCase": 3,
    "outputPath": "src/main/resources/dataset/inputs/example.json"
  },
  "records": []
}
```

Never output a raw top-level array. Never include `generatorVersion`.

## Record shape

Each record must contain:

```json
{
  "recordId": "string",
  "inputText": "string",
  "metadata": {
    "mode": "extraction",
    "channel": "crm_ticket",
    "scenarioKind": null,
    "noiseTags": [],
    "completenessMode": "complete",
    "variantIndex": 0
  },
  "expectedClassification": {
    "requestTypeId": "rt_new_mobile_order",
    "unclassifiableReason": null
  },
  "expectedExtractions": [],
  "evidence": {
    "materializedFieldPaths": [],
    "intentionallyOmittedFieldPaths": [],
    "evidenceByFieldPath": {}
  }
}
```

In classification mode, `expectedExtractions` must be an empty array. In extraction mode, `expectedExtractions` must contain exactly one object:

```json
{
  "requestTypeId": "rt_new_mobile_order",
  "dtoClassName": "SingleMobileOrderRequestDto",
  "dto": {}
}
```

## Extraction generation

For each extraction record:

1. Choose exactly one known request type.
2. Build one DTO instance using only fields from `request_types.yaml` and nested fields from `component_types.yaml`.
3. Choose service, product, discount, operator, address, and enum values from the domain YAML files.
4. Render the DTO values as Czech input text according to the selected channel metadata from `channels.yaml`.
5. For `completenessMode=complete`, render all process-relevant values.
6. For `completenessMode=incomplete`, omit selected values from the rendered text and set the corresponding DTO fields to `null` or empty arrays.
7. For `completenessMode=mixed`, generate both complete and incomplete cases.
8. Set `expectedClassification.requestTypeId` to the selected request type.
9. Include exactly one item in `expectedExtractions`.

The extraction DTO should contain process-usable fields: catalog IDs, enum values, booleans, numeric quantities, normalized phones, normalized emails, and normalized address IDs. Long free-text retention context such as a churn reason or competitor offer may appear in `inputText`, but must not be added to the DTO unless such a field exists in `request_types.yaml`.

## Catalog-backed fields

Use these field-to-catalog mappings when building extraction DTOs:

- Service ID fields use `services.yaml`: `mobileTariffId`, `familyTariffId`, `mobileLines[].planServiceId`, `internetServiceId`, `tvServiceId`, `currentServiceIds`, and `targetServiceId`.
- Product-backed fields use `products.yaml`: `routerProductId`, `meshNodeQuantity`, `expressInstallationRequested`, `setTopBoxQuantity`, and `sportsPackRequested`.
- Discount ID fields use `discounts.yaml`: `requestedDiscountIds`.
- Address fields (`installationAddress`) are not catalog-backed. Generate a random plausible Czech address as a structured object `{psc, city, street, houseNumber}` with a realistic Czech PSČ (e.g. `190 00`), city name, street name, and house number.
- Operator enum fields use `operators.yaml`: `donorOperator` and `mobileLines[].donorOperator`.
- Other enum-backed fields use `enums.yaml`.

When multiple catalog entries could fit a field, choose the one whose ID, name, and aliases best match the request type and the generated wording. Do not require business-rule keys such as allowed request types in the catalog YAML files.

## Classification generation

For each classification record:

1. Use `classification_targets.yaml` to choose the scenario kind.
2. Render Czech input text that matches the selected scenario kind and its descriptive properties.
3. Keep `expectedExtractions` as an empty array.
4. Set `expectedClassification.requestTypeId` to exactly one known request type ID or `unclassifiable`.
5. If the result is `unclassifiable`, set `unclassifiableReason` using the reason IDs from `classification_targets.yaml`.

When `mode=classification`, `requestTypeIds=all`, and no explicit `scenarioKinds` are provided, keep the classification dataset balanced. For each output file, generate the same number of positive examples for every known request type and the same number of `unclassifiable` examples. Rotate unclassifiable scenario kinds across variants so that all unclassifiable reasons are represented across the full benchmark matrix.

## Channel rendering

Render each input according to the selected channel entry in `channels.yaml`. Use `styleCharacteristics` as the channel profile. If a channel entry contains `requiredSpeakers`, all listed speakers must appear in the generated text.

## Noise rendering

Apply up to `noiseCount` distinct tags from `noise_profiles.yaml` whose `appliesTo` contains the selected channel. Record selected tags in `metadata.noiseTags`.

Noise tag behavior:

- `shorthandInternal`: use CRM-style abbreviations and compact fragments.
- `greetingSignatureNoise`: add greeting, closing phrase, or broker signature to broker emails.
- `hesitationDisfluency`: add natural speech hesitation to call transcripts.
- `smalltalkNoise`: add short unrelated politeness turns.
- `irrelevantInfo`: add side information that does not change expected output.
- `ambiguousWording`: make wording less direct while preserving the intended gold label where possible.
- `unknownTailSnippet`: add an out-of-scope request snippet. Use this only when the expected classification should become `unclassifiable`, or when the generation case explicitly requires unknown-tail content.
- `typosAndMisspellings`: add minor Czech typos that do not change meaning.
- `entityFormattingVariation`: change only the surface form of names, phones, emails, addresses, and operator names. Keep expected DTO values canonical.

For `entityFormattingVariation`, permitted surface variation includes phone numbers without the country prefix, phone numbers with hyphens, parentheses or different spacing, e-mails with changed letter case or surrounding spaces, names with salutation or reversed order, address field surface variations (different PSČ formatting, street abbreviations), and operator aliases from `operators.yaml`.

Do not use initials or incomplete names if the full expected name would not be recoverable from the input.

## Normalization rules for expected DTOs

- Phone numbers must use the canonical synthetic values from `entity_pools.yaml`. Preserve the full synthetic number including the `9999` prefix and all local digits.
- E-mails must be lowercase and without surrounding whitespace.
- Customer names must use `Jméno Příjmení`; remove salutation and normalize reversed order only when both parts are present.
- Catalog-backed fields must use IDs from the corresponding domain YAML file.
- `installationAddress` must be a structured object with `psc`, `city`, `street`, and `houseNumber` extracted from the input text.
- Enum-backed fields must use values from `enums.yaml` or operator IDs from `operators.yaml`.
- Counts must be numeric JSON values, not strings.
- Booleans must be JSON booleans, not strings.

## Evidence rules

- `materializedFieldPaths` must list only fields actually supported by rendered text.
- `intentionallyOmittedFieldPaths` must list only fields intentionally absent from rendered text.
- For every expected extraction object, the union of `materializedFieldPaths` and `intentionallyOmittedFieldPaths` must cover all DTO field paths, including nested paths.
- A field path must not appear in both lists.
- For nested list objects, use indexed field paths such as `mobileLines[0].lineRole`.
- `evidenceByFieldPath` keys must correspond to materialized field paths.
- Evidence snippets should be literal substrings from `inputText` whenever possible.
- `normalizedValue` must use the same JSON value type as the DTO value whenever possible.

## Hard constraints

1. All generated `inputText` values must be in Czech.
2. JSON keys and field-path strings must stay in camelCase.
3. Use only request types from `request_types.yaml`.
4. Use only catalog IDs and enum values from the loaded domain YAML files.
5. Do not generate DTO fields that are not defined in `request_types.yaml` or `component_types.yaml`.
6. In extraction mode, generate exactly one known request type per record.
7. In classification mode, keep `expectedExtractions` empty.
8. `expectedClassification.requestTypeId` must always contain exactly one value.
9. If `expectedClassification.requestTypeId` is `unclassifiable`, `unclassifiableReason` must be one of the reason IDs from `classification_targets.yaml`.
10. `call_transcript` must always contain both `Operátor:` and `Zákazník:` turns.
11. `crm_ticket` must not contain greeting or signature.
12. `broker_email` may contain greeting and signature.
13. For incomplete extraction cases, omitted values must be absent from `inputText` and represented as `null` or empty arrays in the expected DTO.
14. The output file must never be a bare array.
15. The output JSON must be valid RFC8259 JSON. Do not use placeholders such as `...`.
