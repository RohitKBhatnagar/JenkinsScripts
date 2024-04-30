//All Nodes and Executors details from CJOC directly from Script Console
println "Master HostName and IP Address - ${java.net.InetAddress.getLocalHost()}"

def nodes = []
Jenkins.instance.getAllItems(com.cloudbees.opscenter.server.model.SharedSlave.class).each {
  nodes.add([type:it.class.simpleName, name:it.displayName, label:it.labelString, executors:it.numExecutors])
}


def host = [type:'$type', name:'$name', url:Jenkins.instance.rootUrl, cores:Runtime.runtime.availableProcessors(), nodes:nodes, offline:false]

return new groovy.json.JsonBuilder(host).toString()
////////////////////////////////////////////////////////////////////////
//------------------------------------------------------------
////////////////////////////////////////////////////////////////////////
//All Node sand Executors details from Controller
println "Master HostName and IP Address - ${java.net.InetAddress.getLocalHost()}"

def nodes = []
Jenkins.instance.getNodes().each {
  nodes.add([type:it.class.simpleName, name:it.displayName, label:it.labelString, executors:it.numExecutors])
}


def host = [type:'$type', name:'$name', url:Jenkins.instance.rootUrl, cores:Runtime.runtime.availableProcessors(), nodes:nodes, offline:false]

return new groovy.json.JsonBuilder(host).toString()


/////////////////////////////////////

//Following prints all nodes and executors along with chef client version as JSON output when run from respective controllers Script-Console directly
import hudson.util.RemotingDiagnostics
def chefClientCmd = '''println("chef-client -version".execute().text)'''
//def chefClientCmd = '''println("cat /home/jenkins/.ssh/authorized_keys".execute().text)'''
def chefStr = "";

println "Master HostName and IP Address - ${java.net.InetAddress.getLocalHost()}"

def nodes = []
Jenkins.instance.getNodes().each {
  def isLinuxLabel = it.getLabelString().contains("LIN")
  if (it.toComputer().isOnline() && isLinuxLabel)
  	chefStr = RemotingDiagnostics.executeGroovy(chefClientCmd, it.toComputer().getChannel())
  else
    chefStr = ""
  nodes.add([type:it.class.simpleName, name:it.displayName, label:it.labelString, executors:it.numExecutors, chefVersion:chefStr])
}


def host = [type:'$type', name:'$name', url:Jenkins.instance.rootUrl, cores:Runtime.runtime.availableProcessors(), nodes:nodes, offline:false]

return new groovy.json.JsonBuilder(host).toString()
