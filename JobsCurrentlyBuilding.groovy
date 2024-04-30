import org.jenkinsci.plugins.workflow.job.WorkflowRun

def iWorkFlowBuildCount = 1
import java.text.SimpleDateFormat
def sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss.SSS")
def startDate = new Date()
println "Start time : ${sdf.format(startDate)}"
//----------------------------------------

//---------------------------------------------------------------------------
//Display jobs currently building....
//---------------------------------------------------------------------------


Jenkins.instance.getAllItems(Job.class).each 
{
  it -> 
  try
  {
    if(it.isBuilding())
    {
      try
      {
        //def bbSCM = null
        bbSCM = it.getScm()
        if(bbSCM)
        {
            println "#${iWorkFlowBuildCount}:	BUILDING Job-Name: \"${it.fullName}\" SCM-URL: \"${bbSCM.getUserRemoteConfigs()[0].getUrl()}\"";
            iWorkFlowBuildCount++;
        }
      }
      catch(MissingMethodException mmexp)
      {
        println "#${iWorkFlowBuildCount}:	BUILDING Job-Names: \"${it.fullName}\"  -- No SCM found!";
        iWorkFlowBuildCount++;
      }
      catch(Exception exp)
      {
        println "*************************************************************";
        println "Generic exception raised when checking for BUILDING job name!";
        println "${exp.message}"
        println "*************************************************************";
      }
    }
  }
  catch(Exception exp)
  {
    println "************************";
    println "Generic exception raised!";
    println "${exp.message}"
    println "************************";
  }
}


//--------------------------------------------------------------
def endDate = new Date()
println "End time: ${sdf.format(endDate)}"

use(groovy.time.TimeCategory) 
{
    def duration = endDate - startDate
  	print "Elapsed time: Days: ${duration.days}, Hours: ${duration.hours}, Minutes: ${duration.minutes}, Seconds: ${duration.seconds}"
}


//====================================