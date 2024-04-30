import java.text.SimpleDateFormat
def iCount = 0

def sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss.SSS")
def startDate = new Date()
println "Start time : ${sdf.format(startDate)}"
//-------------------------------------------------

String strMaster = java.net.InetAddress.getLocalHost();
println "================================================================================================="
println "Find all builds performed within  past 24 hours and then prints the build log for each build job!"
println ">>>>>>>>> CD Master - ${strMaster} <<<<<<<<<<<<<<<<<<<<<<<<<<<<<"
println "================================================================================================="

import hudson.console.PlainTextConsoleOutputStream
import java.io.ByteArrayOutputStream
import java.util.Date
import jenkins.model.Jenkins
import org.apache.commons.io.IOUtils
import org.jenkinsci.plugins.workflow.job.WorkflowJob
import org.jenkinsci.plugins.workflow.job.WorkflowRun
//import org.groovy.json

class LabelBuild 
{
    long Count
    String Name
    String StartTime
    String StatusSummary
    String Duration
    String HostName
    String Label
    Map Environment
}

def slave_label_map = [:]
for (slave in Jenkins.instance.slaves) 
{
    //println "${iCount++},${slave.nodeName},${slave.labelString},${slave.numExecutors}" //Prints the LABELS associated and EXECUTORS count in a single line for each slave
    words = slave.labelString.split()
    def labelListForSlave = []
    words.each() 
    {
        labelListForSlave.add(it);
    }
    slave_label_map.put(slave.name, labelListForSlave)
}

//println "slave_label_map - ${slave_label_map}"

// slave_label_map.eachWithIndex{
//     entry, index ->
//   println "${index} : ${entry.key} - ${entry.value}"
// }

def Date before60Minutes
use( groovy.time.TimeCategory ) 
{
    before60Minutes = new Date() - 60.minutes
}

