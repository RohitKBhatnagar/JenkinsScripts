//Quick queue check
/*** BEGIN META {
 "name" : "Get Queue State",
 "comment" : "Get current Queue/Executors state. Useful to troubleshoot queue and check that tasks are correctly handled",
 "parameters" : [ ],
 "core": "1.642",
 "authors" : [
 { name : "Allan Burdajewicz" }
 ]
 } END META**/

println "+++++++++++++++ Node Details +++++++++++++++++++"
def all = [ Jenkins.instance ]
all.addAll(Jenkins.instance.nodes)
all.each {
    def c = it.toComputer()
    println "Node - [$it.nodeName], Executors - $it.numExecutors, Label - $it.assignedLabels, Accepted Tasks - $it.acceptingTasks, Properties - $it.nodeProperties, Offline - $c.offline"
}
println ""
println "---- Queue Details -----------"
Jenkins.instance.queue.items.each {
    println "ID - $it.id, Blocked -  $it.blocked, Buildable - $it.buildable, Stuck - $it.stuck, Assigned Label - $it.assignedLabel, Cause - $it.causes"
}

return null