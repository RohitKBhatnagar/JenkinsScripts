/***
* Terminate all currently running jobs on the executors
**/

import jenkins.model.Jenkins
import hudson.*
import hudson.model.*
import jenkins.*

// Remove everything which is currently queued

//Jenkins.instance.queue.clear() //This will clear the queue

def buildingJobs = Jenkins.instance.getAllItems(Job.class).findAll {
    it.isBuilding()
}

println "Count, Full Name, Display Name, Job URL, Root Dir"; //, Full Display Name";
println "=====, =========, ============, =======, ========"; //, =================";
buildingJobs.eachWithIndex { it, itCount ->
    def jobName = it.toString()
    def val = jobName.split("\\[|\\]")
  	println "${itCount}, ${it.getFullName()}, ${it.getDisplayNameOrNull()}, ${it.getAbsoluteUrl()}, ${it.getRootDir()}"; //, ${it.getFullDisplayName()}";
  //printAllMethods(it.getParent())

    // 'Abort jobs' is the name of the job I have created, and I do not want it to abort itself.

    /*if((val[1].trim())!='Abort jobs') {
        def job = Jenkins.instance.getItemByFullName(val[1].trim())
        for (build in job.builds) {
            if (build.isBuilding()) {
                println(build)
                build.doStop();
            }
        }
    }
	*/
}

return null

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