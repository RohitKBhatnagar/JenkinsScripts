//Display list of projects that were built more than 180 days ago.

import hudson.slaves.*
import java.util.concurrent.*
import org.jenkinsci.plugins.workflow.job.WorkflowJob
import java.util.Calendar
import java.text.SimpleDateFormat
import java.util.Date
import com.cloudbees.hudson.plugins.folder.Folder

//import java.util.GregorianCalendar

jenkins = Hudson.instance
controller = Jenkins.getInstance().getComputer('').getHostName()
println "===================================================================================================================="
println "Counts of all builds and their respective age including whether they are disabled, frozen/patch titled or never run!"
println "Report collected on the Controller with hostname - ${controller}"
println "===================================================================================================================="

def sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss.SSS")
def startDate = new Date()
println "Start time : ${sdf.format(startDate)}"


Calendar c = Calendar.getInstance()
Calendar oneHundredEightyDays = Calendar.getInstance()
Calendar oneYear = Calendar.getInstance()

now = c.instance
def date = new Date()
c.setTime(date);

oneHundredEightyDays.add(Calendar.DATE, -180)
oneYear.add(Calendar.DATE, -365)

println("Starting report generation at - ${now.time}")
println "++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++"
def allItems = Jenkins.instance.getAllItems()
def iItemCount = 0

//println("item count=${allItems.size()}")

// counts is an array to collect stats
//      0       1        2       3       4  
// |one year|180 days||disabled|freeze|never
def counts = [0, 0, 0, 0, 0] as int[]
def build_time;

def folderName = ''
for (item in allItems) {
    //println("\t ${iItemCount++} - ${item.name}")
    //println( "-------------------" );
    //printAllMethods( item );
    //println( "-------------------" );
    try {
        if (item.name ==~ /(?i)(freeze|patch).*/) {
            counts[3]++
            println"Frozen, ${item.getClass()}, '${item.getRootDir()}', '${item.getFullName()}', '${item.getAbsoluteUrl()}'";
            continue
        }
        if (item.disabled) {
            counts[2]++
            println"Disabled, ${item.getClass()}, '${item.getRootDir()}', '${item.getFullName()}', '${item.getAbsoluteUrl()}'";
            continue
        }

    //Skip if item is say folder name
    try{
      if(item instanceof Folder)
      {
      	//println "Folder, ${item.name}"
        folderName = item.name
        continue;
      }
      else
        build_time = item.getLastBuild()    
    }catch (Exception e){
        //Do nothing as this maybe a folder
        continue;
    }

        if (build_time != null) {
        build_time = item.getLastBuild().getTimestamp()    
            
            if (build_time.compareTo(oneYear) < 1) {
                counts[0]++
                println"365, ${item.getClass()}, '${item.getRootDir()}', '${item.getFullName()}', '${item.getAbsoluteUrl()}'";
                continue
            }
            if (build_time.compareTo(oneHundredEightyDays) < 1 && build_time.compareTo(oneYear) >= 1) {
                counts[1]++
                println"180, ${item.getClass()}, '${item.getRootDir()}', '${item.getFullName()}', '${item.getAbsoluteUrl()}'";
                continue
            }
        } else {
            //never built
            counts[4]++
            println"Never, ${item.getClass()}, '${item.getRootDir()}', '${item.getFullName()}', '${item.getAbsoluteUrl()}'";
        }
    }
    catch (Exception e) {
        println "Exception raised ... - ${e}"
    }
}

println("\t|one year\t|180 days\t|disabled\t\t|frozen\t\t|never\t\t|")
println("\t=======================================================================================================================")
println("\t|one year+ : ${counts[0]}\t|180+ days : ${counts[1]}\t|disabled : ${counts[2]}\t|freeze : ${counts[3]}\t|never : ${counts[4]}\t|")
println("Total 180+ days old builds in ${controller} are : ${counts.sum() - (counts[4] + counts[3] + counts[2])} where total item count is : ${allItems.size}")

println "################################################################################"
println("\tOne year, 180 days, disabled, frozen, never")
println("\t${counts[0]}, ${counts[1]}, ${counts[2]}, ${counts[3]}, ${counts[4]}");

//--------------------------------------------------
println "++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++"
def endDate = new Date()
println "End time: ${sdf.format(endDate)}"


void printAllMethods( obj ){
    if( !obj ){
    println( "Object is null\r\n" );
    return;
    }
  if( !obj.metaClass && obj.getClass() ){
        printAllMethods( obj.getClass() );
    return;
    }
  def str = "class ${obj.getClass().name} functions:\r\n";
  obj.metaClass.methods.name.unique().each{ 
    str += it+"(); "; 
  }
  println "${str}\r\n";
}

