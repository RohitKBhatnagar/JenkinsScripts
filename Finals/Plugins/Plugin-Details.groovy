//Display some insights into Jenkins Plugins

Jenkins.instance.pluginManager.plugins.eachWithIndex { it, iCount -> 
  println "${iCount}: ${it.getShortName()}, ${it.getDisplayName()}, ${it.getVersion()}, ${it.getVersionNumber()}, CORE version Required - ${it.getRequiredCoreVersion()}, ${it.getUrl()}"
  println "\t ${it.getMandatoryDependencies()}, ${it.getDependencies()}, ${it.getActiveWarnings()}"
}

return null


////////////////////////////////

Jenkins.instance.pluginManager.plugins.eachWithIndex { it, iCount -> 
  println "${iCount}: ${it.getShortName()}, ${it.getDisplayName()}, ${it.getVersion()}, ${it.getVersionNumber()}, CORE version Required - ${it.getRequiredCoreVersion()}, ${it.getUrl()}"
  //println "\t ${it.getMandatoryDependencies()}, ${it.getDependencies()}, ${it.getActiveWarnings()}"
}

return null


/****
//println "ls -latrch /sys_apps_01/jenkins_nfs".execute().text


def sout = new StringBuilder(), serr = new StringBuilder()
//def cmd = "ls -latrch /sys_apps_01/jenkins_nfs |grep -i hudson"
//def cmd = "cat /sys_apps_01/jenkins_nfs/com.cloudbees.hudson.plugins.folder.config.AbstractFolderConfiguration.xml" //hudson.maven.MavenModuleSet.xml" //hudson.plugins.git.GitSCM.xml //hudson.plugins.build_timeout.operations.BuildStepOperation.xml
//def cmd = "cat /sys_apps_01/jenkins_nfs/config.xml"
//def cmd = "cat /sys_apps_01/jenkins_nfs/org.jenkinsci.plugins.workflow.flow.GlobalDefaultFlowDurabilityLevel.xml"
//def cmd = "cat /sys_apps_01/jenkins_nfs/jenkins.model.JenkinsLocationConfiguration.xml"
//def cmd = "cat /sys_apps_01/jenkins_nfs/com.cloudbees.jenkins.plugins.bitbucket.endpoints.BitbucketEndpointConfiguration.xml"
//def cmd = "cat /sys_apps_01/jenkins_nfs/hudson.plugins.emailext.ExtendedEmailPublisher.xml"
//def cmd = "ls -latrch /sys_apps_01/jenkins_nfs/hudson.plugins.logfilesizechecker.LogfilesizecheckerWrapper.xml"
//def cmd = "cat /sys_apps_01/jenkins_nfs/nectar-rbac.xml"
//def cmd = "ls -latrch /sys_apps_01/jenkins_nfs/jgroups/"

def cmd = "ls -l /sys_apps_01/plugins/"
def proc = ['bash', '-c', cmd].execute()
proc.consumeProcessOutput(sout, serr)
proc.waitForOrKill(1000)
println "$sout"
********/


//Sorting plugins collected first before displaying
def CD8Plugins = [:]
Jenkins.instance.pluginManager.plugins.eachWithIndex { it, iCount -> 
  def tmp = ["${it.getShortName()}": "${it.getVersion()}"]
  CD8Plugins.putAll(tmp);
}

CD8Plugins.sort().each { it->
  println "${it.key}, ${it.value}"
}

