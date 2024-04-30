#!groovy
import java.text.SimpleDateFormat
//def iItemCount = 1
def iJobCount = 0

def sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss.SSS")
def startDate = new Date()
println "Start time : ${sdf.format(startDate)}"

//----------------------------------------------------------------------
//Displays total job counts for top level items on the Jenkins Instance
//----------------------------------------------------------------------

//Master HostName and IP Address
println "Master executed on - ${java.net.InetAddress.getLocalHost()}"

println "----------------------------------------------------------------------"
println "Displays total item counts for top level items on the Jenkins Instance"
println "----------------------------------------------------------------------"

def jenkins = Jenkins.getInstance()
//Get Workspaces for all immediate items
def topItems = jenkins.getItems()
topItems.eachWithIndex
{
  topI, topICount ->
  def allItemJobs = topI.getAllJobs() //https://docs.huihoo.com/javadoc/jenkins/1.512/hudson/model/Job.html
  iJobCount = 1
  allItemJobs.each
  {
    itemJob ->
    try 
    {
        iJobCount++
    }
    catch(Exception exp)
    {
        println "Exception - ${exp.message}"
    }
  }
  println "${topICount} | Item-Name : ${topI.getName()} | Item-Count : ${iJobCount}"
}

//---------------------------------------------------------------------------
def endDate = new Date()
println "End time: ${sdf.format(endDate)}"

use(groovy.time.TimeCategory) 
{
    def duration = endDate - startDate
      print "Elapsed time: Days: ${duration.days}, Hours: ${duration.hours}, Minutes: ${duration.minutes}, Seconds: ${duration.seconds}"
}