# Example invocations

## Extraction benchmark for one request type

```text
/generate-telecom-inputs mode=extraction request_type_ids=rt_internet_tv_bundle_order channels=crm_ticket,broker_email,call_transcript noise_mode=light variants_per_base=1 output_path=src/main/resources/dataset/inputs/extraction_bundle.json
```

## Classification benchmark for unknown and ambiguous cases only

```text
/generate-telecom-inputs mode=classification classifier_outcome_types=unknown_or_out_of_scope,ambiguous_or_insufficient_signal channels=crm_ticket,call_transcript noise_mode=light output_path=src/main/resources/dataset/inputs/classification_unknown_ambiguous.json
```

## Mixed dataset with both extraction and classification

```text
/generate-telecom-inputs mode=both request_type_ids=all channels=crm_ticket,broker_email noise_mode=mixed variants_per_base=2 output_path=src/main/resources/dataset/inputs/all_inputs_v5.json
```

## Only explicit classification scenarios

```text
/generate-telecom-inputs mode=classification classification_scenario_ids=cls_multi_known_01,cls_known_with_unknown_tail_01 channels=broker_email noise_mode=light output_path=src/main/resources/dataset/inputs/classification_complex.json
```

## Dry run

```text
/generate-telecom-inputs mode=both request_type_ids=all dry_run=true
```
