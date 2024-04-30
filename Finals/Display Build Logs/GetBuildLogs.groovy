#!groovy
import com.cloudbees.hudson.plugins.folder.AbstractFolder;
import org.jenkinsci.plugins.workflow.job.WorkflowJob
import org.jenkinsci.plugins.workflow.job.WorkflowRun

import hudson.console.PlainTextConsoleOutputStream
import java.io.ByteArrayOutputStream
import org.apache.commons.io.IOUtils

import java.text.SimpleDateFormat
def sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss.SSS")
def startDate = new Date()
println "Start time : ${sdf.format(startDate)}"

//---------------------------------------------------------------------------
//Fetches the build log (console output) for the Job-Name and Build# provided
//---------------------------------------------------------------------------

//Master HostName and IP Address
println "Master executed on - ${java.net.InetAddress.getLocalHost()}"

println "---------------------------------------------------------------------------"
println "Fetches the build log (console output) for the Job-Name and Build# provided"
println "---------------------------------------------------------------------------"


def folderName = "Builder-Tools-Engine"
def name = "test-maven-build-std"; //"hello_dmp_habitat"; //"aveksa-build-dev";
println "Job name being searched - ${name}";
def iBuildNo = 0
println "Build# being searched - ${iBuildNo}. If no build# is provided then script would look for last build details."

//println "${Jenkins.instance.getItemByFullName(folderName, AbstractFolder).getAllItems(WorkflowJob)}"

def iAllCount = 1;
def iCount = 0;
String logText = ""
def allItems = Jenkins.instance.getItemByFullName(folderName, AbstractFolder).getAllItems(WorkflowJob)
try
{
    def myItems = allItems.findAll 
    {
        try
        {
          //println "${iAllCount++} - ${it.name}"
          if(it.name == name)
          {
            WorkflowRun build
            if(iBuildNo > 0)
                build = it.getBuildByNumber(iBuildNo) 
            else
              build = it.getLastBuild()
            println "-------------------"
            println build
            println "-------------------"
            ByteArrayOutputStream os = new ByteArrayOutputStream()
            InputStreamReader is = build.logText.readAll()
            PlainTextConsoleOutputStream pos = new PlainTextConsoleOutputStream(os)
            IOUtils.copy(is, pos)
            println "Log file size - ${os.size()} bytes"
            logText = os.toString()
            println "==============================="
            //println "${logText}" //Don't print the log as it hijacks the screen session
            os.close()
            is.close()
            pos.close()
          }
        }
        catch(Exception exp)
        {
            println("${it.class.simpleName} - ${exp.message}")
        }
    }
}
catch(Exception excp)
{
    println "Generic exception - ${excp.message}"
}

//---------------------------------------------------------------------------
println "+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++"
def endDate = new Date()
println "End time: ${sdf.format(endDate)}"

use(groovy.time.TimeCategory) 
{
    def duration = endDate - startDate
    print "Elapsed time: Days: ${duration.days}, Hours: ${duration.hours}, Minutes: ${duration.minutes}, Seconds: ${duration.seconds}"
}

return null