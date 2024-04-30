#!groovy
import java.text.SimpleDateFormat
def iTotalItems = 0
def iCount = 0
def iItemCount = 1
def iFolderCount = 0
def iJobCount = 0 //WorkflowJob
def iMultiBranchProject = 0 //WorkflowMultiBranchProject
def iMavenModuleSet = 0;
def iMavenModule = 0;
def iFreeStyle = 0 //hudson.model.FreeStyleProject
def iJobTemplate = 0;
def iOrgFolder = 0;
def iMatrixConf = 0;
def iMatrixProj = 0;
def iFolderTemp = 0;
def iIvyModuleSet = 0
def iAuxModel = 0
def iBuilderTemplate = 0
def iPublisherTemplate = 0

def sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss.SSS")
def startDate = new Date()
println "Start time : ${sdf.format(startDate)}"

//----------------------------------------------------------------------
//Displays total job counts for top level items on the Jenkins Instance
//----------------------------------------------------------------------

//Master HostName and IP Address
println "Master executed on - ${java.net.InetAddress.getLocalHost()}"

println "--------------------------------------------------------------------------"
println "Iterates through various job items defined in the Jenkins Instance"
println "Populates total job counts for each job-type against each top-level-folder"
println "--------------------------------------------------------------------------"

import com.cloudbees.hudson.plugins.folder.*
import com.cloudbees.hudson.plugins.modeling.impl.folder.FolderTemplate
import org.jenkinsci.plugins.workflow.job.*;
import org.jenkinsci.plugins.workflow.multibranch.*;
import com.cloudbees.hudson.plugins.modeling.impl.jobTemplate.*; 
import hudson.maven.MavenModuleSet;
import hudson.model.FreeStyleProject;
import hudson.maven.MavenModule;
import jenkins.branch.OrganizationFolder;
import hudson.matrix.*; //MatrixConfiguration; //hudson.matrix.MatrixProject
import hudson.ivy.IvyModuleSet;
import com.cloudbees.hudson.plugins.modeling.impl.auxiliary.AuxModel;
import com.cloudbees.hudson.plugins.modeling.impl.builder.BuilderTemplate;
import com.cloudbees.hudson.plugins.modeling.impl.publisher.PublisherTemplate;

import com.cloudbees.hudson.plugins.folder.AbstractFolder;

//println "Count, Folder Name, Total Items, Folder-Count, Workflow-Job-Count, Maven-ModuleSet-Count, MultiBranch-Count, FreeStyle-Count, JobTemplate-Count, MavenModule-Count, Oragnization-Folder-Count, Matrix-Configuration-Count, Matrix-Project-Count, Folder-Template-Count, AuxModel-Count, IvyModuleSet-Count, BuilderTemplate-Count, PublisherTemplate-Count, UnKnown-ItemType-Count"
println "Count, Folder Name, Total Items, Folder, Workflow-Job, Maven-ModuleSet, MultiBranch, FreeStyle, JobTemplate, MavenModule, Oragnization-Folder, Matrix-Configuration, Matrix-Project, Folder-Template, AuxModel, IvyModuleSet, BuilderTemplate, PublisherTemplate, UnKnown-ItemType"

def jenkins = Jenkins.getInstance()
//Get Workspaces for all immediate items
def topItems = jenkins.getItems()
topItems.eachWithIndex
{
  topI, topICount ->
  //Reinitialize all counters
  iCount = 0
  iItemCount = 0
  iFolderCount = 0
  iJobCount = 0 //WorkflowJob
  iMultiBranchProject = 0 //WorkflowMultiBranchProject
  iMavenModuleSet = 0;
  iMavenModule = 0;
  iFreeStyle = 0 //hudson.model.FreeStyleProject
  iJobTemplate = 0;
  iOrgFolder = 0;
  iMatrixConf = 0;
  iMatrixProj = 0;
  iFolderTemp = 0;
  iIvyModuleSet = 0
  iAuxModel = 0
  iBuilderTemplate = 0
  iPublisherTemplate = 0
  def topFolderItems = jenkins.getItemByFullName(topI.name, AbstractFolder).getAllItems();
    topFolderItems.each
    {
      topFolderI ->
      ++iTotalItems;
      try
      {    
        ++iItemCount;
        if(topFolderI instanceof Folder)
          ++iFolderCount;
        else if(topFolderI instanceof WorkflowJob)
              ++iJobCount
        else if(topFolderI instanceof MavenModuleSet)
          ++iMavenModuleSet;
        else if(topFolderI instanceof WorkflowMultiBranchProject)
          ++iMultiBranchProject
        else if(topFolderI instanceof FreeStyleProject)
          ++iFreeStyle
        else if(topFolderI instanceof JobTemplate)
          ++iJobTemplate
        else if(topFolderI instanceof MavenModule)
          ++iMavenModule
        else if (topFolderI instanceof OrganizationFolder)
          ++iOrgFolder
        else if (topFolderI instanceof MatrixConfiguration)
          ++iMatrixConf
        else if (topFolderI instanceof MatrixProject)
          ++iMatrixProj
        else if (topFolderI instanceof FolderTemplate)
          ++iFolderTemp
        else if (topFolderI instanceof IvyModuleSet)
          ++iIvyModuleSet
        else if (topFolderI instanceof AuxModel)
          ++iAuxModel
        else if (topFolderI instanceof BuilderTemplate)
          ++iBuilderTemplate
        else if (topFolderI instanceof PublisherTemplate)
          ++iPublisherTemplate
        else
          ++iCount;
          //println "----- ${iItemCount} | Item-Name : ${topFolderI.getName()} | Class-Name : ${topFolderI.getClass()} ------"
      }
      catch(Exception exp)
      {
        println "++++++++ Exception - ${exp.message} ++++++"
      }
      //println "${iItemCount} | Item-Name : ${topFolderI.getName()} | Folder-Count : ${iFolderCount} | Job-Count : ${iJobCount} | UnKnown : ${iCount}"
    }

//Final report print out
  println "${++topICount}, ${topI.getName()}, ${iItemCount}, ${iFolderCount}, ${iJobCount}, ${iMavenModuleSet}, ${iMultiBranchProject}, ${iFreeStyle}, ${iJobTemplate}, ${iMavenModule}, ${iOrgFolder}, ${iMatrixConf}, ${iMatrixProj}, ${iFolderTemp}, ${iAuxModel}, ${iIvyModuleSet}, ${iBuilderTemplate}, ${iPublisherTemplate}, ${iCount}"
}

println "Grand total items in ${java.net.InetAddress.getLocalHost()} - ${iTotalItems} as of ${sdf.format(startDate)}"
//---------------------------------------------------------------------------
def endDate = new Date()
println "End time: ${sdf.format(endDate)}"

use(groovy.time.TimeCategory) 
{
    def duration = endDate - startDate
      print "Elapsed time: Days: ${duration.days}, Hours: ${duration.hours}, Minutes: ${duration.minutes}, Seconds: ${duration.seconds}"
}

return null