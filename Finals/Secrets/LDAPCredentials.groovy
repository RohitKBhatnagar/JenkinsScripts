#!groovy
import java.text.SimpleDateFormat
//println "cat /sys_apps_01/jenkins_nfs/config.xml".execute().text

//println "grep <managerPasswordSecret> /sys_apps_01/jenkins_nfs/config.xml".execute().text

controller = Jenkins.getInstance().getComputer('').getHostName()
println "================================================================================================================================="
println "Opens primary root config.xml and extracts just the tags '<managerDN>' & '<managerPasswordSecret>' and then decrypts the password"
println "Run on the Controller with hostname - ${controller}"
println "================================================================================================================================="

def sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss.SSS")
def startDate = new Date()
println "Start time : ${sdf.format(startDate)}"
println " ------- ****************************** --------------- "


def soutUID = new StringBuilder(), sout = new StringBuilder(), serr = new StringBuilder()

def procUID = 'grep <managerDN> /sys_apps_01/jenkins_nfs/config.xml'.execute()
procUID.consumeProcessOutput(soutUID, serr)
procUID.waitForOrKill(1000)
//println "${soutUID}"
def ldapDN = new XmlParser().parseText(soutUID.toString());
ldapDN.each {
  it ->
  println "LDAP DN - ${it}"
}
//println "$serr"

def proc = 'grep <managerPasswordSecret> /sys_apps_01/jenkins_nfs/config.xml'.execute()
proc.consumeProcessOutput(sout, serr)
proc.waitForOrKill(1000)
//println "out> $sout err> $serr"
//println "$sout"
def ldapPwd = new XmlParser().parseText(sout.toString());
//println "${ldapPwd}";

//println " ------- ****************************** --------------- "

def encPwd;
ldapPwd.each {
  it ->
  println "LDAP Encrypted Password - ${it}"
  encPwd = it;
}
println("LDAP Password - ${hudson.util.Secret.decrypt(encPwd)}")
println "$serr"


//--------------------------------------------------
println " ------- ****************************** --------------- "
def endDate = new Date()
println "End time: ${sdf.format(endDate)}"

return