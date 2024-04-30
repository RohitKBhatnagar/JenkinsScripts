println "Master HostName and IP Address - ${java.net.InetAddress.getLocalHost()}"
def sout = new StringBuilder(), serr = new StringBuilder()
def proc
//// ----------- To Use pipe command use this format -----------
println "Backing up directory - InvoiceSolutions" 
def cmd = "rsync -avm --include='config.xml' --include='nectar-rbac.xml' --include='*/' --exclude='*' /sys_apps_01/jenkins_nfs/jobs/InvoiceSolutions/ /sys_apps_01/jenkins_utils/backups/cd4/MAY19/InvoiceSolutions/ /sys_apps_01/jenkins_utils/backups/cd4/MAY19/InvoiceSolutions_MAY19Bkup.log" 
proc = ['bash', '-c', cmd].execute()
//Clear StringBuilder
sout.setLength(0)
serr.setLength(0)
proc.waitForProcessOutput(sout, serr);
println "out> $sout err> $serr"
println " ------- "
println "ls -latrch /sys_apps_01/jenkins_utils/backups/cd4/MAY19/InvoiceSolutions".execute().text

//Reset cmd, sout, serr
cmd = ""
sout.setLength(0)
serr.setLength(0)

println "Backng up directory - RealTimeGiro" 
cmd = "rsync -avm --include='config.xml' --include='nectar-rbac.xml' --include='*/' --exclude='*' /sys_apps_01/jenkins_nfs/jobs/RealTimeGiro/ /sys_apps_01/jenkins_utils/backups/cd4/MAY19/RealTimeGiro/ /sys_apps_01/jenkins_utils/backups/cd4/MAY19/RealTimeGiro_MAY19Bkup.log"
//println cmd
proc = ['bash', '-c', cmd].execute()
proc.waitForProcessOutput(sout, serr);
println "out> $sout err> $serr"
println " ------- "
println "ls -latrch /sys_apps_01/jenkins_utils/backups/cd4/MAY19/RealTimeGiro".execute().text

//Reset cmd, sout, serr
cmd = "" 
sout.setLength(0) serr.setLength(0) 
proc =['bash', '-c', cmd].execute() 
println "Backing up directory - RealTimeNicsReal" 
cmd = "rsync -avm --include='config.xml' --include='nectar-rbac.xml' --include='*/' --exclude='*' /sys_apps_01/jenkins_nfs/jobs/RealTimeNicsReal/ /sys_apps_01/jenkins_utils/backups/cd4/MAY19/RealTimeNicsReal/ /sys_apps_01/jenkins_utils/backups/cd4/MAY19/RealTimeNicsReal_MAY19Bkup.log"
//println cmd
proc = ['bash', '-c', cmd].execute()
proc.waitForProcessOutput(sout, serr);
println "out> $sout err> $serr"
println " ------- "
println "ls -latrch /sys_apps_01/jenkins_utils/backups/cd4/MAY19/RealTimeNicsReal".execute().text

//Reset cmd, sout, serr
cmd = "" 
sout.setLength(0) serr.setLength(0) 
proc =['bash', '-c', cmd].execute() 
println "Backing up directory - RealTimeBankart" 
cmd = "rsync -avm --include='config.xml' --include='nectar-rbac.xml' --include='*/' --exclude='*' /sys_apps_01/jenkins_nfs/jobs/RealTimeBankart/ /sys_apps_01/jenkins_utils/backups/cd4/MAY19/RealTimeBankart/ /sys_apps_01/jenkins_utils/backups/cd4/MAY19/RealTimeBankart_MAY19Bkup.log"
//println cmd
proc = ['bash', '-c', cmd].execute()
proc.waitForProcessOutput(sout, serr);
println "out> $sout err> $serr"
println " ------- "
println "ls -latrch /sys_apps_01/jenkins_utils/backups/cd4/MAY19/RealTimeBankart".execute().text