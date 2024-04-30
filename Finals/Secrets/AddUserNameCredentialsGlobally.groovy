//Adds username-with-password credentials to the global store

// imports
import com.cloudbees.plugins.credentials.*
import com.cloudbees.plugins.credentials.domains.Domain
import com.cloudbees.plugins.credentials.impl.*
import hudson.util.Secret
import jenkins.model.Jenkins

// parameters
def jenkinsKeyUsernameWithPasswordParameters = [
  description:  'Jenkins BitBucket read-only generic credentials for CD9 controller for repository(s) with public access.',
  id:           'JNKCD9-BTE-BB',
  secret:       'OTc3NTU0Nzk0ODE5OjaDwgcsSry//MbYdgNh+W9ja5KS',
  userName:     'StlCD9PrdJnkBB'
]

// def jenkinsKeyUsernameWithPasswordParameters = [
//   description:  'Jenkins BitBucket read-only generic credentials for CD12 controller for repository(s) with public access. Borrowed from CD4 Controller!',
//   id:           'JNKCD4-BTE-BB12',
//   secret:       'NTk4MDQ3NTk4NDM0OvnOiOQnHhVL8ndVWZfdtdCs9E8m',
//   userName:     'StlCD4PrdJnkBB'
// ]

// get Jenkins instance
Jenkins jenkins = Jenkins.getInstance()

// get credentials domain
def domain = Domain.global()

// get credentials store
def store = jenkins.getExtensionList('com.cloudbees.plugins.credentials.SystemCredentialsProvider')[0].getStore()

// define Bitbucket secret
def jenkinsKeyUsernameWithPassword = new UsernamePasswordCredentialsImpl(
  CredentialsScope.GLOBAL,
  jenkinsKeyUsernameWithPasswordParameters.id,
  jenkinsKeyUsernameWithPasswordParameters.description,
  jenkinsKeyUsernameWithPasswordParameters.userName,
  jenkinsKeyUsernameWithPasswordParameters.secret
)

// add credential to store
store.addCredentials(domain, jenkinsKeyUsernameWithPassword)

// save to disk
jenkins.save()