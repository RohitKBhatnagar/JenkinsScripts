//==============================================================================================
//Generic Date and Time Print and Elapsed Time
//==============================================================================================
//import groovy.time.*
import java.text.SimpleDateFormat
def startDate = new Date()
//def startTime = startDate.getTime()
//startDate = Date.parse("yyy-MM-dd'T'HH:mm:ss.SSSZ",startDate.replace("+01:00","+0100"))
//startDate = Date.parse("yyy-MM-dd'T'HH:mm:ss.SSSZ",startDate.replaceAll(/([+\-])(\d\d):(\d\d)/, /$1$2$3/))
def sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss.SSS")
println sdf.format(startDate)

def endDate = new Date()
//endDate = Date.parse("yyy-MM-dd'T'HH:mm:ss.SSSZ",endDate.replace("+01:00","+0100"))
//endDate = Date.parse("yyy-MM-dd'T'HH:mm:ss.SSSZ",endDate.replaceAll(/([+\-])(\d\d):(\d\d)/, /$1$2$3/))
//def endTime = endDate.getTime()
println sdf.format(endDate)

//TimeDuration duration = TimeCategory.minus(endTime, startTime)
//println duration

use(groovy.time.TimeCategory) 
{
    def duration = endDate - startDate
  print "Days: ${duration.days}, Hours: ${duration.hours}, Seconds: ${duration.seconds}"
}

//++++++++++++++++++++++++++++++++++++++++++++++++++++++++
//==============================================================================================
//Print all jobs as freestyle and maven [based upon these projects defined as 'AbstractProject']
//==============================================================================================

def iCount = 1
import java.text.SimpleDateFormat
def sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss.SSS")
def startDate = new Date()
println "Start time : ${sdf.format(startDate)}"


Jenkins.instance.getAllItems(AbstractProject.class).each 
{
  it -> 
  try
  {
    //def bbSCM = null
    bbSCM = it.getScm()
    if(bbSCM)
    {
      println "#${iCount}:	Job-Name: \"${it.fullName}\" SCM-URL: \"${bbSCM.getUserRemoteConfigs()[0].getUrl()}\"";
      iCount++;
    }
    //else
      //println "#${iCount++}:	Job-Name: \"${it.fullName}\"";
  }
  catch(MissingMethodException mmexp)
  {
    println "#${iCount}:	Job-Name: \"${it.fullName}\"";
    iCount++;
  }
  catch(Exception exp)
  {
    println "*************************";
  	println "Generic exception raised!";
    println "*************************";
  }
}

def endDate = new Date()
println "End time: ${sdf.format(endDate)}"

use(groovy.time.TimeCategory) 
{
    def duration = endDate - startDate
  	print "Elapsed time: Days: ${duration.days}, Hours: ${duration.hours}, Minutes: ${duration.minutes}, Seconds: ${duration.seconds}"
}

//++++++++++++++++++++++++++++++++++++++++++++++++++++++++
//==============================================================================================
//Print all pipeline jobs [based upon these projects defined as a Workflow Job]
//==============================================================================================

def iCount = 1
import java.text.SimpleDateFormat
def sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss.SSS")
def startDate = new Date()
println "Start time : ${sdf.format(startDate)}"


//Jenkins.instance.getAllItems(AbstractProject.class).each 
Hudson.instance.getAllItems(org.jenkinsci.plugins.workflow.job.WorkflowJob).each
{
  it -> 
  try
  {
    //def bbSCM = null
    bbSCM = it.getScm()
    if(bbSCM)
    {
      println "#${iCount}:	Job-Name: \"${it.fullName}\" SCM-URL: \"${bbSCM.getUserRemoteConfigs()[0].getUrl()}\"";
      iCount++;
    }
    //else
      //println "#${iCount++}:	Job-Name: \"${it.fullName}\"";
  }
  catch(MissingMethodException mmexp)
  {
    println "#${iCount}:	Job-Name: \"${it.fullName}\"";
    iCount++;
  }
  catch(Exception exp)
  {
    println "*************************";
  	println "Generic exception raised!";
    println "*************************";
  }
}

def endDate = new Date()
println "End time: ${sdf.format(endDate)}"

use(groovy.time.TimeCategory) 
{
    def duration = endDate - startDate
  	print "Elapsed time: Days: ${duration.days}, Hours: ${duration.hours}, Minutes: ${duration.minutes}, Seconds: ${duration.seconds}"
}


//++++++++++++++++++++++++++++++++++++++++++++++++++++++++
//============================================================================================================
//Print all multi-branch pipeline jobs [based upon these projects defined as a WorkflowMultiBranchProject Job]
//============================================================================================================


def iCount = 1
import java.text.SimpleDateFormat
def sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss.SSS")
def startDate = new Date()
println "Start time : ${sdf.format(startDate)}"


