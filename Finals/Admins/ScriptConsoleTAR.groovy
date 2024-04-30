//grep -R 'Operation not permitted' | awk -F: '{print $4}' | sort | uniq -c | sort -rn


//Script Console script for creating a tar file directly on Jenkins directory
println "ls -latrch /sys_apps_01/jenkins_nfs/logs/custom/".execute().text

//println "tar -czvf Folder.tar.gz /sys_apps_01/jenkins_nfs/logs/custom/Folder*".execute().text

println "Master HostName and IP Address - ${java.net.InetAddress.getLocalHost()}"
def sout = new StringBuilder(), serr = new StringBuilder()
def cmd = 'tar -czvf Credentials.tar.gz /sys_apps_01/jenkins_nfs/logs/custom/Credentials*'
def proc = ['bash', '-c', cmd].execute()
//Clear StringBuilder
sout.setLength(0)
serr.setLength(0)
proc.consumeProcessOutput(sout, serr)
proc.waitForOrKill(30000)
//println "out> $sout err> $serr"
println "$sout"
println " ------- ****************************** --------------- "
println "$serr"


///////
//The compressed file upload to artifactory
//println "ls -latrch /sys_apps_01/jenkins_nfs/logs/custom/".execute().text
println "pwd".execute().text
///apps_01/webapps/tomcat/logs/jenkins_server0
println "ls -latrch /apps_01/webapps/tomcat/logs/jenkins_server0".execute().text
//https://artifacts.mastercard.int:443/artifactory/archive-internal-release/com/mastercard/pipe/

println "Master HostName and IP Address - ${java.net.InetAddress.getLocalHost()}"
def sout = new StringBuilder(), serr = new StringBuilder()
def cmd = 'curl -u e059704:***** -T Credentials.tar.gz https://artifacts.mastercard.int:443/artifactory/archive-internal-release/com/mastercard/pipe/logs/cd3/Credentials.tar.gz'
def proc = ['bash', '-c', cmd].execute()
//Clear StringBuilder
sout.setLength(0)
serr.setLength(0)
proc.consumeProcessOutput(sout, serr)
proc.waitForOrKill(30000)
//println "out> $sout err> $serr"
println "$sout"
println " ------- ****************************** --------------- "
println "$serr"