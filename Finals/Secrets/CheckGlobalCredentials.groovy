#!groovy
//Groovy Script to search for passed credential in controllers global credentials list
import java.text.SimpleDateFormat

def sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss.SSS")
def startDate = new Date()
println "Start time : ${sdf.format(startDate)}"
//-------------------------------------------------

String strMaster = java.net.InetAddress.getLocalHost();
println "================================================================================================="
println "Displays credential details maintained globally on ${strMaster} for the passed user name"
println "================================================================================================="

String strSearchUserName = "ldap_fusion_svn"
String strSearchUserName2 = "ldap_bamboo_svn"

def credentials_store = com.cloudbees.plugins.credentials.CredentialsProvider.lookupCredentials(
    com.cloudbees.plugins.credentials.common.StandardCredentials.class,
    Jenkins.getInstance(),
    null,
    null
);

println "=================================================="
println "++++++++++++++++++++++++++++++++++++++++++++++++++"
credentials_store.eachWithIndex { it, i ->
    if (it instanceof com.cloudbees.plugins.credentials.impl.UsernamePasswordCredentialsImpl) {
        if(it.username.matches(strSearchUserName) || it.username.matches(strSearchUserName2))
        {
        //println "credentials: -> ${it} \t XXX: username: ${it.username} password: ${it.password} description: ${it.description}"
        println "${i}, Username-Password-Credentials, id=${it.username}, desc=${it.description}, pass=${it.password}"
        //println "=================================================="
        }
    }
    else if(it instanceof com.cloudbees.jenkins.plugins.sshcredentials.impl.BasicSSHUserPrivateKey) {
        if(it.id.matches(strSearchUserName) || it.id.matches(strSearchUserName2))
        {
        //println("credentials: -> ${it} \t" + String.format("id=%s  desc=%s key=%s\n", it.id, it.description, it.privateKeySource.getPrivateKeys()))
        println("${i}, Basic-SSH-User-Private-Key, " + String.format("id=%s, desc=%s, key=%s", it.id, it.description, it.privateKeySource.getPrivateKeys()))
        //println "=================================================="
        }
    }
    else if(it instanceof org.jenkinsci.plugins.plaincredentials.impl.StringCredentialsImpl) {
        if(it.id.matches(strSearchUserName) || it.id.matches(strSearchUserName2))
        {
        //println("credentials: -> ${it} \t" + String.format("id=%s  desc=%s pass=%sn", it.id, it.description, it.secret))
        println("${i}, String-Credentials, " + String.format("id=%s, desc=%s, pass=%s", it.id, it.description, it.secret))
        //println "=================================================="
        }
    }
    else if(it instanceof org.jenkinsci.plugins.plaincredentials.impl.FileCredentialsImpl) {
        if(it.id.matches(strSearchUserName) || it.id.matches(strSearchUserName2))
        {
        //println("credentials: -> ${it} \t" + String.format("id=%s  desc=%s pass(bytes)=%sn FileName=%s", it.id, it.description, it.secretBytes, it.fileName))
        println("${i}, File-Credentials, " + String.format("id=%s, desc=%s, pass(bytes)=%s, FileName=%s", it.id, it.description, it.secretBytes, it.fileName))
        //println "=================================================="
        }
    }
    else
    {
        println "${i} - Unknown type - ${it}"
        println "=================================================="
    }
}

//--------------------------------------------------
def endDate = new Date()
println "End time: ${sdf.format(endDate)}"

use(groovy.time.TimeCategory) 
{
    def duration = endDate - startDate
      print "Elapsed time: Days: ${duration.days}, Hours: ${duration.hours}, Minutes: ${duration.minutes}, Seconds: ${duration.seconds}"
}

return null;
