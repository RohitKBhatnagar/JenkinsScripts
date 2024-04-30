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


String strSecret = "$password";
Credentials c = new UsernamePasswordCredentialsImpl(CredentialsScope.GLOBAL, "$credID", "$description", "$user", "$password")
//println "$c"
for (folder in jenkins.getAllItems(Folder.class)) {
    //println "$folder"
    if(folder.name.equals(folderName) || folder.displayName.equals(folderName)) {
        println "Inside $folderName ---------"
        AbstractFolder<?> folderAbs = AbstractFolder.class.cast(folder)
        FolderCredentialsProperty property = folderAbs.getProperties().get(FolderCredentialsProperty.class);
        if (property) 
        {
          //println "FolderCredentialsProperty - $property";
          def dmnCred = property.getStore().getCredentials(Domain.global());
          if(dmnCred.size() > 0)
          {
            //println "Domain Credentials - $dmnCred";
            dmnCred.each {  it ->
                if(it instanceof com.cloudbees.plugins.credentials.impl.UsernamePasswordCredentialsImpl)
                {
                  //println "UsernamePasswordCredentialsImpl - ${it}"
                  def usrName = it.username;
                  def dmnSecret = it.password

                  //println "User - $usrName \t - \t Secret - $dmnSecret"
                  println "$usrName\t - \t$dmnSecret"
                  //if(usrName.equals('StlCDPrdJnkBB') || usrName.equals('StlCD2PrdJnkBB') || usrName.equals('StlCD3PrdJnkBB') || usrName.equals('StlCD4PrdJnkBB') || usrName.equals('StlCD5PrdJnkBB'))
                  if(usrName.equals('0c88e7cc-c66b-34ca-aed8-8def3a95be41'))     
                  {
                    println "$usrName matched"
                    if((String)dmnSecret == strSecret)
                    {
                        println "Do Nothing!"
                    }
                    else
                    {
                        println "Credential Store - ${property.getStore()}"
                        /*if(property.getStore().removeCredentials(Domain.global(), c))
                            property.getStore().addCredentials(Domain.global(), c);
                        else
                            property.getStore().addCredentials(Domain.global(), c);*/
                    }
                  }
                }
            }
          }
        }
    }
}