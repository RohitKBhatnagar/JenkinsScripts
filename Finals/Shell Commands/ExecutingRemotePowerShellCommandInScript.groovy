//Way to execute powershell command on Windows Jenkins Agent
//Notice that in order to execute 'chef-client -version' command we have increased waitForOrKill command with 10 secs wait

def sout = new StringBuilder(), serr = new StringBuilder()

//def cmd = 'WMIC CPU Get DeviceID,NumberOfCores,NumberOfLogicalProcessors | findstr /v \"DeviceID\"'
//NOT WORKING ---- def cmd = "powershell.exe -NonInteractive -ExecutionPolicy Bypass -Command \"\$ErrorActionPreference='Stop';[Console]::OutputEncoding=[System.Text.Encoding]::UTF8;dir;EXIT \$global:LastExitCode\""
//def cmd = 'Write-Output "Hello, World!"'
//def cmd = 'dir \\b c:\\chef\\chef-client*'
//def cmd = "Invoke-Command -ScriptBlock {Get-UICulture}"
//def cmd = "dir env:"
//def cmd = "Get-ChildItem env:"
//def cmd = "\$env:Path"
//def cmd = "which chef-client" // /cygdrive/c/opscode/chef/bin/chef-client
def cmd = "chef-client -version"
//def cmd = "[Diagnostics.Process]::Start('chef-client -version').WaitForExit()"
def proc = ['powershell', '-c', cmd].execute()

proc.consumeProcessOutput(sout, serr)
proc.waitForOrKill(10000)
println "$sout"

//powershell.exe -NonInteractive -ExecutionPolicy Bypass -Command \"\$ErrorActionPreference='Stop';[Console]::OutputEncoding=[System.Text.Encoding]::UTF8;dir;EXIT \$global:LastExitCode\"


///////////////////////////////////////////////////////////////////////

//----------- Same script but uses waitForProcessOutput and doesn't need 10 secs wait like above ----------
//URL for improvisation - http://docs.groovy-lang.org/docs/groovy-latest/html/api/org/codehaus/groovy/runtime/ProcessGroovyMethods.html
def sout = new StringBuilder(), serr = new StringBuilder()

//def cmd = 'WMIC CPU Get DeviceID,NumberOfCores,NumberOfLogicalProcessors | findstr /v \"DeviceID\"'
//def cmd = 'Write-Output "Hello, World!"'
//def cmd = 'dir \\b c:\\chef\\chef-client*'
//def cmd = "Invoke-Command -ScriptBlock {Get-UICulture}"
//def cmd = "dir env:"
//def cmd = "Get-ChildItem env:"
//def cmd = "\$env:Path"
//def cmd = "which chef-client" // /cygdrive/c/opscode/chef/bin/chef-client
def cmd = "chef-client -version"
//def cmd = "[Diagnostics.Process]::Start('chef-client -version').WaitForExit()"
def proc = ['powershell', '-c', cmd].execute()

proc.waitForProcessOutput(sout, serr)
//proc.consumeProcessOutput(sout, serr)
//proc.waitForOrKill(10000)
println "$sout"

