// Cancel running builds
def numCancels = 0
for (job in Jenkins.instance.items) {
  if (job.hasProperty('builds')) {
    for (build in job.builds) {
      if(build.)
      if (build.isBuilding()) {
            println "Stopping ${build.toString()}"
            build.doStop();
            println "${build.toString()} stopped."
            numCancels++
        }
    }
  }
}
println "${numCancels} Jobs canceled."


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


/////////////
////https://cd3.mastercard.int/jenkins/job/DISMIDS/job/Build/job/mid-services/
import hudson.model.*
def q = Jenkins.instance.queue
q.items.findAll { it.task.name.contains('DISMIDS') }.eachWithIndex { it, i ->
//  q.cancel(it.task) 
  println "$i, ${it.task}"
  q.cancel(it.task)
}