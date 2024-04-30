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
import groovy.json.*

// Define hour to compare (hour=24 will find builds that were built more than 1 day ago)
def iMin = 60;
def iSec = 60;
def iHrInSecs = iMin * iSec;
def calNow = Calendar.instance;
def lstBuilds = [];

def Date dtSnap = new Date()
def before60Mins

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
				use( TimeCategory ) 
				{
                  //before60Mins = new Date() - 60.minutes
                  before60Mins = dtSnap - 60.minutes
                  //println "${before60Mins}"
                }
          		if(!new Date(build_time).before(before60Mins))
          		{
                    iLastHrBuilds++
                    def String jobResult =  itemJob.getLastBuild().result;
                    def String buildDetails =  itemJob.getLastBuild();
                    //strPrint = "DETAILS | ${itemJob.getLastBuild()} | Time | ${itemJob.getLastBuild().getTime()} | Result | ${itemJob.getLastBuild().result} | Duration | ${itemJob.getLastBuild().durationString} | Duration(ms) | ${itemJob.getLastBuild().getDuration()}"
                    strPrint = "DETAILS | ${buildDetails} | Time | ${itemJob.getLastBuild().getTime()} | Result | ${jobResult} | Duration | ${itemJob.getLastBuild().durationString} | Duration(ms) | ${itemJob.getLastBuild().getDuration()}"
                    println "ITEMS | ${iItemCount++} | ${topI.getName()} | ${iJobCount++} | ${itemJob.getName()} | ${strPrint}"
                    //System.out.println "ITEMS | ${iItemCount++} | ${topI.getName()} | ${iJobCount++} | ${itemJob.getName()} | ${strPrint}"

                    //println "${itemJob.getLastBuild().getRootDir()}"
                  	println "AbsoluteURL - ${itemJob.getAbsoluteUrl()}, API - ${itemJob.getApi()}, Description - ${itemJob.getDescription()}, Display - ${itemJob.getDisplayName()}, Full - ${itemJob.getFullDisplayName()}, FullName - ${itemJob.getFullName()}, Name - ${itemJob.getName()}, Parent - ${itemJob.getParent()}, Relative - ${itemJob.getRelativeDisplayNameFrom()}, Group - ${itemJob.getRelativeNameFromGroup()}, RootDir - ${itemJob.getRootDir()}, SearchName - ${itemJob.getSearchName()}, SearchURL - ${itemJob.getSearchUrl()}, ShortURL - ${itemJob.getShortUrl()}, Target - {itemJob.getTarget()}, Task - ${itemJob.getTaskNoun()}, URL  - ${itemJob.getUrl()}"
                  	def String itemClass = itemJob.getClass().name;
                  	println "${itemClass}"

                    def String firstBuild = itemJob.getFirstBuild();
                    def String buildDir = itemJob.getLastBuild().getRootDir();
                    def hrItem = [
                        [ getItemCount: {iItemCount}, getItemName : {topI.getName()}, getJobCount: {iJobCount}, getJobName : {itemJob.getName()}, getBuildResult : {jobResult}, getBuildNo : {itemJob.getLastBuild().getNumber()},getBuildDuration : {itemJob.getLastBuild().getDuration()}, getBuildDurationString : {itemJob.getLastBuild().durationString}, getBuildDir: {buildDir}, getBuildDetails:{buildDetails}, getFirstBuild: {firstBuild}, getBuildsFrom: {sdf.format(before60Mins)}, getBuildsTo: {sdf.format(dtSnap)}
                        ]
                    ]

                    def jsonItemBuilder = new groovy.json.JsonBuilder()  
                    jsonItemBuilder(
                    //{
                                    //HRBuilds hrItem.collect {          
                                    hrItem.collect {          
                                        [   ItemCount: it.getItemCount(),
                                            ItemName: it.getItemName(),
                                            JobCount: it.getJobCount(),
                                            JobName: it.getJobName(),              
                                            BuildNo: it.getBuildNo(),
                                            BuildResult: it.getBuildResult(),
                                            BuildDuration: it.getBuildDuration(),
                                            BuildDurationString: it.getBuildDurationString(),
                                            BuildDir: it.getBuildDir(),
                                            BuildDetails: it.getBuildDetails(),
                                            FirstBuild: it.getFirstBuild(),
                                            BuildsFrom: it.getBuildsFrom(),
                                            BuildsTo: it.getBuildsTo()
                                        ]      
                                    } 
                    //}
                    )
                    println jsonItemBuilder.toPrettyString() 
                    System.out.println jsonItemBuilder.toPrettyString()
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

//JSON print to the Server.log file
def itemTotals = [
                [ getTotalItems: {iTotalItems}, getTotalBuilds : {iTotalBuilds}, getBuildsOlderThanAnHr: {iOlderThan60Mins}, getJobsBuilding : {iIsBuilding}, getJobsWithNoBuilds : {iNoBuilds}, getLastHrBuilds : {iLastHrBuilds}, getBuildsFrom: {sdf.format(before60Mins)}, getBuildsTo: {sdf.format(dtSnap)}
                ]
             ]

            def jsonTotalItemBuilder = new groovy.json.JsonBuilder()  
            jsonTotalItemBuilder 
            {
                BuildTotals itemTotals.collect {          
                    [   TotalItems: it.getTotalItems(),
                        TotalBuilds: it.getTotalBuilds(),
                        BuildsOlderThanAnHr: it.getBuildsOlderThanAnHr(),
                        JobsBuilding: it.getJobsBuilding(),              
                        JobsWithNoBuilds: it.getJobsWithNoBuilds(),
                        LastHrBuilds: it.getLastHrBuilds(),
                        BuildsFrom: it.getBuildsFrom(),
                        BuildsTo: it.getBuildsTo()
                    ]      
                } 
            }
            println jsonTotalItemBuilder.toPrettyString() 
            System.out.println jsonTotalItemBuilder.toPrettyString()
//------------------------------------

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

