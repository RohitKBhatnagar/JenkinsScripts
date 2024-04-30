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
FolderProxyGroupContainer propertyFPG = folderAbs1.getProperties().get(FolderProxyGroupContainer.class)
println "Folder Proxy Group Container - $propertyFPG"
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
/***/
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
/***/