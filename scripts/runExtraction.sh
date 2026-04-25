#!/usr/bin/env bash

BASE_URL="http://localhost:8080/api/execution/extraction-validation"
VARIANT="DIRECT"
MODEL="GPT_5_NANO"

files=(
"C:\Users\Jan\Desktop\projects\llm-process-automation-prototype\src\main\resources\dataset\inputs\ext_broker_n1_complete.json"
"C:\Users\Jan\Desktop\projects\llm-process-automation-prototype\src\main\resources\dataset\inputs\ext_broker_n1_incomplete.json"
"C:\Users\Jan\Desktop\projects\llm-process-automation-prototype\src\main\resources\dataset\inputs\ext_broker_n3_complete.json"
"C:\Users\Jan\Desktop\projects\llm-process-automation-prototype\src\main\resources\dataset\inputs\ext_broker_n3_incomplete.json"
"C:\Users\Jan\Desktop\projects\llm-process-automation-prototype\src\main\resources\dataset\inputs\ext_broker_n5_complete.json"
"C:\Users\Jan\Desktop\projects\llm-process-automation-prototype\src\main\resources\dataset\inputs\ext_broker_n5_incomplete.json"
"C:\Users\Jan\Desktop\projects\llm-process-automation-prototype\src\main\resources\dataset\inputs\ext_call_n1_complete.json"
"C:\Users\Jan\Desktop\projects\llm-process-automation-prototype\src\main\resources\dataset\inputs\ext_call_n1_incomplete.json"
"C:\Users\Jan\Desktop\projects\llm-process-automation-prototype\src\main\resources\dataset\inputs\ext_call_n3_complete.json"
"C:\Users\Jan\Desktop\projects\llm-process-automation-prototype\src\main\resources\dataset\inputs\ext_call_n3_incomplete.json"
"C:\Users\Jan\Desktop\projects\llm-process-automation-prototype\src\main\resources\dataset\inputs\ext_call_n5_complete.json"
"C:\Users\Jan\Desktop\projects\llm-process-automation-prototype\src\main\resources\dataset\inputs\ext_call_n5_incomplete.json"
"C:\Users\Jan\Desktop\projects\llm-process-automation-prototype\src\main\resources\dataset\inputs\ext_crm_n1_complete.json"
"C:\Users\Jan\Desktop\projects\llm-process-automation-prototype\src\main\resources\dataset\inputs\ext_crm_n1_incomplete.json"
"C:\Users\Jan\Desktop\projects\llm-process-automation-prototype\src\main\resources\dataset\inputs\ext_crm_n3_complete.json"
"C:\Users\Jan\Desktop\projects\llm-process-automation-prototype\src\main\resources\dataset\inputs\ext_crm_n3_incomplete.json"
"C:\Users\Jan\Desktop\projects\llm-process-automation-prototype\src\main\resources\dataset\inputs\ext_crm_n5_complete.json"
"C:\Users\Jan\Desktop\projects\llm-process-automation-prototype\src\main\resources\dataset\inputs\ext_crm_n5_incomplete.json"
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