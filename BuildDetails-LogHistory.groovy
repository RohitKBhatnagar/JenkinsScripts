import java.text.SimpleDateFormat
def iCount = 1

def sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss.SSS")
def startDate = new Date()
println "Start time : ${sdf.format(startDate)}"
//-------------------------------------------------

println "========================================================"
println "Build History and Log Files based upon Job name provided"
println "========================================================"


def printLogHistory(job) { 
    long timestampPeriod = period.toLong() * 24l * 60l * 60l * 1000l
    long refDate = new Date().getTime() - timestampPeriod
    def j = jenkins.model.Jenkins.instance.getItem(job);   
      long lastBuildTime = new File(j.getLastBuild().getRootDir().getAbsolutePath() + "/log").lastModified()
      int lastBuildNumber = j.getLastBuild().getNumber()
      if(j == null) 
    {
      println "Job was not found. Script exits."
      return
      }
    j.getBuilds().byTimestamp(0,refDate).each 
    {
          if (it.getNumber() == lastBuildNumber)
        {
              println "Files from last Build same as current build. Maybe the only build present!"              
        }
        else
        {
              //File file = new File(it.getRootDir().getAbsolutePath() + "/log");              
              //if(file.exists())
            println "Last build time - ${lastBuildtime}     Last Build # - ${lastBuildNumber}"
        }
    }
}

// The name of the job.
//def jobName = "some-job"

if(jobName != null && jobName.length() > 0)
{
    printLogHistory(jobName)
}
else
{
    jenkins.model.Jenkins.instance.getItems().each
    {
          printLogHistory(it.getName())
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