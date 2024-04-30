#!groovy
//Snaphot of Jenkins LoadStatistics

import hudson.model.LoadStatistics;
import hudson.model.LoadStatistics.LoadStatisticsSnapshot;
import hudson.model.LoadStatistics.LoadStatisticsUpdater;

def loadStats = Jenkins.instance.unlabeledLoad; //https://javadoc.jenkins.io/jenkins/model/Jenkins.html#unlabeledLoad
//def loadStatsSnap = loadStats.LoadStatistics.LoadStatisticsSnapshot;
//def loadStatsUpdater = LoadStatistics.LoadStatisticsUpdater.doRun();
//def loadStatsBldr = LoadStatistics.LoadStatisticsSnapshot.Builder;

println "${loadStats}";
//println "${loadStatsUpdater}";
println "${loadStats.availableExecutors}";


def loadStatsSnap = loadStats.computeSnapshot();
println "Idle Executors - ${loadStatsSnap.getIdleExecutors()}"
println "Queue Length - ${loadStatsSnap.getQueueLength()}"
println "Total Executors - ${loadStatsSnap.getOnlineExecutors()}";

//println "${loadStatsBldr}";
//println "${loadStatsUpdater}";
println "=====================================";
println "====== UNLABELED LOAD ======";
println "=====================================";
def allNodes = loadStats.getNodes();
//println "Nodes - ${allNodes}";

allNodes.eachWithIndex { it, i ->
  println "$i - $it, Label - ${it.getLabelString()}, Name - ${it.getDisplayName()}, Executors - ${it.getNumExecutors()}, URL - ${it.getSearchUrl()}, Accepting - ${it.	isAcceptingTasks()}}"
}

println "**************************************************"

def allLoad = Jenkins.instance.overallLoad; //https://javadoc.jenkins.io/hudson/model/OverallLoadStatistics.html
def allLoadStatsSnap = allLoad.computeSnapshot();
//println "All Idle Executors - ${allLoadStatsSnap.getIdleExecutors()}"
println "Overall # of idle executors - ${allLoad.computeIdleExecutors()}"
println "Overall # of Q length - ${allLoad.computeQueueLength()}"
println "Overall # of executors - ${allLoad.computeTotalExecutors()}"

println "=====================================";
println "====== OVERALL LOAD ======";
println "=====================================";
def overAllNodes = allLoad.getNodes();
//println "Nodes - ${overAllNodes}";

overAllNodes.eachWithIndex { it, i ->
  println "$i - Label - ${it.getLabelString()}, Name - ${it.getDisplayName()}, Executors - ${it.getNumExecutors()}, URL - ${it.getSearchUrl()}, Accepting - ${it.	isAcceptingTasks()}}"
}

return;