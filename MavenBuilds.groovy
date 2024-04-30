import hudson.model.*
import hudson.tasks.*

import java.text.SimpleDateFormat
def iCount = 1

def sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss.SSS")
def startDate = new Date()
println "Start time : ${sdf.format(startDate)}"

//---------------------------------------------------------------------------
//Display Maven builds
//---------------------------------------------------------------------------

 
Jenkins.instance.getAllItems(hudson.maven.MavenModuleSet.class).findAll{job -> job.isBuildable()}.each
//Jenkins.instance.getAllItems(hudson.maven.MavenModuleSet.class).each //Gets all Maven jobs
{
    job ->
    //job.getDisabledModules(true).each{module -> module.delete()}
    println(iCount++ + ": \"" + job.name + "\"")
}

def endDate = new Date()
println "End time: ${sdf.format(endDate)}"

use(groovy.time.TimeCategory) 
{
    def duration = endDate - startDate
  	print "Elapsed time: Days: ${duration.days}, Hours: ${duration.hours}, Minutes: ${duration.minutes}, Seconds: ${duration.seconds}"
}