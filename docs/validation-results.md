# Validation Results

This document describes the validation outputs written by the prototype and the CSV schema used by the currently
committed `src/main/resources/dataset/results/summary.csv` file.

The validation results are intended for benchmark reporting, thesis tables, and manual inspection of model errors. They
should be interpreted as results of a controlled research prototype, not as production monitoring data.

---

## Output location

Validation outputs are written to:

```text
src/main/resources/dataset/results/
```

The directory contains two types of output:

1. **Per-run JSON result files** — detailed comparison of expected and actual model output for one dataset/model/prompt
   run.
2. **`summary.csv`** — append-only aggregate CSV containing one row per processed record.

---

## Per-run JSON result files

Each benchmark run creates one JSON file. The filename identifies the input dataset, task type, prompt variant, and
model.

Current result filename examples:

```text
cls_broker_n1__classification__direct__gpt-5-nano.json
cls_crm_n5__classification__few_shot__gemma3_27b.json
ext_call_n3_complete__validation__direct__gpt-4o-mini.json
ext_crm_n5_complete__validation__few_shot__gpt-5-nano.json
```

In the committed result set, classification files use `__classification__` in the filename and extraction validation
files use `__validation__`. For analysis, use the `mode` field in the JSON records or in `summary.csv` as the canonical
task indicator.

### Top-level JSON fields

Both classification and extraction result files use the same top-level structure.

| Field           | Description                                                                                      |
|-----------------|--------------------------------------------------------------------------------------------------|
| `inputFile`     | Absolute path to the input dataset file used for the run.                                        |
| `promptVariant` | Prompt strategy used for the run, for example `DIRECT` or `FEW_SHOT`.                            |
| `modelId`       | Model identifier string, for example `gpt-5-nano`, `gpt-4o-mini`, `gemma3:12b`, or `gemma3:27b`. |
| `records`       | List of per-record validation results.                                                           |

---

## Classification record schema

Classification records compare the expected request type with the request type returned by the model.

| Field                 | Description                                                                      |
|-----------------------|----------------------------------------------------------------------------------|
| `recordId`            | Record identifier from the input dataset.                                        |
| `channel`             | Input channel, for example `broker_email`, `call_transcript`, or `crm_ticket`.   |
| `mode`                | Validation mode. For classification records this is `classification`.            |
| `noiseTags`           | List of noise tags applied to the input record.                                  |
| `promptVariant`       | Prompt strategy used for this record.                                            |
| `modelId`             | Model identifier string used for this record.                                    |
| `invocationSucceeded` | Whether the model call completed without an invocation error.                    |
| `expectedRequestType` | Gold-standard request type from the dataset.                                     |
| `actualRequestType`   | Request type returned by the model.                                              |
| `correct`             | `true` when `actualRequestType` equals `expectedRequestType`; otherwise `false`. |
| `promptTokens`        | Prompt token count reported for the invocation.                                  |
| `completionTokens`    | Completion token count reported for the invocation.                              |
| `errorMessage`        | Error detail when invocation or parsing failed; otherwise usually `null`.        |

For classification, the corresponding `summary.csv` value `matchRate` is derived from `correct`: `1.0` for a correct
classification and `0.0` for an incorrect classification.

---

## Extraction record schema

Extraction records compare the expected DTO with the DTO returned by the model. The comparison is evaluated at
field-path level.

| Field                 | Description                                                                                   |
|-----------------------|-----------------------------------------------------------------------------------------------|
| `recordId`            | Record identifier from the input dataset.                                                     |
| `requestTypeId`       | Request type associated with the extraction record.                                           |
| `channel`             | Input channel, for example `broker_email`, `call_transcript`, or `crm_ticket`.                |
| `mode`                | Validation mode. For extraction records this is `extraction`.                                 |
| `noiseTags`           | List of noise tags applied to the input record.                                               |
| `promptVariant`       | Prompt strategy used for this record.                                                         |
| `modelId`             | Model identifier string used for this record.                                                 |
| `invocationSucceeded` | Whether the model call completed without an invocation or parsing error.                      |
| `exactMatch`          | `true` when the compared DTO fields fully match the expected DTO according to the comparator. |
| `missingFieldPaths`   | Field paths intentionally absent from the input text in incomplete-data records.              |
| `totalComparedPaths`  | Number of DTO field paths included in the comparison.                                         |
| `matchedPaths`        | Number of compared field paths where actual output matched the expected value.                |
| `matchRate`           | Field-level match rate, calculated as `matchedPaths / totalComparedPaths`.                    |
| `differences`         | List of field-level differences between expected and actual DTOs.                             |
| `expectedDto`         | Gold-standard DTO from the dataset.                                                           |
| `actualDto`           | DTO returned by the model.                                                                    |
| `promptTokens`        | Prompt token count reported for the invocation.                                               |
| `completionTokens`    | Completion token count reported for the invocation.                                           |
| `errorMessage`        | Error detail when invocation, parsing, or comparison failed; otherwise usually `null`.        |

### Difference object schema

Each item in `differences` identifies one mismatch.

| Field            | Description                                                                                                                                         |
|------------------|-----------------------------------------------------------------------------------------------------------------------------------------------------|
| `path`           | Dot/index notation path to the mismatched field, for example `requestedServices[0].serviceId`.                                                      |
| `differenceType` | Type of mismatch. Current comparator constants include `missing_field`, `unexpected_field`, `type_mismatch`, `null_mismatch`, and `value_mismatch`. |
| `expected`       | Expected value at the compared path.                                                                                                                |
| `actual`         | Actual model-returned value at the compared path.                                                                                                   |

---

## `summary.csv`

