# Example invocations

## Extraction benchmark for all request types

`/generate-telecom-benchmark-lean mode=extraction requestTypeIds=all channels=crm_ticket,broker_email,call_transcript noiseProfile=light completenessMode=mixed variantsPerCase=2 outputPath=src/main/resources/dataset/inputs/extraction_benchmark.json`

## Classification benchmark with single and mixed scenarios

`/generate-telecom-benchmark-lean mode=classification requestTypeIds=all channels=broker_email,call_transcript noiseProfile=light scenarioKinds=singleKnownExact,singleKnownPartial,singleKnownWithUnknownTail,multiKnownMixed,unknownOnly variantsPerCase=2 outputPath=src/main/resources/dataset/inputs/classification_benchmark.json`

## Combined benchmark with 1–2 known requests per input

`/generate-telecom-benchmark-lean mode=combined requestTypeIds=all channels=crm_ticket,broker_email noiseProfile=light scenarioKinds=singleKnownExact,singleKnownPartial,multiKnownMixed knownRequestCount=1-2 perRequestCompletenessMode=mixed variantsPerCase=2 outputPath=src/main/resources/dataset/inputs/combined_benchmark.json`