/*******
class com.cloudbees.hudson.plugins.folder.Folder functions:
equals(); getClass(); hashCode(); notify(); notifyAll(); toString(); wait(); getSearch(); getSearchIndex(); getSearchName(); addAction(); addOrReplaceAction(); doContextMenu(); 
getAction(); getActions(); getAllActions(); getDynamic(); removeAction(); removeActions(); replaceAction(); replaceActions(); delete(); doCheckNewName(); doConfigDotXml(); 
doConfirmRename(); doDoDelete(); doReload(); doSubmitDescription(); getACL(); getAbsoluteUrl(); getAllJobs(); getApi(); getConfigFile(); getDescription(); getDisplayName(); 
getDisplayNameOrNull(); getFullDisplayName(); getFullName(); getName(); getParent(); getPronoun(); getRelativeDisplayNameFrom(); getRelativeNameFromGroup(); getRootDir(); 
getSearchUrl(); getShortUrl(); getTarget(); getTaskNoun(); getUrl(); isNameEditable(); movedTo(); onCopiedFrom(); onLoad(); resolveForCLI(); save(); setDescription(); 
setDisplayName(); setDisplayNameOrNull(); updateByXml(); writeConfigDotXml(); addProperty(); addView(); canDelete(); deleteView(); doChildrenContextMenu(); doConfigSubmit(); 
doCreateView(); doDisable(); doEnable(); doLastBuild(); doViewExistsCheck(); getBuildHealth(); getBuildHealthReports(); getDescriptor(); getFolderViews(); getHealthMetrics(); 
getIcon(); getIconColor(); getItem(); getItemGroup(); getItems(); getJob(); getOverrides(); getPrimaryView(); getProperties(); getRootDirFor(); getStaplerFallback(); 
getUrlChildPrefix(); getView(); getViewActions(); getViews(); getViewsTabBar(); hasVisibleItems(); invalidateBuildHealthReports(); isDisabled(); loadChildren(); loadJobTotal(); 
makeDisabled(); onDeleted(); onRenamed(); onViewRenamed(); renameTo(); resetFolderViews(); setIcon(); setPrimaryView(); supportsMakeDisabled(); add(); canAdd(); copy(); 
createProject(); createProjectFromXML(); doCreateItem(); getColumns(); getItemDescriptors(); getNewPronoun(); isAllowedChild(); isAllowedChildDescriptor(); onCreatedFromScratch(); remove(); 



------------------

class org.jenkinsci.plugins.workflow.job.WorkflowJob functions:
equals(); getClass(); hashCode(); notify(); notifyAll(); toString(); wait(); getSearch(); getSearchIndex(); getSearchName(); addAction(); addOrReplaceAction(); doContextMenu(); 
getAction(); getActions(); getAllActions(); getDynamic(); removeAction(); removeActions(); replaceAction(); replaceActions(); delete(); doCheckNewName(); doConfigDotXml(); doConfirmRename(); 
doDoDelete(); doReload(); doSubmitDescription(); getACL(); getAbsoluteUrl(); getAllJobs(); getApi(); getConfigFile(); getDescription(); getDisplayName(); getDisplayNameOrNull(); 
getFullDisplayName(); getFullName(); getName(); getParent(); getPronoun(); getRelativeDisplayNameFrom(); getRelativeNameFromGroup(); getRootDir(); getSearchUrl(); getShortUrl(); 
getTarget(); getTaskNoun(); getUrl(); isNameEditable(); movedTo(); onCopiedFrom(); onLoad(); resolveForCLI(); save(); setDescription(); setDisplayName(); setDisplayNameOrNull(); 
updateByXml(); writeConfigDotXml(); addProperty(); assignBuildNumber(); doBuildStatus(); doChildrenContextMenu(); doConfigSubmit(); doDescription(); doDoRename(); doRssAll(); 
doRssChangelog(); doRssFailed(); getAllProperties(); getBuild(); getBuildByNumber(); getBuildDir(); getBuildDiscarder(); getBuildForCLI(); getBuildHealth(); getBuildHealthReports(); 
getBuildStatusIconClassName(); getBuildStatusUrl(); getBuildTimeGraph(); getBuilds(); getBuildsAsMap(); getBuildsByTimestamp(); getCharacteristicEnvVars(); getEnvironment(); 
getEstimatedDuration(); getFirstBuild(); getIconColor(); getLastBuild(); getLastBuildsOverThreshold(); getLastCompletedBuild(); getLastFailedBuild(); getLastStableBuild(); 
getLastSuccessfulBuild(); getLastUnstableBuild(); getLastUnsuccessfulBuild(); getLogRotator(); getNearestBuild(); getNearestOldBuild(); getNewBuilds(); getNextBuildNumber(); 
getOverrides(); getPermalinks(); getProperties(); getProperty(); getQueueItem(); getTimeline(); getWidgets(); isBuildable(); isBuilding(); isHoldOffBuildUntilSave(); isInQueue(); 
isKeepDependencies(); isLogUpdated(); logRotate(); onCreatedFromScratch(); removeProperty(); renameTo(); setBuildDiscarder(); setLogRotator(); supportsLogRotator(); 
updateNextBuildNumber(); addTrigger(); addTriggersJobPropertyWithoutStart(); alias(); asItem(); checkAbortPermission(); getAssignedLabel(); getAuthToken(); getCauseOfBlockage(); 
getDefinition(); getDescriptor(); getHasCustomQuietPeriod(); getLastBuiltOn(); getLazyBuildMixIn(); getQuietPeriod(); getSCMTrigger(); getSCMs(); getSameNodeConstraint(); 
getSubTasks(); getTriggers(); getTriggersJobProperty(); getTypicalSCM(); hasAbortPermission(); isConcurrentBuild(); isDisabled(); isResumeBlocked(); poll(); scheduleBuild2(); 
setConcurrentBuild(); setDefinition(); setDisabled(); setQuietPeriod(); setResumeBlocked(); setTriggers(); supportsMakeDisabled(); 


----------------

class org.jenkinsci.plugins.workflow.multibranch.WorkflowMultiBranchProject functions:
equals(); getClass(); hashCode(); notify(); notifyAll(); toString(); wait(); getSearch(); getSearchIndex(); getSearchName(); addAction(); addOrReplaceAction(); doContextMenu(); 
getAction(); getActions(); getAllActions(); getDynamic(); removeAction(); removeActions(); replaceAction(); replaceActions(); delete(); doCheckNewName(); doConfigDotXml(); 
doConfirmRename(); doDoDelete(); doReload(); doSubmitDescription(); getACL(); getAbsoluteUrl(); getAllJobs(); getApi(); getConfigFile(); getDescription(); getDisplayName(); 
getDisplayNameOrNull(); getFullDisplayName(); getFullName(); getName(); getParent(); getPronoun(); getRelativeDisplayNameFrom(); getRelativeNameFromGroup(); getRootDir(); 
getSearchUrl(); getShortUrl(); getTarget(); getTaskNoun(); getUrl(); isNameEditable(); movedTo(); onCopiedFrom(); onLoad(); resolveForCLI(); save(); setDescription(); 
setDisplayName(); setDisplayNameOrNull(); updateByXml(); writeConfigDotXml(); addProperty(); addView(); canDelete(); deleteView(); doChildrenContextMenu(); doConfigSubmit(); 
doCreateView(); doDisable(); doEnable(); doLastBuild(); doViewExistsCheck(); getBuildHealth(); getBuildHealthReports(); getDescriptor(); getFolderViews(); getHealthMetrics(); 
getIcon(); getIconColor(); getItem(); getItemGroup(); getItems(); getJob(); getOverrides(); getPrimaryView(); getProperties(); getRootDirFor(); getStaplerFallback(); 
getUrlChildPrefix(); getView(); getViewActions(); getViews(); getViewsTabBar(); hasVisibleItems(); invalidateBuildHealthReports(); isDisabled(); loadChildren(); loadJobTotal(); 
makeDisabled(); onDeleted(); onRenamed(); onViewRenamed(); renameTo(); resetFolderViews(); setIcon(); setPrimaryView(); supportsMakeDisabled(); addTrigger(); checkAbortPermission(); 
createExecutable(); doBuild(); getAssignedLabel(); getCauseOfBlockage(); getComputation(); getDefaultAuthentication(); getEstimatedDuration(); getLastBuiltOn(); getLastFailedBuild(); 
getLastStableBuild(); getLastSuccessfulBuild(); getOrphanedItemStrategy(); getOrphanedItemStrategyDescriptors(); getOwnerTask(); getResourceList(); getSameNodeConstraint(); 
getSubTasks(); getTriggerDescriptors(); getTriggers(); getWhyBlocked(); hasAbortPermission(); isBuildBlocked(); isBuildable(); isConcurrentBuild(); isHasEvents(); 
onCreatedFromScratch(); removeTrigger(); scheduleBuild(); scheduleBuild2(); setOrphanedItemStrategy(); getBranch(); getBranchPropertyStrategy(); getComputationDir(); 
getIconClassName(); getIndexing(); getItemByBranchName(); getJobsDir(); getProjectClass(); getProjectFactory(); getSCMSource(); getSCMSourceCriteria(); getSCMSources(); 
getSourcePronoun(); getSources(); getSourcesList(); onSCMSourceUpdated(); rawDecode(); setProjectFactory(); setSourcesList(); 


-------------
class hudson.model.FreeStyleProject functions:
equals(); getClass(); hashCode(); notify(); notifyAll(); toString(); wait(); getSearch(); getSearchIndex(); getSearchName(); addAction(); addOrReplaceAction(); doContextMenu(); 
getAction(); getActions(); getAllActions(); getDynamic(); removeAction(); removeActions(); replaceAction(); replaceActions(); delete(); doCheckNewName(); doConfigDotXml(); 
doConfirmRename(); doDoDelete(); doReload(); doSubmitDescription(); getACL(); getAbsoluteUrl(); getAllJobs(); getApi(); getConfigFile(); getDescription(); getDisplayName(); 
getDisplayNameOrNull(); getFullDisplayName(); getFullName(); getName(); getParent(); getPronoun(); getRelativeDisplayNameFrom(); getRelativeNameFromGroup(); getRootDir(); 
getSearchUrl(); getShortUrl(); getTarget(); getTaskNoun(); getUrl(); isNameEditable(); movedTo(); onCopiedFrom(); onLoad(); resolveForCLI(); save(); setDescription(); 
setDisplayName(); setDisplayNameOrNull(); updateByXml(); writeConfigDotXml(); addProperty(); assignBuildNumber(); doBuildStatus(); doChildrenContextMenu(); doConfigSubmit(); 
doDescription(); doDoRename(); doRssAll(); doRssChangelog(); doRssFailed(); getAllProperties(); getBuild(); getBuildByNumber(); getBuildDir(); getBuildDiscarder(); getBuildForCLI(); 
getBuildHealth(); getBuildHealthReports(); getBuildStatusIconClassName(); getBuildStatusUrl(); getBuildTimeGraph(); getBuilds(); getBuildsAsMap(); getBuildsByTimestamp(); 
getCharacteristicEnvVars(); getEnvironment(); getEstimatedDuration(); getFirstBuild(); getIconColor(); getLastBuild(); getLastBuildsOverThreshold(); getLastCompletedBuild(); 
getLastFailedBuild(); getLastStableBuild(); getLastSuccessfulBuild(); getLastUnstableBuild(); getLastUnsuccessfulBuild(); getLogRotator(); getNearestBuild(); getNearestOldBuild(); 
getNewBuilds(); getNextBuildNumber(); getOverrides(); getPermalinks(); getProperties(); getProperty(); getQueueItem(); getTimeline(); getWidgets(); isBuildable(); 
isBuilding(); isHoldOffBuildUntilSave(); isInQueue(); isKeepDependencies(); isLogUpdated(); logRotate(); onCreatedFromScratch(); removeProperty(); renameTo(); setBuildDiscarder(); 
setLogRotator(); supportsLogRotator(); updateNextBuildNumber(); _getRuns(); addTrigger(); blockBuildWhenDownstreamBuilding(); blockBuildWhenUpstreamBuilding(); 
checkAbortPermission(); checkout(); createExecutable(); disable(); doBuild(); doBuildWithParameters(); doCheckRetryCount(); doDoWipeOutWorkspace(); doPolling(); doWs(); enable(); 
findNearest(); getAssignedLabel(); getAssignedLabelString(); getAuthToken(); getBuildNowText(); getBuildTriggerUpstreamProjects(); getBuildingDownstream(); getBuildingUpstream(); 
getCauseOfBlockage(); getCustomWorkspace(); getDelay(); getDownstreamProjects(); getDownstreamProjectsForApi(); getHasCustomQuietPeriod(); getJDK(); getLastBuiltOn(); 
getLazyBuildMixIn(); getModuleRoot(); getModuleRoots(); getProminentActions(); getPublishersList(); getQuietPeriod(); getRelationship(); getRelevantLabels(); getResourceList(); 
getRootProject(); getSameNodeConstraint(); getScm(); getScmCheckoutRetryCount(); getScmCheckoutStrategy(); getSomeBuildWithWorkspace(); getSomeWorkspace(); getSubTasks(); 
getTransitiveDownstreamProjects(); getTransitiveUpstreamProjects(); getTrigger(); getTriggers(); getUpstreamProjects(); getUpstreamProjectsForApi(); getWorkspace(); 
getWorkspaceResource(); hasAbortPermission(); hasCustomScmCheckoutRetryCount(); hasParticipant(); isConcurrentBuild(); isConfigurable(); isDisabled(); isFingerprintConfigured(); 
poll(); pollSCMChanges(); removeRun(); removeTrigger(); scheduleBuild(); scheduleBuild2(); schedulePolling(); setAssignedLabel(); setAssignedNode(); 
setBlockBuildWhenDownstreamBuilding(); setBlockBuildWhenUpstreamBuilding(); setConcurrentBuild(); setCustomWorkspace(); setDisabled(); setJDK(); setQuietPeriod(); setScm(); 
setScmCheckoutStrategy(); supportsMakeDisabled(); addPublisher(); asItem(); asProject(); getBuildWrappers(); getBuildWrappersList(); getBuilders(); getBuildersList(); 
getPublisher(); getPublishers(); getSCMTrigger(); getSCMs(); inferMavenInstallation(); removePublisher(); getDescriptor(); 

-------------
class hudson.maven.MavenModuleSet functions:
 7214  equals(); getClass(); hashCode(); notify(); notifyAll(); toString(); wait(); getSearch(); getSearchIndex(); getSearchName(); addAction(); addOrReplaceAction(); 
 doContextMenu(); getAction(); getActions(); getAllActions(); getDynamic(); removeAction(); removeActions(); replaceAction(); replaceActions(); delete(); doCheckNewName(); 
 doConfigDotXml(); doConfirmRename(); doDoDelete(); doReload(); doSubmitDescription(); getACL(); getAbsoluteUrl(); getAllJobs(); getApi(); getConfigFile(); getDescription(); 
 getDisplayName(); getDisplayNameOrNull(); getFullDisplayName(); getFullName(); getName(); getParent(); getPronoun(); getRelativeDisplayNameFrom(); getRelativeNameFromGroup(); 
 getRootDir(); getSearchUrl(); getShortUrl(); getTarget(); getTaskNoun(); getUrl(); isNameEditable(); movedTo(); onCopiedFrom(); onLoad(); resolveForCLI(); save(); setDescription(); 
 setDisplayName(); setDisplayNameOrNull(); updateByXml(); writeConfigDotXml(); addProperty(); assignBuildNumber(); doBuildStatus(); doChildrenContextMenu(); doConfigSubmit(); 
 doDescription(); doDoRename(); doRssAll(); doRssChangelog(); doRssFailed(); getAllProperties(); getBuild(); getBuildByNumber(); getBuildDir(); getBuildDiscarder(); getBuildForCLI(); 
 getBuildHealth(); getBuildHealthReports(); getBuildStatusIconClassName(); getBuildStatusUrl(); getBuildTimeGraph(); getBuilds(); getBuildsAsMap(); getBuildsByTimestamp(); 
 getCharacteristicEnvVars(); getEnvironment(); getEstimatedDuration(); getFirstBuild(); getIconColor(); getLastBuild(); getLastBuildsOverThreshold(); getLastCompletedBuild(); 
 getLastFailedBuild(); getLastStableBuild(); getLastSuccessfulBuild(); getLastUnstableBuild(); getLastUnsuccessfulBuild(); getLogRotator(); getNearestBuild(); getNearestOldBuild(); 
 getNewBuilds(); getNextBuildNumber(); getOverrides(); getPermalinks(); getProperties(); getProperty(); getQueueItem(); getTimeline(); getWidgets(); isBuildable(); isBuilding(); 
 isHoldOffBuildUntilSave(); isInQueue(); isKeepDependencies(); isLogUpdated(); logRotate(); onCreatedFromScratch(); removeProperty(); renameTo(); setBuildDiscarder(); 
 setLogRotator(); supportsLogRotator(); updateNextBuildNumber(); _getRuns(); addTrigger(); blockBuildWhenDownstreamBuilding(); blockBuildWhenUpstreamBuilding(); checkAbortPermission(); 
 checkout(); createExecutable(); disable(); doBuild(); doBuildWithParameters(); doCheckRetryCount(); doDoWipeOutWorkspace(); doPolling(); doWs(); enable(); findNearest(); 
 getAssignedLabel(); getAssignedLabelString(); getAuthToken(); getBuildNowText(); getBuildTriggerUpstreamProjects(); getBuildingDownstream(); getBuildingUpstream(); getCauseOfBlockage(); 
 getCustomWorkspace(); getDelay(); getDownstreamProjects(); getDownstreamProjectsForApi(); getHasCustomQuietPeriod(); getJDK(); getLastBuiltOn(); getLazyBuildMixIn(); 
 getModuleRoot(); getModuleRoots(); getProminentActions(); getPublishersList(); getQuietPeriod(); getRelationship(); getRelevantLabels(); getResourceList(); getRootProject(); 
 getSameNodeConstraint(); getScm(); getScmCheckoutRetryCount(); getScmCheckoutStrategy(); getSomeBuildWithWorkspace(); getSomeWorkspace(); getSubTasks(); 
 getTransitiveDownstreamProjects(); getTransitiveUpstreamProjects(); getTrigger(); getTriggers(); getUpstreamProjects(); getUpstreamProjectsForApi(); getWorkspace(); 
 getWorkspaceResource(); hasAbortPermission(); hasCustomScmCheckoutRetryCount(); hasParticipant(); isConcurrentBuild(); isConfigurable(); isDisabled(); isFingerprintConfigured(); 
 poll(); pollSCMChanges(); removeRun(); removeTrigger(); scheduleBuild(); scheduleBuild2(); schedulePolling(); setAssignedLabel(); setAssignedNode(); 
 setBlockBuildWhenDownstreamBuilding(); setBlockBuildWhenUpstreamBuilding(); setConcurrentBuild(); setCustomWorkspace(); setDisabled(); setJDK(); setQuietPeriod(); setScm(); 
 setScmCheckoutStrategy(); supportsMakeDisabled(); asProject(); createIndenter(); doCheckFileInWorkspace(); doDoDeleteAllDisabledModules(); getAlternateSettings(); 
 getApproximateQueueItemsQuickly(); getBlockTriggerWhenBuilding(); getBuildWrappers(); getBuildWrappersList(); getDescriptor(); getDisabledModules(); getExplicitLocalRepository(); 
 getGlobalSettings(); getGoals(); getItem(); getItems(); getLocalRepository(); getMaven(); getMavenOpts(); getMavenProperties(); getMavenValidationLevel(); 
 getModule(); getModules(); getPostbuilders(); getPrebuilders(); getProfiles(); getPublishers(); getQueueItems(); getReporters(); getRootDirFor(); getRootModule(); getRootPOM(); 
 getRunPostStepsIfResult(); getSettings(); getTestResultAction(); getUrlChildPrefix(); getUserConfiguredGoals(); hasDisabledModule(); ignoreUnsuccessfulUpstreams(); 
 ignoreUpstremChanges(); inferMavenInstallation(); isAggregatorStyleBuild(); isArchivingDisabled(); isDisableTriggerDownstreamProjects(); isFingerprintingDisabled(); 
 isIncrementalBuild(); isNonRecursive(); isPerModuleEmail(); isProcessPlugins(); isResolveDependencies(); isSiteArchivingDisabled(); onDeleted(); onRenamed(); readResolve(); 
 runHeadless(); setAggregatorStyleBuild(); setAlternateSettings(); setBlockTriggerWhenBuilding(); setDisableTriggerDownstreamProjects(); setGlobalSettings(); setGoals(); 
 setIgnoreUnsuccessfulUpstreams(); setIgnoreUpstremChanges(); setIncrementalBuild(); setIsArchivingDisabled(); setIsFingerprintingDisabled(); setIsSiteArchivingDisabled(); 
 setLocalRepository(); setMaven(); setMavenOpts(); setProcessPlugins(); setResolveDependencies(); setRootPOM(); setRunHeadless(); setRunPostStepsIfResult(); setSettings(); 
 setUsePrivateRepository(); usesPrivateRepository();


 -----------
class hudson.maven.MavenModule functions:
equals(); getClass(); hashCode(); notify(); notifyAll(); toString(); wait(); getSearch(); getSearchIndex(); getSearchName(); addAction(); addOrReplaceAction(); doContextMenu(); 
getAction(); getActions(); getAllActions(); getDynamic(); removeAction(); removeActions(); replaceAction(); replaceActions(); delete(); doCheckNewName(); doConfigDotXml(); doConfirmRename(); 
doDoDelete(); doReload(); doSubmitDescription(); getACL(); getAbsoluteUrl(); getAllJobs(); getApi(); getConfigFile(); getDescription(); getDisplayName(); getDisplayNameOrNull(); 
getFullDisplayName(); getFullName(); getName(); getParent(); getPronoun(); getRelativeDisplayNameFrom(); getRelativeNameFromGroup(); getRootDir(); getSearchUrl(); getShortUrl(); 
getTarget(); getTaskNoun(); getUrl(); isNameEditable(); movedTo(); onCopiedFrom(); onLoad(); resolveForCLI(); save(); setDescription(); setDisplayName(); setDisplayNameOrNull(); 
updateByXml(); writeConfigDotXml(); addProperty(); assignBuildNumber(); doBuildStatus(); doChildrenContextMenu(); doConfigSubmit(); doDescription(); doDoRename(); doRssAll(); 
doRssChangelog(); doRssFailed(); getAllProperties(); getBuild(); getBuildByNumber(); getBuildDir(); getBuildDiscarder(); getBuildForCLI(); getBuildHealth(); getBuildHealthReports(); 
getBuildStatusIconClassName(); getBuildStatusUrl(); getBuildTimeGraph(); getBuilds(); getBuildsAsMap(); getBuildsByTimestamp(); getCharacteristicEnvVars(); getEnvironment(); 
getEstimatedDuration(); getFirstBuild(); getIconColor(); getLastBuild(); getLastBuildsOverThreshold(); getLastCompletedBuild(); getLastFailedBuild(); getLastStableBuild(); getLastSuccessfulBuild(); 
getLastUnstableBuild(); getLastUnsuccessfulBuild(); getLogRotator(); getNearestBuild(); getNearestOldBuild(); getNewBuilds(); getNextBuildNumber(); getOverrides(); getPermalinks(); 
getProperties(); getProperty(); getQueueItem(); getTimeline(); getWidgets(); isBuildable(); isBuilding(); isHoldOffBuildUntilSave(); isInQueue(); isKeepDependencies(); isLogUpdated(); 
logRotate(); onCreatedFromScratch(); removeProperty(); renameTo(); setBuildDiscarder(); setLogRotator(); supportsLogRotator(); updateNextBuildNumber(); _getRuns(); addTrigger(); 
blockBuildWhenDownstreamBuilding(); blockBuildWhenUpstreamBuilding(); checkAbortPermission(); checkout(); createExecutable(); disable(); doBuild(); doBuildWithParameters(); doCheckRetryCount(); 
doDoWipeOutWorkspace(); doPolling(); doWs(); enable(); findNearest(); getAssignedLabel(); getAssignedLabelString(); getAuthToken(); getBuildNowText(); getBuildTriggerUpstreamProjects(); 
getBuildingDownstream(); getBuildingUpstream(); getCauseOfBlockage(); getCustomWorkspace(); getDelay(); getDownstreamProjects(); getDownstreamProjectsForApi(); getHasCustomQuietPeriod(); 
getJDK(); getLastBuiltOn(); getLazyBuildMixIn(); getModuleRoot(); getModuleRoots(); getProminentActions(); getPublishersList(); getQuietPeriod(); getRelationship(); getRelevantLabels(); 
getResourceList(); getRootProject(); getSameNodeConstraint(); getScm(); getScmCheckoutRetryCount(); getScmCheckoutStrategy(); getSomeBuildWithWorkspace(); getSomeWorkspace(); getSubTasks(); 
getTransitiveDownstreamProjects(); getTransitiveUpstreamProjects(); getTrigger(); getTriggers(); getUpstreamProjects(); getUpstreamProjectsForApi(); getWorkspace(); getWorkspaceResource(); 
hasAbortPermission(); hasCustomScmCheckoutRetryCount(); hasParticipant(); isConcurrentBuild(); isConfigurable(); isDisabled(); isFingerprintConfigured(); poll(); pollSCMChanges(); 
removeRun(); removeTrigger(); scheduleBuild(); scheduleBuild2(); schedulePolling(); setAssignedLabel(); setAssignedNode(); setBlockBuildWhenDownstreamBuilding(); setBlockBuildWhenUpstreamBuilding(); 
setConcurrentBuild(); setCustomWorkspace(); setDisabled(); setJDK(); setQuietPeriod(); setScm(); setScmCheckoutStrategy(); supportsMakeDisabled(); asDependency(); getArtifactId(); 
getChildren(); getDependencies(); getGoals(); getGroupId(); getModuleName(); getPackaging(); getRelativePath(); getReporters(); getSubsidiaries(); getUserConfiguredGoals(); getVersion(); 
inferMavenInstallation(); isSameModule(); 

-----------
class jenkins.branch.OrganizationFolder functions:
equals(); getClass(); hashCode(); notify(); notifyAll(); toString(); wait(); getSearch(); getSearchIndex(); getSearchName(); addAction(); addOrReplaceAction(); doContextMenu(); 
getAction(); getActions(); getAllActions(); getDynamic(); removeAction(); removeActions(); replaceAction(); replaceActions(); delete(); doCheckNewName(); doConfigDotXml(); doConfirmRename(); 
doDoDelete(); doReload(); doSubmitDescription(); getACL(); getAbsoluteUrl(); getAllJobs(); getApi(); getConfigFile(); getDescription(); getDisplayName(); getDisplayNameOrNull(); 
getFullDisplayName(); getFullName(); getName(); getParent(); getPronoun(); getRelativeDisplayNameFrom(); getRelativeNameFromGroup(); getRootDir(); getSearchUrl(); getShortUrl(); 
getTarget(); getTaskNoun(); getUrl(); isNameEditable(); movedTo(); onCopiedFrom(); onLoad(); resolveForCLI(); save(); setDescription(); setDisplayName(); setDisplayNameOrNull(); 
updateByXml(); writeConfigDotXml(); addProperty(); addView(); canDelete(); deleteView(); doChildrenContextMenu(); doConfigSubmit(); doCreateView(); doDisable(); doEnable(); doLastBuild(); 
doViewExistsCheck(); getBuildHealth(); getBuildHealthReports(); getDescriptor(); getFolderViews(); getHealthMetrics(); getIcon(); getIconColor(); getItem(); getItemGroup(); 
getItems(); getJob(); getOverrides(); getPrimaryView(); getProperties(); getRootDirFor(); getStaplerFallback(); getUrlChildPrefix(); getView(); getViewActions(); getViews(); 
getViewsTabBar(); hasVisibleItems(); invalidateBuildHealthReports(); isDisabled(); loadChildren(); loadJobTotal(); makeDisabled(); onDeleted(); onRenamed(); onViewRenamed(); renameTo(); 
resetFolderViews(); setIcon(); setPrimaryView(); supportsMakeDisabled(); addTrigger(); checkAbortPermission(); createExecutable(); doBuild(); getAssignedLabel(); getCauseOfBlockage(); 
getComputation(); getDefaultAuthentication(); getEstimatedDuration(); getLastBuiltOn(); getLastFailedBuild(); getLastStableBuild(); getLastSuccessfulBuild(); getOrphanedItemStrategy(); 
getOrphanedItemStrategyDescriptors(); getOwnerTask(); getResourceList(); getSameNodeConstraint(); getSubTasks(); getTriggerDescriptors(); getTriggers(); getWhyBlocked(); 
hasAbortPermission(); isBuildBlocked(); isBuildable(); isConcurrentBuild(); isHasEvents(); onCreatedFromScratch(); removeTrigger(); scheduleBuild(); scheduleBuild2(); setOrphanedItemStrategy(); 
getBuildStrategies(); getIconClassName(); getItemByProjectName(); getNavigators(); getProjectFactories(); getSCMNavigators(); getSCMSource(); getSCMSourceCriteria(); 
getSCMSources(); getSourcePronoun(); getStrategy(); isSingleOrigin(); onSCMSourceUpdated(); setStrategy();

---------
class hudson.matrix.MatrixConfiguration functions:
equals(); getClass(); hashCode(); notify(); notifyAll(); toString(); wait(); getSearch(); getSearchIndex(); getSearchName(); addAction(); addOrReplaceAction(); doContextMenu(); 
getAction(); getActions(); getAllActions(); getDynamic(); removeAction(); removeActions(); replaceAction(); replaceActions(); delete(); doCheckNewName(); doConfigDotXml(); 
doConfirmRename(); doDoDelete(); doReload(); doSubmitDescription(); getACL(); getAbsoluteUrl(); getAllJobs(); getApi(); getConfigFile(); getDescription(); getDisplayName(); 
getDisplayNameOrNull(); getFullDisplayName(); getFullName(); getName(); getParent(); getPronoun(); getRelativeDisplayNameFrom(); getRelativeNameFromGroup(); getRootDir(); getSearchUrl(); 
getShortUrl(); getTarget(); getTaskNoun(); getUrl(); isNameEditable(); movedTo(); onCopiedFrom(); onLoad(); resolveForCLI(); save(); setDescription(); setDisplayName(); 
setDisplayNameOrNull(); updateByXml(); writeConfigDotXml(); addProperty(); assignBuildNumber(); doBuildStatus(); doChildrenContextMenu(); doConfigSubmit(); doDescription(); 
doDoRename(); doRssAll(); doRssChangelog(); doRssFailed(); getAllProperties(); getBuild(); getBuildByNumber(); getBuildDir(); getBuildDiscarder(); getBuildForCLI(); getBuildHealth(); 
getBuildHealthReports(); getBuildStatusIconClassName(); getBuildStatusUrl(); getBuildTimeGraph(); getBuilds(); getBuildsAsMap(); getBuildsByTimestamp(); getCharacteristicEnvVars(); 
getEnvironment(); getEstimatedDuration(); getFirstBuild(); getIconColor(); getLastBuild(); getLastBuildsOverThreshold(); getLastCompletedBuild(); getLastFailedBuild(); 
getLastStableBuild(); getLastSuccessfulBuild(); getLastUnstableBuild(); getLastUnsuccessfulBuild(); getLogRotator(); getNearestBuild(); getNearestOldBuild(); getNewBuilds(); 
getNextBuildNumber(); getOverrides(); getPermalinks(); getProperties(); getProperty(); getQueueItem(); getTimeline(); getWidgets(); isBuildable(); isBuilding(); isHoldOffBuildUntilSave(); 
isInQueue(); isKeepDependencies(); isLogUpdated(); logRotate(); onCreatedFromScratch(); removeProperty(); renameTo(); setBuildDiscarder(); setLogRotator(); supportsLogRotator(); 
updateNextBuildNumber(); _getRuns(); addTrigger(); blockBuildWhenDownstreamBuilding(); blockBuildWhenUpstreamBuilding(); checkAbortPermission(); checkout(); createExecutable(); 
disable(); doBuild(); doBuildWithParameters(); doCheckRetryCount(); doDoWipeOutWorkspace(); doPolling(); doWs(); enable(); findNearest(); getAssignedLabel(); getAssignedLabelString(); 
getAuthToken(); getBuildNowText(); getBuildTriggerUpstreamProjects(); getBuildingDownstream(); getBuildingUpstream(); getCauseOfBlockage(); getCustomWorkspace(); 
getDelay(); getDownstreamProjects(); getDownstreamProjectsForApi(); getHasCustomQuietPeriod(); getJDK(); getLastBuiltOn(); getLazyBuildMixIn(); getModuleRoot(); getModuleRoots(); 
getProminentActions(); getPublishersList(); getQuietPeriod(); getRelationship(); getRelevantLabels(); getResourceList(); getRootProject(); getSameNodeConstraint(); getScm(); 
getScmCheckoutRetryCount(); getScmCheckoutStrategy(); getSomeBuildWithWorkspace(); getSomeWorkspace(); getSubTasks(); getTransitiveDownstreamProjects(); getTransitiveUpstreamProjects(); 
getTrigger(); getTriggers(); getUpstreamProjects(); getUpstreamProjectsForApi(); getWorkspace(); getWorkspaceResource(); hasAbortPermission(); hasCustomScmCheckoutRetryCount(); 
hasParticipant(); isConcurrentBuild(); isConfigurable(); isDisabled(); isFingerprintConfigured(); poll(); pollSCMChanges(); removeRun(); removeTrigger(); scheduleBuild(); 
scheduleBuild2(); schedulePolling(); setAssignedLabel(); setAssignedNode(); setBlockBuildWhenDownstreamBuilding(); setBlockBuildWhenUpstreamBuilding(); setConcurrentBuild(); 
setCustomWorkspace(); setDisabled(); setJDK(); setQuietPeriod(); setScm(); setScmCheckoutStrategy(); supportsMakeDisabled(); addPublisher(); asItem(); asProject(); getBuildWrappers(); 
getBuildWrappersList(); getBuilders(); getBuildersList(); getPublisher(); getPublishers(); getSCMTrigger(); getSCMs(); inferMavenInstallation(); removePublisher(); doConfigure(); 
doDisable(); getCombination(); isActiveConfiguration(); makeDisabled(); 

----------
class hudson.matrix.MatrixProject functions:
equals(); getClass(); hashCode(); notify(); notifyAll(); toString(); wait(); getSearch(); getSearchIndex(); getSearchName(); addAction(); addOrReplaceAction(); doContextMenu(); 
getAction(); getActions(); getAllActions(); getDynamic(); removeAction(); removeActions(); replaceAction(); replaceActions(); delete(); doCheckNewName(); doConfigDotXml(); 
doConfirmRename(); doDoDelete(); doReload(); doSubmitDescription(); getACL(); getAbsoluteUrl(); getAllJobs(); getApi(); getConfigFile(); getDescription(); getDisplayName(); 
getDisplayNameOrNull(); getFullDisplayName(); getFullName(); getName(); getParent(); getPronoun(); getRelativeDisplayNameFrom(); getRelativeNameFromGroup(); getRootDir(); getSearchUrl(); 
getShortUrl(); getTarget(); getTaskNoun(); getUrl(); isNameEditable(); movedTo(); onCopiedFrom(); onLoad(); resolveForCLI(); save(); setDescription(); setDisplayName(); 
setDisplayNameOrNull(); updateByXml(); writeConfigDotXml(); addProperty(); assignBuildNumber(); doBuildStatus(); doChildrenContextMenu(); doConfigSubmit(); doDescription(); 
doDoRename(); doRssAll(); doRssChangelog(); doRssFailed(); getAllProperties(); getBuild(); getBuildByNumber(); getBuildDir(); getBuildDiscarder(); getBuildForCLI(); getBuildHealth(); 
getBuildHealthReports(); getBuildStatusIconClassName(); getBuildStatusUrl(); getBuildTimeGraph(); getBuilds(); getBuildsAsMap(); getBuildsByTimestamp(); getCharacteristicEnvVars(); 
getEnvironment(); getEstimatedDuration(); getFirstBuild(); getIconColor(); getLastBuild(); getLastBuildsOverThreshold(); getLastCompletedBuild(); getLastFailedBuild(); 
getLastStableBuild(); getLastSuccessfulBuild(); getLastUnstableBuild(); getLastUnsuccessfulBuild(); getLogRotator(); getNearestBuild(); getNearestOldBuild(); getNewBuilds(); 
getNextBuildNumber(); getOverrides(); getPermalinks(); getProperties(); getProperty(); getQueueItem(); getTimeline(); getWidgets(); isBuildable(); isBuilding(); isHoldOffBuildUntilSave(); 
isInQueue(); isKeepDependencies(); isLogUpdated(); logRotate(); onCreatedFromScratch(); removeProperty(); renameTo(); setBuildDiscarder(); setLogRotator(); 
supportsLogRotator(); updateNextBuildNumber(); _getRuns(); addTrigger(); blockBuildWhenDownstreamBuilding(); blockBuildWhenUpstreamBuilding(); checkAbortPermission(); 
checkout(); createExecutable(); disable(); doBuild(); doBuildWithParameters(); doCheckRetryCount(); doDoWipeOutWorkspace(); doPolling(); doWs(); enable(); findNearest(); 
getAssignedLabel(); getAssignedLabelString(); getAuthToken(); getBuildNowText(); getBuildTriggerUpstreamProjects(); getBuildingDownstream(); getBuildingUpstream(); getCauseOfBlockage(); 
getCustomWorkspace(); getDelay(); getDownstreamProjects(); getDownstreamProjectsForApi(); getHasCustomQuietPeriod(); getJDK(); getLastBuiltOn(); getLazyBuildMixIn(); 
getModuleRoot(); getModuleRoots(); getProminentActions(); getPublishersList(); getQuietPeriod(); getRelationship(); getRelevantLabels(); getResourceList(); getRootProject(); 
getSameNodeConstraint(); getScm(); getScmCheckoutRetryCount(); getScmCheckoutStrategy(); getSomeBuildWithWorkspace(); getSomeWorkspace(); getSubTasks(); getTransitiveDownstreamProjects(); 
getTransitiveUpstreamProjects(); getTrigger(); getTriggers(); getUpstreamProjects(); getUpstreamProjectsForApi(); getWorkspace(); getWorkspaceResource(); hasAbortPermission(); 
hasCustomScmCheckoutRetryCount(); hasParticipant(); isConcurrentBuild(); isConfigurable(); isDisabled(); isFingerprintConfigured(); poll(); pollSCMChanges(); removeRun(); 
removeTrigger(); scheduleBuild(); scheduleBuild2(); schedulePolling(); setAssignedLabel(); setAssignedNode(); setBlockBuildWhenDownstreamBuilding(); setBlockBuildWhenUpstreamBuilding(); 
setConcurrentBuild(); setCustomWorkspace(); setDisabled(); setJDK(); setQuietPeriod(); setScm(); setScmCheckoutStrategy(); supportsMakeDisabled(); alias(); asProject(); 
getActiveConfigurations(); getAggregatedTestResultAction(); getAxes(); getBuildWrappers(); getBuildWrappersList(); getBuilders(); getBuildersList(); getChildCustomWorkspace(); 
getCombinationFilter(); getDescriptor(); getExecutionStrategy(); getItem(); getItems(); getJDKs(); getLabels(); getLayouter(); getPublisher(); getPublishers(); 
getRootDirFor(); getSorter(); getTouchStoneCombinationFilter(); getTouchStoneResultCondition(); getUrlChildPrefix(); getUserAxes(); hasChildCustomWorkspace(); isRunSequentially(); 
onDeleted(); onRenamed(); setAxes(); setChildCustomWorkspace(); setCombinationFilter(); setExecutionStrategy(); setRunSequentially(); setSorter(); setTouchStoneCombinationFilter(); 
setTouchStoneResultCondition();

----------



*******/