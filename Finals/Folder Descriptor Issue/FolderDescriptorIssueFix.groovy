import com.cloudbees.hudson.plugins.folder.properties.FolderProxyGroupContainer

def dryRun = true
def folderToBeMigrated = "Full/Folder/Path"
def folders = Jenkins.instance.getAllItems(com.cloudbees.hudson.plugins.folder.Folder)
folders.each { f ->
  if (f.getFullName() == folderToBeMigrated){
    println "Found folder " + f.getFullName()
    FolderProxyGroupContainer property = f.getProperties().get(FolderProxyGroupContainer.class)
    println "FolderProxyGroupContainer is " + property
    if (!dryRun) {
      if (property != null) {
        println "Migrating folder. Check Controller logs to verify successful migration"
        property.migrateConfig();
      }
    }  
  }
}
println "done"
return


//run "reload" command after successfully done