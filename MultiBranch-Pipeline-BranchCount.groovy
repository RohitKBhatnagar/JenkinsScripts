#!Groovy
import java.text.SimpleDateFormat
def iCount = 1

def sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss.SSS")
def startDate = new Date()
println "Start time : ${sdf.format(startDate)}"

//================================================

println "------------------------------------------------------------------------------"
println "Displays all multi branch jobs and branch counts on ${java.net.InetAddress.getLocalHost()}"
println "------------------------------------------------------------------------------"

import hudson.model.Cause.UserIdCause
import hudson.model.Item
import hudson.model.Job
import hudson.model.ParametersAction
import hudson.model.User
import hudson.security.AccessDeniedException2
import jenkins.model.Jenkins
import org.jenkinsci.plugins.github_branch_source.PullRequestSCMHead
import org.jenkinsci.plugins.workflow.multibranch.BranchJobProperty
import org.jenkinsci.plugins.workflow.multibranch.WorkflowMultiBranchProject
 
int totalCount = 0
Jenkins.getInstance().getAllItems(WorkflowMultiBranchProject.class).findAll { WorkflowMultiBranchProject project ->
 
  int branchCount = 0
  project.items.findAll { Job j ->
    branchCount++
    totalCount++
  }
  //if (branchCount >= 20 ){
  //println "========================"
  println "Project Name: ${project.fullName} - Branch Count: ${branchCount}"
  //println "Branch Count:: ${branchCount}"}
}
println "Total Branch: ${totalCount}"

//=================================
def endDate = new Date()
println "End time: ${sdf.format(endDate)}"

use(groovy.time.TimeCategory) 
{
    def duration = endDate - startDate
  	print "Elapsed time: Days: ${duration.days}, Hours: ${duration.hours}, Minutes: ${duration.minutes}, Seconds: ${duration.seconds}"
}