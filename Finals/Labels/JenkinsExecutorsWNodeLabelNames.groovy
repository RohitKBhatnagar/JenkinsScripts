#!groovy
//Snaphot of Jenkins LoadStatistics

import hudson.model.LoadStatistics;
import hudson.model.LoadStatistics.LoadStatisticsSnapshot;
import hudson.model.LoadStatistics.LoadStatisticsUpdater;

def loadStats = Jenkins.instance.unlabeledLoad; //https://javadoc.jenkins.io/jenkins/model/Jenkins.html#unlabeledLoad
println "**************************************************"

def allLoad = Jenkins.instance.overallLoad; //https://javadoc.jenkins.io/hudson/model/OverallLoadStatistics.html
def allLoadStatsSnap = allLoad.computeSnapshot();
//println "All Idle Executors - ${allLoadStatsSnap.getIdleExecutors()}"
//println "Overall # of idle executors - ${allLoad.computeIdleExecutors()}"
//println "Overall # of Q length - ${allLoad.computeQueueLength()}"
println "Overall # of executors - ${allLoad.computeTotalExecutors()}"

println "=====================================";
println "====== OVERALL LOAD ======";
println "=====================================";
def overAllNodes = allLoad.getNodes();
//println "Nodes - ${overAllNodes}";

println "#, Label, Host Name, Executors"
overAllNodes.eachWithIndex { it, i ->
  println "$i, ${it.getLabelString()}, ${it.getDisplayName()}, ${it.getNumExecutors()}"
}

return;