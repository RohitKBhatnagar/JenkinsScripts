//Displays all credentials inside specific top level folder only - Controller Script
import com.cloudbees.plugins.credentials.Credentials;
import com.cloudbees.plugins.credentials.CredentialsProvider;
import com.cloudbees.hudson.plugins.folder.AbstractFolder;

import jenkins.*
import jenkins.model.*
import hudson.*
import hudson.model.*

import hudson.scm.*
import hudson.tasks.*
import com.cloudbees.hudson.plugins.folder.*

def topFolderName="MPSI" //CD13

Set<Credentials> allCredentials = new HashSet<Credentials>()

def creds = com.cloudbees.plugins.credentials.CredentialsProvider.lookupCredentials(com.cloudbees.plugins.credentials.Credentials)

allCredentials.addAll(creds)

jen = Jenkins.instance

jen.getItems().each {
    if(it instanceof Folder)
    {
        if(it.name.equals(topFolderName))
        {
            println "Pulling all credentials including sub folders for ${it.name}"
            creds = com.cloudbees.plugins.credentials.CredentialsProvider.lookupCredentials(com.cloudbees.plugins.credentials.Credentials, it)
            allCredentials.addAll(creds)
            processFolder(it, allCredentials)
        }
    }
}

int i = 0
for (c in allCredentials) {
  i++
    //if(c.id == 'mc_mpsi_bizops_usr')
    //{
        //println(c.id)
        //println(c)
        //Find fingerprints for the credential id passed
        /*fp = CredentialsProvider.getFingerprintOf(c)
        if (fp) { 
            fp_str = "Fingerprinted jobs found as : " + fp.getJobs()
        } else {
            fp_str = "(No Fingerprints found)"
        }*/
        /////////////////////////////////////
        //if (c.properties.username) {
//            println(' description: ' + c.description)
        //}
        if (c.properties.username) {
            //println(' username: ' + c.username)
            //println(" Fingerprint for " + c.id + " - " + c.username + " : " + c.description  + " | " + fp_str)
          println(i + ": ID: " + c.id + " - UserName : " + c.username + " : Description: " + c.description)
        }
        if (c.properties.password) {
            println('\t password: ' + c.password)
        }
        if (c.properties.passphrase) {
            println('\t passphrase: ' + c.passphrase)
        }
        if (c.properties.secret) {
            println('\t secret: ' + c.secret)
        }
        if (c.properties.privateKeySource) {
            println('\t privateKey: ' + c.getPrivateKey())
        }
    //}
    //println('')
}

void processFolder(Item folder, HashSet<Credentials> allCredentials)
{
    folder.getItems().each {
        if(it instanceof Folder)
        {
            //println "Sub Folder - ${it.name}"
            creds = com.cloudbees.plugins.credentials.CredentialsProvider.lookupCredentials(com.cloudbees.plugins.credentials.Credentials, it)
            allCredentials.addAll(creds)
            processFolder(it, allCredentials)
        }
    }
}
///////////////////////////////////////////////////////////////////////
import java.text.SimpleDateFormat

def folderName="MPSI"; //CD13 //"PrepaidManagementServicesBizOps" // change value with the full name of the folder you want to look for members and roles

def sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss.SSS")
def startDate = new Date()
println "Start time : ${sdf.format(startDate)}"
//-------------------------------------------------

String strMaster = java.net.InetAddress.getLocalHost();
println "================================================================================================================"
println "Displays all members behind a top level folder amd associated groups and roles and then decrypts their passwords"
println ">>>>> CD Master - ${strMaster} and the TOP LEVEL Folder Name - ${folderName} <<<<<<<<<<<<<<<<<<<<"
println "================================================================================================================"



import com.cloudbees.hudson.plugins.folder.AbstractFolder;

def folder = Jenkins.getInstance().getItemByFullName(folderName);

def property = folder.getProperties().get(com.cloudbees.hudson.plugins.folder.properties.FolderCredentialsProvider.FolderCredentialsProperty.class)
println "${property}"
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
		//println "${i}, Username-Password-Credentials, id=${it.username}, desc=${it.description}, pass=${it.password}, decrypted=${hudson.util.Secret.decrypt(it.password)}"
		//println "${i}, Username-Password-Credentials, id=${it.username}, desc=${it.description}, pass=${it.password}, ${it.password.getPlainText()}, decrypted=${hudson.util.Secret.decrypt(it.password.getPlainText())}"
		//println "${i}, Username-Password-Credentials, id=${it.username}, desc=${it.description}, pass=${it.password}, decrypted=${hudson.util.Secret.fromString(it.password).getPlainText()}"
		
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

println "=================================================="
def endDate = new Date()
println "End time: ${sdf.format(endDate)}"

use(groovy.time.TimeCategory) 
{
    def duration = endDate - startDate
  	print "Elapsed time: Days: ${duration.days}, Hours: ${duration.hours}, Minutes: ${duration.minutes}, Seconds: ${duration.seconds}"
}

return null;
