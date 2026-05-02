---
name: generate-telecom-benchmark-final
description: Generate Czech telecom benchmark datasets from request type schemas, domain catalogs, entity pools, channel profiles, noise tags, and the output contract.
when_to_use: Use this skill when the user asks to generate benchmark datasets for extraction or classification evaluation in the telecom prototype.
argument-hint: "mode= requestTypeIds=all channels=crm_ticket,broker_email,call_transcript noiseCount=<0..N> completenessMode=complete|incomplete|mixed variantsPerCase=1 outputPath="
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

Generate Czech benchmark datasets for two modes: `classification` and `extraction`.

Treat YAML files as structured data sources. Domain YAML files and generator helper YAML files may contain domain values, enum values, descriptive metadata, channel style characteristics, required speaker metadata, noise-tag identifiers, and entity pools. Do not add migration notes, experiment explanations, prompt instructions, or one-off human comments into those YAML files.

The JSON shape is defined by `output_contract.md`. Follow it exactly. Treat every field listed there as required unless the contract explicitly allows `null`.

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
- `src/main/resources/dataset/generator_helpers/output_contract.md`

Stop if any required file is missing.

Default output directory: `src/main/resources/dataset/inputs/`.

## Meaning of input files

- `request_types.yaml` defines known request types and their target DTO field shapes.
- `component_types.yaml` defines reusable nested DTO components.
- `services.yaml`, `products.yaml`, `discounts.yaml`, `operators.yaml`, and `enums.yaml` define domain and catalog values that may be rendered in the input text and normalized into expected DTO values.
- `entity_pools.yaml` provides concrete synthetic customers, phones, emails, ported numbers, broker signatures, address components, retention-context snippets, out-of-scope snippets, and smalltalk snippets.
- `channels.yaml` defines supported input channels and their style metadata.
- `noise_profiles.yaml` lists supported noise tags and their applicable channels.
- `classification_targets.yaml` lists classification scenario kinds, runtime alignment with `RequestType`, and unclassifiable reason IDs.
- `output_contract.md` defines the required JSON envelope, record metadata, expected outputs, and evidence block.

## Parameters

Required:

- `mode`: `classification` or `extraction`.

Optional:

- `requestTypeIds`: comma-separated list of known request type IDs, or `all`. Default: `all`.
- `channels`: comma-separated subset of `crm_ticket`, `broker_email`, `call_transcript`. Default: all channels.
- `noiseCount`: integer from `0` to `N`, where `N` is the number of applicable noise tags for the selected channel and case. Default: `0`.
- `completenessMode`: `complete`, `incomplete`, or `mixed`. Default: `mixed`. This parameter is relevant for extraction mode.
- `variantsPerCase`: positive integer. Default: `1`.
- `outputPath`: explicit output file path. If omitted, write to the default output directory.

Mode-specific optional parameters:

- `classification.scenarioKinds`: comma-separated subset of `singleKnownExact`, `singleKnownPartial`, `singleKnownWithUnknownTail`, `multiKnownMixed`, `unknownOnly`, `ambiguousInsufficient`.
- `classification.knownRequestCount`: integer or range such as `1` or `2-3`.

## Output contract

Write one valid JSON object per generated file. Use the envelope, record shape, expected output shape, evidence shape, and field names defined in `output_contract.md`. Do not infer optional or recommended fields; the contract is strict.

Never output a raw top-level array. Never include `generatorVersion`, `templateId`, `templateIds`, or `scenarioTemplateId`.

## General grounding rule

Every non-null expected extraction value must be grounded in the rendered `inputText` through one of these mechanisms:

1. A literal text signal, such as a name, phone, email, number, address part, or quantity.
2. A known mapping from a rendered service, product, discount, operator, enum, or alias to its canonical ID or enum value.
3. An explicit absence signal, such as `bez portace`, `bez sportovního balíčku`, or `bez slevy`, when the expected value is `false`, `0`, `[]`, or `null` because the text explicitly states that the item is absent.

Do not put values into `expectedExtractions[*].dto` only because they are convenient, default, or plausible. If a value is not textually supported and not intentionally omitted, regenerate the text or set the field to `null` and record the field as omitted.

## Extraction generation

For each extraction record:

1. Choose exactly one known request type from `request_types.yaml` and the requested `requestTypeIds` parameter.
2. Build one DTO instance using only fields from `request_types.yaml` and nested fields from `component_types.yaml`.
3. Render the DTO values as Czech input text according to the selected channel metadata from `channels.yaml`.
4. Set `expectedClassification.requestTypeId` to the selected request type.
5. Set `expectedClassification.unclassifiableReason` to `null`.
6. Include exactly one item in `expectedExtractions`.
7. Use the `dtoClassName` from `request_types.yaml`.
8. Fill `metadata.requestTypeIds` with a one-item list containing the selected request type ID.
9. Fill the `evidence` block according to `output_contract.md`.

