# Validation Results

After each benchmark run the validation service writes two outputs to `src/main/resources/dataset/results/`.

## Per-run result file

One JSON file is created per run, named after the input file, task type, prompt variant, and model:

```
{inputBase}__{task}__{variant}__{model}.json

cls_broker_n1__classification__direct__gpt-5-nano.json
ext_call_n3_complete__validation__direct__gpt-4o-mini.json
```

**Top-level fields:**

| Field           | Description                                 |
|-----------------|---------------------------------------------|
| `inputFile`     | Absolute path to the input dataset file     |
| `promptVariant` | Prompt strategy used (`DIRECT`, `FEW_SHOT`) |
| `modelId`       | Model identifier string                     |
| `records`       | Per-record comparison results (see below)   |

**Per-record fields (classification):**

| Field                 | Description                                        |
|-----------------------|----------------------------------------------------|
| `recordId`            | Record identifier from the input dataset           |
| `channel`             | Communication channel of the record                |
| `noiseTags`           | Noise tags applied to the record                   |
| `expectedRequestType` | Gold-standard request type from the dataset        |
| `actualRequestType`   | Request type returned by the model                 |
| `correct`             | Whether `actual` matches `expected`                |
| `invocationSucceeded` | Whether the model call completed without error     |
| `promptTokens`        | Prompt token count for the invocation              |
| `completionTokens`    | Completion token count for the invocation          |
| `errorMessage`        | Error detail when `invocationSucceeded` is `false` |

**Per-record fields (extraction):**

| Field                | Description                                                               |
|----------------------|---------------------------------------------------------------------------|
| `recordId`           | Record identifier from the input dataset                                  |
| `requestTypeId`      | Request type of the record                                                |
| `channel`            | Communication channel of the record                                       |
| `noiseTags`          | Noise tags applied to the record                                          |
| `exactMatch`         | Whether the model DTO exactly matches the expected DTO                    |
| `totalComparedPaths` | Number of DTO field paths evaluated                                       |
| `matchedPaths`       | Number of field paths where model output matches the gold value           |
| `matchRate`          | `matchedPaths / totalComparedPaths` (0.0 – 1.0)                           |
| `missingFieldPaths`  | Field paths intentionally absent from the input (incomplete-data records) |
| `differences`        | List of field-level discrepancies (see below)                             |
| `expectedDto`        | Gold-standard DTO from the dataset                                        |
| `actualDto`          | DTO returned by the model                                                 |
| `promptTokens`       | Prompt token count for the invocation                                     |
| `completionTokens`   | Completion token count for the invocation                                 |
| `errorMessage`       | Error detail when `invocationSucceeded` is `false`                        |

Each entry in `differences` identifies exactly where the model output diverged:

| Field            | Description                                                           |
|------------------|-----------------------------------------------------------------------|
| `path`           | Dot/index notation field path (e.g. `requestedServices[0].serviceId`) |
| `differenceType` | Nature of the mismatch (e.g. `VALUE_MISMATCH`, `MISSING`)             |
| `expected`       | Gold value at that path                                               |
| `actual`         | Model-returned value at that path                                     |

## summary.csv

Every record result from every run is appended to `src/main/resources/dataset/results/summary.csv`.
The file accumulates rows across runs and is created automatically with a header on first write.

| Column             | Description                                                   |
|--------------------|---------------------------------------------------------------|
| `validationType`   | `EXTRACTION` or `CLASSIFICATION`                              |
| `resultFileName`   | Name of the corresponding per-run result file                 |
| `mode`             | `extraction` or `classification`                              |
| `channel`          | Communication channel of the record                           |
| `noiseCount`       | Number of noise tags applied                                  |
| `promptVariant`    | Prompt strategy (`DIRECT`, `FEW_SHOT`)                        |
| `modelType`        | Model identifier string                                       |
| `matchRate`        | Field match rate (extraction) or 0/1 correct (classification) |
| `promptTokens`     | Prompt token count                                            |
| `completionTokens` | Completion token count                                        |
