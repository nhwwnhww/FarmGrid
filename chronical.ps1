##  MIT License

#   Copyright (c) 2024 Henry Batt
#
#   Permission is hereby granted, free of charge, to any person obtaining a copy
#   of this software and associated documentation files (the "Software"), to deal
#   in the Software without restriction, including without limitation the rights
#   to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
#   copies of the Software, and to permit persons to whom the Software is
#   furnished to do so, subject to the following conditions:
#
#   The above copyright notice and this permission notice shall be included in all
#   copies or substantial portions of the Software.
#
#   THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
#   IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
#   FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
#   AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
#   LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
#   OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
#   SOFTWARE.

## -!- Chronical Archiver -!- ##
# -- Archive Structure -- #
# Which files and directories should be included in the archive. Formatted as comma separated strings. Overrides the 'source_path'.
$local:archive_structure = "src/farm/core/*",
    "src/farm/files/*",
    "src/farm/debugged/*",
    "test/farm/inventory/BasicInventoryTest.java",
    "test/farm/inventory/FancyInventoryTest.java",
    "ai/*"

# -- Parameters to modify -- #
$local:parameters = @{
    paths = @{
        source_path = "chronical_paths"	# Optional file with archive structure. Only used if 'archive_structure' is empty. Each archive item should be on a new line.
        destination_path = "submission.zip"	# Path of archive to create
        backup_path = "past_submissions\"	# Backup existing zip to this directory
        temp_path = "zip_my_contents"		# Temporarily zip directory preserve zip structure
    }
    make_backups    = $true			# Whether or not to keep backups of old archives. Iff false they are just overridden.
    leave_open      = $false			# Whether or not the shell stays open after execution
    skip_non_exist  = $true			# Skip over files that don't exist. Iff false the program halts
    silent          = $false		# Hide console output (Overridden if verbose mode is enabled)
    verbose         = $false		# Echo out verbose messages (Overrides silent mode)
    compression_level = "Optimal"	# The compression level of archive (Optimal, Fastest, NoCompression)
}

# ----------------------------------------------------------------------

# Unblock script to remove warning on usage
if ($parameters.verbose) { echo "Unblocking script" };
Unblock-File "$PSScriptRoot\$($MyInvocation.MyCommand.Name)"

# Convert paths from local to absolute.
if ($parameters.verbose) { echo "Expanding paths from local to absolute" };
foreach ($local:path in @($parameters.paths.keys)) {
    $parameters.paths[$path] = "$PSScriptRoot\$($parameters.paths[$path])"
}

# If structure not defined by 'archive_structure' attempt to load in the 'source_path' file.
if ($parameters.verbose) { echo "Loading archive structure" };
if ($archive_structure -eq "") {
    if ($parameters.verbose) { echo "Attempt to load separate structure file" };
    if (Test-Path $parameters.paths.source_path) {
        if ($parameters.verbose) { echo "Loaded in file" };
        $archive_structure = $(Get-Content $parameters.paths.source_path)
    } else {
        echo "Error: Missing archive structure."
        rm -r $parameters.paths.temp_path # Cleanup previous files
        if (parameters.leave_open) {Write-Host "`n`nPress any key to exit..."; $Host.UI.RawUI.ReadKey("NoEcho,IncludeKeyDown") }
        exit 2
    }
}

# Handle backup creation if required
if ($parameters.verbose) { echo "Checking if backup needs to be made" };
if ($parameters.make_backups -And (Test-Path $parameters.paths.destination_path)) {
    if ($parameters.verbose) { echo "Making a backup of previous archive" };
    mkdir -f $parameters.paths.backup_path | Out-Null
    mv $parameters.paths.destination_path "$($parameters.paths.backup_path)\$($(Get-Item $($parameters.paths.destination_path)).LastWriteTime.ToString(`"yyyy-MM-dd#HH-mm-ss`")).zip"
}

# Create ZIP file using 7zip.
&"$PSScriptRoot\7za.exe" a -tzip "$PSScriptRoot\submission.zip" $archive_structure `
-x!"src/farm/core/CustomerNotFoundException.java" `
-x!"src/farm/core/DuplicateCustomerException.java" `
-x!"src/farm/core/FailedTransactionException.java" `
-x!"src/farm/core/Farm.java" `
-x!"src/farm/core/InvalidStockRequestException.java" `
-x!"src/farm/core/ShopFront.java" `
-x!"src/farm/core/UnableToInteractException.java" `
-x!"src/farm/core/farmgrid/Grid.java"

# Keep prompt open after running till key press
if ($parameters.leave_open) {
    if (! $parameters.silent -Or $parameters.verbose) { echo "Done" };
    Write-Host "Press any key to exit..."
    $Host.UI.RawUI.ReadKey("NoEcho,IncludeKeyDown")
}