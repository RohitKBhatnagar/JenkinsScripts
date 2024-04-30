//CJOC (Script Console and as Cluster Job) - Gets Node-Name, Label, Executors and CHEF-CLIENT version for all nodes against all controllers
import com.cloudbees.opscenter.server.model.*;
import com.cloudbees.opscenter.server.clusterops.steps.*;
import hudson.remoting.*;

def cjoc = getHost(new LocalChannel(), OperationsCenter.class.simpleName, OperationsCenter.class.simpleName)

cjoc.masters = []
Jenkins.instance.getAllItems(ConnectedMaster.class).each {
  cjoc.masters.add(getHost(it.channel, it.class.simpleName, it.encodedName))
}

try {
  cjoc.summary = [
    masters:cjoc.masters.size() + 1, //masters + cjoc
    masterCores:cjoc.masters*.cores.sum() + cjoc.cores,
    executors:cjoc.nodes*.executors.sum() + cjoc.masters*.nodes*.executors.sum().sum(),
    knownCloudExecutors:cjoc.masters*.clouds*.executorsCap.sum().findAll{it}.sum(0) + cjoc.clouds*.executorsCap.findAll{it}.sum(0)
  ]
}catch(e){}

def getHost(channel, type, name) {
  def host
  if(channel) {
    def stream = new ByteArrayOutputStream();
    def listener = new StreamBuildListener(stream);
    channel.call(new MasterGroovyClusterOpStep.Script("""
      def nodes = []
      Jenkins.instance.getNodes().each {
      try {
        import hudson.util.RemotingDiagnostics
        def isLinuxLabel = it.getLabelString().contains("LIN")
        def isWinLabel = it.getLabelString().contains("WIN")
        if (it.toComputer().isOnline() && isWinLabel) {
          chefStr = RemotingDiagnostics.executeGroovy('''
          def serr = new StringBuilder()
          def sout = new StringBuilder()
          def chefPSClientCmd = "chef-client -version"
          def proc = ["powershell", "-c", chefPSClientCmd].execute()
          proc.waitForProcessOutput(sout, serr);
          println sout;
          ''', it.toComputer().getChannel());
        } 
        else if(it.toComputer().isOnline() && isLinuxLabel) {
           chefStr = RemotingDiagnostics.executeGroovy(chefClientCmd, it.toComputer().getChannel());
        }
        println "name:${it.displayName}, label:${it.labelString}, executors:${it.numExecutors}, chefVersion:${chefStr}";
      }
      catch(Exception ex) {
        println "name:${it.displayName}, label:${it.labelString}, executors:${it.numExecutors}, Error: ${ex.message}"
      }
      nodes.add([type:it.class.simpleName, name:it.displayName, label:it.labelString, executors:it.numExecutors, chefVersion:chefStr])
     }

    def host = [type:'$type', name:'$name', url:Jenkins.instance.rootUrl, cores:Runtime.runtime.availableProcessors(), nodes:nodes, offline:false];

    return new groovy.json.JsonBuilder(host).toString()
    """, listener, "host-script.groovy", [:]));
    host = new groovy.json.JsonSlurper().parseText(stream.toString().minus("Result: "));
  } else {
    host = [type:type, name:name, offline:true]
  }
  return host;
}


/////////////////////////////////////////////////////////////

//CJOC (Script Console and as Cluster Job) - Gets Node-Name, Label, Executors and CHEF-CLIENT version for all nodes against all controllers
import com.cloudbees.opscenter.server.model.*;
import com.cloudbees.opscenter.server.clusterops.steps.*;
import hudson.remoting.*;

def cjoc = getHost(new LocalChannel(), OperationsCenter.class.simpleName, OperationsCenter.class.simpleName)

cjoc.masters = []
Jenkins.instance.getAllItems(ConnectedMaster.class).each {
  cjoc.masters.add(getHost(it.channel, it.class.simpleName, it.encodedName))
}

try {
  cjoc.summary = [
    masters:cjoc.masters.size() + 1, //masters + cjoc
    masterCores:cjoc.masters*.cores.sum() + cjoc.cores,
    executors:cjoc.nodes*.executors.sum() + cjoc.masters*.nodes*.executors.sum().sum(),
    knownCloudExecutors:cjoc.masters*.clouds*.executorsCap.sum().findAll{it}.sum(0) + cjoc.clouds*.executorsCap.findAll{it}.sum(0)
  ]
}catch(e){}

def getHost(channel, type, name) {
  def host
  if(channel) {
    def stream = new ByteArrayOutputStream();
    def listener = new StreamBuildListener(stream);
    channel.call(new MasterGroovyClusterOpStep.Script('''
      def nodes = []
      Jenkins.instance.getNodes().each {
      try {
        import hudson.util.RemotingDiagnostics
        def isLinuxLabel = it.getLabelString().contains("LIN")
        def isWinLabel = it.getLabelString().contains("WIN")
        def chefStr = ""
        if (it.toComputer().isOnline() && isWinLabel) {
          chefStr = RemotingDiagnostics.executeGroovy("""
          def serr = new StringBuilder()
          def sout = new StringBuilder()
          def chefPSClientCmd = "chef-client -version"
          def proc = ["powershell", "-c", chefPSClientCmd].execute()
          proc.waitForProcessOutput(sout, serr);
          println sout;
          """, it.toComputer().getChannel());
        } 
        else if(it.toComputer().isOnline() && isLinuxLabel) {
           chefStr = RemotingDiagnostics.executeGroovy(chefClientCmd, it.toComputer().getChannel());
        }
      }
      catch(Exception ex) {
      }
      nodes.add([type:it.class.simpleName, name:it.displayName, label:it.labelString, executors:it.numExecutors, chefVersion:chefStr])
     }

    def host = [type:'$type', name:'$name', url:Jenkins.instance.rootUrl, cores:Runtime.runtime.availableProcessors(), nodes:nodes, offline:false];

    return new groovy.json.JsonBuilder(host).toString()
    ''', listener, "host-script.groovy", [:]));
    host = new groovy.json.JsonSlurper().parseText(stream.toString().minus("Result: "));
  } else {
    host = [type:type, name:name, offline:true]
  }
  return host;
}