ArrayList<LabelBuild> lstLblBlds = new ArrayList<LabelBuild>();
def allItems = Jenkins.instance.getAllItems(WorkflowJob) 
//def allItems = Jenkins.instance.getAllItems()
println "Total Items - ${allItems.size()}"
try
{
    def myItems = allItems.findAll 
    {
        try
        {
            WorkflowRun build = it.builds.first()
            new Date(build.startTimeInMillis).after(before60Minutes)
            //println "------------------ ${iCount++}:    ${build} -------------------------"
        }
        catch(Exception exp)
        {
            //println("${it.class.simpleName} - ${exp.message}")
        }
    }.collect 
    {
        it.builds.findAll 
        { 
            WorkflowRun build ->
            new Date(build.startTimeInMillis).after(before60Minutes)
        }
    }
    .flatten().findAll 
    { 
        WorkflowRun build ->
                iCount++
                try {
                    /*********************************************
                    ByteArrayOutputStream os = new ByteArrayOutputStream()
                    InputStreamReader is = build.logText.readAll()
                    PlainTextConsoleOutputStream pos = new PlainTextConsoleOutputStream(os)
                    IOUtils.copy(is, pos)
                    String text = os.toString()
                    *********************************************/
                    println "${iCount} :: Build URL - ${build.getUrl()}"
                    
                    def sHostName = ""
                    def sLabel = ""
                    /*****************************************************
                    if (text.contains("Running on")) {
                        def srchTxt = /jnk.* / //Please remove the space in between * and /. The space is introduced for comment purposes only!
                        def findNode = (text =~ /$srchTxt/)
                        String sHostString = findNode[0]//.toString()
                        int iIndex = sHostString.indexOf('in', 0)
                        if (iIndex > 0) {
                            sHostName = sHostString.substring(0, iIndex - 1)
                            sLabel = slave_label_map.find { it.key == sHostName }?.value
                        }
                    }
                    os.close()
                    is.close()
                    pos.close()
                    *****************************************************/

                    println "---------------------"
                    def scmsLst = build.getSCMs()
                    scmsLst.each {
                            println "SCM - Key - ${it.getKey()}" // - Type - ${it.getType()}}"
                    }
                    //https://javadoc.jenkins.io/plugin/workflow-job/org/jenkinsci/plugins/workflow/job/WorkflowRun.html
                    def lstChangeSets = build.getChangeSets();
                    //println "ChangeSets- ${lstChangeSets}"
                    //https://javadoc.jenkins.io/plugin/git/hudson/plugins/git/GitChangeSetList.html
                    lstChangeSets.each {
                        println "Kind - ${it.getKind()}"
                        println "EmptySet - ${it.isEmptySet()}"
                        def lstGitChgSet = it.getLogs();
                        println "Git Change Sets - ${lstGitChgSet}"
                        lstGitChgSet.eachWithIndex { itSub, iChgCount ->
                            println "${iChgCount} - Author eMail - ${itSub.getAuthorEmail()}"
                            println "${iChgCount} - Author Name - ${itSub.getAuthorName()}"
                            println "${iChgCount} - Branch - ${itSub.getBranch()}"
                            println "${iChgCount} - Comment - ${itSub.getComment()}"
                            println "${iChgCount} - Commit ID - ${itSub.getCommitId()}"

                            println "${iChgCount} - Date - ${itSub.getDate()}"
                            println "${iChgCount} - ID - ${itSub.getId()}"
                            println "${iChgCount} - Parent Commit - ${itSub.getParentCommit()}"
                            println "${iChgCount} - Revision - ${itSub.getRevision()}"
                            println "${iChgCount} - TimeStamp - ${itSub.getTimestamp()}"

                            def lstAffectedFiles = itSub.getAffectedFiles();
                            //println "List Affected Files - ${lstAffectedFiles}"
                            lstAffectedFiles.eachWithIndex { itASub, itFileCount -> 
                                println "${itFileCount} - Destination - ${itASub.getDst()}"
                                println "${itFileCount} - Path - ${itASub.getPath()}"
                                println "${itFileCount} - Source - ${itASub.getSrc()}"
                            }

                            // def lstAffectedPaths = itSub.getPaths();
                            // //println "List Affected Paths - ${lstAffectedPaths}"
                            // lstAffectedPaths.eachWithIndex { itPSub, itPathCount -> 
                            //     println "${itPathCount} - Destination - ${itPSub.getDst()}"
                            //     println "${itPathCount} - Path - ${itPSub.getPath()}"
                            //     println "${itPathCount} - Source - ${itPSub.getSrc()}"
                            // }
                        }
                        
                    }
                    println "CulpritIds- ${build.getCulpritIds()}"
                    println "Culprits- ${build.getCulprits()}"
                    //println "Execution- ${build.getExecution()}"
                    //println "ExecutionPromise- ${build.getExecutionPromise()}"
                    //println "FlowGraphDataAsHtml- ${build.getFlowGraphDataAsHtml()}"
                    //println "Log- ${build.getLog()}"
                    //println "LogMax- ${build.getLog(50)}"
                    println "LogFile- ${build.getLogFile().getPath()}"
                    //println "LogInputStream- ${build.getLogInputStream()}"
                    //println "LogReader- ${build.getLogReader()}"
                    //println "LogText- ${build.getLogText()}"
                    println "NextBuild- ${build.getNextBuild()}"
                    println "PreviousBuild- ${build.getPreviousBuild()}"
                    //println "RunMixIn- ${build.getRunMixIn()}"
                    //////println "SCMs- ${build.getSCMs()}"
                    println "AllowKill- ${build.hasAllowKill()}"
                    println "AllowTerm- ${build.hasAllowTerm()}"
                    println "StartedYet- ${build.hasntStartedYet()}"
                    println "IsBuilding- ${build.isBuilding()}"
                    println "InProgress- ${build.isInProgress()}"
                    println "LogUpdated- ${build.isLogUpdated()}"

                    println "---------------------"

                    //println "Action - ${build.getAction()}"
                    //println "Actions - ${build.getActions()}"
                    def lstAllActions = build.getAllActions(); //https://javadoc.jenkins-ci.org/hudson/model/Action.html
                    //println "All Actions - ${lstAllActions}"
                    lstAllActions.each{
                        // if(it instanceof hudson.model.CauseAction)
                        // {
                        //     println "Cause_Counts- ${it.getCauseCounts()}"
                        //     println "Causes- ${it.getCauses()}"
                        //     println "Causes_DisplayName- ${it.getDisplayName()}"
                        //     println "Causes_URL_Name- ${it.getUrlName()}"
                        // }
                        if(it instanceof org.jenkinsci.plugins.workflow.support.actions.WorkspaceRunAction)
                        {
                            //https://javadoc.jenkins.io/plugin/workflow-support/org/jenkinsci/plugins/workflow/support/actions/WorkspaceRunAction.html
                            def lstWkspcActions = it.getActions();
                            //println "Workspace-Actions- ${lstWkspcActions}"
                            //https://javadoc.jenkins.io/plugin/workflow-support/org/jenkinsci/plugins/workflow/support/actions/WorkspaceActionImpl.html
                            lstWkspcActions.eachWithIndex { itSub, iSubCount -> 
                                println "${iSubCount} - Workspace(sub)-DisplayName- ${itSub.getDisplayName()}"
                                
                                sHostName = itSub.getNode()
                                sLabel = itSub.getLabels()
                                println "${iSubCount} - Workspace(sub)-Labels- ${sLabel}"
                                println "${iSubCount} - Workspace(sub)-Node- ${sHostName}"
                                println "${iSubCount} - Workspace(sub)-Parent- ${itSub.getParent()}"

                                //https://javadoc.jenkins.io/plugin/workflow-api/org/jenkinsci/plugins/workflow/graph/FlowNode.html
                                println "${iSubCount} - WSAI-Parent-Name- ${itSub.getParent().getDisplayName()}";
                                println "${iSubCount} - WSAI-Parent-ID- ${itSub.getParent().getId()}";
                                println "${iSubCount} - WSAI-Parent-URL- ${itSub.getParent().getUrl()}";
                                println "${iSubCount} - WSAI-Parent-Active- ${itSub.getParent().isActive()}";
                                println "${iSubCount} - WSAI-Parent-FunctionName- ${itSub.getParent().getDisplayFunctionName()}";
                                println "${iSubCount} - WSAI-Parent-TypeDisplayName- ${itSub.getParent().getTypeDisplayName()}"; //Gets a human readable name for this type of the node.
                                println "${iSubCount} - WSAI-Parent-TypeFunctionName- ${itSub.getParent().getTypeFunctionName()}";

                                println "${iSubCount} - Workspace(sub)-Path- ${itSub.getPath()}"
                                println "${iSubCount} - Workspace(sub)-URLName- ${itSub.getUrlName()}"
                            }
                            println "Workspace-DisplayName- ${it.getDisplayName()}"
                            println "Workspace-URL- ${it.getUrlName()}"
                            println "Workspace-IconFileName- ${it.getIconFileName()}"
                        }
                        if(it instanceof org.jenkinsci.plugins.pipeline.modeldefinition.actions.ExecutionModelAction)
                        {
                            //https://javadoc.jenkins.io/plugin/pipeline-model-definition/org/jenkinsci/plugins/pipeline/modeldefinition/actions/ExecutionModelAction.html
                            println "Stages-UUID- ${it.getStagesUUID()}"
                            println " -+ -+ -+ -+ -+ -+ -+ -+ -+ -+ -+ -+ -+ -+ -+ -+ -+ -+ -+ -+ -+ -+"
                            def lstStages = it.getStagesList();
                            //println "Stages-List- ${lstStages}"
                            //https://javadoc.jenkins.io/plugin/pipeline-model-api/org/jenkinsci/plugins/pipeline/modeldefinition/ast/ModelASTStages.html
                            lstStages.eachWithIndex { itMSub, iMSubCount -> 
                                def lstStage = itMSub.getStages()
                                //https://javadoc.jenkins.io/plugin/pipeline-model-api/org/jenkinsci/plugins/pipeline/modeldefinition/ast/ModelASTStage.html
                                lstStage.eachWithIndex { itSub, iSubCount -> 
                                    println "${iSubCount} Stages-Branches- ${itSub.getBranches()}"
                                    println "${iSubCount} Stages-FailFast- ${itSub.getFailFast()}"
                                    println "${iSubCount} Stages-Matrix- ${itSub.getMatrix()}"                                
                                    println "${iSubCount} Stages-Name- ${itSub.getName()}"
                                    println "${iSubCount} Stages-Parallel- ${itSub.getParallel()}"
                                    println "${iSubCount} Stages- ${itSub.getStages()}"
                                }
                            }
                            println " -+ -+ -+ -+ -+ -+ -+ -+ -+ -+ -+ -+ -+ -+ -+ -+ -+ -+ -+ -+ -+ -+"
                            def lstPipeDefs = it.getPipelineDefs();
                            //println "Pipeline-Defs- ${lstPipeDefs}"
                            println " -= -= -= -= -= -= -= -= -= -= -= -= -= -= -= -= -= -= -= -= -= -="
                            //https://javadoc.jenkins.io/plugin/pipeline-model-api/org/jenkinsci/plugins/pipeline/modeldefinition/ast/ModelASTPipelineDef.html
                            lstPipeDefs.eachWithIndex { itSub, iSubCount -> 
                                println "${iSubCount} Stages-Agent- ${itSub.getAgent()}"
                                println "${iSubCount} Stages-Environment- ${itSub.getEnvironment()}"
                                println "${iSubCount} Stages-Libraries- ${itSub.getLibraries()}"                                
                                println "${iSubCount} Stages-Options- ${itSub.getOptions()}"
                                println "${iSubCount} Stages-Parameters- ${itSub.getParameters()}"
                                println "${iSubCount} Stages-PostBuild- ${itSub.getPostBuild()}"
                                println "${iSubCount} Stages-Stages- ${itSub.getStages()}"
                                println "${iSubCount} Stages-Tools- ${itSub.getTools()}"
                                println "${iSubCount} Stages-Triggers- ${itSub.getTriggers()}"
                            }
                            println " -= -= -= -= -= -= -= -= -= -= -= -= -= -= -= -= -= -= -= -= -= -="
                        }
                        if(it instanceof jenkins.metrics.impl.TimeInQueueAction)
                        {
                            //https://javadoc.jenkins.io/plugin/metrics/jenkins/metrics/impl/TimeInQueueAction.html
                            println "BlockedDurationMillis- ${it.getBlockedDurationMillis()}"
                            println "BlockedDurationString- ${it.getBlockedDurationString()}"
                            println "BlockedTimeMillis- ${it.getBlockedTimeMillis()}"
                            println "BlockedTimeString- ${it.getBlockedTimeString()}"
                            println "BuildableDurationMillis- ${it.getBuildableDurationMillis()}"
                            println "BuildableDurationString- ${it.getBuildableDurationString()}"
                            println "BuildableTimeMillis- ${it.getBuildableTimeMillis()}"
                            println "BuildableTimeString- ${it.getBuildableTimeString()}"
                            println "BuildingDurationMillis- ${it.getBuildingDurationMillis()}"
                            println "BuildingDurationString- ${it.getBuildingDurationString()}"
                            println "DisplayName- ${it.getDisplayName()}"
                            println "ExecutingTimeMillis- ${it.getExecutingTimeMillis()}"
                            println "ExecutingTimeString- ${it.getExecutingTimeString()}"
                            println "ExecutorUtilization- ${it.getExecutorUtilization()}"
                            println "IconFileName- ${it.getIconFileName()}"
                            println "QueuingDurationMillis- ${it.getQueuingDurationMillis()}"
                            println "QueuingDurationString- ${it.getQueuingDurationString()}"
                            println "QueuingTimeMillis- ${it.getQueuingTimeMillis()}"
                            println "QueuingTimeString- ${it.getQueuingTimeString()}"
                            println "Run- ${it.getRun()}"
                            println "SubTaskCount- ${it.getSubTaskCount()}"
                            println "TotalDurationMillis- ${it.getTotalDurationMillis()}"
                            println "TotalDurationString- ${it.getTotalDurationString()}"
                            println "UrlName- ${it.getUrlName()}"
                            println "WaitingDurationMillis- ${it.getWaitingDurationMillis()}"
                            println "WaitingDurationString- ${it.getWaitingDurationString()}"
                            println "WaitingTimeMillis- ${it.getWaitingTimeMillis()}"
                            println "WaitingTimeString- ${it.getWaitingTimeString()}"
                            println "HasSubTasks- ${it.isHasSubTasks()}"
                        }
                        if(it instanceof hudson.plugins.git.util.BuildData)
                        {
                            //https://javadoc.jenkins.io/plugin/git/hudson/plugins/git/util/BuildData.html
                            println "BuildsByBranchName - ${it.getBuildsByBranchName()}" 
                            println "DisplayName - ${it.getDisplayName()}"
                            println "Index - ${it.getIndex()}" 
                            println "SCMName - ${it.getScmName()}"
                            println "URLName - ${it.getUrlName()}"
                        }
                        if(it instanceof jenkins.metrics.impl.SubTaskTimeInQueueAction)
                        {
                            println "BlockedDurationMillis - ${it.getBlockedDurationMillis()}"  //Returns the duration this SubTask spent in the queue because it was blocked.
                            println "BuildableDurationMillis - ${it.getBuildableDurationMillis()}"  //Returns the duration this SubTask spent in the queue in a buildable state.
                            println "ExecutingDurationMillis - ${it.getExecutingDurationMillis()}"//Returns the duration this SubTask spent executing.
                            //println "DisplayName - ${it.getDisplayName()}"
                            //println "IconFileName - ${it.getIconFileName()}"
                            println "QueuingDurationMillis - ${it.getQueuingDurationMillis()}" //How long spent queuing (this is the time from when the WorkUnitContext.item entered the queue until WorkUnitContext.synchronizeStart() was called.
                            println "UrlName - ${it.getUrlName()}"
                            println "WaitingDurationMillis - ${it.getWaitingDurationMillis()}" //Returns the duration this SubTask spent in the queue waiting before it could be considered for execution.
                            println "WorkUnitCount - ${it.getWorkUnitCount()}" //Returns the number of executor slots occupied by this SubTask.
                        }
                    }

                    // println "Search - ${build.getSearch()}"
                    // println "Search Index - ${build.getSearchIndex()}"
                    // println "Search Names - ${build.getSearchName()}"

                    // println "Descriptor Name - ${build.getDescriptorByName()}"

//=====================================================================================================
                    // //println "${build.getAbsoluteUrl()}, ${build.getACL()}, ${build.getApi()}, ${build.getArtifactManager()}, ${build.getArtifacts()}, ${build.getArtifactsDir()}, ${build.getArtifactsUpTo(build.getId().toInteger())}"
                    // println "AbsoluteUrl - ${build.getAbsoluteUrl()}"
                    // println "ACL - ${build.getACL()}"
                    // println "API - ${build.getApi()}"
                    // println "ArtifactManager - ${build.getArtifactManager()}"
                    // println "Artifacts - ${build.getArtifacts()}" 
                    // println "ArtifactsDir - ${build.getArtifactsDir()}"
                    // println "Id - ${build.getId()}"
                    // //println "ArtifactsUpTo - ${build.getArtifactsUpTo(build.getId().toInteger())}"
                    // println "Duration - ${build.getDuration()}"
                    // println "DurationString - ${build.getDurationString()}"
                    // //println "Dynamic - ${build.getDynamic()}"
                    // println "Environment - ${build.getEnvironment()}"
                    // println "EnvVars - ${build.getEnvVars()}"
                    // println "EstimatedDuration - ${build.getEstimatedDuration()}"
                    // println "Executor - ${build.getExecutor()}"
                    // println "ExternalizableId - ${build.getExternalizableId()}"
                    // println "FullDisplayName - ${build.getFullDisplayName()}"
                    // println "HasArtifacts - ${build.getHasArtifacts()}"
                    // println "IconColor - ${build.getIconColor()}"
                    // println "Number - ${build.getNumber()}"
                    // println "OneOffExecutor - ${build.getOneOffExecutor()}"
                    // println "Parent - ${build.getParent()}"
                    // println "PreviousBuildInProgress - ${build.getPreviousBuildInProgress()}"
                    // //println "PreviousBuildsOverThreshold - ${build.getPreviousBuildsOverThreshold(build.getId() - 5, ABORTED))}"
                    // println "PreviousBuiltBuild - ${build.getPreviousBuiltBuild()}"
                    // println "PreviousCompletedBuild - ${build.getPreviousCompletedBuild()}"
                    // println "PreviousFailedBuild - ${build.getPreviousFailedBuild()}"
                    // println "PreviousNotFailedBuild - ${build.getPreviousNotFailedBuild()}"
                    // println "PreviousSuccessfulBuild - ${build.getPreviousSuccessfulBuild()}"
                    // println "QueueId - ${build.getQueueId()}"
                    // println "Result - ${build.getResult()}"
                    // println "RootDir - ${build.getRootDir()}"
                    // println "SearchUrl - ${build.getSearchUrl()}"
                    // println "StartTimeInMillis - ${build.getStartTimeInMillis()}"
                    // println "Target - ${build.getTarget()}"
                    // println "Time - ${build.getTime()}"
                    // println "TimeInMillis - ${build.getTimeInMillis()}"
                    // println "Timestamp - ${build.getTimestamp()}"
                    // println "TimestampString - ${build.getTimestampString()}"
                    // println "TimestampString2 - ${build.getTimestampString2()}"
                    // println "TransientActions - ${build.getTransientActions()}"
                    // println "TruncatedDescription - ${build.getTruncatedDescription()}"
                    // println "Url - ${build.getUrl()}"
                    // //println "WhyKeepLog - ${build.getWhyKeepLog()}"

                    
                    //println "${build.getBadgeActions()}, ${build.getBuildFingerprints()}, ${build.getBuildStatusIconClassName()}, ${build.getBuildStatusSummary()}, ${build.getBuildStatusUrl()}, ${build.getCause()}, ${build.getCauses()}, ${build.getCharacteristicEnvVars()}, ${build.getCharset()}, ${build.getDescription()}, ${build.getDisplayName()}, ${build.getDuration()}, ${build.getDurationString()}, ${build.getDynamic()}, ${build.getEnvironment()}, ${build.getEnvVars()}, ${build.getEstimatedDuration()}, ${build.getExecutor()}, ${build.getExternalizableId()}, ${build.getFullDisplayName()}, ${build.getHasArtifacts()}, ${build.getIconColor()}, ${build.getId()}, ${build.getNumber()}, ${build.getOneOffExecutor()}, ${build.getParent()}, ${build.getPreviousBuildInProgress()}, ${build.getPreviousBuildsOverThreshold()}, ${build.getPreviousBuiltBuild()}, ${build.getPreviousCompletedBuild()}, ${build.getPreviousFailedBuild()}, ${build.getPreviousNotFailedBuild()}, ${build.getPreviousSuccessfulBuild()}, ${build.getQueueId()}, ${build.getResult()}, ${build.getRootDir()}, ${build.getSearchUrl()}, ${build.getStartTimeInMillis()}, ${build.getTarget()}, ${build.getTime()}, ${build.getTimeInMillis()}, ${build.getTimestamp()}, ${build.getTimestampString()}, ${build.getTimestampString2()}, ${build.getTransientActions()}, ${build.getTruncatedDescription()}, ${build.getUrl()}, ${build.getWhyKeepLog()}"

                    //Add all details to the List<LabelBuild>
                    def myLabelBuild = new LabelBuild(Count: iCount, Name: build, StartTime: build.time, StatusSummary: build.getBuildStatusSummary().message, HostName: sHostName, Label: sLabel, Duration: build.getDuration(), Environment: build.getEnvironment())
                    lstLblBlds.add(myLabelBuild)
                    println "${iCount} - ${build} - ${build.time} - ${build.getBuildStatusSummary().message} - ${sHostName} - ${sLabel} - ${build.getDurationString()} - ${build.getEnvironment()}"
                }
                catch(Exception e)
                {
                    println "Exception when searching through logs... - ${e.message}";
                }
      	//println "List - ${lstLblBlds}"
    }.absoluteUrl.join('\n')

  println "Label builds so far are ... ${lstLblBlds}"
  	//[Count: it.Count, Name: it.Name, StartTime: it.StartTime, StatusSummary: it.StatusSummary]
  	def jsonLabels = new groovy.json.JsonBuilder()//JsonSlurper()
    jsonLabels {Labels(
        lstLblBlds.collect {
            [Count: it.Count, Name: it.Name, StartTime:it.StartTime, StatusSummary: it.StatusSummary, Duration:it.Duration, HostName: it.HostName, Label:it.Label, Master: strMaster, Environment: it.Environment]
        })
    }

    println "${jsonLabels.toPrettyString()}"

    //jsonLabel.("Count": iCount, "Build")
    println "Build count after - ${before60Minutes} are - ${iCount - 1}" //${myItems.size()}"
  
  //echo "curl -f --data \"${jsonLabels}\" -k https://hec.splunk.mclocal.int:13510/services/collector/raw -H \"Authorization: Splunk a0ec61b4-cb16-4a62-a159-9c3982d11153\""
  
  // def sout = new StringBuilder(), serr = new StringBuilder()
  // def proc = "curl -f --data \"${jsonLabels}\" -k \"https://hec.splunk.mclocal.int:13510/services/collector/raw\" -H \"Authorization: Splunk a0ec61b4-cb16-4a62-a159-9c3982d11153\"".execute()
  // proc.consumeProcessOutput(sout, serr)
  // proc.waitForOrKill(1000)
  // println "out> $sout err> $serr"
}
catch(Exception exp)
{
    println "EXCEPTION - ${exp.message}"
}

//--------------------------------------------------
def endDate = new Date()
println "End time: ${sdf.format(endDate)}"

use(groovy.time.TimeCategory) 
{
    def duration = endDate - startDate
  	print "Elapsed time: Days: ${duration.days}, Hours: ${duration.hours}, Minutes: ${duration.minutes}, Seconds: ${duration.seconds}"
}

// @NonCPS
// def before60Minutes ()
// {
//     use( groovy.time.TimeCategory )
//     {
//         return 60.minutes.ago
//     }
// }