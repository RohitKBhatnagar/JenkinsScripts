/**
* Kill long runnign jobs across all controllers based upon the threshold set
**/

import groovy.time.TimeCategory
import hudson.model.Queue
import org.jenkinsci.plugins.workflow.job.WorkflowRun
import org.jenkinsci.plugins.workflow.support.steps.ExecutorStepExecution

/**
 * Try to infer the WorkflowRun of the executable passed in.
 * @param executable The executable
 * @return The WorkflowRun, or null if this is not a Pipeline run
 */
WorkflowRun getPipelineRunFromExecutable(Queue.Executable executable) {
    if (executable instanceof WorkflowRun) {
        return ((WorkflowRun) executable)
    }

    if (executable.parent instanceof ExecutorStepExecution.PlaceholderTask) {
        def executorPlaceholderTask = ((ExecutorStepExecution.PlaceholderTask) executable.parent)
        return ((WorkflowRun) executorPlaceholderTask.runForDisplay())
    }

    return null
}

def doForAllPipelineInProgress = { Closure closure ->
    use(TimeCategory) {
        def delay = 4.hours
        def processedPipeline = []
        jenkins.model.Jenkins.instanceOrNull.getComputers().each { computer ->

            computer.executors.findAll { exec -> exec.isBusy() && exec.currentExecutable && exec.elapsedTime > delay.toMilliseconds() }.each { exec ->
                println("I am aboring this build: " + exec.getCurrentExecutable())
                exec.interrupt(Result.ABORTED)
                def currentPipelineRun = getPipelineRunFromExecutable(exec.currentExecutable)
                if (currentPipelineRun) {
                    def pipelineRunId = currentPipelineRun.getExternalizableId()
                    if(!processedPipeline.contains(pipelineRunId)) {
                        processedPipeline.add(pipelineRunId)
                        closure(exec, currentPipelineRun)
                    } else {
                        "   (Already processed ${currentPipelineRun})"
                    }
                }
            }
        }
    }
}

boolean somethingHappened = false
doForAllPipelineInProgress { exec, run ->
    println " * Stopping ${run.fullDisplayName} that spent ${exec.elapsedTime}ms building on ${exec.owner.displayName} #${exec.number}..."
  //  run.doStop()
    somethingHappened = true
}

if(somethingHappened) {
    somethingHappened = false
    sleep(30000)
    doForAllPipelineInProgress { exec, run ->
        println " * Forcibly Terminating ${run.fullDisplayName} that spent ${exec.elapsedTime}ms building on ${exec.owner.displayName} #${exec.number}..."
       // run.doTerm()
        somethingHappened = true
    }
}

if(somethingHappened) {
    somethingHappened = false
    sleep(30000)
    doForAllPipelineInProgress { exec, run ->
        println " * Forcibly Killing ${run.fullDisplayName} that spent ${exec.elapsedTime}ms building on ${exec.owner.displayName} #${exec.number}..."
       // run.doKill()
        somethingHappened = true
    }
}
return