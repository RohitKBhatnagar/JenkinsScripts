#!Groovy
import java.text.SimpleDateFormat
def iCount = 1

def sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss.SSS")
def startDate = new Date()
println "Start time : ${sdf.format(startDate)}"
//-------------------------------------------------

println "======================================================="
println "Find all build counts performed prior to specified date"
println "======================================================="

import hudson.console.PlainTextConsoleOutputStream
import java.io.ByteArrayOutputStream
import java.util.Date
import jenkins.model.Jenkins
import org.apache.commons.io.IOUtils
import org.jenkinsci.plugins.workflow.job.WorkflowJob
import org.jenkinsci.plugins.workflow.job.WorkflowRun
int HOURS_IN_MS = 3600000
Date oneDayAgo = new Date().minus(1);
Date twoDaysAgo = new Date().minus(2);
Date thirtyDaysAgo = new Date().minus(30);
def thirtyDaysTime = new Date().minus(30).getTime()
Date sixtyDaysAgo = new Date().minus(60);
Date nintyDaysAgo = new Date().minus(90);


def allItems = Jenkins.instance.getAllItems()

println "Total Items - ${allItems.size()}"

projects = [:]
//def myItems = Jenkins.instance.getAllItems(WorkflowJob).findAll 
def myItems = allItems.findAll 
{
    if(!(it.class.simpleName in projects)) 
    {
        projects[it.class.simpleName] = 0
    }
    projects[it.class.simpleName]++

    try
    {
        if(it instanceof FreeStyleProject)
        {
            try
            {
                if(it.builds.size() > 0)
                {
                    FreeStyleBuild build = it.builds.first()
                    new Date(build.startTimeInMillis).before(new Date().minus(30))
                    //println "FreeStyleProject# ${iCount++}: Build:: ${build} | Result: ${build.result} | Number: ${build.number} | TimeStamp: ${new Date(build.startTimeInMillis).format('MM/dd/yyyy')}"
                }
            }
            catch(Exception ex)
            {
                println("${it.class.simpleName} - ${ex.message}")
            }
        }
        else if(it instanceof WorkflowJob)
        {
            try
            {
                if(it.builds.size() > 0)
                {
                    WorkflowRun build = it.builds.first()
                    if(build)
                    {
                        new Date(build.startTimeInMillis).before(new Date().minus(30))
                        //println "WORKFLOWJOB# ${iCount++}: Build:: ${build} | Result: ${build.result} | Number: ${build.number} | TimeStamp: ${new Date(build.startTimeInMillis).format('MM/dd/yyyy')}"
                    }
                }
            }
            catch(Exception ex)
            {
                println("${it.class.simpleName} - ${ex.message}")
            }
        }
        else if (it instanceof com.cloudbees.hudson.plugins.folder.Folder)
        {
            //println "Folder type!"
            //Do Nothing
        }
        else if(it instanceof org.jenkinsci.plugins.workflow.multibranch.WorkflowMultiBranchProject)
        {
            //println "WorkflowMultiBranchProject type!"
            //Do Nothing
        }
        else
        {
            //println "Unknown type!"
            //Do Thing
        }
    }
    catch(Exception exp)
    {
        println("${it} - ${it.class.simpleName} - ${exp.message}")
    }
}

println "Build count before ${new Date().minus(30)} - ${myItems.size()}"

println "Count projects by type:"
projects.each { k, v ->
    println "    ${k}: ${v}"
}

//--------------------------------------------------
def endDate = new Date()
println "End time: ${sdf.format(endDate)}"

use(groovy.time.TimeCategory) 
{
    def duration = endDate - startDate
  	println "Elapsed time: Days: ${duration.days}, Hours: ${duration.hours}, Minutes: ${duration.minutes}, Seconds: ${duration.seconds}"
}