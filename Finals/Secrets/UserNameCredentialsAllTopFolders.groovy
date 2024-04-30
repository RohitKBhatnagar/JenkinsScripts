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

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

//Generic Groovy Script

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


//////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////
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
    //println (creds);
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
  for (fc in folderCredentials) 
  {
    if ( fc.descriptor.displayName == 'Username with password')
        {
            ////if(c.username.startsWith('StlCD'))
            if(fc.id.endsWith('BB'))
                println "${iTCount}, ${++iFCount}, ${folderName.getFullDisplayName()}, ${fc.id}, ${fc.username}, ${fc.password}" //, ${c.description}"
        }
  }
}
return null

/////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////

///___________ Script executed on target controller _____________________

import jenkins.model.*
import com.cloudbees.hudson.plugins.folder.*
import com.cloudbees.hudson.plugins.folder.properties.*
import com.cloudbees.hudson.plugins.folder.properties.FolderCredentialsProvider.FolderCredentialsProperty
import com.cloudbees.plugins.credentials.impl.*
import com.cloudbees.plugins.credentials.*
import com.cloudbees.plugins.credentials.domains.*
import com.cloudbees.plugins.credentials.impl.UsernamePasswordCredentialsImpl;

jenkins = Jenkins.instance

String strSecret = "MDQwMDQ2OTE3NzU2OtF3dxXFd9ySfkmCyvqmY/p+17/R";
Credentials c = new UsernamePasswordCredentialsImpl(CredentialsScope.GLOBAL, "BT-JNK-BB", "Groovy BB scripter update via build#971 for 'dc883996-cdaf-3b49-ab5f-035c1b433b08' copied on Aug 28, 2021 3:47:43 PM", "StlCDPrdJnkBB", "MDQwMDQ2OTE3NzU2OtF3dxXFd9ySfkmCyvqmY/p+17/R")

for (folder in jenkins.getAllItems(Folder.class)) {
//for (folder in jenkins.getItems(Folder.class)) {
    if(folder.name.equals('Jenkins Platform Team') || folder.displayName.equals('Jenkins Platform Team')) {
        AbstractFolder<?> folderAbs = AbstractFolder.class.cast(folder)
        FolderCredentialsProperty property = folderAbs.getProperties().get(FolderCredentialsProperty.class);
        if (property) 
        {
          def dmnCred = property.getStore().getCredentials(Domain.global());
          if(dmnCred.size() > 0)
          {
            dmnCred.each {  it ->
                if(it instanceof com.cloudbees.plugins.credentials.impl.UsernamePasswordCredentialsImpl)
                {
                  def usrName = it.username;
                  def dmnSecret = it.password

                  if(usrName.equals('StlCDPrdJnkBB') || usrName.equals('StlCD2PrdJnkBB') || usrName.equals
                  ('StlCD3PrdJnkBB') || usrName.equals('StlCD4PrdJnkBB') || usrName.equals('StlCD5PrdJnkBB'))
                  {
                    if((String)dmnSecret == strSecret)
                    {
                        //Do Nothing!
                    }
                    else
                    {
                        if(property.getStore().removeCredentials(Domain.global(), c))
                            property.getStore().addCredentials(Domain.global(), c);
                        else
                            property.getStore().addCredentials(Domain.global(), c);
                    }
                  }
                  else 
                    property.getStore().addCredentials(Domain.global(), c);
                }
                else
                    property.getStore().addCredentials(Domain.global(), c);
            }
          }
          else
            property.getStore().addCredentials(Domain.global(), c);
        }
        else 
        {
            property = new FolderCredentialsProperty([c]);
            folderAbs.addProperty(property)
        }
      println "Credentials updated for 'Jenkins Platform Team'"
    }
  else
  {
    println "Folder name '${folder.name}' or folder display name '${folder.displayName}' does NOT matches 'Jenkins Platform Team'"
  }
}
