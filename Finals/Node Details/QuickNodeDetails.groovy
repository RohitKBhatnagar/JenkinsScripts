//master, regular slaves, and shared slaves
def nodes = []
println "Controller - ${Jenkins.instance.rootUrl}"//, cores:${Runtime.runtime.availableProcessors()}"
Jenkins.instance.computers.each {
  if(it.class.simpleName != 'SharedSlave') {
 	nodes.add(type:it.class.simpleName, name:it.displayName, executors:it.numExecutors, labels:it.assignedLabels.toString())
    println "${it.class.simpleName}, ${it.displayName}, ${it.numExecutors}, ${it.assignedLabels.toString()}"
  }
  else
    nodes.add(type:it.class.simpleName, name:it.displayName, executors:it.numExecutors)
}
println nodes
println "#################"
def host = [url:Jenkins.instance.rootUrl, cores:Runtime.runtime.availableProcessors(), nodes:nodes, offline:false]
//return new groovy.json.JsonBuilder(host).toString()
//println new groovy.json.JsonBuilder(host).toPrettyString()
host.each{entry -> println "$entry.key: $entry.value"}

println "+++++++++++++++++"
/*if(host.any{it -> it.value instanceof String} == true)
	println host
*/
println "-----------------"
/*def labels = host.collect{entry -> 
	println "$entry.value"
}*/
println "================="
//println labels
//prinltn "${host.toString()}"
return null

/*
class hudson.model.Hudson$MasterComputer functions: equals(); getClass
(); hashCode(); notify(); notifyAll(); toString(); wait(); getSearch
(); getSearchIndex(); getSearchName(); addAction(); addOrReplaceAction
(); doContextMenu(); getAction(); getActions(); getAllActions(); getDynamic
(); removeAction(); removeActions(); replaceAction(); replaceActions
(); buildEnvironment(); cliConnect(); cliDisconnect(); cliOffline
(); cliOnline(); connect(); countBusy(); countExecutors(); countIdle
(); currentComputer(); disconnect(); doChangeOfflineCause(); doConfigDotXml
(); doConfigSubmit(); doDoDelete(); doDumpExportTable(); doLaunchSlaveAgent
(); doProgressiveLog(); doRssAll(); doRssFailed(); doRssLatest(); doScript
(); doScriptText(); doToggleOffline(); getACL(); getAllExecutors(); getApi
(); getAssignedLabels(); getBuilds(); getCaption(); getChannel
(); getComputerPanelBoxs(); getConnectTime(); getDefaultCharset
(); getDemandStartMilliseconds(); getDescription(); getDisplayExecutors
(); getDisplayName(); getEnvVars(); getEnvironment(); getExecutors
(); getHeapDump(); getHostName(); getIcon(); getIconAltText
(); getIconClassName(); getIdleStartMilliseconds(); getLoadStatistics
(); getLog(); getLogFile(); getLogRecords(); getLogText(); getMonitorData
(); getName(); getNode(); getNumExecutors(); getOfflineCause
(); getOfflineCauseReason(); getOneOffExecutors(); getRetentionStrategy
(); getSearchUrl(); getSystemProperties(); getTarget(); getTerminatedBy
(); getThreadDump(); getTiedJobs(); getTimeline(); getUrl(); getWorkspaceList
(); interrupt(); isAcceptingTasks(); isConnecting(); isIdle(); isJnlpAgent
(); isLaunchSupported(); isManualLaunchAllowed(); isOffline(); isOnline
(); isPartiallyIdle(); isTemporarilyOffline(); isUnix(); launch
(); recordTermination(); relocateOldLogs(); resolveForCLI
(); setTemporarilyOffline(); taskAccepted(); taskCompleted
(); taskCompletedWithProblems(); updateByXml(); waitUntilOffline
(); waitUntilOnline(); hasPermission(); 
*/