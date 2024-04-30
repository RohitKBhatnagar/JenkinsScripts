//Displays ALL Nodes and executor status alongwith Queue item details

def all = [ Jenkins.instance ]
all.addAll(Jenkins.instance.nodes)
println "S.No, Hostname, Executors, Assigned Labels, Accepting Tasks, Env Variables, Offline"
println "===================================================================================="
all.eachWithIndex { it, iCount ->
    def c = it.toComputer()
    println "$iCount, [$it.nodeName], $it.numExecutors, \"$it.assignedLabels\", $it.acceptingTasks, $it.nodeProperties.envVars, $c.offline"
}
println "----"
println "S.No, Queue ID, Q Wait Time (String), Q Wait Time (ms), Blocked, Buildable, Stuck, Q Label, Cause, Job Type, Name, URL, Agent Label"
println "==========================================================================================================================================="
Jenkins.instance.queue.items.eachWithIndex { it, iCount ->
    if(it.task instanceof org.jenkinsci.plugins.workflow.multibranch.WorkflowMultiBranchProject)
      println "$iCount, $it.id, $it.inQueueForString, $it.inQueueSince, $it.blocked, $it.buildable, $it.stuck, $it.assignedLabel, $it.causeOfBlockage.shortDescription, MULTI-BRANCH, $it.task.fullDisplayName, $it.task.url, $it.task.assignedLabel"
    if(it.task instanceof org.jenkinsci.plugins.workflow.job.WorkflowJob)
      println "$iCount, $it.id, $it.inQueueForString, $it.inQueueSince, $it.blocked, $it.buildable, $it.stuck, $it.assignedLabel, $it.causeOfBlockage.shortDescription, WORKFLOW, $it.task.fullDisplayName, $it.task.url, $it.task.assignedLabel"
}

return
