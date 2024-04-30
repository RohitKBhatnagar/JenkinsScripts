#!groovy
import java.text.SimpleDateFormat
//def iCount = 1
def iItemCount = 1
def iJobCount = 1
def iTotalBuilds = 0
def iTotalItems = 0

def sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss.SSS")
def startDate = new Date()
println "Start time : ${sdf.format(startDate)}"

//-----------------------------------------------------------
//Collects build status of all jobs against a top level item
//-----------------------------------------------------------

//Master HostName and IP Address
println "Master HostName and IP Address - ${java.net.InetAddress.getLocalHost()}"

def strCondition = "SUCCESS"
def jenkins = Jenkins.getInstance()
//Get Workspaces for all immediate items
def topItems = jenkins.getItems()
topItems.each
{
  topI ->
  iTotalItems++ //Counts total top level items
  //println "${iItemCount++} | ${topI.getName()}"
  def allItemJobs = topI.getAllJobs() //https://docs.huihoo.com/javadoc/jenkins/1.512/hudson/model/Job.html
  iJobCount = 1
  allItemJobs.each
  {
    itemJob ->
    try 
    {
        iTotalBuilds++ //Counts total builds in entire master
        def String jobResult =  itemJob.getLastBuild().result;
        def String strPrint = ""
        if(strCondition.equalsIgnoreCase(jobResult))
        {
            //println "\t\t Last Build details - ${itemJob.getLastBuild()} | Time - ${itemJob.getLastBuild().getTime()} | Result - ${itemJob.getLastBuild().result}"
            strPrint = "${itemJob.getLastBuild()} | Time - ${itemJob.getLastBuild().getTime()} | Result - ${itemJob.getLastBuild().result} | Duration - ${itemJob.getLastBuild().durationString}"
        }
        else //"FAILURE"
        {
            //println "\t\t Last Build details - ${itemJob.getLastBuild()} | Time - ${itemJob.getLastBuild().getTime()} | Result - ${itemJob.getLastBuild().result}"
            strPrint = "${itemJob.getLastBuild()} | Time - ${itemJob.getLastBuild().getTime()} | Result - ${itemJob.getLastBuild().result} | Duration - ${itemJob.getLastBuild().durationString}"
            //println "+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++"
            //println "${itemJob.getLastBuild().logText.readAll()}"
        }
        //println "\t${iJobCount++} | ${itemJob.getName()} | ${strPrint}"
        println "${iItemCount++} | ${topI.getName()} | ${iJobCount++} | ${itemJob.getName()} | ${strPrint}"
    }
    catch(NullPointerException npexp)
    {
      //println "Exception - ${npexp.message}"
      //println "\t\t\t${iJobCount++} | ${itemJob.getName()} | ${npexp.message}"
      println "${iItemCount++} | ${topI.getName()} | ${iJobCount++} | ${itemJob.getName()} | NULL-POINTER EXCEPTION | ${npexp.message}"
    }
    catch(Exception exp)
    {
        //println "Exception - ${exp.message}"
        //println "\t\t\t\t${iJobCount++} | ${itemJob.getName()} | ${exp.message}"
        println "${iItemCount++} | ${topI.getName()} | ${iJobCount++} | ${itemJob.getName()} | GENERIC EXCEPTION | ${exp.message}"
    }
  }
}

println "Grand Totals | ${iTotalItems} | ${iTotalBuilds}"
//---------------------------------------------------------------------------
def endDate = new Date()
println "End time: ${sdf.format(endDate)}"

use(groovy.time.TimeCategory) 
{
    def duration = endDate - startDate
  	print "Elapsed time: Days: ${duration.days}, Hours: ${duration.hours}, Minutes: ${duration.minutes}, Seconds: ${duration.seconds}"
}