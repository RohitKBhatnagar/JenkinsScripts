import hudson.model.*
import hudson.tasks.*

import java.text.SimpleDateFormat
def iCount = 1

def sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss.SSS")
def startDate = new Date()
println "Start time : ${sdf.format(startDate)}"

println "---------------------------------------------------------------------------"
println "Display Maven tasks of Freestyle Jobs"
println "---------------------------------------------------------------------------"

 
// Jenkins.instance.getAllItems(hudson.maven.MavenModuleSet.class).findAll{job -> job.isBuildable()}.each
// //Jenkins.instance.getAllItems(hudson.maven.MavenModuleSet.class).each //Gets all Maven jobs
// {
//     job ->
//     //job.getDisabledModules(true).each{module -> module.delete()}
//     println(iCount++ + ": \"" + job.name + "\"")
// }


// For each project
//for(item in Hudson.instance.allItems) 
for(item in Jenkins.instance.allItems) 
{
  if(item instanceof FreeStyleProject) 
  {
    println(iCount++ + ": FreeStyle Project -> JOB-Name - " + item.name);
    // Find current recipients defined in project
    for (builder in item.builders)
    {
      println(">> " + builder);
      if (builder instanceof Maven) 
      {
        println(">>MAVEN BUILDER");
        println(">>TARGETS : " + builder.targets);
        println(">>NAME : " + builder.mavenName);
        println(">>POM : " + builder.pom);
        // .properties is overridden by groovy
        println(">>PROPERTIES : " + builder.@properties);
        println(">>JVM-OPTIONS : " + builder.jvmOptions);
        println(">>USE PRIVATE REPO : " + builder.usePrivateRepository);
        println(">>USER SETTINGS : " + builder.settings);
        println(">>GLOBAL SETTINGS : " + builder.globalSettings);
      }
    }
    println("\n=======\n");
  }
}


def endDate = new Date()
println "End time: ${sdf.format(endDate)}"

use(groovy.time.TimeCategory) 
{
    def duration = endDate - startDate
  	print "Elapsed time: Days: ${duration.days}, Hours: ${duration.hours}, Minutes: ${duration.minutes}, Seconds: ${duration.seconds}"
}