The extraction DTO must contain process-usable values: catalog IDs, enum values, booleans, numeric quantities, normalized phones, normalized emails, and structured address components. Long free-text retention context may appear in `inputText`, but must not be added to the DTO unless such a field exists in `request_types.yaml`.

## Catalog-backed and enum-backed fields

Use these mappings when building extraction DTOs:

- Service ID fields use `services.yaml`: `mobileTariffId`, `familyTariffId`, `mobileLines[].planServiceId`, `internetServiceId`, `tvServiceId`, `currentServiceIds`, and `targetServiceId`.
- Product-related fields use `products.yaml` aliases as rendered text signals: `routerProductId`, `meshNodeQuantity`, `expressInstallationRequested`, `setTopBoxQuantity`, and `sportsPackRequested`.
- Discount ID fields use `discounts.yaml`: `requestedDiscountIds`.
- Operator fields use `operators.yaml`: `donorOperator` and `mobileLines[].donorOperator`.
- Enum fields use `enums.yaml`: `customerStatus`, `lineRole`, and classification request type values.
- `installationAddress` is not catalog-backed. Generate it as a structured object with `psc`, `city`, `street`, and `houseNumber`.

When multiple catalog entries could fit a field, use the one whose ID, name, and aliases best match the request type and the rendered wording.

Do not require business-rule keys such as allowed request types, allowed services, allowed products, allowed discounts, or allowed contract terms inside catalog YAML files.

## Address generation

For request types with `installationAddress`, generate a plausible Czech structured address with exactly these fields: `psc`, `city`, `street`, and `houseNumber`.

The address may be assembled from `entity_pools.yaml` address parts or generated as another plausible Czech address. The expected DTO must use normalized components:

- `psc`: Czech postal code formatted as `NNN NN`.
- `city`: city name with normal Czech capitalization and diacritics.
- `street`: street name without unnecessary abbreviations.
- `houseNumber`: house number or house number/orientation number as rendered.

If the address is partially missing in an incomplete case, keep the object and set missing leaf fields to `null`. If the whole address is missing, set `installationAddress` to `null`.

## Completeness modes

### `complete`

Generate enough text signal to support all DTO fields.

For dependent or absence-based fields:

- If `portingRequested` is `false`, explicitly render an absence signal such as `bez přenosu čísla`; dependent fields such as `portedNumber` and `donorOperator` may be `null` and are not considered missing.
- If a requested discount list is empty, explicitly render an absence signal such as `bez další slevy` and use `[]`.
- If `meshNodeQuantity` is `0`, explicitly render an absence signal such as `bez mesh jednotek`.
- If `sportsPackRequested` or `expressInstallationRequested` is `false`, explicitly render an absence signal.

`missingFieldPaths` and `intentionallyOmittedFieldPaths` must be empty in complete records.

### `incomplete`

Generate a realistic request where selected fields are absent from `inputText`.

For omitted fields:

- Scalar fields: set the DTO field to `null`.
- Boolean fields: set the DTO field to `null` unless the text explicitly supports `true` or `false`.
- Numeric fields: set the DTO field to `null` unless the text explicitly supports a number or explicit zero.
- List fields: set the DTO field to `null` when the whole list is unknown; use `[]` only when the text explicitly supports an empty list.
- Nested object fields: set the parent object to `null` if the whole object is missing; otherwise set only missing leaf fields to `null`.

List omitted field paths in both `expectedExtractions[*].missingFieldPaths` and `evidence.intentionallyOmittedFieldPaths`.

### `mixed`

Generate both complete and incomplete records while preserving the same output contract.

## Classification generation

For each classification record:

1. Use `classification_targets.yaml` to choose the scenario kind.
2. Render Czech input text that matches the selected scenario kind and its descriptive properties.
3. Keep `expectedExtractions` as an empty array.
4. Set `expectedClassification.requestTypeId` to exactly one known request type ID or `unclassifiable`.
5. If the result is `unclassifiable`, set `unclassifiableReason` using the reason IDs from `classification_targets.yaml`.
6. Fill `metadata.requestTypeIds` with the known request type IDs intentionally represented in the input. For `unknownOnly`, use an empty list.
7. Use `evidence.requestSegments` to document the classification signal.

When `mode=classification`, `requestTypeIds=all`, and no explicit `scenarioKinds` are provided, keep the classification dataset balanced. For each output file, generate the same number of positive examples for every known request type and the same number of `unclassifiable` examples. Rotate unclassifiable scenario kinds across variants so that all unclassifiable reasons are represented across the full benchmark matrix.

## Classification scenario handling

Use the descriptive properties in `classification_targets.yaml`:

