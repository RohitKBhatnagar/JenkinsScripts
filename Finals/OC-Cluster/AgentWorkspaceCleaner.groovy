//This is a scripted pipeline and the code is taken from the jenkins_utility_scripts
//https://fusion.mastercard.int/stash/projects/PIPE/repos/jenkins_utility_scripts/browse/groovy/globalAgentCleaner.groovy

import hudson.util.RemotingDiagnostics
import hudson.model.*;
import hudson.matrix.*;
import hudson.maven.*;
import hudson.util.*;
import hudson.FilePath.FileCallable;
import hudson.slaves.OfflineCause;
import hudson.node_monitors.*;

space_consumed = '''
try {
  println "df -kh | grep jenkins".execute().text
  println "df -kh | grep tmp".execute().text
  println "df -kh /cygdrive/d".execute().text
}
catch (Exception ex) {       
   println ("**********************************************\\n${ex}\\n**********************************************")
}
'''

docker_clean = '''
try {
  println "docker ps".execute().text
  println "docker container prune -f --filter until=72".execute().text
  println "docker system prune -af".execute().text
}
catch (Exception ex) {       
   println ("**********************************************\\n${ex}\\n**********************************************")
}
'''.trim()

cache_clean = '''
try {
   println "git clone https://globalrepository.mclocal.int/stash/scm/pipe/jenkins_utility_scripts.git".execute().text
   println "/sys_apps_01/maven/apache-maven-3.3.9/bin/mvn -f jenkins_utility_scripts/groovy/pom.xml clean:clean".execute().text
   println "find /tmp -mtime +1 -type d -user jenkins -exec rm -rf {} +".execute().text
}
catch (Exception ex) {       
   println ("**********************************************\\n${ex}\\n**********************************************")
}
'''.trim()

for (node in Jenkins.instance.nodes) {
  computer = node.toComputer()
  if (!computer.online) continue
  if (node.name.contains("dsm")) continue
  if (node.getLabelString().contains("UTS")) continue
  if (node.getLabelString().contains("AIX")) continue
  if (!node.getLabelString()) continue
  if (node.getLabelString().contains("CHEF")) continue
  if (node.getLabelString().contains("WIN")) continue
  if (node.getLabelString().contains("DEVCLD-LIN7")) continue
  println ("###############################################\nRunning on " + node.name + "\n###############################################")  
  println RemotingDiagnostics.executeGroovy(space_consumed, computer.channel)    
  if (node.getLabelString().contains("CHEF") || node.getLabelString().contains("DEVCLD-LIN7")) {
    println ("==========================================\nCleaning Docker on " + node.name + "\n==========================================")
    //println RemotingDiagnostics.executeGroovy(docker_clean, computer.channel)
  }
  if (node.getLabelString().contains("LIN")) {
    println ("==========================================\nCleaning Maven cache on " + node.name + "\n==========================================")
    println RemotingDiagnostics.executeGroovy(cache_clean, computer.channel)
  }  
  if (node.getLabelString().contains("LIN") || node.getLabelString().contains("WIN12")) {
    println ("==========================================\nCleaning Workspace on " + node.name + "\n==========================================")
    computer.setTemporarilyOffline(true, new hudson.slaves.OfflineCause.ByCLI("Agent_cleaner: disk cleanup"))
    performCleanup(node)
    computer.setTemporarilyOffline(false, null)
  }  
  println RemotingDiagnostics.executeGroovy(space_consumed, computer.channel)  
  println ("###############################################\nCleaning completed on " + node.name + "\n###############################################")  
}
 
def performCleanup(computer) {
  for (item in Jenkins.instance.getAllItems(Job.class)) {
    // MavenModule is superfluous project returned by getAllItems()
    if (!(item instanceof MatrixConfiguration || item instanceof MavenModule)) {
      jobName = item.getFullDisplayName()
      if (item.isBuilding()) {
        println(".. job " + jobName + " is currently running, workspace skipped")
      } 
      else {
        numbuilds = item.builds.size()
        if (numbuilds != 0) {
          workspacePath = computer.getWorkspaceFor(item)
          lastbuild = item.builds[numbuilds - 1]
          pathAsString = workspacePath.getRemote()
          if (workspacePath.exists()) {
            workspacePath.deleteRecursive()
            println(".... deleted from location " + pathAsString)
          }
        }
      }
    }
  } 
}