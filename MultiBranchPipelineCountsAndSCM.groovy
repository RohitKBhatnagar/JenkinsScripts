#!groovy
import java.text.SimpleDateFormat
def iItemCount = 1
def iJobCount = 1

def sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss.SSS")
def startDate = new Date()
println "Start time : ${sdf.format(startDate)}"

//----------------------------------------------------------------------
//Displays total job counts for top level items on the Jenkins Instance
//----------------------------------------------------------------------

//Master HostName and IP Address
println "Master executed on - ${java.net.InetAddress.getLocalHost()}"

println "----------------------------------------------------------------------"
println "Displays MultiBranch Projects pipeline def. location and SCM link"
println "----------------------------------------------------------------------"

Jenkins.instance.getAllItems(org.jenkinsci.plugins.workflow.multibranch.WorkflowMultiBranchProject.class).each 
{
  def allItemJobs = it.getAllJobs()
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

  def pf = it.getProjectFactory()
  def flowUrl;
    if (pf instanceof com.cloudbees.workflow.multibranch.CustomBranchProjectFactory) 
    {
        def cpsFlow = pf.getDefinition()
        if ( cpsFlow instanceof org.jenkinsci.plugins.workflow.cps.CpsScmFlowDefinition) 
        {
            def gitScm = cpsFlow.scm
            flowUrl = gitScm.key
        } 
        else 
        {
            flowUrl = "inline"
        }
    } 
    else if ( pf instanceof org.jenkinsci.plugins.workflow.multibranch.WorkflowBranchProjectFactory ) 
    {
        flowUrl = "JenkinsFile"
    }
  def branchSources = it.sources
    branchSources.each 
    {
        src ->
        def mySrc = src.getSource()
        int branchCount = 1
        it.items.findAll 
        { 
            Job j ->
            branchCount++
        }
        if(mySrc instanceof com.cloudbees.jenkins.plugins.bitbucket.BitbucketSCMSource) 
        {
            //println iItemCount++ + "# " + it.name + "," + Job-Count : iJobCount + "," + it.getUrl() + "," + mySrc['serverUrl'] + "/scm/" + mySrc['repoOwner'] + "/" + mySrc['repository'] + "," + branchCount + "," + flowUrl
            println "${iItemCount++}, ${it.name}, ${iJobCount}, ${it.getUrl()}, ${mySrc['serverUrl']}/scm/${mySrc['repoOwner']}/${mySrc['repository']}, ${branchCount}, ${flowUrl}"
        } 
        else 
        {
            //println iItemCount++ + "# " + it.name + "," + Job-Count : iJobCount + "," + it.getUrl() + "," + mySrc.getRemote() + "," + branchCount + "," + flowUrl
            println "${iItemCount++}, ${it.name}, ${iJobCount}, ${it.getUrl()}, ${mySrc.getRemote()}, ${branchCount}, ${flowUrl}"
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