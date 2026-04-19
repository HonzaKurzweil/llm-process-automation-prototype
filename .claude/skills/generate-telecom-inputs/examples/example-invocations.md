# Example invocations

## Extraction benchmark for one request type

`/generate-telecom-inputs mode=extraction request_type_ids=rt_fixed_internet_with_hardware_order channels=crm_ticket,broker_email,call_transcript noise_mode=light variants_per_base=2 output_path=src/main/resources/dataset/inputs/extraction_fixed_internet.json`

## Classification benchmark for unclear inputs

`/generate-telecom-inputs mode=classification classifier_outcome_types=single_known_request_type_with_unknown_tail,multi_intent_known_request_types,unknown_or_out_of_scope channels=broker_email,call_transcript noise_mode=mixed variants_per_base=2 output_path=src/main/resources/dataset/inputs/classification_unclear.json`

## Mixed benchmark with dry run first

`/generate-telecom-inputs mode=both request_type_ids=all channels=crm_ticket,broker_email,call_transcript noise_mode=light variants_per_base=1 dry_run=true`
