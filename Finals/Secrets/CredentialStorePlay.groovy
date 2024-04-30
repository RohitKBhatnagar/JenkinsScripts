import jenkins.model.*
import com.cloudbees.hudson.plugins.folder.*
import com.cloudbees.hudson.plugins.folder.properties.*
import com.cloudbees.hudson.plugins.folder.properties.FolderCredentialsProvider.FolderCredentialsProperty
import com.cloudbees.plugins.credentials.impl.*
import com.cloudbees.plugins.credentials.*
import com.cloudbees.plugins.credentials.domains.*
import com.cloudbees.plugins.credentials.impl.UsernamePasswordCredentialsImpl;

jenkins = Jenkins.instance

def folderName = 'ESSDeployBizOps'; //On CD3
def credID = 'MIDS-JNK-BB';
def description = 'Groovy BB scripter update';
def user = '0c88e7cc-c66b-34ca-aed8-8def3a95be41';
def password = null;

Set<Credentials> allCredentials = new HashSet<Credentials>()

def creds = com.cloudbees.plugins.credentials.CredentialsProvider.lookupCredentials(com.cloudbees.plugins.credentials.Credentials)

allCredentials.addAll(creds)

println "HashSet - $allCredentials";


for (folder in jenkins.getItems(Folder.class)) 
{
    //println "$folder"
    if(folder.name.equals(folderName) || folder.displayName.equals(folderName)) 
	{
        println "Inside $folderName ---------"
        AbstractFolder<?> folderAbs = AbstractFolder.class.cast(folder)
        FolderCredentialsProperty property = folderAbs.getProperties().get(FolderCredentialsProperty.class);
        if (property) 
        {
          println "FolderCredentialsProperty - $property";
          def dmnCred = property.getStore().getCredentials(Domain.global());
          if(dmnCred.size() > 0)
          {
            println "Domain Credentials - $dmnCred";
		  }

		  creds = com.cloudbees.plugins.credentials.CredentialsProvider.lookupCredentials(com.cloudbees.plugins.credentials.Credentials, folder)
		  allCredentials.addAll(creds)
		  println "HashSet finally - $allCredentials";
		}
	}
}