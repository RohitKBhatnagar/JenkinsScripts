//Find currently building jobs with assigned label

import jenkins.model.Jenkins
import hudson.*
import hudson.model.*
import jenkins.*
import org.jenkinsci.plugins.workflow.job.WorkflowJob



def nodeName = 'jnk4stl43'

// Remove everything which is currently queued
//Jenkins.instance.queue.clear()
def buildingJobs = Jenkins.instance.getAllItems(Job.class).findAll {
    it.isBuilding()
}

buildingJobs.each { build ->
  //printAllMethods(build)
  urls = []
  build.eachWithIndex { it, itCount ->  
    try {
      //if(it.class == 'org.jenkinsci.plugins.workflow.job.WorkflowJob')
      if(it instanceof WorkflowJob)
      {
        println "${itCount}, ${it.getAssignedLabel()}, ${it.getFullName()}, ${it.getDisplayNameOrNull()}, ${it.getAbsoluteUrl()}, ${it.getRootDir()}, ${it.getFullDisplayName()}"; //println "${it.name}"
      	nodeName == it.getAssignedLabel() && urls << it.absoluteUrl
      }
      else
      {
        println "${itCount}, ${it.class}, ${it.builtOnStr}, ${it.getFullName()}, ${it.getDisplayNameOrNull()}, ${it.getAbsoluteUrl()}, ${it.getRootDir()}, ${it.getFullDisplayName()}";//println "${it.class}, ${it.name}"
        nodeName == it.builtOnStr && urls << it.absoluteUrl
      }
    }
    catch(Exception ex)
    {
      println ("EXCEPTION - " + ex.message) // + " , " + printAllMethods(it.getClass()))
    }
  }
}
urls && println("${job.name}\n\t${urls.sort().join('\n\t')}")

void printAllMethods( obj ){
    if( !obj ){
    println( "Object is null\r\n" );
    return;
    }
  if( !obj.metaClass && obj.getClass() ){
        printAllMethods( obj.getClass() );
    return;
    }
  def str = "class ${obj.getClass().name} functions:\r\n";
  obj.metaClass.methods.name.unique().each{ 
    str += it+"(); "; 
  }
  println "${str}\r\n";
}
               
return null