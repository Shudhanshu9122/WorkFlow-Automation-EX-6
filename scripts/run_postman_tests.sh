#!/usr/bin/env bash
# Test script for Camunda 8 Lab EX-6

BASE_URL="http://localhost:8080/v2/process-instances"

echo "=== [1] Step 3: Start process instance (Ravi, 3 days) ==="
curl -i -X POST "${BASE_URL}" \
  -H "Content-Type: application/json" \
  -d '{
    "processDefinitionId": "leave-request",
    "variables": {
      "employeeName": "Ravi",
      "days": 3
    }
  }'
echo -e "\n\n"

echo "=== [2] Viva Q2: Misspelled process ID test (leave-requst -> 404) ==="
curl -i -X POST "${BASE_URL}" \
  -H "Content-Type: application/json" \
  -d '{
    "processDefinitionId": "leave-requst",
    "variables": {
      "employeeName": "Ravi",
      "days": 3
    }
  }'
echo -e "\n\n"

echo "=== [3] Viva Q3: Start process 2 more times (Total 3) ==="
curl -s -X POST "${BASE_URL}" -H "Content-Type: application/json" -d '{"processDefinitionId":"leave-request","variables":{"employeeName":"Priya","days":2}}' > /dev/null
curl -s -X POST "${BASE_URL}" -H "Content-Type: application/json" -d '{"processDefinitionId":"leave-request","variables":{"employeeName":"Amit","days":5}}' > /dev/null

echo "=== [4] Query all running instances (Expect totalCount: 3) ==="
curl -s -X GET "${BASE_URL}" | jq . || curl -s -X GET "${BASE_URL}"
echo -e "\n"
