#!groovy

println "Master HostName and IP Address - ${java.net.InetAddress.getLocalHost()}"
def sout = new StringBuilder(), serr = new StringBuilder()
def proc = 'ls -latrch /sys_apps_01/jenkins2/logs/custom/SAML-Logger.log'.execute()
//def proc = 'grep -A 250 -B 5 275e3950-b65f-4935-8150-02c1feace7d3 /apps_01/webapps/tomcat/logs/jenkins_server0/server.log'.execute() //grep 250 lines after and 5 lines before in the target file
//// ----------- To Use pipe command use this format -----------
def cmd = 'df -h|grep jenkins'
proc = ['bash', '-c', cmd].execute()
//Clear StringBuilder
sout.setLength(0)
serr.setLength(0)
proc.consumeProcessOutput(sout, serr)
proc.waitForOrKill(1000)
//println "out> $sout err> $serr"
println "$sout"
println " ------- ****************************** --------------- "
println "$serr"

//If you need to continue with another shell command then use this continuation
sout = new StringBuilder()
serr = new StringBuilder()
proc = 'hostname'.execute()
proc.consumeProcessOutput(sout, serr)
proc.waitForOrKill(1000)
//println "out> $sout err> $serr"
println "$sout"
println " ------- ****************************** --------------- "
println "$serr"

//////////// To run with pipe command on agents - then use this syntax
/**
def sout = new StringBuilder(), serr = new StringBuilder()

def cmd = 'WMIC CPU Get DeviceID,NumberOfCores,NumberOfLogicalProcessors | findstr /v \"DeviceID\"'
def proc = ['bash', '-c', cmd].execute()

proc.consumeProcessOutput(sout, serr)
proc.waitForOrKill(1000)
println "$sout"
**/


////Following is when you need to say run grep command ///////
//def cmd = 'df -h|grep jenkins'
//def proc = ['bash', '-c', cmd].execute()


#!groovy

def sout = new StringBuilder(), serr = new StringBuilder()
//def proc = 'pwd -LP'.execute()
//def proc = 'grep --include=server.log -rnw "/apps_01/webapps/tomcat/logs/jenkins_server0/" -e "GRAND"'.execute()
//def proc = 'grep --include=*.log -rnw . -e "GRAND"'.execute()
def proc = 'grep --include=server.log -rnw . -e ITEMS'.execute()
proc.consumeProcessOutput(sout, serr)
proc.waitForOrKill(30000)
//println "out> $sout err> $serr"
println "$sout"
println " ------- ****************************** --------------- "
println "$serr"


#!groovy

def sout = new StringBuilder(), serr = new StringBuilder()
///------------ Following will display OS Version of the Jenkins Master -----------------
//def proc = 'cat /etc/os-release'.execute()
//def proc = 'lsb_release -a'.execute()
//def proc = 'uname -r'.execute()
//def proc = 'cat /proc/version'.execute()

///------------ Followign displays the SWAPPINESS associated to a particular Jenkins instance ----------
//def proc = 'cat /proc/sys/vm/swappiness'.execute()
proc.consumeProcessOutput(sout, serr)
proc.waitForOrKill(1000)
//println "out> $sout err> $serr"
println "$sout"
println " ------- ****************************** --------------- "
println "$serr"

//=====================================
//Run multiple commands sequentially say upto 3 commands
println "Master HostName and IP Address - ${java.net.InetAddress.getLocalHost()}"

//-------------------- Command #1 --------------------
def sout = new StringBuilder(), serr = new StringBuilder()
def proc = 'ls -latrch /sys_apps_01/jenkins2/jenkins-script.sh'.execute()
proc.consumeProcessOutput(sout, serr)
proc.waitForOrKill(1000)
println "$sout"
println " ------- ****************************** --------------- "
println "$serr"

sout.setLength(0)
serr.setLength(0)
//-------------------- Command #2 --------------------
proc = 'ls -latrch /sys_apps_01/jenkins_nfs/jenkins-script.sh'.execute()
proc.consumeProcessOutput(sout, serr)
proc.waitForOrKill(1000)
println "$sout"
println " ------- ****************************** --------------- "
println "$serr"

sout.setLength(0)
serr.setLength(0)
//--------------------- Command #3 --------------------
def cmd = 'sesudo -list'
proc = ['bash', '-c', cmd].execute()

proc.consumeProcessOutput(sout, serr)
proc.waitForOrKill(1000)
//println "out> $sout err> $serr"
println "$sout"
println " ------- ****************************** --------------- "
println "$serr"

sout.setLength(0)
serr.setLength(0)
//============================================
//Top command on the controller

println "Master HostName and IP Address - ${java.net.InetAddress.getLocalHost()}"
def sout = new StringBuilder(), serr = new StringBuilder()

//println "top -bcn1 -w512".execute().text

def cmd = 'top -bcn1 -w512'
proc = ['bash', '-c', cmd].execute()

proc.consumeProcessOutput(sout, serr)
proc.waitForOrKill(1000)
//println "out> $sout err> $serr"
println "$sout"
println " ------- ****************************** --------------- "
println "$serr"



////////////////////////////////////////////////////////////////////
//cacerts pull out directly on the agent
println "Master HostName and IP Address - ${java.net.InetAddress.getLocalHost()}"
def sout = new StringBuilder(), serr = new StringBuilder()
//// ----------- To Use pipe command use this format -----------
def cmd = 'keytool -storepass changeit -list -keystore /sys_apps_01/java/zulu1.8.0_242/jre/lib/security/cacerts'
def proc = ['bash', '-c', cmd].execute()
//Clear StringBuilder
sout.setLength(0)
serr.setLength(0)
proc.consumeProcessOutput(sout, serr)
proc.waitForOrKill(1000)
//println "out> $sout err> $serr"
println "$sout"
println " ------- ****************************** --------------- "
println "$serr"
