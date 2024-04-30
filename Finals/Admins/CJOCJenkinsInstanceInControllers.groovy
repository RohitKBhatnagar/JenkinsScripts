//URL - https://github.com/cloudbees/jenkins-scripts/blob/master/count-cjoc-json.groovy
//Will work only in Script Console and NOT in CJOC hosted pipeline
//Result is JSON but all combined at CJOC itself!
//Modified to include active instance name in the output - https://www.tabnine.com/code/java/classes/hudson.model.Computer
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
    ]
}catch(e){}

def getHost(channel, type, name){
  def host
  if(channel){
    def stream = new ByteArrayOutputStream();
    def listener = new StreamBuildListener(stream);
    channel.call(new MasterGroovyClusterOpStep.Script("""
      //master, regular slaves, and shared slaves
      def version = jenkins.model.Jenkins.instance.getLegacyInstanceId()

def host = [type:'$type', name:'$name', url:Jenkins.instance.rootUrl, cores:Runtime.runtime.availableProcessors(), InstanceID:version, offline:false, hostname:Jenkins.getActiveInstance().getComputer("").getHostName(), environment:Jenkins.getActiveInstance().getComputer("").getEnvironment()]

      return new groovy.json.JsonBuilder(host).toString()
    """, listener, "host-script.groovy", [:]));
    host = new groovy.json.JsonSlurper().parseText(stream.toString().minus("Result: "));
  } else {
    host = [type:type, name:name, offline:true]
  }
  return host;
}

return new groovy.json.JsonBuilder(cjoc).toPrettyString()