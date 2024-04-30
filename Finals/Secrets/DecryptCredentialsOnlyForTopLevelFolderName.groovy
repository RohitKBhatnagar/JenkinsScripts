import java.text.SimpleDateFormat

def folderName="MarketingBusinessSolutionsDevOps"; //"PrepaidManagementServicesBizOps" // change value with the full name of the folder you want to look for members and roles

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
//println "${property}" //https://javadoc.jenkins.io/plugin/cloudbees-folder/com/cloudbees/hudson/plugins/folder/properties/FolderCredentialsProvider.FolderCredentialsProperty.html
property.getCredentials().eachWithIndex { itCreds, iCreds ->   //com.cloudbees.plugins.credentials.Credentials
  //println "iCreds, itCreds"
  itCreds.eachWithIndex { it, i ->
    //println "$i, $it";
    if (it instanceof com.cloudbees.plugins.credentials.impl.UsernamePasswordCredentialsImpl) {
    //println "credentials: -> ${it} \t XXX: username: ${it.username} password: ${it.password} description: ${it.description}"
        println "${iCreds}::${i}, Username-Password-Credentials, id=${it.username}, desc=${it.description}, pass=${it.password}"
        //println "${i}, Username-Password-Credentials, id=${it.username}, desc=${it.description}, pass=${it.password}, decrypted=${hudson.util.Secret.decrypt(it.password)}"
        //println "${i}, Username-Password-Credentials, id=${it.username}, desc=${it.description}, pass=${it.password}, ${it.password.getPlainText()}, decrypted=${hudson.util.Secret.decrypt(it.password.getPlainText())}"
        //println "${i}, Username-Password-Credentials, id=${it.username}, desc=${it.description}, pass=${it.password}, decrypted=${hudson.util.Secret.fromString(it.password).getPlainText()}"
        
        //println "=================================================="
    }
    else if(it instanceof com.cloudbees.jenkins.plugins.sshcredentials.impl.BasicSSHUserPrivateKey) {
        //println("credentials: -> ${it} \t" + String.format("id=%s  desc=%s key=%s\n", it.id, it.description, it.privateKeySource.getPrivateKeys()))
        println("${iCreds}::${i}, Basic-SSH-User-Private-Key, " + String.format("id=%s, desc=%s, key=%s", it.id, it.description, it.privateKeySource.getPrivateKeys()))
        //println "=================================================="
    }
    else if(it instanceof org.jenkinsci.plugins.plaincredentials.impl.StringCredentialsImpl) {
        //println("credentials: -> ${it} \t" + String.format("id=%s  desc=%s pass=%sn", it.id, it.description, it.secret))
        println("${iCreds}::${i}, String-Credentials, " + String.format("id=%s, desc=%s, pass=%s", it.id, it.description, it.secret))
        //println "=================================================="
    }
    else if(it instanceof com.cloudbees.plugins.credentials.SystemCredentialsProvider) {
        println "++++++++++++++++++++++++++++++++++++++++++++++++++"
        //println "credentials: -> ${it} \t ${it.getConfigFile().asString()}"
        println "${iCreds}::${i}, System-Credentials-Provider, ${it.getConfigFile().asString()}"
        def lstCredentials = it.getCredentials(); //Get all the (Domain.global()) credentials.
        lstCredentials.eachWithIndex{ itCred, iCred -> 
            println "\t\t ${iCred}::${i}:${iCred}, Systems-Credentials-Global-List, Scope=${itCred.scope}, Descriptor=${itCred.descriptor}"
        }
        println "=================================================="
    }
    else if(it instanceof com.cloudbees.plugins.credentials.impl.CertificateCredentialsImpl) {
        println "++++++++++++++++++++++++++++++++++++++++++++++++++"
        //println("credentials: -> ${it} \t" + String.format("id=%s  desc=%s pass=%sn", it.id, it.description, it.secret))
        println("${iCreds}::${i}, Certificate-Credentials, " + String.format("id=%s, desc=%s, pass=%s, Src=%s, KeyStore=%s", it.id, it.description, it.password, it.keyStoreSource, it.keyStore))
        println "=================================================="
    }
    else if(it instanceof org.jenkinsci.plugins.plaincredentials.impl.FileCredentialsImpl) {
        //println("credentials: -> ${it} \t" + String.format("id=%s  desc=%s pass(bytes)=%sn FileName=%s", it.id, it.description, it.secretBytes, it.fileName))
        println("${iCreds}::${i}, File-Credentials, " + String.format("id=%s, desc=%s, pass(bytes)=%s, FileName=%s", it.id, it.description, it.secretBytes, it.fileName))
        //println "=================================================="
    }
    else
    {
        println "${iCreds}::${i} - Unknown type - ${it}"
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
