import java.text.SimpleDateFormat
def iCount = 1

def sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss.SSS")
def startDate = new Date()
println "Start time : ${sdf.format(startDate)}"
//--------------------------------------------------


import jenkins.model.Jenkins;


println "========================================================="
println "Lists name and java version installed on each Agents"
println "========================================================="
 
/*** BEGIN META {
  "name" : "Show Agent Java Version",
  "comment" : "It lists name and java version installed on each Agents"
} END META**/

import hudson.model.Node
import hudson.model.Slave
import jenkins.model.Jenkins

Jenkins jenkins = Jenkins.instance
for (Node node in jenkins.nodes) {
  // Make sure slave is online
  if (!node.toComputer().online) {
    println "Node '$node.nodeName' is currently offline - skipping check"
    continue;
  } else {
    props = node.toComputer().getSystemProperties();
    println "${iCount++}:   Node '$node.nodeName' is running with Java version - " + props.get('java.runtime.version');
    println "${props}";
    //println "Node '$node.nodeName' is running with Maven version - " + props.get('mvn.runtime.version');
    //println "Node '$node.nodeName' is running with .NET Core version - " + props.get('dotnet.runtime.version');
  }
}

//--------------------------------------------------
def endDate = new Date()
println "End time: ${sdf.format(endDate)}"

use(groovy.time.TimeCategory) 
{
    def duration = endDate - startDate
  	print "Elapsed time: Days: ${duration.days}, Hours: ${duration.hours}, Minutes: ${duration.minutes}, Seconds: ${duration.seconds}"
}
