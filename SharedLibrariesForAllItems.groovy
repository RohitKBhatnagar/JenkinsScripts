#!groovy
import java.text.SimpleDateFormat
def iCount = 0;
def iBTECount = 0;

def sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss.SSS")
def startDate = new Date()
println "Start time : ${sdf.format(startDate)}"

//------------------------------------------------------------------------------------------
//Displays shared libraries name and SCM-URL for all items defined in this Jenkins instance
//------------------------------------------------------------------------------------------

println "------------------------------------------------------------------------------------------"
println "Displays shared libraries name and SCM-URL for all items defined in this Jenkins instance"
println "------------------------------------------------------------------------------------------"

//Master HostName and IP Address
println "Master executed on - ${java.net.InetAddress.getLocalHost()}"

def folders = Jenkins.instance.getAllItems(com.cloudbees.hudson.plugins.folder.Folder.class)
folders.each{ folder ->
  folder.properties.each{ property ->
    if (property instanceof org.jenkinsci.plugins.workflow.libs.FolderLibraries) {
      myLib = property.getLibraries()
      //println "Top Level Folder Name:  ${folder.fullName}"
      myLib.each { lib ->
        //println "       Shared Library Name: ${lib.name}"
        //println "       Implicit value: ${lib.implicit}" //Whether the library should be made accessible to qualifying jobs without any explicit Library declaration.
        lib.retriever.each { retriever ->
          if ( retriever instanceof org.jenkinsci.plugins.workflow.libs.SCMSourceRetriever) {
            scmInfo = retriever.getScm()
            if (scmInfo)
            {
                if(scmInfo.remote.equals("https://globalrepository.mclocal.int/stash/scm/bte/bte.git"))
                    iBTECount++;
                else 
                    iCount++
                    //println "${iCount++} | Folder : ${folder.fullName} | Library : ${lib.name} | Version : ${lib.defaultVersion} | Implicit : ${lib.implicit} | SCM: ${scmInfo.remote}"
            }
          }
        }
      }
    }
  }
}

println "Non-BTE : ${iCount++} | BTE : ${iBTECount++}"

//---------------------------------------------------------------------------
def endDate = new Date()
println "End time: ${sdf.format(endDate)}"

use(groovy.time.TimeCategory) 
{
    def duration = endDate - startDate
  	print "Elapsed time: Days: ${duration.days}, Hours: ${duration.hours}, Minutes: ${duration.minutes}, Seconds: ${duration.seconds}"
}