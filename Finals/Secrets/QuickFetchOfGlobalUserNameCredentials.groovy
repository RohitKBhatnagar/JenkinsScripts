//Displays only username credentials for global and each folder based upon a condition.
//Script will do a CSV output displaying folder/sub-folder information
import com.cloudbees.plugins.credentials.Credentials
import com.cloudbees.hudson.plugins.folder.AbstractFolder;

println "Top-Folder#, Credential#, TopFolderName, CredentialID, UserName, Password, Description"
println "======================================================================================"

Set<Credentials> allCredentials = new HashSet<Credentials>()
Map<String, HashSet<Credentials>> exCreds = new Map<String, HashSet<Credentials>>();

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

int iTopCount = 0, iFolderCount = 0
def topItems = Jenkins.instance.getAllItems(com.cloudbees.hudson.plugins.folder.Folder)
//def topItems = Jenkins.instance.getItems(com.cloudbees.hudson.plugins.folder.Folder)
topItems.each 
{
    f ->
    //println "${f}"
    //allCredentials.clear()
    //Set<Credentials> folderCredentials = new HashSet<Credentials>()
    creds.clear()
    creds = com.cloudbees.plugins.credentials.CredentialsProvider.lookupCredentials(com.cloudbees.plugins.credentials.Credentials, f)
    //println ("${f.getFullDisplayName()}, ${creds.size()}");
    //allCredentials.addAll(creds)
    ++iTopCount
    iFolderCount = 0
    //displayCreds(f.name, allCredentials, iTopCount, iFolderCount);
    //displayCreds(f, allCredentials, iTopCount, iFolderCount);
               
    //println "------------------ $f --------------------"
    displayCredentials(f, creds, iTopCount, iFolderCount);
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
                println "${iTCount}, ${++iFCount}, ${folderName.getFullDisplayName()}, ${c.id}, ${c.username}, ${c.password}" //, ${c.description}"
        }
    }
}

void displayCredentials(def folderName, ArrayList folderCredentials, int iTCount, int iFCount)
{
  println folderCredentials.size()
  for (fc in folderCredentials) 
  {
    try
    {
      if ( fc.descriptor.displayName == 'Username with password')
          {
              ////if(c.username.startsWith('StlCD'))
              if(fc.id.endsWith('BB'))
                exCreds.addAll(folderName.getFullDisplayName(), fc);
                  //println "${iTCount}, ${++iFCount}, ${folderName.getFullDisplayName()}, ${fc.id}, ${fc.username}, ${fc.password}" //, ${c.description}"
          }
    }
    catch(Exception e)
    {
      println "$e.message"
    }
  }
}
return null

//////////////////////////////////////////////

def creds = com.cloudbees.plugins.credentials.CredentialsProvider.lookupCredentials(
        com.cloudbees.plugins.credentials.common.StandardUsernamePasswordCredentials.class,
        jenkins.model.Jenkins.instance)
//println creds

creds.each { it ->
    //println "credentials: -> ${it}"
    if (it instanceof com.cloudbees.plugins.credentials.impl.UsernamePasswordCredentialsImpl) {
        println "XXX: id: ${it.id}, username: ${it.username} password: ${it.password}" // description: ${it.description}"
    }
}

return null


///////////////////////////////////

def credentials_store = jenkins.model.Jenkins.instance.getExtensionList(
        'com.cloudbees.plugins.credentials.SystemCredentialsProvider'
        )

println "credentials_store: ${credentials_store}"
//println " Description: ${credentials_store.description}"
//println " Target: ${credentials_store.target}"
credentials_store.each {  println "credentials_store.each: ${it}" }

credentials_store[0].credentials.each { it ->
    println "credentials: -> ${it}"
    if (it instanceof com.cloudbees.plugins.credentials.impl.UsernamePasswordCredentialsImpl) {
        println "XXX: username: ${it.id} ${it.username} password: ${it.password} description: ${it.description}"
    }
}

return null

////////////////////////////////////////////////////

//Displays only username credentials for global and each folder based upon a condition.
//Script will do a CSV output displaying folder/sub-folder information
import com.cloudbees.plugins.credentials.Credentials
import com.cloudbees.hudson.plugins.folder.AbstractFolder;

println "Top-Folder#, Credential#, TopFolderName, CredentialID, UserName, Password, Description"
println "======================================================================================"

Set<Credentials> allCredentials = new HashSet<Credentials>()
Map<String, String> fldrCredentials; // = new Map<String, String>();

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

int iTopCount = 0, iFolderCount = 0
def topItems = Jenkins.instance.getAllItems(com.cloudbees.hudson.plugins.folder.Folder)
//def topItems = Jenkins.instance.getItems(com.cloudbees.hudson.plugins.folder.Folder)
topItems.each 
{
    f ->
    //println "${f}"
    allCredentials.clear()
    //Set<Credentials> folderCredentials = new HashSet<Credentials>()
    creds.clear()
    creds = com.cloudbees.plugins.credentials.CredentialsProvider.lookupCredentials(com.cloudbees.plugins.credentials.Credentials, f)
    //println ("${f.getFullDisplayName()}, ${creds.size()}");
    allCredentials.addAll(creds)
    ++iTopCount
    iFolderCount = 0
    //displayCreds(f.name, allCredentials, iTopCount, iFolderCount);
    //displayCreds(f, allCredentials, iTopCount, iFolderCount);
               
    //println "------------------ $f --------------------"
    displayCredentials(f, creds, iTopCount, iFolderCount);
    println "$fldrCredentials"
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
                println "${iTCount}, ${++iFCount}, ${folderName.getFullDisplayName()}, ${c.id}, ${c.username}, ${c.password}" //, ${c.description}"
        }
    }
}

void displayCredentials(def folderName, ArrayList folderCredentials, int iTCount, int iFCount)
{
  //println folderCredentials.size()
  for (fc in folderCredentials) 
  {
    try
    {
      if ( fc.descriptor.displayName == 'Username with password')
          {
              ////if(c.username.startsWith('StlCD'))
              if(fc.id.endsWith('BB'))
                fldrCredentials.put(folderName.getFullDisplayName(), "${fc.id}, ${fc.username}, ${fc.password}");
                  //println "${iTCount}, ${++iFCount}, ${folderName.getFullDisplayName()}, ${fc.id}, ${fc.username}, ${fc.password}" //, ${c.description}"
          }
    }
    catch(Exception e)
    {
      println "$e.message"
    }
  }
}
return null