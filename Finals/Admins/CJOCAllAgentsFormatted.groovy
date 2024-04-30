//URL - https://github.com/cloudbees/jenkins-scripts/blob/master/count-cjoc-json.groovy
//Will work only in Script Console and NOT in CJOC hosted pipeline
//Result is JSON but all combined at CJOC itself!
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
    executors:cjoc.nodes*.executors.sum() + cjoc.masters*.nodes*.executors.sum().sum()
  ]
}catch(e){}

def getHost(channel, type, name){
  def host
  if(channel){
    def stream = new ByteArrayOutputStream();
    def listener = new StreamBuildListener(stream);
    channel.call(new MasterGroovyClusterOpStep.Script("""
      //master, regular slaves, and shared slaves
      def nodes = []
      Jenkins.instance.nodes.each {
        try{
        nodes.add([type:it.class.simpleName, name:it.displayName, executors:it.numExecutors, labels:it.getLabelString()])
        }catch(e){println e.message}
      }

      def host = [type:'$type', name:'$name', url:Jenkins.instance.rootUrl, cores:Runtime.runtime.availableProcessors(), nodes:nodes, offline:false]

      return new groovy.json.JsonBuilder(host).toString()
    """, listener, "host-script.groovy", [:]));
    host = new groovy.json.JsonSlurper().parseText(stream.toString().minus("Result: "));
  } else {
    host = [type:type, name:name, offline:true]
  }

  def controllerName = ''
  host.eachWithIndex { it, index ->
    def KeyName = it.getKey()
    //println KeyName
    //println "${it}, ${it.getClass()}, ${index}, ${index.getClass()}, ${it.getKey()}, ${it.getKey().getClass()}, ${it.getValue()}, ${it.getValue().getClass()}"
    if(KeyName.contains('name'))
    {
      controllerName = it.getValue()
      //println controllerName
    }
    if(KeyName.contains('nodes'))
    {
      def KeyValues = it.getValue()
      //println "Value - ${KeyValues}"
      KeyValues.each { itNodes ->
        //We have Array list storing an Array here, hence we need to iterate once more
        //println itNodes
        def agentExecutors = ''
        def agentLabel = ''
        def agentName = ''
        itNodes.each { agent ->
          if(agent.getKey().contains('executors'))
            agentExecutors = agent.getValue()
          if(agent.getKey().contains('labels'))
            agentLabel = agent.getValue()
          if(agent.getKey().contains('name'))
            agentName = agent.getValue()
          //println "${agent}, ${agent.getClass()}, ${agent.getKey()}, ${agent.getValue()}"
        }
        println "${controllerName}, '${agentName}', ${agentExecutors}, ${agentLabel}"
      }
    }
  }
  return host;
}

//println ""
//println "==================================="
//return new groovy.json.JsonBuilder(cjoc).toPrettyString()


