#!Groovy

import java.text.SimpleDateFormat
def iCount = 1

def sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss.SSS")
def startDate = new Date()
println "Start time : ${sdf.format(startDate)}"

//Master HostName and IP Address
println "Master HostName and IP Address - ${java.net.InetAddress.getLocalHost()}"

//---------------------------------------------------------------------------
//All Nodes - Java Version
//---------------------------------------------------------------------------

println "---------------------------------------------------------------------------"
println "All Nodes - Java Instances installed as well as default version"
println "---------------------------------------------------------------------------"

import hudson.model.Node
import hudson.model.Slave
import jenkins.model.Jenkins
import hudson.util.RemotingDiagnostics

Jenkins jenkins = Jenkins.instance
for (Node node in jenkins.nodes) {
  // Make sure slave is online
  if (!node.toComputer().online) {
    println "Node '$node.nodeName' is currently offline - skipping check"
    continue;
  } else {
    //if(node.nodeName == 'ech-10-157-153-71')
    //{
      props = node.toComputer().getSystemProperties();
      println "Node '$node.nodeName' is running " + props.get('java.runtime.version');

      String agent_name = node.nodeName;

      try {
          //groovy script you want executed on an agent
          // String groovy_script = '''
          // println System.getenv("PATH")
          // println "uname -a".execute().text
          // '''.trim()

          boolean isUnix = Boolean.TRUE.equals(node.toComputer().isUnix());
          //println "Linux instance - ${isUnix}"
          String groovy_script
          if(isUnix)
          {
              groovy_script = '''
              println "ls -latrch /sys_apps_01/java/".execute().text
              '''.trim()
          }
          else
          {
              groovy_script = '''
              println "dir /cygdrive/c/'Program Files'/Java".execute().text
              '''.trim()
          }

          String result = RemotingDiagnostics.executeGroovy(groovy_script, node.channel);
          println result;
      }
      catch(Exception exp)
      {
          println "${exp.message}"
      }
    //}
  }
}

//---------------------------------------------------------------------------
def endDate = new Date()
println "End time: ${sdf.format(endDate)}"

use(groovy.time.TimeCategory) 
{
    def duration = endDate - startDate
      print "Elapsed time: Days: ${duration.days}, Hours: ${duration.hours}, Minutes: ${duration.minutes}, Seconds: ${duration.seconds}"
}