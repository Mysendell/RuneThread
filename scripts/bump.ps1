# bump.ps1
param (
    [string]$part
)

$currentVersion = (Get-Content gradle.properties | Select-String -Pattern 'mod_version=(.*)').Matches.Groups[1].Value

$versionParts = $currentVersion -split '\.'
$major = [int]$versionParts[0]
$minor = [int]$versionParts[1]
$patch = [int]$versionParts[2]

switch ($part) {
    "major" {
        $major++
        $minor = 0
        $patch = 0
    }
    "minor" {
        $minor++
        $patch = 0
    }
    "patch" {
        $patch++
    }
    default {
        Write-Host "Usage: .\bump.ps1 [major|minor|patch]"
        exit 1
    }
}

$newVersion = "$major.$minor.$patch"

(Get-Content gradle.properties) -replace "mod_version=.*", "mod_version=$newVersion" | Set-Content gradle.properties

Write-Host "Version bumped to $newVersion"