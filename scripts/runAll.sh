#!/usr/bin/env bash

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

run() {
  local script="$1" variant="$2" model="$3"
  echo "========================================"
  echo "START: $script variant=$variant model=$model"
  echo "========================================"
  bash "$SCRIPT_DIR/$script" "$variant" "$model"
  echo "========================================"
  echo "DONE:  $script variant=$variant model=$model"
  echo "========================================"
}

for model in GEMMA3_12B GEMMA3_27B GPT_4O_MINI GPT_5_NANO; do
  for variant in DIRECT FEW_SHOT; do
    run runExtraction.sh    "$variant" "$model"
    run runClassification.sh "$variant" "$model"
  done
done

echo "All runs complete."
