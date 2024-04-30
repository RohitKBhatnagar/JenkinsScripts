//https://confluence.mastercard.int/display/DEP/Jenkins+Platform+Expansion+STL-2020+VM%27s+Check+List

//Credentials in all folders only [This will not go inside individual credentials folder]
import java.text.SimpleDateFormat

//def folderName="JenkinsPlatformTeam"; //"PrepaidManagementServicesBizOps" // change value with the full name of the folder you want to look for members and roles

def sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss.SSS")
def startDate = new Date()
println "Start time : ${sdf.format(startDate)}"
//-------------------------------------------------

String strMaster = java.net.InetAddress.getLocalHost();
println "================================================================================================================"
println "Displays all members behind a top level folder amd associated groups and roles and then decrypts their passwords"
println ">>>>> CD Controller - ${strMaster} <<<<<<<<<<<<<<<<<<<<"
//println ">>>>> CD Master - ${strMaster} and the TOP LEVEL Folder Name - ${folderName} <<<<<<<<<<<<<<<<<<<<"
println "================================================================================================================"


import com.cloudbees.plugins.credentials.Credentials
import com.cloudbees.hudson.plugins.folder.AbstractFolder;

println "#, Top-Folder#, CredentialType#, Credential#, TopFolderName, CredentialID, UserName, Password, Description"
println "======================================================================================================="

Set<Credentials> allCredentials = new HashSet<Credentials>()

def creds = com.cloudbees.plugins.credentials.CredentialsProvider.lookupCredentials(com.cloudbees.plugins.credentials.Credentials)

allCredentials.addAll(creds)

/*********** GLOBAL ***********************/
println "------ GLOBAL (Username with password) --------"
for (c in allCredentials) 
{
    if ( c.descriptor.displayName == 'Username with password')
    {
        //if(c.username.startsWith('StlCD'))
            println "${c.id}, ${c.descriptor.displayName}, ${c.username}, ${c.password}" //, ${c.description}"
    }
}
/*********** GLOBAL ***********************/

println('')
println('')
println('------ TOP-LEVEL-FOLDERS (Username with password) --------')

//def folder = Jenkins.getInstance().getItemByFullName(folderName);
//def topItems = Jenkins.getInstance().getItemByFullName(folderName, AbstractFolder).getAllItems();
def topItems = Jenkins.getInstance().getAllItems(AbstractFolder);

int iAllCount = 0;

topItems.eachWithIndex 
{
	folder, fldrCount ->
	try {
		def property = folder.getProperties().get(com.cloudbees.hudson.plugins.folder.properties.FolderCredentialsProvider.FolderCredentialsProperty.class)
		//println "${property}" //https://javadoc.jenkins.io/plugin/cloudbees-folder/com/cloudbees/hudson/plugins/folder/properties/FolderCredentialsProvider.FolderCredentialsProperty.html
		if(property)
		{
			property.getCredentials().eachWithIndex { itCreds, iCreds ->   //com.cloudbees.plugins.credentials.Credentials
			  //println "$iCreds, $itCreds"
			  itCreds.eachWithIndex { it, i ->
			  	//println "$i, $it"
			  	if (it instanceof com.cloudbees.plugins.credentials.impl.UsernamePasswordCredentialsImpl) {
			    	if(it.id.endsWith('BB'))
			    		println "${++iAllCount}, ${fldrCount}, ${iCreds}, ${i}, ${folder.fullDisplayName}, ${it.id}, ${it.username}, ${it.password}" //, desc=${it.description}, "
			      		//println "${iCreds}::${i}, Folder=${folder.fullDisplayName}, ID=${it.id}, UserName=${it.username}, pass=${it.password}}" //, desc=${it.description}, "
			        //println "=================================================="
			    }
                /*******************
			    else if(it instanceof com.cloudbees.jenkins.plugins.sshcredentials.impl.BasicSSHUserPrivateKey) {
			        println("${iCreds}::${i}, Basic-SSH-User-Private-Key, Folder=${folder.getFullDisplayName()}, ID=${it.id}, " + String.format("id=%s, desc=%s, key=%s", it.id, it.description, it.privateKeySource.getPrivateKeys()))
			        //println "=================================================="
			    }
			    else if(it instanceof org.jenkinsci.plugins.plaincredentials.impl.StringCredentialsImpl) {
			        //println("credentials: -> ${it} \t" + String.format("id=%s  desc=%s pass=%sn", it.id, it.description, it.secret))
			        println("${iCreds}::${i}, String-Credentials, Folder=${folder.getFullDisplayName()}, ID=${it.id}, " + String.format("id=%s, desc=%s, pass=%s", it.id, it.description, it.secret))
			        //println "=================================================="
			    }
			    else if(it instanceof com.cloudbees.plugins.credentials.SystemCredentialsProvider) {
			        println "++++++++++++++++++++++++++++++++++++++++++++++++++"
			        //println "credentials: -> ${it} \t ${it.getConfigFile().asString()}"
			        println "${iCreds}::${i}, System-Credentials-Provider, Folder=${folder.getFullDisplayName()}, ID=${it.id}, ${it.getConfigFile().asString()}"
			        def lstCredentials = it.getCredentials(); //Get all the (Domain.global()) credentials.
			        lstCredentials.eachWithIndex{ itCred, iCred -> 
			            println "\t\t ${iCred}::${i}:${iCred}, Systems-Credentials-Global-List, Scope=${itCred.scope}, Descriptor=${itCred.descriptor}"
			        }
			        println "=================================================="
			    }
			    else if(it instanceof com.cloudbees.plugins.credentials.impl.CertificateCredentialsImpl) {
			        println "++++++++++++++++++++++++++++++++++++++++++++++++++"
			        //println("credentials: -> ${it} \t" + String.format("id=%s  desc=%s pass=%sn", it.id, it.description, it.secret))
			        println("${iCreds}::${i}, Certificate-Credentials, Folder=${folder.getFullDisplayName()}, ID=${it.id}, " + String.format("id=%s, desc=%s, pass=%s, Src=%s, KeyStore=%s", it.id, it.description, it.password, it.keyStoreSource, it.keyStore))
			        println "=================================================="
			    }
			    else if(it instanceof org.jenkinsci.plugins.plaincredentials.impl.FileCredentialsImpl) {
			        //println("credentials: -> ${it} \t" + String.format("id=%s  desc=%s pass(bytes)=%sn FileName=%s", it.id, it.description, it.secretBytes, it.fileName))
			        println("${iCreds}::${i}, File-Credentials, Folder=${folder.getFullDisplayName()}, ID=${it.id}, " + String.format("id=%s, desc=%s, pass(bytes)=%s, FileName=%s", it.id, it.description, it.secretBytes, it.fileName))
			        //println "=================================================="
			    }
			    else
			    {
			        println "${iCreds}::${i} - Folder=${folder.getFullDisplayName()}, Unknown type - ${it}"
			        println "=================================================="
			    }
				***************/
			  }
			  //do something with it.credentials
			}
		}
	}
	catch(Exception e) {
		println "${e.message}"
	}
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


///////////////////////////////////////////////////////////////////////////////////////////////