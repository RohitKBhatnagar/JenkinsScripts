//All Nodes and Executors in Individual Controller

import com.cloudbees.opscenter.server.model.*;
import com.cloudbees.opscenter.server.clusterops.steps.*;
import static groovy.json.JsonOutput.*

//master, regular slaves, and shared slaves
      def nodes = []
      (Jenkins.instance.computers.grep { 
          it.class.superclass?.simpleName != 'AbstractCloudComputer' &&
          it.class.superclass?.simpleName != 'AbstractCloudSlave' &&
          it.class.simpleName != 'EC2AbstractSlave'
        } //+ Jenkins.instance.getAllItems(com.cloudbees.opscenter.server.model.SharedSlave.class)
      ).each {
        //printAllMethods(it)
        nodes.add([type:it.class.simpleName, name:it.displayName, executors:it.numExecutors, labels:it.assignedLabels.toString()])
      }

for (int i = 0; i < nodes.size(); i++) {
    println nodes.get(i)
}
println prettyPrint(toJson(nodes))

      //clouds
      def clouds = []
      Jenkins.instance.clouds.each {
        def cloud = [type:it.descriptor.displayName, name:it.displayName]
        try{
          cloud.executorsCap = it.templates?.inject(0, {a, c -> a + (c.numExecutors * c.instanceCap)})
        }catch(e){}
        try{
          cloud.executorsPerNode = it.numExecutors
        }catch(e){}
        clouds.add(cloud)
      }

      //shared clouds
      //Jenkins.instance.getAllItems(com.cloudbees.opscenter.server.model.SharedCloud.class).each {
        ////TODO may need to check either numExectors or numExecutors * instance caps
        //clouds.add([type:it.class.simpleName, name:it.displayName, executorsPerNode:it.cloud.numExecutors])
      //}

      def host = [type:'$type', name:'$name', url:Jenkins.instance.rootUrl, cores:Runtime.runtime.availableProcessors(), nodes:nodes, clouds:clouds, offline:false]

      return new groovy.json.JsonBuilder(host).toString()



void printAllMethods( obj ){
    if( !obj ){
    println( "Object is null\r\n" );
    return;
    }
  if( !obj.metaClass && obj.getClass() ){
        printAllMethods( obj.getClass() );
    return;
    }
  def str = "class ${obj.getClass().name} functions:\r\n";
  obj.metaClass.methods.name.unique().each{ 
    str += it+"(); "; 
  }
  println "${str}\r\n";
}

/*****
class hudson.model.Hudson$MasterComputer functions:
equals(); getClass(); hashCode(); notify(); notifyAll(); toString(); wait(); getSearch(); getSearchIndex(); getSearchName(); addAction(); addOrReplaceAction(); doContextMenu(); getAction(); getActions(); getAllActions(); getDynamic(); removeAction(); removeActions(); replaceAction(); replaceActions(); buildEnvironment(); cliConnect(); cliDisconnect(); cliOffline(); cliOnline(); connect(); countBusy(); countExecutors(); countIdle(); currentComputer(); disconnect(); doChangeOfflineCause(); doConfigDotXml(); doConfigSubmit(); doDoDelete(); doDumpExportTable(); doLaunchSlaveAgent(); doProgressiveLog(); doRssAll(); doRssFailed(); doRssLatest(); doScript(); doScriptText(); doToggleOffline(); getACL(); getAllExecutors(); getApi(); getAssignedLabels(); getBuilds(); getCaption(); getChannel(); getComputerPanelBoxs(); getConnectTime(); getDefaultCharset(); getDemandStartMilliseconds(); getDescription(); getDisplayExecutors(); getDisplayName(); getEnvVars(); getEnvironment(); getExecutors(); getHeapDump(); getHostName(); getIcon(); getIconAltText(); getIconClassName(); getIdleStartMilliseconds(); getLoadStatistics(); getLog(); getLogFile(); getLogRecords(); getLogText(); getMonitorData(); getName(); getNode(); getNumExecutors(); getOfflineCause(); getOfflineCauseReason(); getOneOffExecutors(); getRetentionStrategy(); getSearchUrl(); getSystemProperties(); getTarget(); getTerminatedBy(); getThreadDump(); getTiedJobs(); getTimeline(); getUrl(); getWorkspaceList(); interrupt(); isAcceptingTasks(); isConnecting(); isIdle(); isJnlpAgent(); isLaunchSupported(); isManualLaunchAllowed(); isOffline(); isOnline(); isPartiallyIdle(); isTemporarilyOffline(); isUnix(); launch(); recordTermination(); relocateOldLogs(); resolveForCLI(); setTemporarilyOffline(); taskAccepted(); taskCompleted(); taskCompletedWithProblems(); updateByXml(); waitUntilOffline(); waitUntilOnline(); hasPermission(); 
***/