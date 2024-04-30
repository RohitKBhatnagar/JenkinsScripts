import java.text.SimpleDateFormat
def iCount = 1

def sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss.SSS")
def startDate = new Date()
println "Start time : ${sdf.format(startDate)}"
//-------------------------------------------------

println "========================================================"
println "Find all builds performed within that past 24 hours whose console output contains the text listed in the search binding."
println "========================================================"

import hudson.console.PlainTextConsoleOutputStream
import java.io.ByteArrayOutputStream
import java.util.Date
import jenkins.model.Jenkins
import org.apache.commons.io.IOUtils
import org.jenkinsci.plugins.workflow.job.WorkflowJob
import org.jenkinsci.plugins.workflow.job.WorkflowRun

int HOURS_IN_MS = 3600000
Date oneDayAgo = new Date(System.currentTimeMillis() - (24 * HOURS_IN_MS));
Date thirtyDaysAgo = new Date(System.currentTimeMillis() - (24 * HOURS_IN_MS * 30)) //LocalDate.now().minusDays(30);//
Date sixtyDaysAgo = new Date(System.currentTimeMillis() - (24 * HOURS_IN_MS * 60))
Date nintyDaysAgo = new Date(System.currentTimeMillis() - (24 * HOURS_IN_MS * 90))

//def myItems = Jenkins.instance.getAllItems(WorkflowJob).findAll 
def myItems = Jenkins.instance.getAllItems().findAll 
{
  try
  {
	WorkflowRun build = it.builds.first()
  	(new Date(build.startTimeInMillis)).after(oneDayAgo)
  }
  catch(Exception exp)
  {}
}.collect 
{
  try
  {
     it.builds.findAll { WorkflowRun build ->
         (new Date(build.startTimeInMillis)).after(oneDayAgo)
     }
  }
  catch(Exception exp)
  {}
}.flatten().findAll 
{ WorkflowRun build ->
    println "------------------ ${iCount++}:    ${build} - Last-Build-Time: ${build.getTime()} - Build-Status: ${build.getBuildStatusSummary().message} -------------------------"
    ByteArrayOutputStream os = new ByteArrayOutputStream()
    InputStreamReader is = build.logText.readAll()
    PlainTextConsoleOutputStream pos = new PlainTextConsoleOutputStream(os)
    IOUtils.copy(is, pos)
    String text = os.toString()
  	println "${text}"
    //Boolean result = text.contains(search)
    os.close()
    is.close()
    pos.close()
    //result
 }*.absoluteUrl.join('\n')

println "----------- ^^^^^^^^^^^^^^^^^^^^ ---------------"
println "Total Builds on ${oneDayAgo} - ${myItems.size()}"
println "----------- ^^^^^^^^^^^^^^^^^^^^ ---------------"

//--------------------------------------------------
def endDate = new Date()
println "End time: ${sdf.format(endDate)}"

use(groovy.time.TimeCategory) 
{
    def duration = endDate - startDate
  	print "Elapsed time: Days: ${duration.days}, Hours: ${duration.hours}, Minutes: ${duration.minutes}, Seconds: ${duration.seconds}"
}