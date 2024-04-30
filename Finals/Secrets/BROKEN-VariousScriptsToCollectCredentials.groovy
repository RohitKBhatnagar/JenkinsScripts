e058870;e082680;e057487;e093357;e057764;e100690;e059704;e075860;e061809;e080666;e114731;e085468;E021169;e046816;e058444;e081583;e070149;e091862;e091863;e092457;E019968;e092458;e092634
;e092459;e071477;e031828;e072089;e062926;e068687;e069756;e047303;


Mishra, Tapas <Tapas.Mishra@mastercard.com>; Avutti, Kumar <Kumar.Avutti@mastercard.com>; Kumar, Rupesh <Rupesh.Kumar@mastercard.com>; Haleemulla, Mohammed <Mohammed.Haleemulla@mastercard.com>; Rananaware, Swati <Swati.Rananaware@mastercard.com>; Bodapati, Vijaya <Vijaya.Bodapati@mastercard.com>; Bhatnagar, Rohit <Rohit.Bhatnagar@mastercard.com>; Tekale, Mangesh <Mangesh.Tekale@mastercard.com>; Ruparel, Shruti <Shruti.Ruparel@mastercard.com>; Chaudhari, Lalit <Lalit.Chaudhari2@mastercard.com>; Singh, Ajeet <Ajeet.Singh2@mastercard.com>; Rajwani, Roshni <Roshni.Rajwani@mastercard.com>; Ghosh, Probal <Probal.Ghosh@mastercard.com>; Rajagopal, Ramasamy <Ramasamy.Rajagopal@mastercard.com>; Thompson, Brian <Brian.Thompson@mastercard.com>; Swarna, Naga <Naga.Swarna@mastercard.com>; Philip, Vinu <Vinu.Philip@mastercard.com>; Singh, Narendra <Narendra.Singh@mastercard.com>; Yerraguntlapalli, Ravi <Ravi.Yerraguntlapalli@mastercard.com>; Reddy, Lakshmikar <Lakshmikar.Reddy@mastercard.com>; Schaefer, Sarah <Sarah.Schaefer@mastercard.com>; Ramanjaneyulu, Gunji <Gunji.Ramanjaneyulu@mastercard.com>; Tankam, Vinayakiran <Vinayakiran.Tankam@mastercard.com>; Medida, Satish <Satish.Medida@mastercard.com>; Weldon, Jeff <Jeff.Weldon@mastercard.com>; Boggavarapu, Kumar <Kumar.Boggavarapu@mastercard.com>; Vashishth, Mukul <Mukul.Vashishth@mastercard.com>; Patil, Chetan <Chetan.Patil@mastercard.com>; Parle, Andrew <Andrew.Parle@mastercard.com>; Nimbalkar, Pushpraj <Pushpraj.Nimbalkar@mastercard.com>; Coll, Jim <Jim.Coll@mastercard.com>



[Convert]::ToBase64String([Text.Encoding]::ASCII.GetBytes("StlCD3PrdJnkBB:OTA5NjYwOTUwNjA3OpDwmUPL8xNMLhlbnuqk+ULiS3yd"))

U3RsQ0QzUHJkSm5rQkI6T1RBNU5qWXdPVFV3TmpBM09wRHdtVVBMOHhOTUxobGJudXFrK1VMaVMzeWQ=

curl -X GET -H "Content-Type: application/json" -H "Authorization: Basic U3RsQ0QzUHJkSm5rQkI6T1RBNU5qWXdPVFV3TmpBM09wRHdtVVBMOHhOTUxobGJudXFrK1VMaVMzeWQ="  http://globalrepository.mclocal.int/stash/rest/api/1.0/admin/users?filter=StlCD3PrdJnkBB

=================


import jenkins.model.Jenkins;
import nectar.plugins.rbac.strategy.*;
import hudson.security.*;
import nectar.plugins.rbac.groups.*;
import nectar.plugins.rbac.roles.*;

//Obtain security configuration
RoleMatrixAuthorizationStrategyImpl strategy = RoleMatrixAuthorizationStrategyImpl.getInstance()
RoleMatrixAuthorizationConfig config = RoleMatrixAuthorizationPlugin.getConfig()

println 'Groups'
config.getGroups().each{ g ->
    println '\t' + g.name
    println '\t\t Group Roles'
    g.getAllRoles().each{rg -> println '\t\t\t' + rg}
    println '\t\t Group Memberships'
    g.getGroupMembership().each{mg -> println '\t\t\t' + mg}
    println '\t\t Group Members'
    g.getMembers().each{mg -> println '\t\t\t' + mg}
    }

