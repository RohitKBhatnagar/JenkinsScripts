import java.text.SimpleDateFormat
def iCount = 1

def sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss.SSS")
def startDate = new Date()
println "Start time : ${sdf.format(startDate)}"

//---------------------------------------------------------------------------
//Displays all details for a Jenkins Instance - https://docs.huihoo.com/javadoc/jenkins/1.512/jenkins/model/Jenkins.html
//---------------------------------------------------------------------------

//Master HostName and IP Address
println java.net.InetAddress.getLocalHost()

/* This scripts shows how to get basic information about Jenkins instance */
def jenkins = Jenkins.getInstance()
println "Jenkins version: ${jenkins.getVersion()}"
//println "Available JDKs: ${jenkins.getInstance().getJDKs()}"
//println "Available Slaves: ${jenkins.getSlaves()}"
//println "Available Nodes: ${jenkins.getNodes()}"
//println "Node Description: ${jenkins.getNodeDescription()}" //For a particular node
//println "Node Properties: ${jenkins.getNodeProperties()}" //For a particular node
//println "Immediate Items: ${jenkins.getItems()}"
//println "Top Level Item Names: ${jenkins.getTopLevelItemNames()}"

//Get Workspaces for all immediate items
def topItems = jenkins.getItems()
topItems.each
{
    //https://docs.huihoo.com/javadoc/jenkins/1.512/hudson/model/Item.html
  topI ->
  //println "Workspace Directory for ${topI} is : ${jenkins.getWorkspaceFor(topI)}"
  println "Workspace Directory for ${topI.getName()} is : ${jenkins.getWorkspaceFor(topI)}"
  //println "\t\tJobs for ${topI} is : ${topI.getAllJobs()}"
  def allItemJobs = topI.getAllJobs() //https://docs.huihoo.com/javadoc/jenkins/1.512/hudson/model/Job.html
  allItemJobs.each
  {
    itemJob ->
    //println "\t Job - ${itemJob}"
    println "\t Job - ${itemJob.getName()}"
    // println "\t\t Job build Dir - ${itemJob.getBuildDir()}"
    // //println "\t\t Job build List - ${itemJob.getBuilds()} "
    // //println "\t\t Job build status URL - ${itemJob.getBuildStatusUrl()} "
    // println "\t\t Job build estimated duration - ${itemJob.getEstimatedDuration()} "
    // println "\t\t Job oldest build in record - ${itemJob.getFirstBuild()} "
     println "\t\t Job last build - ${itemJob.getLastBuild()} "
    println "\t\t\t Job last build number - ${itemJob.getLastBuild().getNumber()} "
    println "\t\t\t Job last build time - ${itemJob.getLastBuild().getTime()} "
    println "\t\t\t Job last build result - ${itemJob.getLastBuild().result} "
    println "\t\t\t Job last build log - ${itemJob.getLastBuild().logText.readAll()}"
    // println "\t\t Job last completed build - ${itemJob.getLastCompletedBuild()} "
    // println "\t\t Job last failed build - ${itemJob.getLastFailedBuild()} "
    
    // //println "\t\t Job properties - ${itemJob.getProperties()} "
    
    // //println "\n\t\t Job build Health - ${itemJob.getBuildHealth()} \n"
    // //println "\n\t\t Job build Health List - ${itemJob.getBuildHealthReports()} \n"
  }
}

//println "Job Names: ${jenkins.getJobNames()}"
//println "Label Names: ${jenkins.getLabels()}"
//println "Available Authentications: ${jenkins.getAuthentication()}"
//println "Available Executors: ${jenkins.getNumExecutors()}"
//println "Available Job Listeners: ${jenkins.getJobListeners()}"
//println "Management Links: ${jenkins.getManagementLinks()}"
//println "RAW Build Dir: ${jenkins.getRawBuildsDir()}"
//println "RAW Workspace Dir: ${jenkins.getRawWorkspaceDir()}"
//println "Root Dir: ${jenkins.getRootDir()}"
//println "Root Path: ${jenkins.getRootPath()}"
//println "Root URL: ${jenkins.getRootUrl()}"
//println "Root URL from Request: ${jenkins.getRootUrlFromRequest()}"
//println "SCM retry count: ${jenkins.getScmCheckoutRetryCount()}"
//println "SCM Listeners: ${jenkins.getSCMListeners()}" //No listeners
//println "Search URL: ${jenkins.getSearchUrl()}" //Empty
//println "Secret Key: ${jenkins.getSecretKey()}"
//println "Secret Key: ${jenkins.getSecretKey()}"
//println "Security: ${jenkins.getSecurity()}"
//println "Self Label: ${jenkins.getSelfLabel()}"
//println "Slave Agent Port Label: ${jenkins.getSlaveAgentPort()}" //0
//println "TCP Slave Agent Listener: ${jenkins.getTcpSlaveAgentListener()}"
//println "Widgets registered: ${jenkins.getWidgets()}"



//---------------------------------------------------------------------------
def endDate = new Date()
println "End time: ${sdf.format(endDate)}"

use(groovy.time.TimeCategory) 
{
    def duration = endDate - startDate
  	print "Elapsed time: Days: ${duration.days}, Hours: ${duration.hours}, Minutes: ${duration.minutes}, Seconds: ${duration.seconds}"
}