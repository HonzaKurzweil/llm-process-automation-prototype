#!/usr/bin/env bash

BASE_URL="http://localhost:8080/api/validation/extraction-validation"
VARIANT="${1:-DIRECT}"
MODEL="${2:-GPT_5_NANO}"

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
INPUTS_DIR="$SCRIPT_DIR/../src/main/resources/dataset/inputs"

files=(
"$INPUTS_DIR/ext_broker_n1_complete.json"
"$INPUTS_DIR/ext_broker_n1_incomplete.json"
"$INPUTS_DIR/ext_broker_n3_complete.json"
"$INPUTS_DIR/ext_broker_n3_incomplete.json"
"$INPUTS_DIR/ext_broker_n5_complete.json"
"$INPUTS_DIR/ext_broker_n5_incomplete.json"
"$INPUTS_DIR/ext_call_n1_complete.json"
"$INPUTS_DIR/ext_call_n1_incomplete.json"
"$INPUTS_DIR/ext_call_n3_complete.json"
"$INPUTS_DIR/ext_call_n3_incomplete.json"
"$INPUTS_DIR/ext_call_n5_complete.json"
"$INPUTS_DIR/ext_call_n5_incomplete.json"
"$INPUTS_DIR/ext_crm_n1_complete.json"
"$INPUTS_DIR/ext_crm_n1_incomplete.json"
"$INPUTS_DIR/ext_crm_n3_complete.json"
"$INPUTS_DIR/ext_crm_n3_incomplete.json"
"$INPUTS_DIR/ext_crm_n5_complete.json"
"$INPUTS_DIR/ext_crm_n5_incomplete.json"
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