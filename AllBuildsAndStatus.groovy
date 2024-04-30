import java.text.SimpleDateFormat
def iCount = 1

def sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss.SSS")
def startDate = new Date()
println "Start time : ${sdf.format(startDate)}"
//-------------------------------------------------

println "========================================================"
println "Build History and Status"
println "========================================================"


def items = Hudson.instance.allItems

def midDate = new Date()
println "Time to get all items : ${sdf.format(midDate)}"

items.each 
{ item ->

  if (item instanceof Job) 
  {

    def builds = item.getBuilds()

    try 
    {
      builds.each 
      { 
        build ->
        def since = groovy.time.TimeCategory.minus( new Date(), build.getTime() )
        //def fromTime = groovy.time.TimeCategory.minus( new Date(), build.getTime() )
        def status = build.getBuildStatusSummary().message
        println "${iCount++}: Build:: ${build} | Since: ${since} | Status: ${status} | Result: ${build.result} | Number: ${build.number} | Duration: ${build.duration}| Description: ${build.description}" 
        iCount++
        // if(iCount >= 100)
        // {
        //   throw new Exception("We are limited to 100 builds only for this output!")
        // }
      }
    }
    catch(Exception exp)
    {
      println "EXCEPTION - ${exp.message}"
    }
  }
}
return

null


//--------------------------------------------------
def endDate = new Date()
println "End time: ${sdf.format(endDate)}"

use(groovy.time.TimeCategory) 
{
    def duration = endDate - startDate
  	print "Elapsed time: Days: ${duration.days}, Hours: ${duration.hours}, Minutes: ${duration.minutes}, Seconds: ${duration.seconds}"
}