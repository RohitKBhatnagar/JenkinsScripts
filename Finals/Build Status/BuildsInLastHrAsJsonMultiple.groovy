import com.cloudbees.groovy.cps.NonCPS
import hudson.model.Executor

/// Workspace cleaner script to be run as a pipeline job
/// Author: Rohit K. Bhatnagar
/// Modified By: Rohit K. Bhatnagar
/// Modified On: August 02, 2020
/// Modified Reason: Adding support for Label referenced Nodes and Hourly statistics for builds conducted.

import java.text.SimpleDateFormat
import java.time.OffsetDateTime;

import jenkins.model.Jenkins
import org.jenkinsci.plugins.workflow.job.WorkflowJob
import org.jenkinsci.plugins.workflow.job.WorkflowRun
import groovy.json.JsonOutput

class LabelBuild {
    long Count
    String Name
    String StartTime
    String StatusSummary
    long Duration
    String HostName
    String Label

    //Following get methods are from https://javadoc.jenkins-ci.org/hudson/model/Run.html

    String BuildID;
    String DurationString
    Map Environment
    //Map EnvVars
    long EstimatedDuration

    //https://javadoc.jenkins-ci.org/hudson/model/Executor.html
    Executor Executor
    long TimeSpentInQueue //Returns the number of milli-seconds the currently executing job spent in the queue waiting for an available executor.
    boolean BuildStuck; //true if the current build is likely stuck
    boolean BuildParked; //true if executor is waiting for a task to execute.

    String ExternalizableId
    String FullDisplayName
    Boolean HasArtifacts
    //BallColor IconColor

    String Number
    //String OneOffExecutor
    String Parent //JobT 	getParent() -     The project this build is for. Calling getDisplayName() to return with a parent name!

    /*String PreviousBuildInProgress
    String PreviousBuildsOverThreshold
    String PreviousBuiltBuild
    String PreviousCompletedBuild
    String PreviousFailedBuild
    String PreviousNotFailedBuild
    String PreviousSuccessfulBuild*/

    long QueueId
    String Result
    String RootDir
    String SearchUrl
    long StartTimeInMillis
    //Object Target
    Date Time
    long TimeInMillis
    //Calendar Timestamp
    String TimestampString
    String TimestampString2
    //String TransientActions
    //String TruncatedDescription
    String Url

    Map SCMs;
    //https://javadoc.jenkins.io/plugin/workflow-job/org/jenkinsci/plugins/workflow/job/WorkflowRun.html#getChangeSets--
    ArrayList<Map> ChangeSets;     //https://javadoc.jenkins.io/plugin/git/hudson/plugins/git/GitChangeSet.html

    String CulpritIDs;
    String Culprits;
    String LogFile
    Long LogLength

    boolean AllowKill;
    boolean AllowTerm;
    boolean StartedYet;
    boolean IsBuilding;
    boolean InProgress;
    boolean LogUpdated;

    //ArrayList<Map> BuildAllActions;
    List<Map> BuildAllActions;
}


def jenkinsMasterWorkFlowItems = Jenkins.getInstanceOrNull().getAllItems(WorkflowJob);
//def jenkinsMasterWorkFlowItems = Jenkins.getInstanceOrNull().getAllItems();
def jenkinsMasterNodes = Jenkins.getInstanceOrNull().getNodes(); 
def String strMaster = java.net.InetAddress.getLocalHost();
def sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss.SSS")
def startDate = new Date()
println "Start time : ${sdf.format(startDate)}"

def slave_label_map = [:]
def iCount = 0
for (slave in jenkinsMasterNodes)
{
	def words = slave.labelString.split()
	def labelListForSlave = []
	words.each() {
		labelListForSlave.add(it);
	}
	slave_label_map.put(slave.name, labelListForSlave)
}

