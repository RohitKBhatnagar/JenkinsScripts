//CJOC (Script Console and as Cluster Job) - Gets Controller-Name, Role Name and Permissions for the Role-Name against all controllers
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
      import jenkins.model.Jenkins;
      import nectar.plugins.rbac.strategy.*;
      import hudson.security.*;
      import nectar.plugins.rbac.groups.*;
      import nectar.plugins.rbac.roles.*;
      def roles = []
      RoleMatrixAuthorizationStrategyImpl strategy = RoleMatrixAuthorizationStrategyImpl.getInstance()
      RoleMatrixAuthorizationConfig config = RoleMatrixAuthorizationPlugin.getConfig()

      config.getRoles().each { r ->
        Role rc = new Role(r)
        rc.getPermissionProxies().each{p -> roles.add([role:r, permissions:p.id, identifier:p.name])}
      }

      def host = [name:'$name', url:Jenkins.instance.rootUrl, roles:roles]

      return new groovy.json.JsonBuilder(host).toString()
    """, listener, "host-script.groovy", [:]));
    host = new groovy.json.JsonSlurper().parseText(stream.toString().minus("Result: "));
  } else {
    host = [type:type, name:name]
  }
  return host;
}

return new groovy.json.JsonBuilder(cjoc).toPrettyString()