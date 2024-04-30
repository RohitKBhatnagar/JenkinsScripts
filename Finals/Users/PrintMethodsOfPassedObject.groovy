/*Especially pay attention to method call 'printAllMethods' where we can print out all methods of the passed object in groovy - https://bateru.com/news/2011/11/code-of-the-day-groovy-print-all-methods-of-an-object/*/
/*Groups associated with each folder*/
import nectar.plugins.rbac.groups.*  
import com.cloudbees.hudson.plugins.folder.*
import com.cloudbees.jenkins.plugins.foldersplus.*
import com.cloudbees.hudson.plugins.folder.properties.FolderProxyGroupContainer

//Parent folder name to start with
String folderName = 'NOFNENE'
folderItem = Jenkins.instance.getAllItems(Folder.class).find{it.name.equals(folderName)}

AbstractFolder < ? > folderAbs1 = AbstractFolder.class.cast(folderItem)
println "Abstract Folder - $folderAbs1"
println "------------------- AbstractFolder.class.cast ----------"
printAllMethods(folderAbs1)
println "------------------- folderAbs1.getProperties().getClass() -----------"
printAllMethods(folderAbs1.getProperties().getClass()) 
println "All properties - ${folderAbs1.getProperties()}"

println "------------------- Properties... ----------"
folderAbs1.getProperties().each {
  it ->
  println it.getClass()
  printAllMethods(it)
  
  //if(it.getClass() == 'com.cloudbees.hudson.plugins.folder.properties.FolderCredentialsProvider$FolderCredentialsProperty')
  //{
    //println "Domain Credentials - ${it.getDomainCredentialsMap()}"
  //}
}

println "------------------- Domain Credentials ----------"
//com.cloudbees.hudson.plugins.folder.properties.FolderCredentialsProvider fcProv = folderAbs1.getProperties().get(FolderCredentialsProvider.class)
com.cloudbees.hudson.plugins.folder.properties.FolderCredentialsProvider$FolderCredentialsProperty fcProv = folderAbs1.getProperties().get(com.cloudbees.hudson.plugins.folder.properties.FolderCredentialsProvider$FolderCredentialsProperty.class)
println "Domain Credentials - ${fcProv.getDomainCredentialsMap()}"
def dmnCreds = fcProv.getDomainCredentialsMap()
//printAllMethods(dmnCreds)
dmnCreds.each {
    it->
    //if(it instanceof com.cloudbees.plugins.credentials.domains.Domain)
    def itUser = it.getKey();
    itUser.each {
        itusr ->
        println "Each domain credential - $itusr"
        if(itusr instanceof com.cloudbees.plugins.credentials.impl.UsernamePasswordCredentialsImpl)
        {
            println "${itusr.id}, ${itusr.username}, ${itusr.password}, ${itusr.description}"
        }
    }
}


println "------------------- FolderProxyGroupContainer.class ----------"
printAllMethods(FolderProxyGroupContainer.class) 
//println "Groups - ${FolderProxyGroupContainer.class.getGroups()}"
FolderProxyGroupContainer propertyFPG = folderAbs1.getProperties().get(FolderProxyGroupContainer.class)
println "Folder Proxy Group Container - $propertyFPG"
print "Folder : " + folderItem.name + "\n"
findAllGroups(propertyFPG)

//println "------------------- Via https://cd4.mastercard.int/jenkins/job/NOFNENE/groups/api/json?groups ----------"
//FolderProxyGroupContainer propertyFPGo = folderAbs1.getProperties().get(SeparatedGroupContainers.class).get(FolderProxyGroupContainer.class)
//println "Folder Proxy Group Container - $propertyFPGo"
//print "Folder : " + folderItem.name + "\n"
//findAllGroups(propertyFPGo)

SecurityGrantsFolderProperty sgfProp = SecurityGrantsFolderProperty.of(folderAbs1)
println "Security Grants Folder Property - $sgfProp"
println "------------------- SecurityGrantsFolderProperty ----------"
printAllMethods(sgfProp)
//def methods = sgfProp.declaredMethods.findAll { !it.synthetic }
//println methods
if (sgfProp != null) {
  println "Health - ${sgfProp.getFolderActions()}"
  sgfProp.getSecurityGrants().each {
      println "SecurityGrant : " + it
    }
  println "Security Grant of - ${sgfProp.of(folderItem).getFolderActions()}"
  }

def findAllGroups(FolderProxyGroupContainer fpgc){
if (fpgc != null) {
  printAllMethods(fpgc)
    	fpgc.getGroups().findAll{it != null}.each {
          println "		Group : " + it.name
          it.getGroupMembership().each{ println 'GroupMember : ' + it.name }
      	  it.getMembers().each{ println '			Member : ' + it }
      	}
  }
  else
  {
    /*SecurityGrantsFolderProperty property1 = SecurityGrantsFolderProperty.of(folderAbs1)
    if (property1 != null) {
      property1.getSecurityGrants().each {
          println "SecurityGrant : " + it
        }
      }*/
  }
}
/***
findAllItems(((com.cloudbees.hudson.plugins.folder.Folder) folderItem).getItems())

def findAllItems(items){  
  for(item in items)
	{
      if (item instanceof com.cloudbees.hudson.plugins.folder.Folder) {
        AbstractFolder < ? > folderAbs1 = AbstractFolder.class.cast(item)
	FolderProxyGroupContainer propertyFPG = folderAbs1.getProperties().get(FolderProxyGroupContainer.class);
        println "\tSub-Folder : " + item.name
        findAllGroups(propertyFPG)
        //Drill into folders
        findAllItems(((com.cloudbees.hudson.plugins.folder.Folder) item).getItems())
      }
    }
}
***/

// Filename: printAllMethodsExample.groovy
void printAllMethods( obj ){
    if( !obj ){
    println( "Object is null\r\n" );
    return;
    }
  if( !obj.metaClass && obj.getClass() ){
        printAllMethods( obj.getClass() );
    return;
    }
  def str = "class ${obj.getClass().name} functions:\r\n";
  obj.metaClass.methods.name.unique().each{ 
    str += it+"(); "; 
  }
  println "${str}\r\n";
}