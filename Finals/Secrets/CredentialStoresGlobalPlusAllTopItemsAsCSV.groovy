import java.text.SimpleDateFormat

def sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss.SSS")
def startDate = new Date()
println "Start time : ${sdf.format(startDate)}"
//-------------------------------------------------

String strMaster = java.net.InetAddress.getLocalHost();
println "================================================================"
println "Displays all Credential Stores entries for all top level folders"
println ">>>>> CD Master - ${strMaster} <<<<<<<<<<<<<<<<<<<<"
println "================================================================"

//Displays all credentials global and inside each top level folder
import com.cloudbees.plugins.credentials.Credentials
import com.cloudbees.hudson.plugins.folder.AbstractFolder;

Set<Credentials> allCredentials = new HashSet<Credentials>()

def creds = com.cloudbees.plugins.credentials.CredentialsProvider.lookupCredentials(com.cloudbees.plugins.credentials.Credentials)

allCredentials.addAll(creds)

/***********/
println "------ GLOBAL --------"
println('')
for (c in allCredentials) 
{
	if(c.descriptor.displayName == 'Secret text')
		println "'${c.id}', '${c.descriptor.displayName}', '${c.description}'"
	else if ( c.descriptor.displayName == 'Username with password')
		println "'${c.id}', '${c.descriptor.displayName}', '${c.username}', '${c.description}'"
	else if ( c.descriptor.displayName == 'Secret file')
		println "'${c.id}', '${c.descriptor.displayName}', '${c.fileName}', '${c.description}'"
	else if ( c.descriptor.displayName == 'Certificate')
		println "'${c.id}', '${c.descriptor.displayName}', '${c.keyStore}', '${c.description}'"
	else if ( c.descriptor.displayName == 'SSH Username with private key')
		println "'${c.id}', '${c.descriptor.displayName}', '${c.username}', '${c.description}'"
	else
		println("======= CredentialID - '${c.id}', Scope - ${c.scope}, Descriptor - '${c.descriptor.displayName}', Type - ${c}")
}
println('')
println('------ Top Folder --------')
println('')
def topFolderName="AccessManagement" //CD5 Folder

//def topItems = Jenkins.instance.getItemByFullName(topFolderName, AbstractFolder)
def topItems = Jenkins.instance.getItems(com.cloudbees.hudson.plugins.folder.Folder)
topItems.each 
{
    f ->
    Set<Credentials> folderCredentials = new HashSet<Credentials>()
    folderCredentials = com.cloudbees.plugins.credentials.CredentialsProvider.lookupCredentials(com.cloudbees.plugins.credentials.Credentials, f)

    for (c in folderCredentials) 
    {
	//println("CredentialID - '${c.id}', Scope - ${c.scope}, Descriptor - '${c.descriptor.displayName}', Type - ${c}")
	if(c.scope == null)
	{
	   if(c.descriptor.displayName == 'Secret text')
		println "'${f.name}', '${c.id}', '${c.descriptor.displayName}', '${c.description}'"
	   else if ( c.descriptor.displayName == 'Username with password')
		println "'${f.name}', '${c.id}', '${c.descriptor.displayName}', '${c.username}', '${c.description}'"
	   else if ( c.descriptor.displayName == 'Secret file')
		println "'${f.name}', '${c.id}', '${c.descriptor.displayName}', '${c.fileName}', '${c.description}'"
	   else if ( c.descriptor.displayName == 'Certificate')
		println "'${f.name}', '${c.id}', '${c.descriptor.displayName}', '${c.keyStore}', '${c.description}'"
	   else if ( c.descriptor.displayName == 'SSH Username with private key')
		println "'${f.name}', '${c.id}', '${c.descriptor.displayName}', '${c.username}', '${c.description}'"
	   else
		println("======= '${f.name}', CredentialID - '${c.id}', Scope - '${c.scope}', Descriptor - '${c.descriptor.displayName}', Type - '${c}'")
	}
    }
}
println('')

//--------------------------------------------------
def endDate = new Date()
println "End time: ${sdf.format(endDate)}"

use(groovy.time.TimeCategory) 
{
    def duration = endDate - startDate
      print "Elapsed time: Days: ${duration.days}, Hours: ${duration.hours}, Minutes: ${duration.minutes}, Seconds: ${duration.seconds}"
}

return null;

