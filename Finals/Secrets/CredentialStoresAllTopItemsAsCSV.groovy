//Displays all credentials global and inside each top level folder
import com.cloudbees.plugins.credentials.Credentials
import com.cloudbees.hudson.plugins.folder.AbstractFolder;

Set<Credentials> allCredentials = new HashSet<Credentials>()

def creds = com.cloudbees.plugins.credentials.CredentialsProvider.lookupCredentials(com.cloudbees.plugins.credentials.Credentials)

allCredentials.addAll(creds)

/***********/
println "------ GLOBAL --------"
for (c in allCredentials) 
{
    //println("======= CredentialID - ${c.id}, Scope - ${c.scope}, Descriptor - ${c.descriptor.displayName}, Type - ${c}")
    if(c.descriptor.displayName == 'Secret text')
        //println "${f.name}, ${c.id}, ${c.secret}, ${c.description}"
        println "${c.id}, ${c.descriptor.displayName}, ${c.secret}, ${c.description}"
        //println "${c.id}, ${c.descriptor.displayName}, ${c.description}"
    else if ( c.descriptor.displayName == 'Username with password')
        //println "${f.name}, ${c.id}, ${c.username}, ${c.password}, ${c.description}"
        println "${c.id}, ${c.descriptor.displayName}, ${c.username}, ${c.password}, ${c.description}"
        //println "${c.id}, ${c.descriptor.displayName}, ${c.username}, ${c.description}"
    else if ( c.descriptor.displayName == 'Secret file')
        //println("------ ${f.name}, CredentialID - ${c.id}, Scope - ${c.scope}, Descriptor - ${c.descriptor.displayName}, Type - ${c}")
        println "------- ${c.id}, ${c.descriptor.displayName}, ${c.fileName}, ${c.description}"
    else if ( c.descriptor.displayName == 'Certificate')
        //println("------ ${f.name}, CredentialID - ${c.id}, Scope - ${c.scope}, Descriptor - ${c.descriptor.displayName}, Type - ${c}")
        println "+++++++ ${c.id}, ${c.descriptor.displayName}, ${c.keyStore}, ${c.description}"
    else if ( c.descriptor.displayName == 'SSH Username with private key')
        //println("------ ${f.name}, CredentialID - ${c.id}, Scope - ${c.scope}, Descriptor - ${c.descriptor.displayName}, Type - ${c}")
        println "^^^^^^^ ${c.id}, ${c.descriptor.displayName}, ${c.username}, ${c.description}"
    else
        println("======= CredentialID - ${c.id}, Scope - ${c.scope}, Descriptor - ${c.descriptor.displayName}, Type - ${c}")
}
/*        println(c.id)
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
*************/
println('')
println('')
println('------ TOP-LEVEL-FOLDERS --------')
def topFolderName="AccessManagement" //CD5 Folder

//def topItems = Jenkins.instance.getItemByFullName(topFolderName, AbstractFolder)
def topItems = Jenkins.instance.getItems(com.cloudbees.hudson.plugins.folder.Folder)
//Jenkins.instance.getAllItems(com.cloudbees.hudson.plugins.folder.Folder).each { 
//topItems.eachWithIndex {
topItems.each {
//Jenkins.instance.getItems(com.cloudbees.hudson.plugins.folder.Folder).each {
    //iTop, f ->
    f ->
    //println "------ ${topItems.name} --------"
    Set<Credentials> folderCredentials = new HashSet<Credentials>()
    folderCredentials = com.cloudbees.plugins.credentials.CredentialsProvider.lookupCredentials(com.cloudbees.plugins.credentials.Credentials, f)
    //allCredentials.addAll(creds)
//}

    for (c in folderCredentials) {
        //println("CredentialID - ${c.id}, Scope - ${c.scope}, Descriptor - ${c.descriptor.displayName}, Type - ${c}")
        //println("${c.descriptor.displayName}")
        if(c.scope == null)
        {
            if(c.descriptor.displayName == 'Secret text')
                //println "${f.name}, ${c.id}, ${c.secret}, ${c.description}"
                //println "${iTop}, ${f.name}, ${c.id}, ${c.descriptor.displayName}, ${c.description}"
                println "${f.name}, ${c.id}, ${c.descriptor.displayName}, ${c.description}"
                //println "${f.name}, ${c.id}, ${c.secret}, ${c.descriptor.displayName}, ${c.description}"
            else if ( c.descriptor.displayName == 'Username with password')
                //println "${f.name}, ${c.id}, ${c.username}, ${c.password}, ${c.description}"
                //println "${iTop}, ${f.name}, ${c.id}, ${c.descriptor.displayName}, ${c.username}, ${c.description}"
                println "${f.name}, ${c.id}, ${c.descriptor.displayName}, ${c.username}, ${c.password}, ${c.description}"
                //println "${f.name}, ${c.id}, ${c.descriptor.displayName}, ${c.username}, ${c.description}"
            else if ( c.descriptor.displayName == 'Secret file')
                //println("------ ${f.name}, CredentialID - ${c.id}, Scope - ${c.scope}, Descriptor - ${c.descriptor.displayName}, Type - ${c}")
                //println "------- ${iTop}, ${f.name}, ${c.id}, ${c.descriptor.displayName}, ${c.fileName}, ${c.description}"
                println "------- ${f.name}, ${c.id}, ${c.descriptor.displayName}, ${c.fileName}, ${c.description}"
            else if ( c.descriptor.displayName == 'Certificate')
                //println("------ ${f.name}, CredentialID - ${c.id}, Scope - ${c.scope}, Descriptor - ${c.descriptor.displayName}, Type - ${c}")
                //println "+++++++ ${iTop}, ${f.name}, ${c.id}, ${c.descriptor.displayName}, ${c.keyStore}, ${c.description}"
                println "+++++++ ${f.name}, ${c.id}, ${c.descriptor.displayName}, ${c.keyStore}, ${c.description}"
            else if ( c.descriptor.displayName == 'SSH Username with private key')
                //println("------ ${f.name}, CredentialID - ${c.id}, Scope - ${c.scope}, Descriptor - ${c.descriptor.displayName}, Type - ${c}")
                //println "^^^^^^^ ${iTop}, ${f.name}, ${c.id}, ${c.descriptor.displayName}, ${c.username}, ${c.description}"
                println "^^^^^^^ ${f.name}, ${c.id}, ${c.descriptor.displayName}, ${c.username}, ${c.description}"
            else
                //println("======= ${iTop}, ${f.name}, CredentialID - ${c.id}, Scope - ${c.scope}, Descriptor - ${c.descriptor.displayName}, Type - ${c}")
                println("======= ${f.name}, CredentialID - ${c.id}, Scope - ${c.scope}, Descriptor - ${c.descriptor.displayName}, Type - ${c}")
        }
        /**
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
        **/
    }
}
println('')