//Jenkins.instance.getAllItems(AbstractProject.class).each 
//Hudson.instance.getAllItems(org.jenkinsci.plugins.workflow.job.WorkflowJob).each
Jenkins.instance.getAllItems(org.jenkinsci.plugins.workflow.multibranch.WorkflowMultiBranchProject).each
{
  it -> 
  try
  {
    //def bbSCM = null
    bbSCM = it.getScm()
    if(bbSCM)
    {
      println "#${iCount}:	Job-Name: \"${it.fullName}\" SCM-URL: \"${bbSCM.getUserRemoteConfigs()[0].getUrl()}\"";
      iCount++;
    }
    //else
      //println "#${iCount++}:	Job-Name: \"${it.fullName}\"";
  }
  catch(MissingMethodException mmexp)
  {
    println "#${iCount}:	Job-Names: \"${it.fullName}\"";
    iCount++;
  }
  catch(Exception exp)
  {
    println "*************************";
  	println "Generic exception raised!";
    println "*************************";
  }
}

def endDate = new Date()
println "End time: ${sdf.format(endDate)}"

use(groovy.time.TimeCategory) 
{
    def duration = endDate - startDate
  	print "Elapsed time: Days: ${duration.days}, Hours: ${duration.hours}, Minutes: ${duration.minutes}, Seconds: ${duration.seconds}"
}


//++++++++++++++++++++++++++++++++++++++++++++++++++++++++
//============================================================================================================
//Print all jobs [based upon last build time based upon predefined cutoff days as limit] built with timestamp
//============================================================================================================

// Set how old the jobs to list should be (in days)
def numDaysBack = 2
def iCount = 1

println "==================================================================================="
println "List all jobs based upon last build time based upon predefined cutoff days as limit"
println "==================================================================================="

import java.text.SimpleDateFormat
def sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss.SSS")
def startDate = new Date()
println "Start time : ${sdf.format(startDate)}"


def cutOfDate = System.currentTimeMillis() - 1000L * 60 * 60 * 24 * numDaysBack

for (job in Jenkins.instance.getAllItems(Job.class)) 
{
  build = job.getLastSuccessfulBuild()
  if (build != null && build.getTimeInMillis() < cutOfDate) 
  {
    println "#${iCount++}:	Job-Name: \"${job.getFullName()}\"	Build#: \"${build.getNumber()}\" Build-Result: \"${build.getResult()}\" Build-TimeStamp2: \"${build.getTimestampString2()}\" Build-TimeStamp: \"${build.getTimestampString()}\""
    
  }
}

def endDate = new Date()
println "End time: ${sdf.format(endDate)}"

use(groovy.time.TimeCategory) 
{
    def duration = endDate - startDate
  	print "Elapsed time: Days: ${duration.days}, Hours: ${duration.hours}, Minutes: ${duration.minutes}, Seconds: ${duration.seconds}"
}






//^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

//================= Play ============== DONOT RUN ==================
import org.jenkinsci.plugins.workflow.job.WorkflowRun

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

                        try
                        {
                            //def bbSCM = null
                            bbSCM = item.getScm()
                            if(bbSCM)
                            {
                              println "#${iWorkFlowBuildCount}:	BUILDING WorkFlow-Job-Name: \"${it.fullName}\" Class-Name: \"${item.class}\" SCM-URL: \"${bbSCM.getUserRemoteConfigs()[0].getUrl()}\"";
                                iWorkFlowBuildCount++;
                            }
                        }
                        catch(MissingMethodException mmexp)
                        {
                            println "#${iWorkFlowBuildCount}:	BUILDING WorkFlow-Job-Names: \"${it.fullName}\" Class-Name: \"${item.class}\"";
                            iWorkFlowBuildCount++;
                        }
                        catch(Exception exp)
                        {
                            println "**************************************";
                            println "Generic exception for WorkFlow raised!";
                            println "**************************************";
                        }

                      }
                      //println "Killed ${run}"
                  } 
              else if(item in FreeStyleBuild) 
                  {
                      FreeStyleBuild run = (FreeStyleBuild) item
                      if(run) //if(!dryRun) 
                        {
                              //run.executor.interrupt(Result.ABORTED)

                            try
                            {
                                //def bbSCM = null
                                bbSCM = item.getScm()
                                if(bbSCM)
                                {
                                    println "#${iWorkFlowBuildCount}:	FREESTYLE-BUILD WorkFlow-Job-Name: \"${item.fullName}\" SCM-URL: \"${bbSCM.getUserRemoteConfigs()[0].getUrl()}\"";
                                    iWorkFlowBuildCount++;
                                }
                            }
                            catch(MissingMethodException mmexp)
                            {
                                println "#${iWorkFlowBuildCount}:	FREESTYLE-BUILD WorkFlow-Job-Names: \"${item.fullName}\"";
                                iWorkFlowBuildCount++;
                            }
                            catch(Exception exp)
                            {
                                println "*********************************************";
                                println "Generic exception for FREESTYLE-BUILD raised!";
                                println "*********************************************";
                            }

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


