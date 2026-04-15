# Example invocations

## Generate one request type, all channels, light noise

```text
/generate-telecom-inputs request_type_id=rt_internet_tv_bundle_order noise_mode=light
```

## Generate only CRM tickets for all references of one request type

```text
/generate-telecom-inputs request_type_id=rt_new_mobile_order channels=crm_ticket noise_mode=none
```

## Generate explicit references only

```text
/generate-telecom-inputs reference_ids=ref_new_mobile_order_01,ref_new_mobile_order_02 channels=broker_email,call_transcript noise_mode=light output_path=src/main/resources/dataset/inputs/mobile_inputs.json
```

## Generate harder mixed-noise inputs

```text
/generate-telecom-inputs request_type_id=all channels=crm_ticket,broker_email noise_mode=mixed variants_per_reference_per_channel=2 output_path=src/main/resources/dataset/inputs/all_mixed_inputs.json
```

## Dry run

```text
/generate-telecom-inputs request_type_id=rt_retention_discount_request channels=call_transcript dry_run=true
```
