//Displays all credentials inside specific top level folder and sub folders only
import com.cloudbees.plugins.credentials.Credentials
import com.cloudbees.hudson.plugins.folder.AbstractFolder;
import com.cloudbees.hudson.plugins.folder.*

def topFdrName = "stlcd"
def credId = "BT-JNK-BB"//"AUTH-SMA-BB"
DisplayCredentials(topFdrName, credId)

//Process top level folders to display credentials
Map DisplayCredentials(String topFolderName, String CredenId)
{
    println "Searching for credentials '${CredenId}' in folder '${topFolderName}'"
    def myRetMap = [:]
    Set<Credentials> allCredentials = new HashSet<Credentials>()
    def creds = com.cloudbees.plugins.credentials.CredentialsProvider.lookupCredentials(com.cloudbees.plugins.credentials.Credentials)
    allCredentials.addAll(creds)

    jen = Jenkins.instance

    //jen.getItems().each {
    jen.getItem(topFolderName).each {
        if(it instanceof Folder)
        {
            if(it.name.equals(topFolderName))
            {
                //println "Pulling all credentials including sub folders for ${it.name}"
                creds = com.cloudbees.plugins.credentials.CredentialsProvider.lookupCredentials(com.cloudbees.plugins.credentials.Credentials, it)
                allCredentials.addAll(creds)
                processFolder(it, allCredentials)
            }
        }
    }

    //println allCredentials

    def lstName  = new ArrayList<String>()
    def lstPwd = new ArrayList<String>();
    for (c in allCredentials) {
        if(c.id.equals(CredenId))
        {
            //println "Found ${CredenId}"
            //println(c.id)
            if (c.properties.username) {
                //println(' description: ' + c.description)
            }
            if (c.properties.username) {
                //println(' username: ' + c.username)
                lstName.add(c.username)
            }
            if (c.properties.password) {
                //println(' password: ' + c.password)
                lstPwd.add(c.password)
            }
            if (c.properties.passphrase) {
                //println(' passphrase: ' + c.passphrase)
            }
            if (c.properties.secret) {
                //println(' secret: ' + c.secret)
            }
            if (c.properties.privateKeySource) {
                //println(' privateKey: ' + c.getPrivateKey())
            }
            
            println('')
        }
    }

    //Transpose the list and put the values to the map
    if(lstName.size() > 0)
    {
        [lstName, lstPwd].transpose().each { myName, myPwd ->
            println myName + " : " + myPwd
            myRetMap.put(myName, myPwd)
        }
    }

    //println "Map elements - ${myRetMap}"
    return myRetMap;
}

//Process any sub-folders
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
