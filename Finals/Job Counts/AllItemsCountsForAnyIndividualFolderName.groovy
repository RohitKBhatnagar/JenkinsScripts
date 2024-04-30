#!groovy
import java.text.SimpleDateFormat
def iCount = 0
def iItemCount = 0
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

def folderName="MCConnect_Automation"; //"MarketingBusinessSolutionsDevOps"; // change value `folder-a` for the full name of the folder you want to disable all jobs in

//----------------------------------------------------------------------
//Displays total counts for various items defined in the Jenkins Instance
//----------------------------------------------------------------------

//Master HostName and IP Address
println "Master executed on - ${java.net.InetAddress.getLocalHost()}"

println "-----------------------------------------------------------------------"
println "Displays total counts for various items defined in the Jenkins Instance"
println "We will populate job counts only for this top level folder - ${folderName}"
println "-----------------------------------------------------------------------"

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


def jenkins = Jenkins.getInstance()
//Get Workspaces for all immediate items
//def topItems = jenkins.getItems(com.cloudbees.hudson.plugins.folder.Folder.class)
//def topItems = jenkins.getAllItems(com.cloudbees.hudson.plugins.folder.Folder.class)


try
{
    //def topItems = jenkins.getAllItems() //ORIGINAL------
    def topItems = jenkins.getItemByFullName(folderName, AbstractFolder).getAllItems();
    topItems.each
    {
      topI ->
      try
      {    
        iItemCount++;
          if(topI instanceof Folder)
        {
          iFolderCount++;
          //println "${iItemCount} | Item-Name : ${topI.getName()} | Class-Name : ${topI.getClass()} | Columns : ${topI.getColumns()} | Items : ${topI.getItems()} | Jobs : ${topI.getAllJobs()}"
          //println "${iItemCount} | Item-Name : ${topI.getName()} | Class-Name : ${topI.getClass()} | Items : ${topI.getItems()} | Jobs : ${topI.getAllJobs()}"
          //println "${iItemCount} | Item-Name : ${topI.getName()} | Class-Name : ${topI.getClass()}"
        }
          else if(topI instanceof WorkflowJob)
        {
              iJobCount++
          //println "${iItemCount} | Item-Name : ${topI.getName()} | Class-Name : ${topI.getClass()}"
        }
        else if(topI instanceof MavenModuleSet)
        {
          iMavenModuleSet++;
          println "${iItemCount} | MavenModuleSet Item-Name : ${topI.getName()} | Class-Name : ${topI.getClass()}"
        }
        else if(topI instanceof WorkflowMultiBranchProject)
        {
          iMultiBranchProject++
          //println "${iItemCount} | Item-Name : ${topI.getName()} | Class-Name : ${topI.getClass()}"
        }
        else if(topI instanceof FreeStyleProject)
        {
          iFreeStyle++
          println "${iItemCount} | FreeStyleProject Item-Name : ${topI.getName()} | Class-Name : ${topI.getClass()}"
        }
        else if(topI instanceof JobTemplate)
        {
          iJobTemplate++
          //println "${iItemCount} | Item-Name : ${topI.getName()} | Class-Name : ${topI.getClass()}"
        }
        else if(topI instanceof MavenModule)
        {
          iMavenModule++
          println "${iItemCount} | MavenModule Item-Name : ${topI.getName()} | Class-Name : ${topI.getClass()}"
        }
        else if (topI instanceof OrganizationFolder)
        {
          iOrgFolder++
          //println "${iItemCount} | Item-Name : ${topI.getName()} | Class-Name : ${topI.getClass()}"
        }
        else if (topI instanceof MatrixConfiguration)
        {
          iMatrixConf++
          println "${iItemCount} | MatrixConfiguration Item-Name : ${topI.getName()} | Class-Name : ${topI.getClass()}"
        }
        else if (topI instanceof MatrixProject)
        {
          iMatrixProj++
          println "${iItemCount} | MatrixProject Item-Name : ${topI.getName()} | Class-Name : ${topI.getClass()}"
        }
        else if (topI instanceof FolderTemplate)
        {
          iFolderTemp++
          //println "${iItemCount} | Item-Name : ${topI.getName()} | Class-Name : ${topI.getClass()}"
        }
        else if (topI instanceof IvyModuleSet)
        {
          iIvyModuleSet++
          println "${iItemCount} | IvyModuleSet Item-Name : ${topI.getName()} | Class-Name : ${topI.getClass()}"
        }
        else if (topI instanceof AuxModel)
        {
          iAuxModel++
          //println "${iItemCount} | AuxModel Item-Name : ${topI.getName()} | Class-Name : ${topI.getClass()}"
        }
        else if (topI instanceof BuilderTemplate)
        {
          iBuilderTemplate++
          //println "${iItemCount} | Item-Name : ${topI.getName()} | Class-Name : ${topI.getClass()}"
        }
        else if (topI instanceof PublisherTemplate)
        {
          iPublisherTemplate++
          //println "${iItemCount} | Item-Name : ${topI.getName()} | Class-Name : ${topI.getClass()}"
        }
        else
        {
          iCount++;
          println "${iItemCount} | Item-Name : ${topI.getName()} | Class-Name : ${topI.getClass()}"
        }
      }
      catch(Exception exp)
      {
        println "Exception - ${exp.message}"
      }
      //println "${iItemCount} | Item-Name : ${topI.getName()} | Folder-Count : ${iFolderCount} | Job-Count : ${iJobCount} | UnKnown : ${iCount}"
    }
}
catch (Exception exp)
{
    println "^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^"
    println "Exception - ${exp.message} - Is this jobname ${folderName} even available in this controller! - ${java.net.InetAddress.getLocalHost()}"
    println "^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^"

    return;
}

