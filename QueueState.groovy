import java.text.SimpleDateFormat
def iNodeCount = 1
def iQItemsCount = 1

def sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss.SSS")
def startDate = new Date()
println "Start time : ${sdf.format(startDate)}"

println "-----------------------------------------------------------------------------------------------------------------------------------------"
println "Displays Current Queue State - Get current Queue/Executors state. Useful to troubleshoot queue and check that tasks are correctly handled"
println "------------- You may use the same script to clear the queue as well ********************************************************************"
println "-----------------------------------------------------------------------------------------------------------------------------------------"


def all = [ Jenkins.instance ]
all.addAll(Jenkins.instance.nodes)
all.each {
    def c = it.toComputer()
    println "$iNodeCount:   Node-Name: [$it.nodeName] , Executors: $it.numExecutors , Assigned-Labels: $it.assignedLabels , Accepting-Tasks: $it.acceptingTasks , Node-Properties: $it.nodeProperties , Offline: $c.offline"
  iNodeCount++
}
println "---------- Queue Item Details -------------"
Jenkins.instance.queue.items.each {
    println "[$iQItemsCount]:   Q-Iem-ID: $it.id , Blocked: $it.blocked , Buildable: $it.buildable , Stuck: $it.stuck , Assigned-Label: $it.assignedLabel , Causes: $it.causes"
	iQItemsCount++;
}

def queue = Hudson.instance.queue
println "Queue contains ${queue.items.length} items"
//Following call clears the queue
//queue.clear()
//println "Queue cleared - Items size - ${queue.items.length}"


def endDate = new Date()
println "End time: ${sdf.format(endDate)}"

use(groovy.time.TimeCategory) 
{
    def duration = endDate - startDate
  	print "Elapsed time: Days: ${duration.days}, Hours: ${duration.hours}, Minutes: ${duration.minutes}, Seconds: ${duration.seconds}"
}