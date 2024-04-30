//Identify HUNG build executors....
import groovy.time.TimeCategory

/**
 * Find all branch indexing that have been running for more than the **delay** specified and interrupt them.
 */
use(TimeCategory) {
  def delay = 2.minutes
  jenkins.model.Jenkins.instanceOrNull.toComputer().allExecutors
    .findAll { exec ->
      //exec.elapsedTime > delay.toMilliseconds()
//      exec.currentExecutable != null &&
      exec.currentExecutable instanceof jenkins.branch.MultiBranchProject.BranchIndexing
    }.each { exec ->
      println " ${exec.getState()}, * Stopping Branch Indexing of ${exec.currentExecutable.parent.fullDisplayName} that spent ${exec.elapsedTime}ms scanning on ${exec.owner.displayName} #${exec.number}..."
      
      /*if(exec.currentExecutable.parent.fullDisplayName.equals("DISMIDS » Build » CI » mid-services")) {
        println "Aborting the thread"
        exec.stop()
        
      }*/
      
      
      /*exec.interrupt(hudson.model.Result.ABORTED, new jenkins.model.CauseOfInterruption() {
        @Override
        String getShortDescription() {
          return "Branch Indexing was running for too long. See Jenkins administrators"
        }
      })*/
    }
}

return 
  