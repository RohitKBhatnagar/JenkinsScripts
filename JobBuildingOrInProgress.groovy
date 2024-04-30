def iFreeStyleBuildCount = 1
def iWorkFlowBuildCount = 1
import java.text.SimpleDateFormat
def sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss.SSS")
def startDate = new Date()
println "Start time : ${sdf.format(startDate)}"

Jenkins.instance.getAllItems(Job.class).findAll 
{
    it.isBuilding()	// || it.isInProgress()
}.collect 
  { 
    	Job job ->
          //find all matching items and return a list but if null then return an empty list
          job.builds.findAll 
            { 
              Run run ->
                run.isBuilding()
            } ?: []
  }.sum().each 
		{ 
          	Run item ->
              if(item in WorkflowRun) 
                  {
                      WorkflowRun run = (WorkflowRun) item
                      if(run) //if(!dryRun) 
                      {
                          //hard kill
                          //run.doKill()
                          //release pipeline concurrency locks
                          //StageStepExecution.exit(run)
                      }
                      //println "Killed ${run}"
                  } 
              else if(item in FreeStyleBuild) 
                  {
                      FreeStyleBuild run = (FreeStyleBuild) item
                      if(!dryRun) 
                          {
                              //run.executor.interrupt(Result.ABORTED)
                          }
                      //println "Killed ${run}"
                  } 
              else 
                  {
                      println "WARNING: Don't know how to handle ${item.class}"
                  }
		}

def endDate = new Date()
println "End time: ${sdf.format(endDate)}"

use(groovy.time.TimeCategory) 
{
    def duration = endDate - startDate
  	print "Elapsed time: Days: ${duration.days}, Hours: ${duration.hours}, Minutes: ${duration.minutes}, Seconds: ${duration.seconds}"
}