`summary.csv` is an append-only aggregate file. It contains one row per processed record and is intended for quick
benchmark aggregation.

Current committed CSV header:

```csv
resultFileName,mode,channel,noiseCount,promptVariant,modelType,matchRate,promptTokens,completionTokens
```

The committed `summary.csv` has **456 physical lines**, including the header, and therefore **455 data rows**.

### Current CSV columns

| Column             | Description                                                                                                                                               |
|--------------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------|
| `resultFileName`   | Name of the corresponding per-run JSON result file.                                                                                                       |
| `mode`             | Task type: `classification` or `extraction`.                                                                                                              |
| `channel`          | Input channel. Current values are `broker_email`, `call_transcript`, and `crm_ticket`.                                                                    |
| `noiseCount`       | Number of noise tags in the record. This is the count of `noiseTags`, not necessarily the same as the `n1`, `n3`, or `n5` suffix in the dataset filename. |
| `promptVariant`    | Prompt strategy used for the run, currently `DIRECT` or `FEW_SHOT`.                                                                                       |
| `modelType`        | Model identifier string, for example `gpt-5-nano`, `gpt-4o-mini`, `gemma3:12b`, or `gemma3:27b`.                                                          |
| `matchRate`        | Classification: `1.0` for correct and `0.0` for incorrect. Extraction: field-level match rate from `0.0` to `1.0`.                                        |
| `promptTokens`     | Prompt token count reported for the invocation.                                                                                                           |
| `completionTokens` | Completion token count reported for the invocation.                                                                                                       |

### Important schema note

The committed `summary.csv` does **not** contain a `validationType` column. Use `mode` to distinguish classification and
extraction rows.

Do not document or query `validationType` unless the CSV is intentionally regenerated with a new 10-column schema and
all rows are updated consistently.

---

## Current committed benchmark snapshot

The following tables summarize the currently committed `summary.csv` file.

### Rows by task

| Mode             |    Rows |
|------------------|--------:|
| `classification` |     270 |
| `extraction`     |     185 |
| **Total**        | **455** |

### Rows by model

| Model         | Rows |
|---------------|-----:|
| `gpt-5-nano`  |  149 |
| `gpt-4o-mini` |   90 |
| `gemma3:12b`  |  108 |
| `gemma3:27b`  |  108 |

### Rows by prompt variant

| Prompt variant | Rows |
|----------------|-----:|
| `DIRECT`       |  342 |
| `FEW_SHOT`     |  113 |

### Classification results by model and prompt

For classification, `matchRate` is equivalent to accuracy because each row is either `1.0` or `0.0`.

| Model        | Prompt variant | Rows | Mean `matchRate` | Minimum | Maximum |
|--------------|----------------|-----:|-----------------:|--------:|--------:|
| `gpt-5-nano` | `DIRECT`       |   54 |            1.000 |   1.000 |   1.000 |
| `gemma3:12b` | `DIRECT`       |   54 |            0.685 |   0.000 |   1.000 |
| `gemma3:12b` | `FEW_SHOT`     |   54 |            0.667 |   0.000 |   1.000 |
| `gemma3:27b` | `DIRECT`       |   54 |            0.667 |   0.000 |   1.000 |
| `gemma3:27b` | `FEW_SHOT`     |   54 |            0.685 |   0.000 |   1.000 |

### Extraction results by model and prompt

For extraction, `matchRate` is the field-level DTO comparison score.

| Model         | Prompt variant | Rows | Mean `matchRate` | Minimum | Maximum |
|---------------|----------------|-----:|-----------------:|--------:|--------:|
| `gpt-4o-mini` | `DIRECT`       |   90 |            1.000 |   1.000 |   1.000 |
| `gpt-5-nano`  | `DIRECT`       |   90 |            0.999 |   0.933 |   1.000 |
| `gpt-5-nano`  | `FEW_SHOT`     |    5 |            1.000 |   1.000 |   1.000 |

The few-shot extraction subset currently contains only 5 rows. It should therefore be treated as a small partial run,
not as a full benchmark comparable to the 90-row direct-prompt extraction runs.

---

## Recommended aggregation rules

Use the following rules when preparing result tables for the thesis:

| Question                                        | Recommended calculation                                                                               |
|-------------------------------------------------|-------------------------------------------------------------------------------------------------------|
| Classification accuracy by model/prompt         | Average `matchRate` where `mode = classification`, grouped by `modelType` and `promptVariant`.        |
| Extraction field-level accuracy by model/prompt | Average `matchRate` where `mode = extraction`, grouped by `modelType` and `promptVariant`.            |
| Channel-level comparison                        | Average `matchRate` grouped by `mode`, `channel`, `modelType`, and `promptVariant`.                   |
| Noise-level comparison                          | Average `matchRate` grouped by `mode`, `noiseCount`, `modelType`, and `promptVariant`.                |
| Error analysis                                  | Use the per-run JSON files, especially `differences`, `errorMessage`, `expectedDto`, and `actualDto`. |

`summary.csv` is suitable for aggregate statistics. It is not sufficient for detailed extraction error analysis because
it does not include `recordId`, `requestTypeId`, `exactMatch`, `missingFieldPaths`, `differences`, `expectedDto`,
`actualDto`, or `errorMessage`.

---

## Regeneration and reproducibility notes

`summary.csv` is append-only. Re-running the same validation adds new rows and can duplicate previous results. For a
clean benchmark run, delete or archive the old `summary.csv` before executing the validation scripts again.

For thesis reporting, freeze one benchmark snapshot and report either the repository commit hash or the date of the
generated result set. This prevents later appended rows from silently changing the reported statistics.
