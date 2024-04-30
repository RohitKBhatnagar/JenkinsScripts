#!groovy
//Groovy Script to list out only global credentials
import java.text.SimpleDateFormat

def sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss.SSS")
def startDate = new Date()
println "Start time : ${sdf.format(startDate)}"
//-------------------------------------------------

String strMaster = java.net.InetAddress.getLocalHost();
println "================================================================================================="
println "Displays credentials of various types maintained globally on ${strMaster}"
println "================================================================================================="

def credentials_store = com.cloudbees.plugins.credentials.CredentialsProvider.lookupCredentials(
    com.cloudbees.plugins.credentials.common.StandardCredentials.class,
    Jenkins.getInstance(),
    null,
    null
);

//println "credentials_store: ${credentials_store}"

println "=================================================="
println "++++++++++++++++++++++++++++++++++++++++++++++++++"
credentials_store.eachWithIndex { it, i ->
    //println "credentials: -> ${it}"
    if (it instanceof com.cloudbees.plugins.credentials.impl.UsernamePasswordCredentialsImpl) {
        //println "credentials: -> ${it} \t XXX: username: ${it.username} password: ${it.password} description: ${it.description}"
        println "${i}, Username-Password-Credentials, id=${it.username}, desc=${it.description}, pass=${it.password}"
        //println "=================================================="
    }
    else if(it instanceof com.cloudbees.jenkins.plugins.sshcredentials.impl.BasicSSHUserPrivateKey) {
        //println("credentials: -> ${it} \t" + String.format("id=%s  desc=%s key=%s\n", it.id, it.description, it.privateKeySource.getPrivateKeys()))
        println("${i}, Basic-SSH-User-Private-Key, " + String.format("id=%s, desc=%s, key=%s", it.id, it.description, it.privateKeySource.getPrivateKeys()))
        //println "=================================================="
    }
    else if(it instanceof org.jenkinsci.plugins.plaincredentials.impl.StringCredentialsImpl) {
        //println("credentials: -> ${it} \t" + String.format("id=%s  desc=%s pass=%sn", it.id, it.description, it.secret))
        println("${i}, String-Credentials, " + String.format("id=%s, desc=%s, pass=%s", it.id, it.description, it.secret))
        //println "=================================================="
    }
    else if(it instanceof com.cloudbees.plugins.credentials.SystemCredentialsProvider) {
        println "++++++++++++++++++++++++++++++++++++++++++++++++++"
        //println "credentials: -> ${it} \t ${it.getConfigFile().asString()}"
        println "${i}, System-Credentials-Provider, ${it.getConfigFile().asString()}"
        def lstCredentials = it.getCredentials(); //Get all the (Domain.global()) credentials.
        lstCredentials.eachWithIndex{ itCred, iCred -> 
            println "\t\t ${iCred}, Systems-Credentials-Global-List, Scope=${itCred.scope}, Descriptor=${itCred.descriptor}"
        }
        println "=================================================="
    }
    else if(it instanceof com.cloudbees.plugins.credentials.impl.CertificateCredentialsImpl) {
        println "++++++++++++++++++++++++++++++++++++++++++++++++++"
        //println("credentials: -> ${it} \t" + String.format("id=%s  desc=%s pass=%sn", it.id, it.description, it.secret))
        println("${i}, Certificate-Credentials, " + String.format("id=%s, desc=%s, pass=%s, Src=%s, KeyStore=%s", it.id, it.description, it.password, it.keyStoreSource, it.keyStore))
        println "=================================================="
    }
    else if(it instanceof org.jenkinsci.plugins.plaincredentials.impl.FileCredentialsImpl) {
        //println("credentials: -> ${it} \t" + String.format("id=%s  desc=%s pass(bytes)=%sn FileName=%s", it.id, it.description, it.secretBytes, it.fileName))
        println("${i}, File-Credentials, " + String.format("id=%s, desc=%s, pass(bytes)=%s, FileName=%s", it.id, it.description, it.secretBytes, it.fileName))
        //println "=================================================="
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
///=====================================================================================================================
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

import java.text.SimpleDateFormat
def sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss.SSS")
def startDate = new Date()
println "Start time : ${sdf.format(startDate)}"

//Master HostName and IP Address
println "Master executed on - ${java.net.InetAddress.getLocalHost()}"

println "----------------------------------------------"
println "Displays Jenkins Global and System Credemtials"
println "++++++++++++++++++++++++++++++++++++++++++++++"

def creds = com.cloudbees.plugins.credentials.CredentialsProvider.lookupCredentials(
    com.cloudbees.plugins.credentials.common.StandardUsernameCredentials.class,
    Jenkins.instance,
    null,
    null
)

println "------------------------------------------------------"
println "Looking up into Credential Providers plugin - ${creds}"
println "------------------------------------------------------"

def iCount = 1;
for(c in creds) {
  if(c instanceof com.cloudbees.jenkins.plugins.sshcredentials.impl.BasicSSHUserPrivateKey){
    println("${iCount} :: " + String.format("id=%s  desc=%s key=%s\n", c.id, c.description, c.privateKeySource.getPrivateKeys()))
    println "=================================================="
  }
  else if (c instanceof com.cloudbees.plugins.credentials.impl.UsernamePasswordCredentialsImpl){
    println("${iCount} :: " + String.format("id=%s  desc=%s user=%s pass=%s\n", c.id, c.description, c.username, c.password))
    println "=================================================="
  }
  else
  {
    println "${iCount} :: Unknown type - ${c}"
    println "=================================================="
  }
  
  iCount++;
}

//---------------------------------------------------------------------------
def endDate = new Date()
println "End time: ${sdf.format(endDate)}"

use(groovy.time.TimeCategory) 
{
    def duration = endDate - startDate
      print "Elapsed time: Days: ${duration.days}, Hours: ${duration.hours}, Minutes: ${duration.minutes}, Seconds: ${duration.seconds}"
}

return null

///=====================================================================================================================
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//Same script but with expansion for each class type....
def credentials_store = jenkins.model.Jenkins.instance.getExtensionList(
        'com.cloudbees.plugins.credentials.SystemCredentialsProvider'
        )

println "credentials_store: ${credentials_store}"
//println " Description: ${credentials_store.description}"
//println " Target: ${credentials_store.target}"
credentials_store.each {  println "credentials_store.each: ${it}" }

println "=================================================="
println "++++++++++++++++++++++++++++++++++++++++++++++++++"
credentials_store[0].credentials.each { it ->
    println "credentials: -> ${it}"
    if (it instanceof com.cloudbees.plugins.credentials.impl.UsernamePasswordCredentialsImpl) {
        println "XXX: username: ${it.username} password: ${it.password} description: ${it.description}"
        println "=================================================="
    }
    else if(it instanceof com.cloudbees.jenkins.plugins.sshcredentials.impl.BasicSSHUserPrivateKey) {
        println(String.format("id=%s  desc=%s key=%s\n", it.id, it.description, it.privateKeySource.getPrivateKeys()))
        println "=================================================="
    }
    else if(it instanceof org.jenkinsci.plugins.plaincredentials.impl.StringCredentialsImpl) {
        println(String.format("id=%s  desc=%s pass=%sn", it.id, it.description, it.secret))
        println "=================================================="
    }
    else if(it instanceof com.cloudbees.plugins.credentials.SystemCredentialsProvider) {
        println "${it.getConfigFile().asString()}"
        println "=================================================="
    }
    else
    {
        println "Unknown type - ${it}"
        println "=================================================="
    }
    /* else if (c instanceof com.cloudbees.plugins.credentials.impl.UsernamePasswordCredentialsImpl) {
        println(String.format("id=%s  desc=%s user=%s pass=%sn", c.id, c.description, c.username, c.password))
        println "=================================================="
    } */
}

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//Script to pull out credentials against a particular top level job folder name
//=============================================================================
import com.cloudbees.hudson.plugins.folder.AbstractFolder;
def folderName="PrepaidManagementServicesBizOps" // change value `folder-a` for the full name of the folder you want to disable all jobs in


def credentials_store = com.cloudbees.plugins.credentials.CredentialsProvider.lookupCredentials(
    com.cloudbees.plugins.credentials.common.StandardCredentials.class,
    Jenkins.getInstance().getItemByFullName(folderName) ,
    null,
    null
);

println "credentials_store: ${credentials_store}"
credentials_store.each {  println "credentials_store.each: ${it}" }

println "=================================================="
println "++++++++++++++++++++++++++++++++++++++++++++++++++"
credentials_store.each { it ->
    println "credentials: -> ${it}"
    if (it instanceof com.cloudbees.plugins.credentials.impl.UsernamePasswordCredentialsImpl) {
        println "XXX: username: ${it.username} password: ${it.password} description: ${it.description}"
        println "=================================================="
    }
    else if(it instanceof com.cloudbees.jenkins.plugins.sshcredentials.impl.BasicSSHUserPrivateKey) {
        println(String.format("id=%s  desc=%s key=%s\n", it.id, it.description, it.privateKeySource.getPrivateKeys()))
        println "=================================================="
    }
    else if(it instanceof org.jenkinsci.plugins.plaincredentials.impl.StringCredentialsImpl) {
        println(String.format("id=%s  desc=%s pass=%sn", it.id, it.description, it.secret))
        println "=================================================="
    }
    else if(it instanceof com.cloudbees.plugins.credentials.SystemCredentialsProvider) {
        println "${it.getConfigFile().asString()}"
        println "=================================================="
    }
    else
    {
        println "Unknown type - ${it}"
        println "=================================================="
    }
}

return null;

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//Script to pull out more details for all credentials against a particular top level job folder name
//==================================================================================================

import java.text.SimpleDateFormat

def folderName="MarketingBusinessSolutionsDevOps" // change value `folder-a` for the full name of the folder you want to disable all jobs in

def sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss.SSS")
def startDate = new Date()
println "Start time : ${sdf.format(startDate)}"
//-------------------------------------------------

String strMaster = java.net.InetAddress.getLocalHost();
println "================================================================================================="
println "Displays credentials of various types behind a top level folder name provided"
println ">>>>> CD Master - ${strMaster} and the TOP LEVEL Folder Name - ${folderName} <<<<<<<<<<<<<<<<<<<<"
println "================================================================================================="

import com.cloudbees.hudson.plugins.folder.AbstractFolder;


def credentials_store = com.cloudbees.plugins.credentials.CredentialsProvider.lookupCredentials(
    com.cloudbees.plugins.credentials.common.StandardCredentials.class,
    Jenkins.getInstance().getItemByFullName(folderName) ,
    null,
    null
);

//println "credentials_store: ${credentials_store}"

println "=================================================="
println "++++++++++++++++++++++++++++++++++++++++++++++++++"
credentials_store.eachWithIndex { it, i ->
    //println "credentials: -> ${it}"
    if (it instanceof com.cloudbees.plugins.credentials.impl.UsernamePasswordCredentialsImpl) {
        //println "credentials: -> ${it} \t XXX: username: ${it.username} password: ${it.password} description: ${it.description}"
        println "${i}, Username-Password-Credentials, id=${it.username}, desc=${it.description}, pass=${it.password}"
        //println "=================================================="
    }
    else if(it instanceof com.cloudbees.jenkins.plugins.sshcredentials.impl.BasicSSHUserPrivateKey) {
        //println("credentials: -> ${it} \t" + String.format("id=%s  desc=%s key=%s\n", it.id, it.description, it.privateKeySource.getPrivateKeys()))
        println("${i}, Basic-SSH-User-Private-Key, " + String.format("id=%s, desc=%s, key=%s", it.id, it.description, it.privateKeySource.getPrivateKeys()))
        //println "=================================================="
    }
    else if(it instanceof org.jenkinsci.plugins.plaincredentials.impl.StringCredentialsImpl) {
        //println("credentials: -> ${it} \t" + String.format("id=%s  desc=%s pass=%sn", it.id, it.description, it.secret))
        println("${i}, String-Credentials, " + String.format("id=%s, desc=%s, pass=%s", it.id, it.description, it.secret))
        //println "=================================================="
    }
    else if(it instanceof com.cloudbees.plugins.credentials.SystemCredentialsProvider) {
        println "++++++++++++++++++++++++++++++++++++++++++++++++++"
        //println "credentials: -> ${it} \t ${it.getConfigFile().asString()}"
        println "${i}, System-Credentials-Provider, ${it.getConfigFile().asString()}"
        def lstCredentials = it.getCredentials(); //Get all the (Domain.global()) credentials.
        lstCredentials.eachWithIndex{ itCred, iCred -> 
            println "\t\t ${iCred}, Systems-Credentials-Global-List, Scope=${itCred.scope}, Descriptor=${itCred.descriptor}"
        }
        println "=================================================="
    }
    else if(it instanceof com.cloudbees.plugins.credentials.impl.CertificateCredentialsImpl) {
        println "++++++++++++++++++++++++++++++++++++++++++++++++++"
        //println("credentials: -> ${it} \t" + String.format("id=%s  desc=%s pass=%sn", it.id, it.description, it.secret))
        println("${i}, Certificate-Credentials, " + String.format("id=%s, desc=%s, pass=%s, Src=%s, KeyStore=%s", it.id, it.description, it.password, it.keyStoreSource, it.keyStore))
        println "=================================================="
    }
    else if(it instanceof org.jenkinsci.plugins.plaincredentials.impl.FileCredentialsImpl) {
        //println("credentials: -> ${it} \t" + String.format("id=%s  desc=%s pass(bytes)=%sn FileName=%s", it.id, it.description, it.secretBytes, it.fileName))
        println("${i}, File-Credentials, " + String.format("id=%s, desc=%s, pass(bytes)=%s, FileName=%s", it.id, it.description, it.secretBytes, it.fileName))
        //println "=================================================="
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


///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//Script to pull out more details for all credentials against a particular top level job folder name directly
//===========================================================================================================
//URL - https://javadoc.jenkins.io/plugin/cloudbees-folder/com/cloudbees/hudson/plugins/folder/properties/FolderCredentialsProvider.FolderCredentialsProperty.html#getDomainCredentials--
//URL - https://javadoc.jenkins.io/plugin/credentials/com/cloudbees/plugins/credentials/domains/DomainCredentials.html
//URL - https://javadoc.jenkins.io/plugin/credentials/com/cloudbees/plugins/credentials/domains/Domain.html 
println "=================================================="
def folderName="MarketingBusinessSolutionsDevOps" // change value `folder-a` for the full name of the folder you want to disable all jobs in

import com.cloudbees.hudson.plugins.folder.AbstractFolder;

def folder = Jenkins.getInstance().getItemByFullName(folderName);

def property = folder.getProperties().get(com.cloudbees.hudson.plugins.folder.properties.FolderCredentialsProvider.FolderCredentialsProperty.class)
property.getDomainCredentials().eachWithIndex { itDmnCreds, iDmnCreds -> 
  //com.cloudbees.plugins.credentials.domains.DomainCredentials
  //println "$i, $it.domain.name"
  println "$iDmnCreds, $itDmnCreds"
  def domain = itDmnCreds.domain;
  domain.eachWithIndex {itDmn, iDmn ->
    println "$iDmn, Name=${itDmn.name}, Desc=${itDmn.description}, URL=${itDmn.url}, Global=${itDmn.global}";
  }
  println "^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^";
  def domainCreds = itDmnCreds.credentials;
  domainCreds.eachWithIndex { it, i ->
    //println "$i, $it";
    if (it instanceof com.cloudbees.plugins.credentials.impl.UsernamePasswordCredentialsImpl) {
    //println "credentials: -> ${it} \t XXX: username: ${it.username} password: ${it.password} description: ${it.description}"
        println "${i}, Username-Password-Credentials, id=${it.username}, desc=${it.description}, pass=${it.password}"
        //println "=================================================="
    }
    else if(it instanceof com.cloudbees.jenkins.plugins.sshcredentials.impl.BasicSSHUserPrivateKey) {
        //println("credentials: -> ${it} \t" + String.format("id=%s  desc=%s key=%s\n", it.id, it.description, it.privateKeySource.getPrivateKeys()))
        println("${i}, Basic-SSH-User-Private-Key, " + String.format("id=%s, desc=%s, key=%s", it.id, it.description, it.privateKeySource.getPrivateKeys()))
        //println "=================================================="
    }
    else if(it instanceof org.jenkinsci.plugins.plaincredentials.impl.StringCredentialsImpl) {
        //println("credentials: -> ${it} \t" + String.format("id=%s  desc=%s pass=%sn", it.id, it.description, it.secret))
        println("${i}, String-Credentials, " + String.format("id=%s, desc=%s, pass=%s", it.id, it.description, it.secret))
        //println "=================================================="
    }
    else if(it instanceof com.cloudbees.plugins.credentials.SystemCredentialsProvider) {
        println "++++++++++++++++++++++++++++++++++++++++++++++++++"
        //println "credentials: -> ${it} \t ${it.getConfigFile().asString()}"
        println "${i}, System-Credentials-Provider, ${it.getConfigFile().asString()}"
        def lstCredentials = it.getCredentials(); //Get all the (Domain.global()) credentials.
        lstCredentials.eachWithIndex{ itCred, iCred -> 
            println "\t\t ${iCred}, Systems-Credentials-Global-List, Scope=${itCred.scope}, Descriptor=${itCred.descriptor}"
        }
        println "=================================================="
    }
    else if(it instanceof com.cloudbees.plugins.credentials.impl.CertificateCredentialsImpl) {
        println "++++++++++++++++++++++++++++++++++++++++++++++++++"
        //println("credentials: -> ${it} \t" + String.format("id=%s  desc=%s pass=%sn", it.id, it.description, it.secret))
        println("${i}, Certificate-Credentials, " + String.format("id=%s, desc=%s, pass=%s, Src=%s, KeyStore=%s", it.id, it.description, it.password, it.keyStoreSource, it.keyStore))
        println "=================================================="
    }
    else if(it instanceof org.jenkinsci.plugins.plaincredentials.impl.FileCredentialsImpl) {
        //println("credentials: -> ${it} \t" + String.format("id=%s  desc=%s pass(bytes)=%sn FileName=%s", it.id, it.description, it.secretBytes, it.fileName))
        println("${i}, File-Credentials, " + String.format("id=%s, desc=%s, pass(bytes)=%s, FileName=%s", it.id, it.description, it.secretBytes, it.fileName))
        //println "=================================================="
    }
    else
    {
        println "${i} - Unknown type - ${it}"
        println "=================================================="
    }
  }
  //do something with it.credentials
}

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//Description: List ID and Description of all credentials on a Jenkins Instance.
//===========================================================================================================

/**
URL - https://raw.githubusercontent.com/cloudbees/jenkins-scripts/master/list-credential.groovy
**/
import com.cloudbees.plugins.credentials.Credentials

Set<Credentials> allCredentials = new HashSet<Credentials>();

def creds = com.cloudbees.plugins.credentials.CredentialsProvider.lookupCredentials(
      com.cloudbees.plugins.credentials.Credentials.class
);

allCredentials.addAll(creds)

Jenkins.instance.getAllItems(com.cloudbees.hudson.plugins.folder.Folder.class).each{ f ->
 creds = com.cloudbees.plugins.credentials.CredentialsProvider.lookupCredentials(
      com.cloudbees.plugins.credentials.Credentials.class, f)
  allCredentials.addAll(creds)
  
}

for (c in allCredentials) {
   println(c.id + ": " + c.description)
}
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//Iterate and decrypt credentials from the console
//===========================================================================================================

/**
URL - https://codurance.com/2019/05/30/accessing-and-dumping-jenkins-credentials/
**/
def creds = com.cloudbees.plugins.credentials.CredentialsProvider.lookupCredentials(
    com.cloudbees.plugins.credentials.common.StandardUsernameCredentials.class,
    Jenkins.instance,
    null,
    null
)

for(c in creds) {
  if(c instanceof com.cloudbees.jenkins.plugins.sshcredentials.impl.BasicSSHUserPrivateKey){
    println(String.format("id=%s  desc=%s key=%s\n", c.id, c.description, c.privateKeySource.getPrivateKeys()))
  }
  if (c instanceof com.cloudbees.plugins.credentials.impl.UsernamePasswordCredentialsImpl){
    println(String.format("id=%s  desc=%s user=%s pass=%s\n", c.id, c.description, c.username, c.password))
  }
}
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////