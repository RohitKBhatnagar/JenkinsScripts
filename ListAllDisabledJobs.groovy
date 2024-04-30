#!Groovy
import java.text.SimpleDateFormat
def iCount = 1

def sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss.SSS")
def startDate = new Date()
println "Start time : ${sdf.format(startDate)}"

//================================================

println "------------------------------------------------------------------------------"
println "Displays all disabled jobs and types on ${java.net.InetAddress.getLocalHost()}"
println "------------------------------------------------------------------------------"


//jenkins.model.Jenkins.instance.getAllItems(jenkins.model.ParameterizedJobMixIn.ParameterizedJob.class).findAll
jenkins.model.Jenkins.instance.getAllItems().findAll
{
  job -> !(
    		job.metaClass.methods*.name.findAll
           	{
             	method -> method == "isDisabled"
           	}.isEmpty()
          ) 
}.findAll
{
  job -> job.disabled
}.each
{
  job ->

  def className = job.getClass();
  def lastBuild = job.getLastBuild()? job.getLastBuild().number: "Never built once";
  def lastBuildOnDate = job.getLastBuild()? job.getLastBuild().getTime():"Never built once"
  def url = job.getUrl()
  println "${iCount++}: ClassName: ${className.simpleName} | URL: ${url} | Build# ${lastBuild} | Built-On: ${lastBuildOnDate} | Disabled: ${job.disabled}"
}

//=================================
def endDate = new Date()
println "End time: ${sdf.format(endDate)}"

use(groovy.time.TimeCategory) 
{
    def duration = endDate - startDate
  	print "Elapsed time: Days: ${duration.days}, Hours: ${duration.hours}, Minutes: ${duration.minutes}, Seconds: ${duration.seconds}"
}