- `singleKnownExact`: one clear known request type; expected classification is that request type.
- `singleKnownPartial`: one known request type with missing details; expected classification is still that request type.
- `singleKnownWithUnknownTail`: one known request type plus an out-of-scope request; expected classification is `unclassifiable` with `knownRequestTypeWithUnknownTail`.
- `multiKnownMixed`: two or more known request types in one input; expected classification is `unclassifiable` with `multipleKnownRequestTypesPresent`.
- `unknownOnly`: only out-of-scope content; expected classification is `unclassifiable` with `unknownOrOutOfScope`.
- `ambiguousInsufficient`: too vague or underspecified for safe classification; expected classification is `unclassifiable` with `ambiguousOrInsufficientSignal`.

## Channel rendering

Render each input according to the selected channel entry in `channels.yaml`.

- `crm_ticket`: compact internal note, no greeting, no signature.
- `broker_email`: coherent email-style text, may include greeting, closing, and broker signature.
- `call_transcript`: dialogue-like transcript. If `requiredSpeakers` are present in `channels.yaml`, every listed speaker must appear in the generated text.

Use `styleCharacteristics` as the channel profile. Preserve the same expected DTO and expected classification semantics across channel variants unless the selected scenario kind or completeness mode requires otherwise.

## Noise rendering

Apply up to `noiseCount` distinct tags from `noise_profiles.yaml` whose `appliesTo` contains the selected channel. Record only actually applied tags in `metadata.noiseTags`.

Noise tag behavior:

- `shorthandInternal`: use CRM-style abbreviations and compact fragments.
- `greetingSignatureNoise`: add greeting, closing phrase, or broker signature to broker emails.
- `hesitationDisfluency`: add natural speech hesitation to call transcripts.
- `smalltalkNoise`: add short unrelated politeness turns.
- `irrelevantInfo`: add side information that does not change expected output.
- `ambiguousWording`: make wording less direct while preserving the intended gold label where possible.
- `unknownTailSnippet`: add an out-of-scope request snippet.
- `typosAndMisspellings`: add minor Czech typos that do not change meaning.
- `entityFormattingVariation`: change only the surface form of names, phones, emails, addresses, and operator names. Keep expected DTO values canonical.

Use `unknownTailSnippet` only when the expected classification should become `unclassifiable`, or when the generation case explicitly requires unknown-tail content. Do not apply `unknownTailSnippet` to extraction records that should remain a single known request type.

For `entityFormattingVariation`, permitted surface variation includes:

- Phone numbers without the country prefix, with hyphens, parentheses, or different spacing.
- E-mails with changed letter case or surrounding whitespace.
- Names with salutation or reversed order.
- Address formatting differences such as compact postal code, street abbreviation, or reordered address parts.
- Operator aliases from `operators.yaml`.

Do not use initials or incomplete names if the full expected name would not be recoverable from the input.

## Normalization rules for expected DTOs

- Phone numbers must use canonical synthetic values from `entity_pools.yaml`. Preserve the full synthetic number including the `9999` prefix and all local digits.
- Ported numbers must use canonical synthetic values from `entity_pools.yaml`.
- E-mails must be lowercase and without surrounding whitespace.
- Customer names must use `Jméno Příjmení`; remove salutation and normalize reversed order only when both parts are present.
- Catalog-backed fields must use IDs from the corresponding domain YAML file.
- Operator-backed fields must use operator IDs from `operators.yaml`.
- Enum-backed fields must use values from `enums.yaml`.
- `installationAddress` must be a structured object with normalized `psc`, `city`, `street`, and `houseNumber` values.
- `contractTermMonths` must use `0` for no fixed commitment and `24` for a 24-month commitment.
- Counts must be numeric JSON values, not strings.
- Booleans must be JSON booleans, not strings.

## Evidence rules

Follow `output_contract.md` exactly.

Additional constraints:

- `materializedFieldPaths` must list only fields actually supported by rendered text.
- `intentionallyOmittedFieldPaths` must list only fields intentionally absent from rendered text.
- For every expected extraction object, the union of `materializedFieldPaths` and `intentionallyOmittedFieldPaths` must cover all DTO field paths, including nested paths.
- A field path must not appear in both lists.
- For nested objects, use field paths such as `installationAddress.psc`.
- For nested list objects, use indexed field paths such as `mobileLines[0].lineRole`.
- For empty but explicitly materialized lists, use the list field path itself, such as `requestedDiscountIds`.
- `evidenceByFieldPath` keys must correspond to materialized field paths.
- Evidence snippets must be literal substrings from `inputText` whenever a literal text signal exists.
- `normalizedValue` must use the same JSON value type as the DTO value whenever the DTO path exists.

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
13. For incomplete extraction cases, omitted values must be absent from `inputText` and represented as `null` or an intentionally omitted list/object according to this skill and `output_contract.md`.
14. The output file must never be a bare array.
15. The output JSON must be valid RFC8259 JSON.
16. Do not use placeholders such as `...`.
