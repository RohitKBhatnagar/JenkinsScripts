//Displays all credentials inside specific top level folder only
import com.cloudbees.plugins.credentials.Credentials
import com.cloudbees.hudson.plugins.folder.AbstractFolder;

import jenkins.*
import jenkins.model.*
import hudson.*
import hudson.model.*

import hudson.scm.*
import hudson.tasks.*
import com.cloudbees.hudson.plugins.folder.*

def topFolderName="stlcd"

Set<Credentials> allCredentials = new HashSet<Credentials>()

def creds = com.cloudbees.plugins.credentials.CredentialsProvider.lookupCredentials(com.cloudbees.plugins.credentials.Credentials)

allCredentials.addAll(creds)

jen = Jenkins.instance

jen.getItems().each {
    if(it instanceof Folder)
    {
        if(it.name.equals(topFolderName))
        {
            println "Pulling all credentials including sub folders for ${it.name}"
            creds = com.cloudbees.plugins.credentials.CredentialsProvider.lookupCredentials(com.cloudbees.plugins.credentials.Credentials, it)
            allCredentials.addAll(creds)
            processFolder(it, allCredentials)
        }
    }
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

void processFolder(Item folder, HashSet<Credentials> allCredentials)
{
    folder.getItems().each {
        if(it instanceof Folder)
        {
            println "Sub Folder - ${it.name}"
            creds = com.cloudbees.plugins.credentials.CredentialsProvider.lookupCredentials(com.cloudbees.plugins.credentials.Credentials, it)
            allCredentials.addAll(creds)
            processFolder(it, allCredentials)
        }
    }
}
