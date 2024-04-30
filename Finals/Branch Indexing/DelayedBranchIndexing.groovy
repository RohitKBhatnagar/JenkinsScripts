import groovy.time.TimeCategory

/**
 * Find all branch indexing that have been running for more than the **delay** specified 
 */
use(TimeCategory) {
  def delay = 30.minutes /*hours*/
  jenkins.model.Jenkins.instanceOrNull.toComputer().allExecutors
    .findAll { exec -> 
      exec.elapsedTime > delay.toMilliseconds()
      exec.currentExecutable != null && 
      exec.currentExecutable instanceof jenkins.branch.MultiBranchProject.BranchIndexing 
    }.each { exec ->
      println " * Branch Indexing of ${exec.currentExecutable.parent.fullDisplayName} has spent ${exec.elapsedTime}ms scanning on ${exec.owner.displayName} #${exec.number}..."
    }
}

return