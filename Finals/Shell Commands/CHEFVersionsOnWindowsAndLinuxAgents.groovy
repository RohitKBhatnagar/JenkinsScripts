//Get CHEF-Client version for both Windows and Linux Nodes in the controller where this script is executed, handles exception scenarios


import hudson.util.RemotingDiagnostics
def chefClientCmd = '''println("chef-client -version".execute().text)''';
def chefStr = "";
Jenkins.instance.getNodes().each {
  try {
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
  catch(Exception ex)
  {
    println "name:${it.displayName}, label:${it.labelString}, executors:${it.numExecutors}, Error: ${ex.message}"
  }
 }

return null


//////////////////////////////////////
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