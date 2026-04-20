# Example invocations

## Extraction benchmark for one request type

```text
/generate-telecom-inputs mode=extraction requestTypeIds=rt_internet_tv_bundle_order channels=crm_ticket,broker_email,call_transcript noiseMode=light variantsPerBase=1 outputPath=src/main/resources/dataset/inputs/extraction_bundle.json
```

## Classification benchmark for unknown and ambiguous cases only

```text
/generate-telecom-inputs mode=classification classifierOutcomeTypes=unknown_or_out_of_scope,ambiguous_or_insufficient_signal channels=crm_ticket,call_transcript noiseMode=light outputPath=src/main/resources/dataset/inputs/classification_unknown_ambiguous.json
```

## Mixed dataset with both extraction and classification

```text
/generate-telecom-inputs mode=both requestTypeIds=all channels=crm_ticket,broker_email noiseMode=mixed variantsPerBase=2 outputPath=src/main/resources/dataset/inputs/all_inputs_v6.json
```

## Only explicit classification scenarios

```text
/generate-telecom-inputs mode=classification classificationScenarioIds=cls_multi_known_01,cls_known_with_unknown_tail_01 channels=broker_email noiseMode=light outputPath=src/main/resources/dataset/inputs/classification_complex.json
```

## Dry run

```text
/generate-telecom-inputs mode=both requestTypeIds=all dryRun=true
```
