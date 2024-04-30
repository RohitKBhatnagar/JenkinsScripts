//Displays only username credentials for global and each top level folders based upon a condition
import com.cloudbees.plugins.credentials.Credentials
import com.cloudbees.hudson.plugins.folder.AbstractFolder;

Set<Credentials> allCredentials = new HashSet<Credentials>()

def creds = com.cloudbees.plugins.credentials.CredentialsProvider.lookupCredentials(com.cloudbees.plugins.credentials.Credentials)

allCredentials.addAll(creds)

/*********** GLOBAL ***********************/
println "------ GLOBAL (Username with password) --------"
for (c in allCredentials) 
{
    if ( c.descriptor.displayName == 'Username with password')
    {
        if(c.username.startsWith('StlCD'))
            println "${c.id}, ${c.descriptor.displayName}, ${c.username}, ${c.password}, ${c.description}"
    }
}
/*********** GLOBAL ***********************/

println('')
println('')
println('------ TOP-LEVEL-FOLDERS (Username with password) --------')

int iTopCount = 0, iFolderCount = 0
//def topItems = Jenkins.instance.getItemByFullName(topFolderName, AbstractFolder)
def topItems = Jenkins.instance.getItems(com.cloudbees.hudson.plugins.folder.Folder)
topItems.each 
{
    f ->
    Set<Credentials> folderCredentials = new HashSet<Credentials>()
    folderCredentials = com.cloudbees.plugins.credentials.CredentialsProvider.lookupCredentials(com.cloudbees.plugins.credentials.Credentials, f)
    ++iTopCount
    iFolderCount = 0
    for (c in folderCredentials) 
    {
        if(c.scope == null)
        {
            if ( c.descriptor.displayName == 'Username with password')
            {
                if(c.username.startsWith('StlCD'))
                    println "${iTopCount}, ${++iFolderCount}, ${f.name}, ${c.id}, ${c.username}, ${c.password}, ${c.description}"
            }
        }
    }
}
return null


/////////////////////////////////////////

//Displays all credentials global and inside each top level folder ending with 'BB' [Judt grouped all together with NO folder information]
import com.cloudbees.plugins.credentials.Credentials

Set<Credentials> allCredentials = new HashSet<Credentials>()

def creds = com.cloudbees.plugins.credentials.CredentialsProvider.lookupCredentials(com.cloudbees.plugins.credentials.Credentials)

allCredentials.addAll(creds)

Jenkins.instance.getAllItems(com.cloudbees.hudson.plugins.folder.Folder).each { 
    f ->
    println "------------ $f ----------"
    creds = com.cloudbees.plugins.credentials.CredentialsProvider.lookupCredentials(com.cloudbees.plugins.credentials.Credentials, f)
    allCredentials.addAll(creds)
}

for (c in allCredentials) {
  if(c.id.endsWith('BB'))
  {
    println(c.id)
    if (c.properties.username) {
        println(' description: ' + c.description)
    }
    if (c.properties.username) {
        println(' username: ' + c.username)
    }
    if (c.properties.password) {
        println(' password: ' + c.password)
    }
    if (c.properties.passphrase) {
        println(' passphrase: ' + c.passphrase)
    }
    if (c.properties.secret) {
        println(' secret: ' + c.secret)
    }
    if (c.properties.privateKeySource) {
        println(' privateKey: ' + c.getPrivateKey())
    }
    println('')
  }
}


///////////////////////////////////////////////////

//Displays only username credentials for global and each top level folders based upon a condition displaying folder name as well in CSV
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
//def topItems = Jenkins.instance.getItemByFullName(topFolderName, AbstractFolder)
def topItems = Jenkins.instance.getAllItems(com.cloudbees.hudson.plugins.folder.Folder)
topItems.each 
{
    f ->
    allCredentials.clear()
    //Set<Credentials> folderCredentials = new HashSet<Credentials>()
    creds = com.cloudbees.plugins.credentials.CredentialsProvider.lookupCredentials(com.cloudbees.plugins.credentials.Credentials, f)
    //println (creds);
    allCredentials.addAll(creds)
    ++iTopCount
    iFolderCount = 0
    displayCreds(f.name, allCredentials, iTopCount, iFolderCount);
}

void displayCreds(String folderName, HashSet<Credentials> folderCredentials, int iTCount, int iFCount)
{
    for (c in folderCredentials) 
    {
        if ( c.descriptor.displayName == 'Username with password')
        {
            ////if(c.username.startsWith('StlCD'))
            if(c.id.endsWith('BB'))
                println "${iTCount}, ${++iFCount}, ${folderName}, ${c.id}, ${c.username}, ${c.password}, ${c.description}"
        }
    }
}
return null

//////////////////////////////////////////////////////
//-- FINAL ------------------------

//Displays only username credentials for all folders based upon a condition including folder information
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