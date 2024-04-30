// Update the PeriodicFolderTrigger of each job inside of a Cloudbees folder.
// Useful for updating individual repos as you cannot do this through the UI.
// https://stackoverflow.com/questions/57077851/jenkins-github-plugin-scan-organization-triggers


import com.cloudbees.hudson.plugins.folder.computed.PeriodicFolderTrigger
import jenkins.model.Jenkins
import jenkins.branch.OrganizationFolder

println "Multibranch Items\n-------"
Jenkins.instance.getAllItems(org.jenkinsci.plugins.workflow.multibranch.WorkflowMultiBranchProject.class).each { it.triggers
       .findAll { k,v -> v instanceof com.cloudbees.hudson.plugins.folder.computed.PeriodicFolderTrigger }
       .each { k,v -> 
            if(it.fullName.equals("Workflow-Services/BMC/bmc-client-lib"))
            {
              println "Updating branch indexing interval for $it.name from ${v.getInterval()} to 28 days"
                  setInterval(it);
                }
             }                                                                                               
}

def setInterval(folder) {
  println "[INFO] : Updating ${folder.name}... " 
  folder.getTriggers().find {triggerEntry ->
    def key = triggerEntry.key
    if (key instanceof PeriodicFolderTrigger.DescriptorImpl){
      println "[INFO] : Current interval : " + triggerEntry.value.getInterval()
      // Valid intervals are here:
      // https://github.com/jenkinsci/cloudbees-folder-plugin/blob/master/src/main/java/com/cloudbees/hudson/plugins/folder/computed/PeriodicFolderTrigger.java#L261-L278
      def newInterval = new PeriodicFolderTrigger("28d")
      folder.addTrigger(newInterval)
      folder.save()
      println "[INFO] : New interval : " + newInterval.getInterval()
    }
  }
}

return