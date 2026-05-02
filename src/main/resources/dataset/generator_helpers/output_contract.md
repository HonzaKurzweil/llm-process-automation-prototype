# Output contract

This document defines the strict JSON contract for generated telecom benchmark input files.

Generation behavior is defined in `.claude/skills/generate-telecom-benchmark-lean/SKILL.md`. Domain and helper YAML files provide schemas, catalog values, channel metadata, classification target metadata, and reusable synthetic entities.

This document intentionally does not contain concrete sample records. It uses type notation only. Generated values must come from the loaded YAML files, the selected generation parameters, and the rendered `inputText`.

## Contract notation

- `string`, `integer`, `boolean`, `null`, `object`, and `array<T>` denote JSON value types.
- `nullable<T>` means either `T` or `null`.
- `enum[...]` means one value from the listed set.
- `knownRequestTypeId` means a request type ID defined in `request_types.yaml`.
- `dtoClassName` means the DTO class name configured for the selected request type in `request_types.yaml`.
- `fieldPath` means a DTO path in camelCase. Nested paths use dot notation. List item paths use zero-based indexes.

All objects defined in this contract are closed objects unless stated otherwise. Closed objects must not contain keys outside the listed field set.

## Top-level envelope

`GeneratedInputFile` is a closed object with exactly these required fields:

| Field | Type | Constraint |
|---|---|---|
| `generatedAt` | `string` | Date in `YYYY-MM-DD` format. |
| `generationRunParams` | `GenerationRunParams` | Parameters shared by the file. |
| `records` | `array<Record>` | Generated benchmark records. |

The top-level value must never be a raw array.

## GenerationRunParams

`GenerationRunParams` is a closed object with exactly these required fields:

| Field | Type | Constraint |
|---|---|---|
| `mode` | `enum[classification, extraction]` | Generation mode for the file. |
| `requestTypeIds` | `string` or `array<string>` | Either `all` or a list of known request type IDs requested for generation. |
| `channels` | `array<string>` | Channel IDs selected from `channels.yaml`. |
| `noiseCount` | `integer` | Requested noise count for records in the file. |
| `completenessMode` | `nullable<enum[complete, incomplete, mixed]>` | Required as non-null in extraction mode; must be `null` in classification mode. |
| `scenarioKinds` | `nullable<array<string>>` | Classification scenario kinds selected from `classification_targets.yaml`; must be `null` in extraction mode. |
| `knownRequestCount` | `nullable<integer|string>` | Requested number or range of known request types for classification generation; must be `null` in extraction mode when not used. |
| `variantsPerCase` | `integer` | Requested number of variants per generated case. |
| `outputPath` | `string` | Target output path. |

## Record

`Record` is a closed object with exactly these required fields:

| Field | Type | Constraint |
|---|---|---|
| `recordId` | `string` | Stable ID unique inside the file. |
| `inputText` | `string` | Rendered Czech input shown to the evaluated model. |
| `metadata` | `RecordMetadata` | Record-specific generation metadata. |
| `expectedClassification` | `ClassificationExpectation` | Expected classification output. |
| `expectedExtractions` | `array<ExtractionExpectation>` | Empty in classification mode; one item in extraction mode. |
| `evidence` | `Evidence` | Audit metadata grounding expected outputs in the input text. |

Do not include `templateId`, `templateIds`, `scenarioTemplateId`, `generationNote`, or `generatorVersion`.

## RecordMetadata

`RecordMetadata` is a closed object with exactly these required fields:

| Field | Type | Constraint |
|---|---|---|
| `mode` | `enum[classification, extraction]` | Must match `generationRunParams.mode`. |
| `channel` | `string` | Selected channel ID from `channels.yaml`. |
| `requestTypeIds` | `array<string>` | Known request type IDs intentionally present in the input text. Use an empty array when no known request type is present. |
| `requestTypeId` | `nullable<string>` | Primary known request type ID when one primary known request is present; otherwise `null`. |
| `scenarioKind` | `nullable<string>` | Classification scenario kind from `classification_targets.yaml`; must be `null` in extraction mode. |
| `completenessMode` | `nullable<enum[complete, incomplete, mixed]>` | Required as non-null in extraction mode; must be `null` in classification mode. |
| `requestedNoiseCount` | `integer` | Requested noise count for this record. |
| `noiseTags` | `array<string>` | Actually applied noise tags from `noise_profiles.yaml`. |
| `variantIndex` | `integer` | Zero-based variant index inside the generated case. |

Mode constraints:

- In extraction mode, `requestTypeIds` must contain exactly one known request type ID and `requestTypeId` must contain the same value.
- In classification mode, `requestTypeIds` must list the known request types intentionally represented in the input text. For unknown-only records it must be an empty array.

## ClassificationExpectation

`ClassificationExpectation` is a closed object with exactly these required fields:

| Field | Type | Constraint |
|---|---|---|
| `requestTypeId` | `string` | Exactly one known request type ID or `unclassifiable`. |
| `unclassifiableReason` | `nullable<string>` | Must be `null` when `requestTypeId` is a known request type. Must contain a reason ID from `classification_targets.yaml` when `requestTypeId` is `unclassifiable`. |

## ExtractionExpectation

`ExtractionExpectation` is a closed object with exactly these required fields:

