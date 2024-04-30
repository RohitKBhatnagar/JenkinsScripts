
//SCRIPT to validate SSH Private and Public keys 
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateCrtKey;
import java.security.interfaces.RSAPublicKey;

def PRIVATE_KEY = com.cloudbees.plugins.credentials.SystemCredentialsProvider.getInstance().getStore().getCredentials(com.cloudbees.plugins.credentials.domains.Domain.global()).find { it.getId().equals('master-ssh-cred') }.getPrivateKey()

println " ------------ PRIVATE KEY of ${java.net.InetAddress.getLocalHost()} -------------"
println PRIVATE_KEY

RSAPrivateCrtKey privateKey = /*(RSAPrivateCrtKey)*/ com.cloudbees.plugins.credentials.SystemCredentialsProvider.getInstance().getStore().getCredentials(com.cloudbees.plugins.credentials.domains.Domain.global()).find { it.getId().equals('master-ssh-cred') }.getPrivateKey();
RSAPublicKey publicKey = (RSAPublicKey) "ssh-rsa AAAAB3NzaC1yc2EAAAADAQABAAABAQDd+rQXBeB+hILeiNUB2KgAesNnVE9MW0i0BfIFUEqJMIpiN2ttKiGA2MmyAGqL+RG/OYdlL8Mua35AuXguwg4lowILJvijdwcklaMRMW0TBcaVltc5G6GGWjG0vBA7llM3c3d8m95JG7zTuCeGJ5ZZEki/VaYpdko62EJvaY9oqUgRJP98qrkHx3PVGFQEnr6gdZ7EFZbpe1Iu6K5LHdpK/PVPeiiFWb6Njvrv7jQqoSR5jaDQligYcFivAZ4UgEjPDK6BtgnNtOxLronQNhS+HFXxiJB52xxtwKfhQQdqlylAHzmw/PG5wWlNtmtlCgFpPIZx4l/u3t/lblu8dMJF root@lstl9jkm8282";

// comment this out to verify the behavior when the keys are different
//keyPair = keyGen.generateKeyPair();
//publicKey = (RSAPublicKey) keyPair.getPublic();

boolean keyPairMatches = privateKey.getModulus().equals(publicKey.getModulus()) &&
    privateKey.getPublicExponent().equals(publicKey.getPublicExponent());

prinltn "RESULT - ${keyPairMatches}"