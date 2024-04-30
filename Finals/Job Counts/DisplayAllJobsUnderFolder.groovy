//Prints the folder names under a top-level-folder name provided. As an example in CD2 controller the list of SPMADMIN folders are shown in this example

import jenkins.*
import jenkins.model.*
import hudson.*
import hudson.model.*

import hudson.scm.*
import hudson.tasks.*
import com.cloudbees.hudson.plugins.folder.*
import com.cloudbees.hudson.plugins.folder.AbstractFolder;

def folderName="SPMADMIN";
long lJobCount = 0;
long lFolderJobCount = 0;
long lFolderCount = 0;
long lSubFolderCount = 0;

jen = Jenkins.instance

//jen.getItems().each {
jen.getItemByFullName(folderName, AbstractFolder).getItems().each {
    if(it instanceof Folder)
    {
        lSubFolderCount = 0;
        lFolderJobCount = 0;
        lFolderCount++;
        println "${lFolderCount}, Folder Name - ${it.name}"
        lSubFolderCount = processFolder(it, lFolderCount, lSubFolderCount, lJobCount, lFolderJobCount )
    }else{
        lJobCount = processJob(it, lJobCount, lFolderJobCount)
    }
}

long processJob(Item job, long jJobCount, long jFolderJobCount){
  //println "\t\t\t\t\t\t${++jFolderJobCount}, ${++jJobCount}, ${job.fullName}"
  //printAllMethods(job)
  jJobCount++;
  return jJobCount;
}

long processFolder(Item folder, long iFolderCount, long iSubFolderCount, long iJobCount, long iFolderJobCount)
{
    folder.getItems().each {
        if(it instanceof Folder)
        {
            iSubFolderCount++;
            println "\t\t${iFolderCount}, ${iSubFolderCount}, Sub Folder - ${it.name}"
            iSubFolderCount = processFolder(it, iFolderCount, iSubFolderCount, iJobCount, iFolderJobCount)
        }
        else
        {
            //iJobCount++;
            iJobCount = processJob(it, iJobCount, iFolderJobCount)
            println "\t\t\t\t${iJobCount}, ${++iFolderJobCount}, ${it.fullName}"
        }
    }
    return iSubFolderCount;
}

return null;

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


/***
class org.jenkinsci.plugins.workflow.job.WorkflowJob functions:
equals(); getClass(); hashCode(); notify(); notifyAll(); toString(); wait(); getSearch(); getSearchIndex(); getSearchName(); addAction(); addOrReplaceAction(); doContextMenu(); getAction(); getActions(); getAllActions(); getDynamic(); removeAction(); removeActions(); replaceAction(); replaceActions(); delete(); doCheckNewName(); doConfigDotXml(); doConfirmRename(); doDoDelete(); doReload(); doSubmitDescription(); getACL(); getAbsoluteUrl(); getAllJobs(); getApi(); getConfigFile(); getDescription(); getDisplayName(); getDisplayNameOrNull(); getFullDisplayName(); getFullName(); getName(); getParent(); getPronoun(); getRelativeDisplayNameFrom(); getRelativeNameFromGroup(); getRootDir(); getSearchUrl(); getShortUrl(); getTarget(); getTaskNoun(); getUrl(); isNameEditable(); movedTo(); onCopiedFrom(); onLoad(); resolveForCLI(); save(); setDescription(); setDisplayName(); setDisplayNameOrNull(); updateByXml(); writeConfigDotXml(); addProperty(); assignBuildNumber(); doBuildStatus(); doChildrenContextMenu(); doConfigSubmit(); doDescription(); doDoRename(); doRssAll(); doRssChangelog(); doRssFailed(); getAllProperties(); getBuild(); getBuildByNumber(); getBuildDir(); getBuildDiscarder(); getBuildForCLI(); getBuildHealth(); getBuildHealthReports(); getBuildStatusIconClassName(); getBuildStatusUrl(); getBuildTimeGraph(); getBuilds(); getBuildsAsMap(); getBuildsByTimestamp(); getCharacteristicEnvVars(); getEnvironment(); getEstimatedDuration(); getFirstBuild(); getIconColor(); getLastBuild(); getLastBuildsOverThreshold(); getLastCompletedBuild(); getLastFailedBuild(); getLastStableBuild(); getLastSuccessfulBuild(); getLastUnstableBuild(); getLastUnsuccessfulBuild(); getLogRotator(); getNearestBuild(); getNearestOldBuild(); getNewBuilds(); getNextBuildNumber(); getOverrides(); getPermalinks(); getProperties(); getProperty(); getQueueItem(); getTimeline(); getWidgets(); isBuildable(); isBuilding(); isHoldOffBuildUntilSave(); isInQueue(); isKeepDependencies(); isLogUpdated(); logRotate(); onCreatedFromScratch(); removeProperty(); renameTo(); setBuildDiscarder(); setLogRotator(); supportsLogRotator(); updateNextBuildNumber(); addTrigger(); addTriggersJobPropertyWithoutStart(); alias(); asItem(); checkAbortPermission(); getAssignedLabel(); getAuthToken(); getCauseOfBlockage(); getDefinition(); getDescriptor(); getHasCustomQuietPeriod(); getLastBuiltOn(); getLazyBuildMixIn(); getQuietPeriod(); getSCMTrigger(); getSCMs(); getSameNodeConstraint(); getSubTasks(); getTriggers(); getTriggersJobProperty(); getTypicalSCM(); hasAbortPermission(); isConcurrentBuild(); isDisabled(); isResumeBlocked(); poll(); scheduleBuild2(); setConcurrentBuild(); setDefinition(); setDisabled(); setQuietPeriod(); setResumeBlocked(); setTriggers(); supportsMakeDisabled();
***/