/*
   This script counts up all of the active builds across all agents and prints the total.
*/
import jenkins.model.Jenkins

Jenkins j = Jenkins.instance

int active_builds = 0
int inactive_executors = 0
j.slaves.each { slave ->
    if(!slave.computer.isOffline()) {
        def executors = slave.computer.executors
        executors.each { executor ->
            if(executor.isBusy()) {
                active_builds++
            } else {
                inactive_executors++
            }
        }
    }
}
println "Queue: ${j.queue.items.size()}, Active: ${active_builds}, Free executors: ${inactive_executors}"