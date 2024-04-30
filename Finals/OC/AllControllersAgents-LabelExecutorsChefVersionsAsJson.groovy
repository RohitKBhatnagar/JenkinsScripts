//CJOC (Script Console and as Cluster Job) - Gets Node-Name, Label, Executors and CHEF-CLIENT version for all nodes against all controllers
import com.cloudbees.opscenter.server.model.*;
import com.cloudbees.opscenter.server.clusterops.steps.*;
import hudson.remoting.*;

def cjoc = getHost(new LocalChannel(), OperationsCenter.class.simpleName, OperationsCenter.class.simpleName)

cjoc.masters = []
Jenkins.instance.getAllItems(ConnectedMaster.class).each {
  cjoc.masters.add(getHost(it.channel, it.class.simpleName, it.encodedName))
}

try{
  cjoc.summary = [
    masters:cjoc.masters.size() + 1, //masters + cjoc
    masterCores:cjoc.masters*.cores.sum() + cjoc.cores,
    executors:cjoc.nodes*.executors.sum() + cjoc.masters*.nodes*.executors.sum().sum(),
    knownCloudExecutors:cjoc.masters*.clouds*.executorsCap.sum().findAll{it}.sum(0) + cjoc.clouds*.executorsCap.findAll{it}.sum(0)
  ]
}catch(e){}

def getHost(channel, type, name){
  def host
  if(channel){
    def stream = new ByteArrayOutputStream();
    def listener = new StreamBuildListener(stream);
    channel.call(new MasterGroovyClusterOpStep.Script("""
      def nodes = []
      import hudson.util.RemotingDiagnostics
      def chefClientCmd = '''println("chef-client -version".execute().text)'''
      def chefStr = "";
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
    """, listener, "host-script.groovy", [:]));
    host = new groovy.json.JsonSlurper().parseText(stream.toString().minus("Result: "));
  } else {
    host = [type:type, name:name, offline:true]
  }
  return host;
}

return new groovy.json.JsonBuilder(cjoc).toPrettyString()

/** Replace the groovy script above to just display CHEF-Client Version for corresponding controller ******
 * Make sure you include the \n as \\\n when using it inside above OC based script
def nodes = []
import hudson.util.RemotingDiagnostics
def chefClientCmd = '''println("chef-client -version".execute().text)'''
def chefStr = "";
//println Jenkins.instance.rootUrl
Jenkins.instance.getNodes().each {
  def isLinuxLabel = it.getLabelString().contains("LIN")
  if (it.toComputer().isOnline() && isLinuxLabel)
  chefStr = RemotingDiagnostics.executeGroovy(chefClientCmd, it.toComputer().getChannel())
  else
    chefStr = ""
  chefStr = chefStr.replace("\n\n","")
  nodes.add([name:it.displayName, chefVersion:chefStr])
  //println name:it.displayName, chefVersion:chefStr
}

def host = [url:Jenkins.instance.rootUrl, nodes:nodes]

return new groovy.json.JsonBuilder(host).toString()
*************/