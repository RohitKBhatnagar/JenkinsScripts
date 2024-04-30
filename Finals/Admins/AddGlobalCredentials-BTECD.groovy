//Adds username-with-password credentials to the global store

// imports
import com.cloudbees.plugins.credentials.*
import com.cloudbees.plugins.credentials.domains.Domain
import com.cloudbees.plugins.credentials.impl.*
import hudson.util.Secret
import jenkins.model.Jenkins

// parameters
def jenkinsKeyUsernameWithPasswordParameters = [
  description:  'ECMS API authentication for ECMS User',
  id:           'ecms_user',
  secret:       'wfxRAnErn!',
  userName:     'cd-engine-ecms-user'
]

// get Jenkins instance
Jenkins jenkins = Jenkins.getInstance()

// get credentials domain
def domain = Domain.global()

// get credentials store
def store = jenkins.getExtensionList('com.cloudbees.plugins.credentials.SystemCredentialsProvider')[0].getStore()

// define ECMS User secret
def jenkinsKeyUsernameWithPassword = new UsernamePasswordCredentialsImpl(
  CredentialsScope.GLOBAL,
  jenkinsKeyUsernameWithPasswordParameters.id,
  jenkinsKeyUsernameWithPasswordParameters.description,
  jenkinsKeyUsernameWithPasswordParameters.userName,
  jenkinsKeyUsernameWithPasswordParameters.secret
)
jenkinsKeyUsernameWithPassword.setUsernameSecret(true)

// add credential to store
store.addCredentials(domain, jenkinsKeyUsernameWithPassword)

// save to disk
jenkins.save()

//Adding 2nd credential

jenkinsKeyUsernameWithPasswordParameters.clear()
// parameters
jenkinsKeyUsernameWithPasswordParameters = [
  description:  'BTE Vault Credentials',
  id:           'bte_vault_prod',
  secret:       '245adfb2-aea0-e170-40b1-88cc5a299d76',
  userName:     '83d28709-5241-e35d-8d1b-acb21630dab4'
]

store = null
store = jenkins.getExtensionList('com.cloudbees.plugins.credentials.SystemCredentialsProvider')[0].getStore()
jenkinsKeyUsernameWithPassword = null

// define BTE Vault secret
jenkinsKeyUsernameWithPassword = new UsernamePasswordCredentialsImpl(
  CredentialsScope.GLOBAL,
  jenkinsKeyUsernameWithPasswordParameters.id,
  jenkinsKeyUsernameWithPasswordParameters.description,
  jenkinsKeyUsernameWithPasswordParameters.userName,
  jenkinsKeyUsernameWithPasswordParameters.secret
)
jenkinsKeyUsernameWithPassword.setUsernameSecret(true)

// add credential to store
store.addCredentials(domain, jenkinsKeyUsernameWithPassword)

// save to disk
jenkins.save()


/*** Global Credential Request from BTE CD Team *********
Hi Rohit,

Here are the details you are looking for. Please let me know if you have any questions.

1.   controller name where you want global credentials 
1.  need this on all controllers where BTE CD will run so CD-CD5, 9,10
2.  Credential ID we wish to use
1.  ecms_user
2.  bte_vault_prod
3.  Credentials themselves 
1.  username: cd-engine-ecms-user, password: wfxRAnErn!
2.  username: 83d28709-5241-e35d-8d1b-acb21630dab4, password: 245adfb2-aea0-e170-40b1-88cc5a299d76
4.  Do you want to encrypt the credentials meaning username, secret etc
1.  If by encrypt you mean, treating the username as a secret yes we want that.  
5.  Finally just a small write up to seek approval from IS that there is no risk either credentials being exposed to our builder community since as a global entry they can be accessed by anyone in that controller
1.  This will not expose our builder community because the 1st credential is a generic user that was created specifically for this purpose and is not used in any other processes and the 2nd credential provides access to vault mount which only holds the client certificate needed in this process. It doesn't provide access to any sensitive information.


Alissa Bates
Senior Software Engineer
Automation Frameworks | Builder's Enablement Program

Mastercard
tel (636) 722-6061 | mobile (636) 312-5782 
 

From: Willett, Sara <Sara.Willett@mastercard.com>
Date: Wednesday, September 21, 2022 at 6:44 PM
To: CD_Team_Pipelines <CD_Team_Pipelines@mastercard.com>
Subject: Fwd: Global Credential Request 


Sara Willett
Builders Enablement | Automation Frameworks

Typos courtesy of my iPhone
________________________________________
From: Bhatnagar, Rohit <Rohit.Bhatnagar@mastercard.com>
Sent: Wednesday, September 21, 2022 3:18 PM
To: Willett, Sara <Sara.Willett@mastercard.com>
Cc: Nadella, Sekhar <Sekhar.Nadella@mastercard.com>; CD_Team_Pipelines <CD_Team_Pipelines@mastercard.com>
Subject: Re: Global Credential Request 
 
Hi Sara,
No worries in this respect, we can certainly help you define them at global level. Can you share following details?
1.   controller name where you want global credentials 
2.  Credential ID we wish to use
3.  Credentials themselves 
4.  Do you want to encrypt the credentials meaning username, secret etc
5.  Finally just a small write up to seek approval from IS that there is no risk either credentials being exposed to our builder community since as a global entry they can be accessed by anyone in that controller 

Thanks
-Rohit
(636) 515-6336
Sent from Outlook for iOS
________________________________________
From: Willett, Sara <Sara.Willett@mastercard.com>
Sent: Wednesday, September 21, 2022 4:03:03 PM
To: Bhatnagar, Rohit <Rohit.Bhatnagar@mastercard.com>
Cc: Nadella, Sekhar <Sekhar.Nadella@mastercard.com>; CD_Team_Pipelines <CD_Team_Pipelines@mastercard.com>
Subject: Global Credential Request 
 
Hi Rohit & Sekhar,
 
When we implemented certificate automation in BTE we had to manually generate client certificates in Venafi for each program to be used to authenticate to the ECMS API. We are now working on automating the renewal of these program certificates, but in order to renew these client certificates we must use the Venafi API. To use the Venafi API we need to pass the API a set of credentials (specifically a generic user’s credentials we have created just for this process) to get a token and we need to fetch a client cert from vault using an app role/secret id. We then use this token and the client certificate fetched from vault to authenticate to the Venafi API. 
 
The problem we are facing now is Jenkins credentials are currently stored at the folder level and as we all well know there are BTE jobs all over Jenkins, many of which we don’t have access to. For this process to work we need to store these credentials at a global level in Jenkins. The credentials specifically would be the generic user we created just for this process and the app role/secretId to access our vault mount. 
 
Is this possible and what is the process to get these global credentials added to Jenkins?
 
 
Sara Willett
Vice President, Automation Frameworks
Builder's Enablement Program | Architecture & Technology

Mastercard
Office: Colorado | Mobile: 1-636-439-7334 
 
 

*********************************************************/