import java.text.SimpleDateFormat
def iNodeCount = 1
def iQItemsCount = 1

def sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss.SSS")
def startDate = new Date()
println "Start time : ${sdf.format(startDate)}"
//================================================================================================================================================

println "-----------------------------------------------------------------------------------------------------------------------------------------"
println "Clears workspace on all Nodes. This is a direct DELETE command on all workspace. Please be careful when executing this script!"
println "-----------------------------------------------------------------------------------------------------------------------------------------"

//Master HostName and IP Address
println "Master HostName and IP Address executed on - ${java.net.InetAddress.getLocalHost()}"

import hudson.FilePath;

for (slave in hudson.model.Hudson.instance.slaves)
{
  println "Node: ${slave.name}"
  FilePath fp = slave.createPath(slave.getRootPath().toString() + File.separator + "workspace"); 
  println "${fp}"
  if(fp)
  {
  	fp.deleteRecursive(); 
  	println "Workspace has been cleared..."
  }
  else
    println "File path to the workspace NOT found!"
}


//================================================================================================================================================
def endDate = new Date()
println "End time: ${sdf.format(endDate)}"

use(groovy.time.TimeCategory) 
{
    def duration = endDate - startDate
  	print "Elapsed time: Days: ${duration.days}, Hours: ${duration.hours}, Minutes: ${duration.minutes}, Seconds: ${duration.seconds}"
}