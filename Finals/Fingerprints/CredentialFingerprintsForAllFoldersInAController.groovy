//Controller groovy script to find out fingerprints for all credentials defined across all folders (includes sub-folders)
import com.cloudbees.plugins.credentials.Credentials
import com.cloudbees.plugins.credentials.CredentialsProvider

def myMap = [:]
//Set<Credentials> allCredentials = new HashSet<Credentials>();
Jenkins.instance.getAllItems(com.cloudbees.hudson.plugins.folder.Folder.class).each{ f ->
  //println f.name
  Set<Credentials> fldrCredentials = new HashSet<Credentials>();
 creds = com.cloudbees.plugins.credentials.CredentialsProvider.lookupCredentials(
      com.cloudbees.plugins.credentials.Credentials.class, f)
  fldrCredentials.addAll(creds)
  myMap.putAll((f.name):(fldrCredentials))
}
myMap.each {entry ->
  println "======= Folder Name: '${entry.key}' ========="
  entry.value.each { c->
    if (CredentialsProvider.FINGERPRINT_ENABLED) {
      fp = CredentialsProvider.getFingerprintOf(c)
      if (fp) {  
          fp_jobs = fp.getJobs()
      } else {
          fp_jobs = null
      } 
    }
      //println("\t" + c.id + " : " + c.description)
      println("\t Credential-Name: '$c.id' , Description: '$c.description'")
      if(fp_jobs)
      {
        int i = 0
        for (jbs in fp_jobs) { 
          println "\t\t ${++i} : $jbs"
        }
        println("\t------------")
      }
  }
}

///////////////ORIGINAL - https://github.com/cloudbees/jenkins-scripts/blob/master/list-credential.groovy ///////////////////////////
import com.cloudbees.plugins.credentials.Credentials
import com.cloudbees.plugins.credentials.CredentialsProvider

Set<Credentials> allCredentials = new HashSet<Credentials>();
Jenkins.instance.getAllItems(com.cloudbees.hudson.plugins.folder.Folder.class).each{ f ->
 creds = com.cloudbees.plugins.credentials.CredentialsProvider.lookupCredentials(
      com.cloudbees.plugins.credentials.Credentials.class, f)
  allCredentials.addAll(creds)

}
for (c in allCredentials) {
  if (CredentialsProvider.FINGERPRINT_ENABLED) {
    fp = CredentialsProvider.getFingerprintOf(c)
    if (fp) {  
      fp_str = "Fingerprinted jobs: " + fp.getJobs()
    } else {
      fp_str = "(No Fingerprints)"
    }  
  }
  println(c.id + " : " + c.description  + " | " + fp_str)
}

/////////////////////////////////////////////////////////// ORIGINAL ABOVE /////////////////


/**
class hudson.model.Fingerprint functions: fp
equals(); getClass(); hashCode(); notify(); notifyAll(); toString(); wait(); _getUsages(); add(); addFor(); delete(); getActions(); getApi(); getDisplayName(); getFacet(); getFacetBlockingDeletion(); getFacets(); getFileName(); getHashString(); getJobs(); getOriginal(); getPersistedFacets(); getRangeSet(); getSortedFacets(); getTimestamp(); getTimestampString(); getUsages(); getXStream(); isAlive(); load(); rename(); save(); trim(); 
------------
class java.util.ArrayList functions: fp.getJobs()
equals(); getClass(); hashCode(); notify(); notifyAll(); toString(); wait(); add(); addAll(); clear(); contains(); containsAll(); isEmpty(); iterator(); remove(); removeAll(); retainAll(); size(); toArray(); get(); indexOf(); lastIndexOf(); listIterator(); set(); subList(); clone(); ensureCapacity(); forEach(); removeIf(); replaceAll(); sort(); spliterator(); trimToSize(); 
**/

/////////////////////

