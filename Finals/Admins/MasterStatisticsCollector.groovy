//Author - Rohit K. Bhatnagar
//Purpose - Collects builds executed in last HR on all Masters connected to the CJOC
import java.text.SimpleDateFormat
import groovy.time.TimeCategory

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

def jenkins = Jenkins.getInstance()
def topItems = jenkins.getItems()
topItems.each
{
  topI ->
  iTotalItems++ //Counts total top level items
  def allItemJobs = topI.getAllJobs()
  iJobCount = 1
  def String strPrint = ""
  allItemJobs.each
  {
    itemJob ->
    try 
    {
      iTotalBuilds++ //Counts total builds in entire master
      if(itemJob.building)
        iIsBuilding++;
      def build_time = itemJob.getLastBuild().startTimeInMillis;
      //println "${build_time} - ${new Date(build_time)}"
      def Date before60Minutes
      use( groovy.time.TimeCategory ) 
      {
        before60Minutes = new Date() - 60.minutes
      }
      //println "${before60Minutes}"
      def buildB4Time = new Date(build_time).before(before60Minutes);
      
      if(!buildB4Time)
      {
        iLastHrBuilds++
        //println "${iLastHrBuilds} - ${itemJob.getName()}"
        def lastBuild = itemJob.getLastBuild()
        def String jobResult =  itemJob.getLastBuild().result;
        def buildTime = itemJob.getLastBuild().getTime()
        def buildDuration = itemJob.getLastBuild().getDuration()
        def strBuildDuration = itemJob.getLastBuild().durationString
        strPrint = "DETAILS | ${lastBuild} | Time | ${buildTime} | Result | ${jobResult} | Duration | ${strBuildDuration} | Duration(ms) | ${buildDuration}"
        println "ITEMS | ${iItemCount++} | ${topI.getName()} | ${iJobCount++} | ${itemJob.getName()} | ${strPrint}"
        System.out.println "ITEMS (CJOC) | ${iItemCount++} | ${topI.getName()} | ${iJobCount++} | ${itemJob.getName()} | ${strPrint}"
      }
      else 
        iOlderThan60Mins++
    }
    catch(NullPointerException npexp)
    {
      iNoBuilds++
        //println "${iItemCount++} | ${topI.getName()} | ${iJobCount++} | ${itemJob.getName()} | NULL-POINTER EXCEPTION | ${npexp.message}"
    }
    catch(Exception exp)
    {
      println "${iItemCount++} | ${topI.getName()} | ${iJobCount++} | ${itemJob.getName()} | GENERIC EXCEPTION | ${exp.message}"
    }
  }
}

println "ITEMS Totals | TOTAL ITEMS | ${iTotalItems} | TOTAL BUILDS | ${iTotalBuilds} | OLD BUILDS | ${iOlderThan60Mins} | BUILDING | ${iIsBuilding} | NO BUILDS | ${iNoBuilds} | LAST HR BUILDS | ${iLastHrBuilds} | START | ${startDate}"

//---------------------------------------------------------------------------
def endDate = new Date()
def formatEndDate = sdf.format(endDate)
println "End time: ${formatEndDate}"

System.out.println "END | ${formatEndDate} | GRAND ITEMS TOTALS (CJOC) | TOTAL ITEMS | ${iTotalItems} | TOTAL BUILDS | ${iTotalBuilds} | OLD BUILDS | ${iOlderThan60Mins} | BUILDING | ${iIsBuilding} | NO BUILDS | ${iNoBuilds} | LAST HR BUILDS | ${iLastHrBuilds} | START | ${startDate}"
