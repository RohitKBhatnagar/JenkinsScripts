//Display Current Branch Indexing Policies
import com.cloudbees.hudson.plugins.folder.computed.PeriodicFolderTrigger
import jenkins.model.Jenkins
import jenkins.branch.OrganizationFolder

println "Organization Items\n-------"
Jenkins.get().getAllItems(jenkins.branch.OrganizationFolder.class).each { folder -> folder.triggers
       .findAll { k,v -> v instanceof com.cloudbees.hudson.plugins.folder.computed.PeriodicFolderTrigger }
       .each { k,v -> println "Folder name: ${folder.fullName}, Interval: ${v.getInterval()}" }
}

println "Multibranch Items\n-------"
Jenkins.get().getAllItems(org.jenkinsci.plugins.workflow.multibranch.WorkflowMultiBranchProject.class).each { folder -> folder.triggers
       .findAll { k,v -> v instanceof com.cloudbees.hudson.plugins.folder.computed.PeriodicFolderTrigger }
       .each { k,v -> println "Folder name: ${folder.fullName}, ${folder.name}, Interval: ${v.getInterval()}" }
}

return