//Manually kills a Move/Copy/Promote Job

use(groovy.time.TimeCategory) 
{
    def delay = 1.hour
    jenkins.model.Jenkins.instanceOrNull.getComputers()
            .each { 
              computer ->
                computer.getAllExecutors().findAll { 
                  it.isBusy() }.each { 
                  exec ->
                    println(exec)
                    //println(exec.source.fullName)
                    if (exec.elapsedTime > delay.toMilliseconds()) {
                      if(exec.currentExecutable instanceof com.cloudbees.opscenter.replication.ItemReplicationRecord)
                      {
                        println(exec.currentExecutable.source.fullName)
                         if(exec.currentExecutable.source.fullName=="AcquiringSubsystem/CI_JOBS/CI-Jobs-By_Using_Master_Commits") {
                            println " * Aborting ${exec.currentExecutable} that spent ${exec.elapsedTime}ms building..."
                              //Uncomment below lines if you wish to manually abort a Move/Copy/Promote Job
                              /**exec.interrupt(hudson.model.Result.ABORTED, new jenkins.model.CauseOfInterruption() {
                                @Override
                                String getShortDescription() {
                                    return "Build was suspended for too long. See Jenkins administrators"
                                }
                              })**/
                           
                          } 
                      }
                    }
                }
            }
}
return