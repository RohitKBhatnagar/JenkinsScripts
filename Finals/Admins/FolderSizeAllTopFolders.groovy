import java.text.SimpleDateFormat
import com.cloudbees.hudson.plugins.folder.*
import com.cloudbees.hudson.plugins.modeling.impl.folder.FolderTemplate

def iTotalItems = 0;
def iItemCount = 0;
def iFolderCount = 0
def lFolderSize = 0;

def sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss.SSS")
def startDate = new Date()
println "Start time : ${sdf.format(startDate)}"

//------------------------------------------------------------------------
//Displays folder size of all top level items on the Jenkins Instance
//------------------------------------------------------------------------

//Master HostName and IP Address
println "Master executed on - ${java.net.InetAddress.getLocalHost()}"

println "------------------------------------------------------------------------"
println "Displays item counts and folder size for all top level items on the Jenkins Instance"
println "------------------------------------------------------------------------"

def jenkins = Jenkins.getInstance()
def topItems = jenkins.getItems()

println "Top#, Name, Total Items, Folders Count, Size"
println "============================================"
topItems.eachWithIndex
{
  topName, topCount ->
  try
  {
    def allItemJobs = topName.getAllItems() 
    iItemCount = 0;
    iFolderCount = 0;
    lFolderSize = 0;
    allItemJobs.eachWithIndex
    {
      itemJob, itemCount ->
      try 
      {
          iTotalItems++;
          iItemCount++
          if(itemJob instanceof Folder)
              iFolderCount++;
      }
      catch(Exception exp)
      {
          //println "${topName} :: Exception - ${exp.message}"
      }
    }
    def sFolderPath = "/sys_apps_01/jenkins_nfs/jobs/" + topName.getName()
    
    ///////////////////
    def sout = new StringBuilder(), serr = new StringBuilder()
    def proc = "du -sh ${sFolderPath}".execute()
    //proc.waitForProcessOutput(sout, serr)
    proc.consumeProcessOutput(sout, serr)
    proc.waitForOrKill(30000)
    //println "${topName.getName()} size:: ${sout} err> ${serr}"
    ///////////////////

    println "${topCount}, ${topName.getName()}, ${iItemCount}, ${iFolderCount}, ${sout}";
  }
  catch(Exception exp)
  {
    //println "${topName} :: Exception - ${exp.message}"
  }
}

println "____________________________________________________________________________"
println "Total items count on ${java.net.InetAddress.getLocalHost()} - ${iTotalItems}"

//---------------------------------------------------------------------------
def endDate = new Date()
println "End time: ${sdf.format(endDate)}"

use(groovy.time.TimeCategory) 
{
    def duration = endDate - startDate
  	print "Elapsed time: Days: ${duration.days}, Hours: ${duration.hours}, Minutes: ${duration.minutes}, Seconds: ${duration.seconds}"
}



/////////////////Folder Size with infinite wait till folder size is calculated /////////////////


import java.text.SimpleDateFormat
//println "ls -latrch /sys_apps_01/jenkins_nfs/jobs".execute().text
println "ls -latrch /sys_apps_01/jenkins_nfs/jobs/PrepaidManagementServicesBizOps".execute().text
////////////////////////////////////////////////////////
def sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss.SSS")
def startDate = new Date()
println "Start time : ${sdf.format(startDate)}"
///////////////////////////////////////////////////////
def sout = new StringBuilder(), serr = new StringBuilder()
def cmd = "du -sh /sys_apps_01/jenkins_nfs/jobs/PrepaidManagementServicesBizOps"
//def cmd = "[Diagnostics.Process]::Start('chef-client -version').WaitForExit()"
def proc = ['bash', '-c', cmd].execute()

//proc.consumeProcessOutput(sout, serr)
//proc.waitForOrKill(60000)
proc.waitForProcessOutput(sout, serr)
println "$sout"

//---------------------------------------------------------------------------
def endDate = new Date()
println "End time: ${sdf.format(endDate)}"

use(groovy.time.TimeCategory) 
{
    def duration = endDate - startDate
    print "Elapsed time: Days: ${duration.days}, Hours: ${duration.hours}, Minutes: ${duration.minutes}, Seconds: ${duration.seconds}"
}