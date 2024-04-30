//https://github.com/jenkinsci/jenkins/blob/master/core/src/main/java/hudson/model/OverallLoadStatistics.java
//Load Statistics

import hudson.model.OverallLoadStatistics;

OverallLoadStatistics overallLoadStatistics = new OverallLoadStatistics()

printAllMethods(overallLoadStatistics)

String sep = "|"
String summaryHeader = "Total_Executors${sep}Idle_Executors${sep}Queue_Length${sep}Snapshot${sep}"
String summary = "${overallLoadStatistics.computeTotalExecutors()}${sep}\
${overallLoadStatistics.computeIdleExecutors()}${sep}${overallLoadStatistics.computeQueueLength()}${sep}\
${overallLoadStatistics.computeSnapshot()}"

//println "${overallLoadStatistics.computeSnapshot().getConnectingExecutors()}"
//println "${overallLoadStatistics.computeSnapshot().connectingExecutors}"

println(summaryHeader)
println(summary)

// Filename: printAllMethodsExample.groovy
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


/***************************
 * https://javadoc.jenkins-ci.org/hudson/model/Queue.html
Build queue.
This class implements the core scheduling logic. Queue.Task represents the executable task that are placed in the queue. While in the queue, it's wrapped into Item so that we can keep track of additional data used for deciding what to execute when.

Items in queue goes through several stages, as depicted below:


 (enter) --> waitingList --+--> blockedProjects
                           |        ^
                           |        |
                           |        v
                           +--> buildables ---> pending ---> left
                                    ^              |
                                    |              |
                                    +---(rarely)---+
 
Note: In the normal case of events pending items only move to left. However they can move back if the node they are assigned to execute on disappears before their Executor thread starts, where the node is removed before the Queue.Executable has been instantiated it is safe to move the pending item back to buildable. Once the Queue.Executable has been instantiated the only option is to let the Queue.Executable bomb out as soon as it starts to try an execute on the node that no longer exists.

In addition, at any stage, an item can be removed from the queue (for example, when the user cancels a job in the queue.) See the corresponding field for their exact meanings.
****************************/