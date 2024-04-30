//Displays all credentials global and inside each top level folder
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
