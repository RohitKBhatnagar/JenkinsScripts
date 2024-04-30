import hudson.tasks.*

import java.text.SimpleDateFormat
def iCount = 1

def sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss.SSS")
def startDate = new Date()
println "Start time : ${sdf.format(startDate)}"

//----------------------------------------------------------------------------------

println "=========================================================================================="
println "Display all items and associated projects/jobs with associated counts by Type"
println "=========================================================================================="
println ">>>>>>>>> CD Master - ${java.net.InetAddress.getLocalHost()} <<<<<<<<<<<<<<<<<<<<<<<<<<<<<"

 
import hudson.model.Job
import jenkins.model.Jenkins

projects = [:]
Jenkins.instance.getAllItems(Job.class).each 
{ Job j ->
    String jc = j.class.simpleName
    if(!(jc in projects)) 
    {
        projects[jc] = 0
    }
    projects[jc]++
}

println "Count projects by type:"
projects.each 
{ k, v ->
    println "    ${k}: ${v}"
}

//----------------------------------------------------------------------------------
def endDate = new Date()
println "End time: ${sdf.format(endDate)}"

use(groovy.time.TimeCategory) 
{
    def duration = endDate - startDate
  	print "Elapsed time: Days: ${duration.days}, Hours: ${duration.hours}, Minutes: ${duration.minutes}, Seconds: ${duration.seconds}"
}

return null