/*****
Calendar c = Calendar.getInstance()
//def date = new Date()
c.setTime(before60Minutes);
println("The build is run at ${c.time}");
println(c.get(Calendar.HOUR_OF_DAY) + ":" + c.get(Calendar.MINUTE));
*****/
def Date before60Minutes
use( groovy.time.TimeCategory ) 
{
  before60Minutes = new Date() - 60.minutes
}

ArrayList<LabelBuild> lstLblBlds = new ArrayList<LabelBuild>();
def allItems = jenkinsMasterWorkFlowItems
println "Total Workflow Items - ${allItems.size()}"
try
{
	def myItems = allItems.findAll
	{
		try
		{
			WorkflowRun build = it.builds.first()
			new Date(build.startTimeInMillis).after(before60Minutes)
		}
		catch(Exception exp)
		{
			//Do Nothing
			//println("${it.class.simpleName} - ${exp.message}")
		}
	}.collect
	{
		it.builds.findAll
		{
			WorkflowRun build ->
				new Date(build.startTimeInMillis).after(before60Minutes)
		}
	}.flatten().findAll
	{
		WorkflowRun build ->
			iCount++
			try {
				def sHostName = ""
				String sLabel = ""

				//SCMs
				Map lstSCMs = [:]
				def scmsLst = build.getSCMs();
				scmsLst.each {
					lstSCMs.put('SCMKey', it.getKey()); // - Type - ${it.getType()}}"
				}
				//Build based ChangeSets Listing
				ArrayList<Map> lstChgSetBlds = new ArrayList<Map>();//https://javadoc.jenkins.io/plugin/workflow-job/org/jenkinsci/plugins/workflow/job/WorkflowRun.html
				List<Map> lstOfChgSets = new ArrayList<Map>();
				def lstChangeSets = build.getChangeSets();  //https://javadoc.jenkins.io/plugin/git/hudson/plugins/git/GitChangeSetList.html
				lstChangeSets.eachWithIndex { it, itCount ->
					Map lstChgSets = [:];
					lstChgSets.put('Kind', it.getKind())
					lstChgSets.put('EmptySet', it.isEmptySet())
					def lstGitChgSet = it.getLogs();
					List<Map> lstOfGitChangeSet = new ArrayList<Map>();
					lstGitChgSet.eachWithIndex { itSub, iChgCount ->
						Map lstGitChangeSet = [:]
						lstGitChangeSet.put('AuthorMail', itSub.getAuthorEmail())
						lstGitChangeSet.put('AuthorName', itSub.getAuthorName())
						lstGitChangeSet.put('Branch', itSub.getBranch())
						lstGitChangeSet.put('Comment', itSub.getComment())
						lstGitChangeSet.put('CommitID', itSub.getCommitId())
						lstGitChangeSet.put('Date', itSub.getDate())
						lstGitChangeSet.put('ID', itSub.getId())
						lstGitChangeSet.put('ParentCommit', itSub.getParentCommit())
						lstGitChangeSet.put('Revision', itSub.getRevision())
						lstGitChangeSet.put('TimeStamp', itSub.getTimestamp())

						List<Map> lstOfFiles = new ArrayList<Map>();
						def lstAffectedFiles = itSub.getAffectedFiles();
						lstAffectedFiles.eachWithIndex { itASub, itACount ->
							Map lstFiles = [:]
							lstFiles.put('Destination', itASub.getDst())
							lstFiles.put('Path', itASub.getPath())
							lstFiles.put('Source', itASub.getSrc())

							//lstOfFiles.add(itACount, lstFiles)
							lstOfFiles.add(lstFiles)
						}
						//lstOfGitChangeSet.add(iChgCount, lstGitChangeSet);
						lstOfGitChangeSet.add(lstGitChangeSet);
						//lstOfGitChangeSet.addAll(iChgCount, lstOfFiles);
						lstOfGitChangeSet.addAll(lstOfFiles);
					}
					//lstOfChgSets.add(itCount, lstChgSets)
					lstOfChgSets.add(lstChgSets)
					//lstOfChgSets.addAll(itCount, lstOfGitChangeSet)
					lstOfChgSets.addAll(lstOfGitChangeSet)
				}
				lstChgSetBlds.addAll(lstOfChgSets);

				//DEBUGGING------
				//println "Change-Sets - ${lstChgSetBlds}"

				//--------------------- Build AllActions - START ----------------
				//ArrayList<Map> lstBldAllActions = new ArrayList<Map>();//https://javadoc.jenkins.io/plugin/workflow-job/org/jenkinsci/plugins/workflow/job/WorkflowRun.html
				List<Map> lstOfAllBldActions = new ArrayList<Map>();
				def lstAllActions = build.getAllActions(); //https://javadoc.jenkins-ci.org/hudson/model/Action.html
				lstAllActions.each {
					List<Map> lstOfBldActions = new ArrayList<Map>();
					if(it instanceof org.jenkinsci.plugins.workflow.support.actions.WorkspaceRunAction)
					{
						Map lstWSRA = [:];
						lstWSRA.put("WSRA-DisplayName", it.getDisplayName())
						lstWSRA.put("WSRA-URL", it.getUrlName())
						lstWSRA.put("WSRA-IconFileName", it.getIconFileName())

						//https://javadoc.jenkins.io/plugin/workflow-support/org/jenkinsci/plugins/workflow/support/actions/WorkspaceRunAction.html
						def lstWkspcActions = it.getActions();

						List<Map> lstOfWSAI = new ArrayList<Map>();
						//https://javadoc.jenkins.io/plugin/workflow-support/org/jenkinsci/plugins/workflow/support/actions/WorkspaceActionImpl.html
						lstWkspcActions.each { itSub ->
							Map lstWSAI = [:]
							lstWSAI.put("WSAI-DisplayName", itSub.getDisplayName());

							sHostName = itSub.getNode()
							sLabel = itSub.getLabels();
							//sLabel = sLabel.replace('[', '');
							//sLabel = sLabel.replace(']', '');
							lstWSAI.put("WSAI-Labels", sLabel);
							lstWSAI.put("WSAI-Node", sHostName);
							lstWSAI.put("WSAI-Parent-Name", itSub.getParent().getDisplayName());
							lstWSAI.put("WSAI-Parent-ID", itSub.getParent().getId());
							lstWSAI.put("WSAI-Parent-URL", itSub.getParent().getUrl());
							lstWSAI.put("WSAI-Parent-Active", itSub.getParent().isActive());
							lstWSAI.put("WSAI-Parent-FunctionName", itSub.getParent().getDisplayFunctionName());
							lstWSAI.put("WSAI-Parent-TypeDisplayName", itSub.getParent().getTypeDisplayName()); //Gets a human readable name for this type of the node.
							lstWSAI.put("WSAI-Parent-TypeFunctionName", itSub.getParent().getTypeFunctionName());
							lstWSAI.put("WSAI-Path", itSub.getPath());
							lstWSAI.put("WSAI-URLName", itSub.getUrlName());

							//DEBUGGING------
							//println "DEBUGGING-WSAI - ${lstWSAI}";

							lstOfWSAI.add(lstWSAI);
						}

						lstOfBldActions.add(lstWSRA);
						lstOfBldActions.addAll(lstOfWSAI);
					}
					lstOfAllBldActions.addAll(lstOfBldActions);

					if(it instanceof org.jenkinsci.plugins.pipeline.modeldefinition.actions.ExecutionModelAction) {
						Map lstEMA = [:]
						lstEMA.put("EMA-UUID", it.getStagesUUID());
						lstOfBldActions.add(lstEMA);
						lstOfBldActions.addAll(it.getStagesList());
						lstOfBldActions.addAll(it.getPipelineDefs());
					}
					if(it instanceof jenkins.metrics.impl.TimeInQueueAction)
					{
						Map lstTQA = [:];
						//https://javadoc.jenkins.io/plugin/metrics/jenkins/metrics/impl/TimeInQueueAction.html
						lstTQA.put("BlockedDurationMillis", it.getBlockedDurationMillis());
						lstTQA.put("BlockedDurationString", it.getBlockedDurationString());
						lstTQA.put("BlockedTimeMillis", it.getBlockedTimeMillis());
						lstTQA.put("BlockedTimeString", it.getBlockedTimeString());
						lstTQA.put("BuildableDurationMillis", it.getBuildableDurationMillis());
						lstTQA.put("BuildableDurationString", it.getBuildableDurationString());
						lstTQA.put("BuildableTimeMillis", it.getBuildableTimeMillis());
						lstTQA.put("BuildableTimeString", it.getBuildableTimeString());
						lstTQA.put("BuildingDurationMillis", it.getBuildingDurationMillis());
						lstTQA.put("BuildingDurationString", it.getBuildingDurationString());
						lstTQA.put("DisplayName", it.getDisplayName());
						lstTQA.put("ExecutingTimeMillis", it.getExecutingTimeMillis());
						lstTQA.put("ExecutingTimeString", it.getExecutingTimeString());
						lstTQA.put("ExecutorUtilization", it.getExecutorUtilization());
						lstTQA.put("IconFileName", it.getIconFileName());
						lstTQA.put("QueuingDurationMillis", it.getQueuingDurationMillis());
						lstTQA.put("QueuingDurationString", it.getQueuingDurationString());
						lstTQA.put("QueuingTimeMillis", it.getQueuingTimeMillis());
						lstTQA.put("QueuingTimeString", it.getQueuingTimeString());
						//https://javadoc.jenkins.io/hudson/model/Run.html?is-external=true
						lstTQA.put("Run-DisplayName", it.getRun().getDisplayName());
						lstTQA.put("Run-FullDisplayName", it.getRun().getFullDisplayName());
						lstTQA.put("Run-BuildStatusURL", it.getRun().getBuildStatusUrl());
						lstTQA.put("Run-Duration", it.getRun().getDuration());
						lstTQA.put("Run-DurationString", it.getRun().getDurationString());

						lstTQA.put("SubTaskCount", it.getSubTaskCount());
						lstTQA.put("TotalDurationMillis", it.getTotalDurationMillis());
						lstTQA.put("TotalDurationString", it.getTotalDurationString());
						lstTQA.put("UrlName", it.getUrlName());
						lstTQA.put("WaitingDurationMillis", it.getWaitingDurationMillis());
						lstTQA.put("WaitingDurationString", it.getWaitingDurationString());
						lstTQA.put("WaitingTimeMillis", it.getWaitingTimeMillis());
						lstTQA.put("WaitingTimeString", it.getWaitingTimeString());
						lstTQA.put("HasSubTasks", it.isHasSubTasks());

						//DEBUGGING------
						//println "DEBUGGING-TimeInQueueAction - ${lstTQA}";

						lstOfBldActions.add(lstTQA);
					}
					if(it instanceof hudson.plugins.git.util.BuildData)
					{
						Map lstBD = [:];
						//https://javadoc.jenkins.io/plugin/git/hudson/plugins/git/util/BuildData.html
						lstBD.put("BD-DisplayName", it.getDisplayName());
						lstBD.put("BD-Index", it.getIndex());
						lstBD.put("BD-SCMName", it.getScmName());
						lstBD.put("BD-URLName", it.getUrlName());

						//DEBUGGING------
						//println "DEBUGGING-BuildData - ${lstBD}";

						lstOfBldActions.add(lstBD);
						lstOfBldActions.addAll(it.getBuildsByBranchName());
					}
					if(it instanceof jenkins.metrics.impl.SubTaskTimeInQueueAction)
					{
						Map lstSTTQA = [:];
						lstSTTQA.put("BlockedDurationMillis", it.getBlockedDurationMillis());  //Returns the duration this SubTask spent in the queue because it was blocked.
						lstSTTQA.put("BuildableDurationMillis", it.getBuildableDurationMillis());  //Returns the duration this SubTask spent in the queue in a buildable state.
						lstSTTQA.put("ExecutingDurationMillis", it.getExecutingDurationMillis());//Returns the duration this SubTask spent executing.
						lstSTTQA.put("DisplayName", it.getDisplayName());
						lstSTTQA.put("IconFileName", it.getIconFileName());
						lstSTTQA.put("QueuingDurationMillis", it.getQueuingDurationMillis()); //How long spent queuing (this is the time from when the WorkUnitContext.item entered the queue until WorkUnitContext.synchronizeStart() was called.
						lstSTTQA.put("UrlName", it.getUrlName());
						lstSTTQA.put("WaitingDurationMillis", it.getWaitingDurationMillis()); //Returns the duration this SubTask spent in the queue waiting before it could be considered for execution.
						lstSTTQA.put("WorkUnitCount", it.getWorkUnitCount()); //Returns the number of executor slots occupied by this SubTask.

						//DEBUGGING------
						//println "DEBUGGING-SubTaskTimeInQueueAction - ${lstSTTQA}";

						lstOfBldActions.add(lstSTTQA);
					}

					lstOfAllBldActions.addAll(lstOfBldActions);

					//lstBldAllActions.addAll(lstOfBldActions);
				}

				//https://javadoc.jenkins-ci.org/hudson/model/Run.html - //https://javadoc.jenkins-ci.org/hudson/model/Executor.html
				Executor buildExecutor = build.getExecutor();
				long queueWaitTime = -1; boolean executorStuck = false; boolean executorParked = false;
				if(buildExecutor) {
					queueWaitTime = buildExecutor.getTimeSpentInQueue();
					executorStuck = buildExecutor.isLikelyStuck()
					executorParked = buildExecutor.isParking()
				}

				//Add all details to the List<LabelBuild>
				def myLabelBuild = new LabelBuild(Count: iCount, Name: build, StartTime: build.time, StatusSummary: build.getBuildStatusSummary().message, HostName: sHostName, Label: sLabel, BuildID: build.getId(), Duration: build.getDuration(), DurationString: build.getDurationString(), Environment: build.getEnvironment(), EstimatedDuration: build.getEstimatedDuration(), TimeSpentInQueue: queueWaitTime, BuildStuck: executorStuck, BuildParked: executorParked, ExternalizableId: build.getExternalizableId(), FullDisplayName: build.getFullDisplayName(), HasArtifacts: build.getHasArtifacts(), Number: build.getNumber(), Parent: build.getParent().getDisplayName(), QueueId: build.getQueueId(), Result: build.getResult().toString(), RootDir: build.getRootDir().getAbsolutePath(), SearchUrl: build.getSearchUrl(), StartTimeInMillis: build.getStartTimeInMillis(), Time: build.getTime(), TimeInMillis: build.getTimeInMillis(), TimestampString: build.getTimestampString(), TimestampString2: build.getTimestampString2(), Url: build.getUrl(), SCMs: lstSCMs, ChangeSets: lstChgSetBlds, CulpritIDs: build.getCulpritIds(), Culprits: build.getCulprits(), LogFile: build.getLogFile().getPath(), LogLength: build.getLogFile().length(), AllowKill: build.hasAllowKill(), AllowTerm: build.hasAllowTerm(), StartedYet: build.hasntStartedYet(), IsBuilding: build.isBuilding(), InProgress: build.isInProgress(), LogUpdated: build.isLogUpdated(), BuildAllActions: lstOfAllBldActions); //lstBldAllActions);

				lstLblBlds.add(myLabelBuild);

				//DEBUGGING------
				//println "LabelBuild - ${lstLblBlds}";
			}
			catch(Exception e)
			{
				println "Exception raised... - ${e.message}";
			}
	}*.absoluteUrl.join('\n')

	println "Build count after - ${before60Minutes} are - ${iCount - 1}"

	String sJsonRoot = "LabelStats";
	//------------------------------------------------------------------------------------------
	/////////////////////////////////////////////////////////////////////

	def jsonTotalItemBuilder = new groovy.json.JsonBuilder()  
		jsonTotalItemBuilder 
		{
			LabelStats lstLblBlds.collect {
				[ 
					lstLblBlds
				]
			}
		}
		println jsonTotalItemBuilder.toPrettyString() 
	//------------------------------------------------------------------------------------------
	/////////////////////////////////////////////////////////////////////////////////////
	////Following is an attempt to entire list of ArrayList elements to be parsed as JSON///
	////============================================================================= ///
	//Print the MAP for debug only
	/************
	lstLblBlds.each { e->
		LabelBuild oLblBld = e
		//steps.echo("${oLblBld.Count} - ${oLblBld.Name} - ${oLblBld.StartTime} - ${oLblBld.StatusSummary} - ${oLblBld.HostName} - ${oLblBld.Label} - ${oLblBld.Duration} - ${sMaster}")

		Map splunkJSON = [Count:oLblBld.Count, Name:oLblBld.Name, StartTime:oLblBld.StartTime, StatusSummary:oLblBld.StatusSummary, Duration:oLblBld.Duration, HostName:oLblBld.HostName, Label:oLblBld.Label, Master:strMaster, BuildID: oLblBld.BuildID, Duration: oLblBld.Duration, DurationString: oLblBld.DurationString, Environment: oLblBld.Environment, EstimatedDuration: oLblBld.EstimatedDuration, TimeSpentInQueue: oLblBld.TimeSpentInQueue, BuildStuck: oLblBld.BuildStuck, BuildParked: oLblBld.BuildParked, ExternalizableId: oLblBld.ExternalizableId, FullDisplayName: oLblBld.FullDisplayName, HasArtifacts: oLblBld.HasArtifacts, Number: oLblBld.Number, Parent: oLblBld.Parent, QueueId: oLblBld.QueueId, Result: oLblBld.Result, RootDir: oLblBld.RootDir, SearchUrl: oLblBld.SearchUrl, StartTimeInMillis: oLblBld.StartTimeInMillis, Time: oLblBld.Time, TimeInMillis: oLblBld.TimeInMillis, TimestampString: oLblBld.TimestampString, TimestampString2: oLblBld.TimestampString2, Url: oLblBld.Url, BuildSCM: oLblBld.SCMs, ChangeSets: oLblBld.ChangeSets, CulpritIDs: oLblBld.CulpritIDs, Culprits: oLblBld.Culprits, LogFile: oLblBld.LogFile, LogLength: oLblBld.LogLength, AllowKill: oLblBld.AllowKill, AllowTerm: oLblBld.AllowTerm, StartedYet: oLblBld.StartedYet, IsBuilding: oLblBld.IsBuilding, InProgress: oLblBld.InProgress, LogUpdated: oLblBld.LogUpdated, BuildAllActions: oLblBld.BuildAllActions];

		def jsonTotalItemBuilder = new groovy.json.JsonBuilder()  
		jsonTotalItemBuilder 
		{
			LabelStats splunkJSON.collect {
				[ 
					splunkJSON
				]
			}
		}
		println jsonTotalItemBuilder.toPrettyString() 
	}
	************************/
}
catch(StringIndexOutOfBoundsException sioobe)
{
	println "STRING INDEX OUT OF BOUNDS EXCEPTION - ${sioobe.message}"
}
catch(Exception exp)
{
	println "EXCEPTION - ${exp.message}"
}

//--------------------------------------------------
def endDate = new Date()
println "End time: ${sdf.format(endDate)}"

return


