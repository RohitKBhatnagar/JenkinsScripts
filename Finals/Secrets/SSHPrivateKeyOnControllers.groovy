//Display PRIVATE KEY on Controllers
def PRIVATE_KEY = com.cloudbees.plugins.credentials.SystemCredentialsProvider.getInstance().getStore().getCredentials(com.cloudbees.plugins.credentials.domains.Domain.global()).find { it.getId().equals('master-ssh-cred') }.getPrivateKey()

println PRIVATE_KEY

//Display PASS PHRASE for PRIVATE KEYS
//Gets the passphrase for the private keys or null if the private keys are not protected by a passphase.
def PASS_PHRASE = com.cloudbees.plugins.credentials.SystemCredentialsProvider.getInstance().getStore().getCredentials(com.cloudbees.plugins.credentials.domains.Domain.global()).find { it.getId().equals('master-ssh-cred') }.getPassphrase()

println PASS_PHRASE

//User name using the SSH Private Keys
def SSH_USER = com.cloudbees.plugins.credentials.SystemCredentialsProvider.getInstance().getStore().getCredentials(com.cloudbees.plugins.credentials.domains.Domain.global()).find { it.getId().equals('master-ssh-cred') }.getUsername()

println SSH_USER