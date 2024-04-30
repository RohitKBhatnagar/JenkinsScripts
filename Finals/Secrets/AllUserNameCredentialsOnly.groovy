//Displays only username credentials for global and each folder based upon a condition.
//Script will do a CSV output displaying folder/sub-folder information
import com.cloudbees.plugins.credentials.Credentials
import com.cloudbees.hudson.plugins.folder.AbstractFolder;

println "Top-Folder#, Credential#, TopFolderName, CredentialID, UserName, Password, Description"
println "======================================================================================"

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
            println "${c.id}, ${c.descriptor.displayName}, ${c.username}, ${c.password}, ${c.description}"
    }
}
/*********** GLOBAL ***********************/

println('')
println('')
println('------ TOP-LEVEL-FOLDERS (Username with password) --------')

int iTopCount = 0, iFolderCount = 0
def topItems = Jenkins.instance.getAllItems(com.cloudbees.hudson.plugins.folder.Folder)
//def topItems = Jenkins.instance.getItems(com.cloudbees.hudson.plugins.folder.Folder)
topItems.each 
{
    f ->
  	//println "${f}"
    allCredentials.clear()
    //Set<Credentials> folderCredentials = new HashSet<Credentials>()
    creds = com.cloudbees.plugins.credentials.CredentialsProvider.lookupCredentials(com.cloudbees.plugins.credentials.Credentials, f)
    //println (creds);
    allCredentials.addAll(creds)
    ++iTopCount
    iFolderCount = 0
    //displayCreds(f.name, allCredentials, iTopCount, iFolderCount);
  	displayCreds(f, allCredentials, iTopCount, iFolderCount);
}

//void displayCreds(String folderName, HashSet<Credentials> folderCredentials, int iTCount, int iFCount)
void displayCreds(def folderName, HashSet<Credentials> folderCredentials, int iTCount, int iFCount)
{
    for (c in folderCredentials) 
    {
        if ( c.descriptor.displayName == 'Username with password')
        {
            ////if(c.username.startsWith('StlCD'))
            if(c.id.endsWith('BB'))
                println "${iTCount}, ${++iFCount}, ${folderName.getFullDisplayName()}, ${c.id}, ${c.username}, ${c.password}, ${c.description}"
        }
    }
}
return null