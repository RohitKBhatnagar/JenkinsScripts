//Executing command at controller script console to extract chef-client -version details but works only on Linux


import hudson.util.RemotingDiagnostics
def chefClientCmd = '''println("chef-client -version".execute().text)'''
def chefStr = "";
Jenkins.instance.getNodes().each {
  def isLinuxLabel = it.getLabelString().contains("LIN")
  if (it.toComputer().isOnline() && isLinuxLabel)
  chefStr = RemotingDiagnostics.executeGroovy(chefClientCmd, it.toComputer().getChannel())
  else
    chefStr = ""
  
  println "name:${it.displayName}, label:${it.labelString}, executors:${it.numExecutors}, chefVersion:${chefStr}"
}

//println "['powershell', '-command', 'dir'].execute()".execute().text

return null

/////////////////////////////////////////

//Another script which waits till RemotingDiagnostics.executeGroovy method finishes execution

////p.waitFor(1,java.util.concurrent.TimeUnit.SECONDS)

import hudson.util.RemotingDiagnostics
//def chefClientCmd = '''println("chef-client -version".execute().text)'''
def chefClientCmd = 'chef-client -version'
def chefStr = "";
Jenkins.instance.getNodes().each {
  def isLinuxLabel = it.getLabelString().contains("LIN")
  if (it.toComputer().isOnline() && isLinuxLabel)
  chefStr = RemotingDiagnostics.executeGroovy("""
def p = '$chefClientCmd'.execute()
p.waitFor()
println p.in.text
""", it.toComputer().getChannel())
  else
    chefStr = ""
  
  println "name:${it.displayName}, label:${it.labelString}, executors:${it.numExecutors}, chefVersion:${chefStr}"
}

//println "['powershell', '-command', 'dir'].execute()".execute().text

return null

/////////////////////////////////////////////////
//Another script which allows waiting can be manipulated for Windows agents
import hudson.util.RemotingDiagnostics
def chefClientCmd = '''println("chef-client -version".execute().text)'''
def chefStr = "";
Jenkins.instance.getNodes().each {
  def isLinuxLabel = it.getLabelString().contains("LIN")
  if (it.toComputer().isOnline() && isLinuxLabel) 
  	chefStr = RemotingDiagnostics.executeGroovy("""
def serr = new StringBuilder()
def sout = new StringBuilder()
def chefClientCmd = \"chef-client -version\"
def proc = ['bash', '-c', chefClientCmd].execute()
proc.waitForProcessOutput(sout, serr)
println sout;
""", it.toComputer().getChannel())
  else {
    chefStr = ""
    /*RemotingDiagnostics.executeGroovy("""
def serr = new StringBuilder()
def sout = new StringBuilder()
def chefClientCmd = \"chef-client -version\"
def proc = ['powershell', '-c', chefClientCmd].execute()
proc.waitForProcessOutput(sout, serr)
println sout;
""", it.toComputer().getChannel())*/
  }
  
  println "name:${it.displayName}, label:${it.labelString}, executors:${it.numExecutors}, chefVersion:${chefStr}"
}

//println "['powershell', '-command', 'dir'].execute()".execute().text

return null


~~~~~~~~~~~~~~~~~~~~~~~~~~~ WORKING with both Windows and Linux nodes included ~~~~~~~~~~~~~~~~~~~~~
import hudson.util.RemotingDiagnostics
def chefClientCmd = '''println("chef-client -version".execute().text)''';
def chefStr = "";
Jenkins.instance.getNodes().each {
  def isLinuxLabel = it.getLabelString().contains("LIN")
  def isWinLabel = it.getLabelString().contains("WIN")
  if (it.toComputer().isOnline() && isWinLabel) 
  {
   	chefStr = RemotingDiagnostics.executeGroovy("""
def serr = new StringBuilder()
def sout = new StringBuilder()
def chefPSClientCmd = "chef-client -version"
def proc = ['powershell', '-c', chefPSClientCmd].execute()
proc.waitForProcessOutput(sout, serr);
println sout;
""", it.toComputer().getChannel());
  } 
  else if(it.toComputer().isOnline() && isLinuxLabel) 
  {
     chefStr = RemotingDiagnostics.executeGroovy(chefClientCmd, it.toComputer().getChannel());
  }
  println "name:${it.displayName}, label:${it.labelString}, executors:${it.numExecutors}, chefVersion:${chefStr}";
 }
return null

---------------------------------------------------------------- WORKING if executed on Controller only -----------------
import hudson.util.RemotingDiagnostics
def chefClientCmd = "println(\"chef-client -version\".execute().text)";
def chefStr = "";
def nodes = []
Jenkins.instance.getNodes().each {
  def isLinuxLabel = it.getLabelString().contains("LIN")
  def isWinLabel = it.getLabelString().contains("WIN")
  if (it.toComputer().isOnline() && isWinLabel) 
  {
   	chefStr = RemotingDiagnostics.executeGroovy("""
def serr = new StringBuilder()
def sout = new StringBuilder()
def chefPSClientCmd = "chef-client -version"
def proc = ['powershell', '-c', chefPSClientCmd].execute()
proc.waitForProcessOutput(sout, serr);
""", it.toComputer().getChannel());
  } 
  else if(it.toComputer().isOnline() && isLinuxLabel) 
  {
     chefStr = RemotingDiagnostics.executeGroovy(chefClientCmd, it.toComputer().getChannel());
  }
  chefStr.replaceAll("[\n\r]", "");
  //println "name:${it.displayName}, label:${it.labelString}, executors:${it.numExecutors}, chefVersion:${chefStr}";
  nodes.add([type:it.class.simpleName, name:it.displayName, label:it.labelString, executors:it.numExecutors, chefVersion:chefStr])
 }
println nodes
return null