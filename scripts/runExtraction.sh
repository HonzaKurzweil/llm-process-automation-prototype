#!/usr/bin/env bash

BASE_URL="http://localhost:8080/api/validation/extraction-validation"
VARIANT="${1:-DIRECT}"
MODEL="${2:-GPT_5_NANO}"

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
INPUTS_DIR="$SCRIPT_DIR/../src/main/resources/dataset/inputs"

files=(
"$INPUTS_DIR/extraction_broker_email_noise1_complete.json"
"$INPUTS_DIR/extraction_broker_email_noise1_incomplete.json"
"$INPUTS_DIR/extraction_broker_email_noise3_complete.json"
"$INPUTS_DIR/extraction_broker_email_noise3_incomplete.json"
"$INPUTS_DIR/extraction_broker_email_noise5_complete.json"
"$INPUTS_DIR/extraction_broker_email_noise5_incomplete.json"
"$INPUTS_DIR/extraction_call_transcript_noise1_complete.json"
"$INPUTS_DIR/extraction_call_transcript_noise1_incomplete.json"
"$INPUTS_DIR/extraction_call_transcript_noise3_complete.json"
"$INPUTS_DIR/extraction_call_transcript_noise3_incomplete.json"
"$INPUTS_DIR/extraction_call_transcript_noise5_complete.json"
"$INPUTS_DIR/extraction_call_transcript_noise5_incomplete.json"
"$INPUTS_DIR/extraction_crm_ticket_noise1_complete.json"
"$INPUTS_DIR/extraction_crm_ticket_noise1_incomplete.json"
"$INPUTS_DIR/extraction_crm_ticket_noise3_complete.json"
"$INPUTS_DIR/extraction_crm_ticket_noise3_incomplete.json"
"$INPUTS_DIR/extraction_crm_ticket_noise5_complete.json"
"$INPUTS_DIR/extraction_crm_ticket_noise5_incomplete.json"
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