println '*Roles*'
config.getRoles().each{r ->
    println '\t' + r
    println '\t\t Role Permissions'
    Role rc = new Role(r)
    rc.getPermissionProxies().each{p -> println '\t\t' + p.id + " - " + p.name}
    }

println '*Permissions*'
Permission.getAll().each{p -> println '\t' + p.id + " - " + p.name}

///////////////////////////////////

import hudson.model.User
import jenkins.model.*
  
def userid = User.current().id
  
println userid
def auths = Jenkins.instance.securityRealm.loadUserByUsername(userid).authorities.collect{
  a -> a.authority
  println a
}
  
return null


//////////////////////

import nectar.plugins.rbac.groups.*  
import com.cloudbees.hudson.plugins.folder.*
import com.cloudbees.jenkins.plugins.foldersplus.*
import com.cloudbees.hudson.plugins.folder.properties.FolderProxyGroupContainer

//Parent folder name to start with
String folderName = 'EmptyFolder'
folderItem = Jenkins.instance.getAllItems(Folder.class).find{it.name.equals(folderName)}

AbstractFolder < ? > folderAbs1 = AbstractFolder.class.cast(folderItem)
FolderProxyGroupContainer propertyFPG = folderAbs1.getProperties().get(FolderProxyGroupContainer.class)
print "Folder : " + folderItem.name + "\n"
findAllGroups(propertyFPG)

def findAllGroups(FolderProxyGroupContainer fpgc){
if (fpgc != null) {
    	fpgc.getGroups().findAll{it != null}.each {
          println "		Group : " + it.name
          it.getGroupMembership().each{ println 'GroupMember : ' + it.name }
      	  it.getMembers().each{ println '			Member : ' + it }
      	}
  }
}

findAllItems(((com.cloudbees.hudson.plugins.folder.Folder) folderItem).getItems())

def findAllItems(items){  
  for(item in items)
	{
      if (item instanceof com.cloudbees.hudson.plugins.folder.Folder) {
        AbstractFolder < ? > folderAbs1 = AbstractFolder.class.cast(item)
	FolderProxyGroupContainer propertyFPG = folderAbs1.getProperties().get(FolderProxyGroupContainer.class);
        println "Folder : " + item.name
        findAllGroups(propertyFPG)
        //Drill into folders
        findAllItems(((com.cloudbees.hudson.plugins.folder.Folder) item).getItems())
      }
    }
} 

return null

///////////////////////

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

////////////////////////////

//NOFBIZOPS

import com.cloudbees.plugins.credentials.Credentials
import com.cloudbees.plugins.credentials.CredentialsProvider
import com.cloudbees.hudson.plugins.folder.Folder

Set<Folder, Credentials> allCredentials = new HashSet<com.cloudbees.hudson.plugins.folder.Folder, Credentials>();
Jenkins.instance.getAllItems(com.cloudbees.hudson.plugins.folder.Folder.class).each{ f ->
 creds = com.cloudbees.plugins.credentials.CredentialsProvider.lookupCredentials(
      com.cloudbees.plugins.credentials.Credentials.class, f)
  //allCredentials.addAll(f.fullName, creds)
  //allCredentials.addAll(f.displayName, creds) //same as f.name
  //allCredentials.addAll(f.url, creds)
  allCredentials.addAll(f, creds)
}

//println allCredentials

Iterator<Folder, Credentials> allFC = allCredentials.iterator();
     while(allFC.hasNext())
     {
        //println(allFC.next());
       println "allFC.Folder.url"
       for (c in Credentials) 
	   	{
	   		println "${c.id}, ${c.descriptor.displayName}, ${c.username}, ${c.password}, ${c.description}"
	   	}
     }
/*
for (c in allCredentials) {
  //if (CredentialsProvider.FINGERPRINT_ENABLED) {
    //fp = CredentialsProvider.getFingerprintOf(c)
    //if (fp) {  
    //	fp_str = "Fingerprinted jobs: " + fp.getJobs()
  	//} else {
    //	fp_str = "(No Fingerprints)"
    //}  
  //}
  //println(c.id + " : " + c.description  + " | " + fp_str)
  println(c + " , " + c.id + " : " + c.description)
}
*/