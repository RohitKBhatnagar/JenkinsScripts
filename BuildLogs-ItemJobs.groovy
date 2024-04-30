import java.text.SimpleDateFormat
def iCount = 1
def iItemCount = 1
def iJobCount = 1

def sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss.SSS")
def startDate = new Date()
println "Start time : ${sdf.format(startDate)}"

//---------------------------------------------------------------------------
//Displays Top level items and jobs within the same
//Additionally we collect last build details and logs details for a Jenkins Instance - https://docs.huihoo.com/javadoc/jenkins/1.512/jenkins/model/Jenkins.html
//---------------------------------------------------------------------------

//Master HostName and IP Address
println "Executed on - ${java.net.InetAddress.getLocalHost()}"

def jenkins = Jenkins.getInstance()
//println "Top Level Item Names: ${jenkins.getTopLevelItemNames()}"
//println "Job Names: ${jenkins.getJobNames()}"

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
    //println "\t Job - ${itemJob}"
    println "\t Job# ${iJobCount++} - ${itemJob.getName()}"
    println "================================================================="
    // println "\t\t Job build Dir - ${itemJob.getBuildDir()}"
    // //println "\t\t Job build List - ${itemJob.getBuilds()} "
    // //println "\t\t Job build status URL - ${itemJob.getBuildStatusUrl()} "
    // println "\t\t Job build estimated duration - ${itemJob.getEstimatedDuration()} "
    // println "\t\t Job oldest build in record - ${itemJob.getFirstBuild()} "

    println "\t\t Last Build details - ${itemJob.getLastBuild()} | Time - ${itemJob.getLastBuild().getTime()} | Result - ${itemJob.getLastBuild().result}"
    println "-----------------------------------------------------------------"
    println "${itemJob.getLastBuild().logText.readAll()}"
    
    // println "\t\t Job last build - ${itemJob.getLastBuild()} "
    // println "\t\t\t Job last build number - ${itemJob.getLastBuild().getNumber()} "
    // println "\t\t\t Job last build time - ${itemJob.getLastBuild().getTime()} "
    // println "\t\t\t Job last build result - ${itemJob.getLastBuild().result} "
    // println "\t\t\t Job last build log - ${itemJob.getLastBuild().logText.readAll()}"
    
    
    // println "\t\t Job last completed build - ${itemJob.getLastCompletedBuild()} "
    // println "\t\t Job last failed build - ${itemJob.getLastFailedBuild()} "
    
    // //println "\t\t Job properties - ${itemJob.getProperties()} "
    
    // //println "\n\t\t Job build Health - ${itemJob.getBuildHealth()} \n"
    // //println "\n\t\t Job build Health List - ${itemJob.getBuildHealthReports()} \n"
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