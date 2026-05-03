#!/usr/bin/env bash

BASE_URL="http://localhost:8080/api/validation/extraction-validation"
VARIANT="${1:-DIRECT}"
MODEL="${2:-GPT_5_NANO}"

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
INPUTS_DIR="$SCRIPT_DIR/../src/main/resources/dataset/inputs"

files=(
"$INPUTS_DIR/extraction_complete_broker_email_noise1.json"
"$INPUTS_DIR/extraction_complete_broker_email_noise3.json"
"$INPUTS_DIR/extraction_complete_broker_email_noise5.json"
"$INPUTS_DIR/extraction_incomplete_broker_email_noise1.json"
"$INPUTS_DIR/extraction_incomplete_broker_email_noise3.json"
"$INPUTS_DIR/extraction_incomplete_broker_email_noise5.json"
"$INPUTS_DIR/extraction_complete_call_transcript_noise1.json"
"$INPUTS_DIR/extraction_complete_call_transcript_noise3.json"
"$INPUTS_DIR/extraction_complete_call_transcript_noise5.json"
"$INPUTS_DIR/extraction_incomplete_call_transcript_noise1.json"
"$INPUTS_DIR/extraction_incomplete_call_transcript_noise3.json"
"$INPUTS_DIR/extraction_incomplete_call_transcript_noise5.json"
"$INPUTS_DIR/extraction_complete_crm_ticket_noise1.json"
"$INPUTS_DIR/extraction_complete_crm_ticket_noise3.json"
"$INPUTS_DIR/extraction_complete_crm_ticket_noise5.json"
"$INPUTS_DIR/extraction_incomplete_crm_ticket_noise1.json"
"$INPUTS_DIR/extraction_incomplete_crm_ticket_noise3.json"
"$INPUTS_DIR/extraction_incomplete_crm_ticket_noise5.json"
)

for file in "${files[@]}"; do
  echo "Processing: $file"

  curl -X POST "$BASE_URL" \
    -H "accept: */*" \
    --data-urlencode "inputPath=$file" \
    --data-urlencode "variant=$VARIANT" \
    --data-urlencode "model=$MODEL" \
    -d ''

  echo -e "\n-----------------------------\n"
done