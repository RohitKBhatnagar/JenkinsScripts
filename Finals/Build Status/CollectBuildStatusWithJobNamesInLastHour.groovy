#!groovy
import java.text.SimpleDateFormat
//def iCount = 1
def iItemCount = 1
def iJobCount = 1
def iTotalBuilds = 0
def iTotalItems = 0
def iIsBuilding = 0
def iOlderThan60Mins = 0
def iNoBuilds = 0 //These should fall under NULLPointerExceptions
def iLastHrBuilds = 0 //Build counts in last hr only

def sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss.SSS")
def startDate = new Date()
println "Start time : ${sdf.format(startDate)}"

//-----------------------------------------------------------------------------
//Collects build status of all jobs against a top level item in the last 1 Hour
//-----------------------------------------------------------------------------

//Master HostName and IP Address
println "Master HostName and IP Address - ${java.net.InetAddress.getLocalHost()}"

//import java.time.*
import groovy.time.TimeCategory

// Define hour to compare (hour=24 will find builds that were built more than 1 day ago)
def iMin = 60;
def iSec = 60;
def iHrInSecs = iMin * iSec;
def calNow = Calendar.instance;
def lstBuilds = [];


def jenkins = Jenkins.getInstance()
//Get Workspaces for all immediate items
def topItems = jenkins.getItems()
topItems.each
{
  topI ->
  iTotalItems++ //Counts total top level items
  //println "${iItemCount++} | ${topI.getName()}"
  //if (!topItems.disabled)
  //{
    def allItemJobs = topI.getAllJobs() //https://docs.huihoo.com/javadoc/jenkins/1.512/hudson/model/Job.html
    iJobCount = 1
    def String strPrint = ""
    allItemJobs.each
    {
        itemJob ->
        try 
        {
            //if(allItemJobs.getLastBuild() != null)
            //{
                iTotalBuilds++ //Counts total builds in entire master
                if(itemJob.building)
                    iIsBuilding++;
                def build_time = itemJob.getLastBuild().startTimeInMillis;
                //println "${build_time} - ${new Date(build_time)}"
				def before60Mins
				use( TimeCategory ) 
				{
                  before60Mins = new Date() - 60.minutes
                  //println "${before60Mins}"
                }
          		if(!new Date(build_time).before(before60Mins))
          		{
                    iLastHrBuilds++
                    def String jobResult =  itemJob.getLastBuild().result;
                    strPrint = "DETAILS | ${itemJob.getLastBuild()} | Time | ${itemJob.getLastBuild().getTime()} | Result | ${itemJob.getLastBuild().result} | Duration | ${itemJob.getLastBuild().durationString} | Duration(ms) | ${itemJob.getLastBuild().getDuration()}"
                    println "ITEMS | ${iItemCount++} | ${topI.getName()} | ${iJobCount++} | ${itemJob.getName()} | ${strPrint}"
                    System.out.println "ITEMS | ${iItemCount++} | ${topI.getName()} | ${iJobCount++} | ${itemJob.getName()} | ${strPrint}"
                }
                else 
                    iOlderThan60Mins++
            //}
        }
        catch(NullPointerException npexp)
        {
            iNoBuilds++
            ////println "\t\t\t${iJobCount++} | ${itemJob.getName()} | ${npexp.message}"
            //println "${iItemCount++} | ${topI.getName()} | ${iJobCount++} | ${itemJob.getName()} | NULL-POINTER EXCEPTION | ${npexp.message}"
        }
        catch(Exception exp)
        {
            ////println "\t\t\t\t${iJobCount++} | ${itemJob.getName()} | ${exp.message}"
            println "${iItemCount++} | ${topI.getName()} | ${iJobCount++} | ${itemJob.getName()} | GENERIC EXCEPTION | ${exp.message}"
        }
    }
  //}
}

println "ITEMS Totals | TOTAL ITEMS | ${iTotalItems} | TOTAL BUILDS | ${iTotalBuilds} | OLD BUILDS | ${iOlderThan60Mins} | BUILDING | ${iIsBuilding} | NO BUILDS | ${iNoBuilds} | LAST HR BUILDS | ${iLastHrBuilds}"

//---------------------------------------------------------------------------
def endDate = new Date()
println "End time: ${sdf.format(endDate)}"

System.out.println "${sdf.format(endDate)} | Grand Totals | TOTAL ITEMS | ${iTotalItems} | TOTAL BUILDS | ${iTotalBuilds} | OLD BUILDS | ${iOlderThan60Mins} | BUILDING | ${iIsBuilding} | NO BUILDS | ${iNoBuilds} | LAST HR BUILDS | ${iLastHrBuilds}"

use(groovy.time.TimeCategory) 
{
    def duration = endDate - startDate
  	println "Elapsed time: Days: ${duration.days}, Hours: ${duration.hours}, Minutes: ${duration.minutes}, Seconds: ${duration.seconds}"
}

return null

// //---------------------------------------------------------------------------
// println "---------------------------------------------------------------------------"
// def sout = new StringBuilder(), serr = new StringBuilder()
// //def proc = 'grep --include -rn . -e "Error"'.execute()
// def proc = 'grep --include=\\server.log -rnw . -e ITEMS'.execute()
// //def proc = 'uptime'.execute()
// proc.consumeProcessOutput(sout, serr)
// proc.waitForOrKill(10000)
// //println "out> $sout err> $serr"
// println "$sout"
// println " ------- ****************************** --------------- "
// println "$serr"

