#!groovy

import java.text.SimpleDateFormat
def iCount = 1
def iBldCount = 1;

def sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss.SSS")
def startDate = new Date()
println "Start time : ${sdf.format(startDate)}"
//-------------------------------------------------

println "================================================================================================="
println "Find all builds performed after certain date for any top level folder name provided!"
println "================================================================================================="

import hudson.console.PlainTextConsoleOutputStream
import java.io.ByteArrayOutputStream
import java.util.Date
import jenkins.model.Jenkins
import org.apache.commons.io.IOUtils
import org.jenkinsci.plugins.workflow.job.WorkflowJob
import org.jenkinsci.plugins.workflow.job.WorkflowRun
import com.cloudbees.hudson.plugins.folder.AbstractFolder;

def folderName="PrepaidManagementServicesBizOps" // change value here for the full name of the folder you want to query against
def Date dtAfterInDays;
use( groovy.time.TimeCategory ) 
{
  dtAfterInDays = new Date().plus(-30)
}

Boolean bUseJobName = true;
if(folderName.length() <= 0)
	bUseJobName = false;


String strMaster = java.net.InetAddress.getLocalHost();
def allItems;
if(bUseJobName)
	allItems = Jenkins.instance.getItemByFullName(folderName, AbstractFolder).getAllItems(WorkflowJob)
else
	allItems = Jenkins.instance.getAllItems(WorkflowJob) 
//def allItems = Jenkins.instance.getAllItems()
println "================================================================================================="
println "Total Item count on ${strMaster} is '${allItems.size()}'"
println "Displaying builds performed after ------- ${dtAfterInDays} --------";
println "================================================================================================="
try
{
    def myItems = allItems.findAll 
    {
        try
        {
            WorkflowRun build = it.builds.first()
				new Date(build.startTimeInMillis).after(dtAfterInDays)
            //println "------------------ ${iCount++}:    ${build} -------------------------"
        }
        catch(Exception exp)
        {
            //println("${it.class.simpleName} - ${exp.message}")
        }
    }.collect 
    {
        it.builds.findAll 
        { 
            WorkflowRun build ->
				new Date(build.startTimeInMillis).after(dtAfterInDays)
        }
    }
    .flatten().findAll 
    { 
        WorkflowRun build ->
        println "------------------ ${iBldCount++}:    ${build} - Build-Time: ${build.time} - Build-Status: ${build.getBuildStatusSummary().message} -------------------------"
    }*.absoluteUrl.join('\n')

    println "Build count after - ${dtAfterInDays} are - ${iBldCount-1}"; //${myItems.size()}"
}
catch(Exception exp)
{
    println "EXCEPTION - ${exp.message}"
}

//--------------------------------------------------
def endDate = new Date()
println "End time: ${sdf.format(endDate)}"

use(groovy.time.TimeCategory) 
{
    def duration = endDate - startDate
  	print "Elapsed time: Days: ${duration.days}, Hours: ${duration.hours}, Minutes: ${duration.minutes}, Seconds: ${duration.seconds}"
}