| Field | Type | Constraint |
|---|---|---|
| `requestTypeId` | `knownRequestTypeId` | Known request type extracted from the record. |
| `dtoClassName` | `dtoClassName` | DTO class name from `request_types.yaml`. |
| `dto` | `object` | Expected DTO object for the selected request type. |
| `missingFieldPaths` | `array<string>` | DTO field paths intentionally absent from `inputText` in incomplete extraction cases. |

Mode constraints:

- In classification mode, `expectedExtractions` must be an empty array.
- In extraction mode, `expectedExtractions` must contain exactly one item.

DTO constraints:

- `dto` must use only fields defined for the selected request type in `request_types.yaml` and nested fields defined in `component_types.yaml`.
- `dto` must not contain additional fields outside the selected request type schema.
- Every top-level DTO field defined for the selected request type must be present in `dto`.
- Missing scalar, boolean, numeric, list, or nested-object values must be represented according to the completeness rules in `SKILL.md`.

## Evidence

`Evidence` is a closed object with exactly these required fields:

| Field | Type | Constraint |
|---|---|---|
| `requestSegments` | `array<RequestSegment>` | Request-like or distractor text segments present in `inputText`. |
| `materializedFieldPaths` | `array<string>` | DTO field paths supported by rendered text. Must be empty in classification-only records. |
| `intentionallyOmittedFieldPaths` | `array<string>` | DTO field paths intentionally absent from rendered text. Empty in complete extraction records and classification records. |
| `evidenceByFieldPath` | `object` | Map from materialized DTO field path to `FieldEvidence`. Empty when there is no extraction expectation. |
| `distractorSnippets` | `array<string>` | Input snippets intentionally irrelevant to the expected classification or DTO. |

The `evidenceByFieldPath` object is open only with respect to its dynamic keys. Every key must be a field path listed in `materializedFieldPaths`, and every value must be a `FieldEvidence` object.

## RequestSegment

`RequestSegment` is a closed object with exactly these required fields:

| Field | Type | Constraint |
|---|---|---|
| `requestTypeId` | `nullable<string>` | Known request type ID for known segments; `null` for unknown, ambiguous, or pure distractor segments. |
| `segmentRole` | `enum[primaryKnown, secondaryKnown, unknownTail, ambiguousSignal, distractor]` | Role of the text segment in the generated record. |
| `evidenceSnippets` | `array<string>` | Literal snippets from `inputText` supporting the segment label. |

Segment role semantics:

| Value | Meaning |
|---|---|
| `primaryKnown` | Main supported request type in the record. |
| `secondaryKnown` | Additional supported request type intentionally present in the same input. |
| `unknownTail` | Additional request-like content outside supported request types. |
| `ambiguousSignal` | Vague or insufficient request-like content. |
| `distractor` | Irrelevant content that must not affect expected outputs. |

## FieldEvidence

`FieldEvidence` is a closed object with exactly these required fields:

| Field | Type | Constraint |
|---|---|---|
| `normalizedValue` | any JSON value | Must equal the DTO value at the corresponding field path whenever the DTO path exists. |
| `evidenceSnippets` | `array<string>` | Literal snippets from `inputText` when a literal text signal exists. |

## Evidence coverage rules

For every extraction record:

1. Compute the expected DTO field paths from `request_types.yaml`, `component_types.yaml`, and the generated DTO object.
2. Include every DTO field path supported by rendered text in `materializedFieldPaths`.
3. Include every DTO field path intentionally absent from rendered text in `intentionallyOmittedFieldPaths`.
4. A field path must not appear in both `materializedFieldPaths` and `intentionallyOmittedFieldPaths`.
5. The union of `materializedFieldPaths` and `intentionallyOmittedFieldPaths` must cover all expected DTO field paths.
6. `expectedExtractions[*].missingFieldPaths` must equal `evidence.intentionallyOmittedFieldPaths`.
7. `evidenceByFieldPath` keys must equal `materializedFieldPaths`.
8. `FieldEvidence.evidenceSnippets` must be literal substrings from `inputText` whenever a literal text signal exists.
9. `FieldEvidence.normalizedValue` must use the same JSON type as the DTO value at the corresponding field path.
10. If a `null`, `false`, `0`, or empty-array value is explicitly supported by an absence signal in `inputText`, treat the field path as materialized.
11. If a `null` or empty-array value exists only because the field was omitted in an incomplete case, treat the field path as intentionally omitted.

For classification-only records:

- `expectedExtractions` must be an empty array.
- `materializedFieldPaths` must be an empty array.
- `intentionallyOmittedFieldPaths` must be an empty array.
- `evidenceByFieldPath` must be an empty object.
- `requestSegments` must document the classification signal.

For nested objects and lists:

- Use dot paths for nested objects.
- Use indexed paths for materialized list items.
- Use the base list path for an intentionally omitted whole list or an explicitly materialized empty list.
- Use leaf paths for partially materialized nested objects.

## Mandatory output rules

1. The generated file must be valid RFC8259 JSON.
2. The top-level value must be a JSON object.
3. JSON keys and field-path strings must stay in camelCase.
4. Do not use placeholders.
5. Do not include additional keys outside this contract except dynamic DTO fields defined by `request_types.yaml` and dynamic `evidenceByFieldPath` keys.
6. Do not include `generatorVersion`, `templateId`, `templateIds`, `scenarioTemplateId`, or `generationNote`.
7. The expected DTO must not contain fields absent from `request_types.yaml` or `component_types.yaml`.
8. The evidence block must not introduce expected values that are unsupported by rendered input text.
