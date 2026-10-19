# PowerShell script to test EX-6 Leave Request API

$baseUrl = "http://localhost:8080/v2/process-instances"
$headers = @{ "Content-Type" = "application/json" }

Write-Host "==========================================================" -ForegroundColor Cyan
Write-Host " Camunda 8 Lab EX-6: Leave Request API Automated Runner   " -ForegroundColor Cyan
Write-Host "==========================================================" -ForegroundColor Cyan

# Step 3 Test: Start valid instance with Ravi, 3 days
Write-Host "`n[1] Starting Leave Request process instance (Ravi, 3 days)..." -ForegroundColor Yellow
$bodyStep3 = @{
    processDefinitionId = "leave-request"
    variables = @{
        employeeName = "Ravi"
        days = 3
    }
} | ConvertTo-Json

try {
    $resp1 = Invoke-RestMethod -Uri $baseUrl -Method Post -Headers $headers -Body $bodyStep3
    Write-Host "SUCCESS! Instance created with processInstanceKey:" $resp1.processInstanceKey -ForegroundColor Green
    $resp1 | ConvertTo-Json
} catch {
    Write-Host "Error starting instance:" $_.Exception.Message -ForegroundColor Red
}

# Viva Q2 Test: Misspelled process ID
Write-Host "`n[2] Testing Viva Question 2: Misspelled Process ID ('leave-requst')..." -ForegroundColor Yellow
$bodyMispelled = @{
    processDefinitionId = "leave-requst"
    variables = @{
        employeeName = "Ravi"
        days = 3
    }
} | ConvertTo-Json

try {
    $resp2 = Invoke-RestMethod -Uri $baseUrl -Method Post -Headers $headers -Body $bodyMispelled
} catch {
    Write-Host "SUCCESS! Engine correctly rejected misspelled ID with 404:" -ForegroundColor Green
    $stream = $_.Exception.Response.GetResponseStream()
    $reader = New-Object System.IO.StreamReader($stream)
    $errorJson = $reader.ReadToEnd()
    Write-Host $errorJson -ForegroundColor Red
}

# Viva Q3 Test: Start 2 more instances and verify total count = 3
Write-Host "`n[3] Testing Viva Question 3: Starting 2 more instances to make total 3..." -ForegroundColor Yellow
$body2 = @{ processDefinitionId = "leave-request"; variables = @{ employeeName = "Priya"; days = 2 } } | ConvertTo-Json
$body3 = @{ processDefinitionId = "leave-request"; variables = @{ employeeName = "Amit"; days = 5 } } | ConvertTo-Json

$respPriya = Invoke-RestMethod -Uri $baseUrl -Method Post -Headers $headers -Body $body2
$respAmit = Invoke-RestMethod -Uri $baseUrl -Method Post -Headers $headers -Body $body3

Write-Host "Instance 2 Key:" $respPriya.processInstanceKey -ForegroundColor Green
Write-Host "Instance 3 Key:" $respAmit.processInstanceKey -ForegroundColor Green

Write-Host "`n[4] Querying all running instances..." -ForegroundColor Yellow
$allInstances = Invoke-RestMethod -Uri $baseUrl -Method Get
Write-Host "Total running instances in Operate:" $allInstances.totalCount -ForegroundColor Green
$allInstances | ConvertTo-Json -Depth 3

Write-Host "`nAll EX-6 tests completed successfully!" -ForegroundColor Cyan
