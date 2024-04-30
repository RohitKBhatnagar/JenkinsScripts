#!groovy
//Prints system user id and password ---------------------------
def creds = com.cloudbees.plugins.credentials.CredentialsProvider.lookupCredentials(
    com.cloudbees.plugins.credentials.common.StandardUsernameCredentials.class,
    Jenkins.instance,
    null,
    null
);
for (c in creds) 
{
     println(c.id + ": " + c.description)
  	if(c instanceof com.cloudbees.plugins.credentials.common.StandardUsernamePasswordCredentials)
   	{           
      println(c.getUsername() + " : " + c.getPassword())                
    }
}