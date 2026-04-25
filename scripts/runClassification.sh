#!/usr/bin/env bash

BASE_URL="http://localhost:8080/api/validation/classification-validation"
VARIANT="${1:-DIRECT}"
MODEL="${2:-GPT_5_NANO}"

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
INPUTS_DIR="$SCRIPT_DIR/../src/main/resources/dataset/inputs"

files=(
"$INPUTS_DIR/cls_broker_n1.json"
"$INPUTS_DIR/cls_broker_n3.json"
"$INPUTS_DIR/cls_broker_n5.json"
"$INPUTS_DIR/cls_call_n1.json"
"$INPUTS_DIR/cls_call_n3.json"
"$INPUTS_DIR/cls_call_n5.json"
"$INPUTS_DIR/cls_crm_n1.json"
"$INPUTS_DIR/cls_crm_n3.json"
"$INPUTS_DIR/cls_crm_n5.json"
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