println "Total-Items-Count | ${iItemCount} | Folder-Count | ${iFolderCount} | Workflow-Job-Count | ${iJobCount} | Maven-ModuleSet-Count | ${iMavenModuleSet} | MultiBranch-Count | ${iMultiBranchProject} | FreeStyle-Count | ${iFreeStyle} | JobTemplate-Count | ${iJobTemplate} | MavenModule-Count | ${iMavenModule} | Oragnization-Folder-Count | ${iOrgFolder} | Matrix-Configuration-Count | ${iMatrixConf} | Matrix-Project-Count | ${iMatrixProj} | Folder-Template-Count | ${iFolderTemp} | AuxModel-Count | ${iAuxModel} | IvyModuleSet-Count | ${iIvyModuleSet} | BuilderTemplate-Count | ${iBuilderTemplate} | PublisherTemplate-Count | ${iPublisherTemplate} | UnKnown | ${iCount}"

println "========="

println "Total-Items-Count | ${iItemCount}"
println "Folder-Count | ${iFolderCount}"
println "Workflow-Job-Count | ${iJobCount}"
println "Maven-ModuleSet-Count | ${iMavenModuleSet}"
println "MultiBranch-Count | ${iMultiBranchProject}"
println "FreeStyle-Count | ${iFreeStyle}"
println "JobTemplate-Count | ${iJobTemplate}"
println "MavenModule-Count | ${iMavenModule}"
println "Oragnization-Folder-Count | ${iOrgFolder}"
println "Matrix-Configuration-Count | ${iMatrixConf}"
println "Matrix-Project-Count | ${iMatrixProj}"
println "Folder-Template-Count | ${iFolderTemp}"
println "AuxModel-Count | ${iAuxModel}"
println "IvyModuleSet-Count | ${iIvyModuleSet}"
println "BuilderTemplate-Count | ${iBuilderTemplate}"
println "PublisherTemplate-Count | ${iPublisherTemplate}"
println "UnKnown | ${iCount}"

println "^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^"
Jenkins.instance.getItemByFullName(folderName, AbstractFolder).getAllItems()
    .findAll { it instanceof ParameterizedJobMixIn.ParameterizedJob || it instanceof AbstractFolder }
    .eachWithIndex { it, i ->
        println("${i+1} - [$it.fullName]")
    }
println "^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^"

//---------------------------------------------------------------------------
def endDate = new Date()
println "End time: ${sdf.format(endDate)}"

use(groovy.time.TimeCategory) 
{
    def duration = endDate - startDate
      print "Elapsed time: Days: ${duration.days}, Hours: ${duration.hours}, Minutes: ${duration.minutes}, Seconds: ${duration.seconds}"
}

return null