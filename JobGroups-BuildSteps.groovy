import hudson.model.*
import hudson.tasks.*

import java.text.SimpleDateFormat
def iCount = 1

def sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss.SSS")
def startDate = new Date()
println "Start time : ${sdf.format(startDate)}"

//---------------------------------------------------------------------------
//Display jobs group by the build steps they use
//---------------------------------------------------------------------------

 
//All the projects on which we can apply the getBuilders method
def allProjects = Hudson.instance.items.findAll{ it instanceof Project }
 
//All the registered build steps in the current Jenkins Instance
def allBuilders = Builder.all()
 
//Group the projects by the build steps used
def projectsGroupByBuildSteps = allBuilders.inject([:]){
   map, builder ->  
   map[builder.clazz.name] = allProjects.findAll{it.builders.any{ it.class.name.contains(builder.clazz.name)}}.collect{it.name}
   map
}
 
//presentation
projectsGroupByBuildSteps.each{
   println """--- $iCount --- $it.key ---
   \t$it.value\n"""
   iCount++;
}

def endDate = new Date()
println "End time: ${sdf.format(endDate)}"

use(groovy.time.TimeCategory) 
{
    def duration = endDate - startDate
  	print "Elapsed time: Days: ${duration.days}, Hours: ${duration.hours}, Minutes: ${duration.minutes}, Seconds: ${duration.seconds}"
}