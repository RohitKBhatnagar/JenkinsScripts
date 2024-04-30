//URL - https://docs.cloudbees.com/docs/cloudbees-ci-kb/latest/troubleshooting-guides/abort-move-copy-promote
//Abort MCP Job
use(groovy.time.TimeCategory) {
    def delay = 1.hour
    jenkins.model.Jenkins.instanceOrNull.getComputers()
            .each { computer ->
                computer.getAllExecutors().findAll { it.isBusy() }.each { exec ->
                    if (exec.elapsedTime > delay.toMilliseconds()) {
                      if(exec.currentExecutable instanceof com.cloudbees.opscenter.replication.ItemReplicationRecord
                         //&& exec.currentExecutable.source.fullName=="AlbertaOpsStage") {
                         && exec.currentExecutable.source.fullName.contains("AlbertaOpsStage")) {
                            println " * Aborting ${exec.currentExecutable} that spent ${exec.elapsedTime}ms building..."
//                            exec.interrupt(hudson.model.Result.ABORTED, new jenkins.model.CauseOfInterruption() {
//                                @Override
//                                String getShortDescription() {
//                                    return "Build was suspended for too long. See Jenkins administrators"
//                                }
//                            })
                        }
                    }
                }
            }
}
return

//* Aborting ItemReplicationRecord{number=7562, mode=COPY, source=jenkins://77f52756d8ee0117f496ae9e66807dc8/AlbertaOpsStage/Test-Playground/test-Nikolay/E2E/gradle-tests-executor, destination=jenkins://77f52756d8ee0117f496ae9e66807dc8/AlbertaOpsStage/Test-Playground/test-Nikolay/gradle-tests-executor, userId='e081586', result=null} that spent 5680266ms building...