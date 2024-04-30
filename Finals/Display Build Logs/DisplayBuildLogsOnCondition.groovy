#!groovy
import java.text.SimpleDateFormat
def iCount = 1
def iItemCount = 1
def iJobCount = 1

def sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss.SSS")
def startDate = new Date()
println "Start time : ${sdf.format(startDate)}"

//-------------------------------------------------------------------------------------------------------------
//Displays build logs based upon last-build-performed, based upon some conditions [if build result is FAILURE]
//Will improvise using this URL - https://medium.com/faun/how-to-get-jenkins-build-job-details-b8c918087030
//--------------------------------------------------------------------------------

//Master HostName and IP Address
println "Master executed on - ${java.net.InetAddress.getLocalHost()}"

def strCondition = "SUCCESS"
def jenkins = Jenkins.getInstance()
//Get Workspaces for all immediate items
def topItems = jenkins.getItems()
topItems.each
{
    //https://docs.huihoo.com/javadoc/jenkins/1.512/hudson/model/Item.html
  topI ->
  //println "Workspace Directory for ${topI} is : ${jenkins.getWorkspaceFor(topI)}"
  println "\nItem# ${iItemCount++} Workspace Directory for ${topI.getName()} is : ${jenkins.getWorkspaceFor(topI)}"
  println "*******************************************************************"
  //println "\t\tJobs for ${topI} is : ${topI.getAllJobs()}"
  def allItemJobs = topI.getAllJobs() //https://docs.huihoo.com/javadoc/jenkins/1.512/hudson/model/Job.html
  iJobCount = 1
  allItemJobs.each
  {
    itemJob ->
    try 
    {
        //println "\t Job - ${itemJob}"
        //println "\t Job - ${itemJob.getName()}"
        println "\t Job# ${iJobCount++} - ${itemJob.getName()}"
        println "================================================================="
        
        def String jobResult =  itemJob.getLastBuild().result;
        //if(itemJob.getLastBuild().result.equalsIgnoreCase(strCondition))
        if(strCondition.equalsIgnoreCase(jobResult))
        {
            println "\t\t Last Build details - ${itemJob.getLastBuild()} | Time - ${itemJob.getLastBuild().getTime()} | Result - ${itemJob.getLastBuild().result}"
            println "-----------------------------------------------------------------"
        }
        else //"FAILURE"
        {
            println "\t\t Last Build details - ${itemJob.getLastBuild()} | Time - ${itemJob.getLastBuild().getTime()} | Result - ${itemJob.getLastBuild().result}"
            println "-----------------------------------------------------------------"
            //println "+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++"
            println "${itemJob.getLastBuild().logText.readAll()}"
        }
    }
    catch(NullPointerException npexp)
    {}
    catch(Exception exp)
    {
        println "Exception - ${exp.message}"
    }
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