#!groovy
//Display builds along with their respective age based upon top-level-folder name provided.

import hudson.slaves.*
import java.util.concurrent.*
import org.jenkinsci.plugins.workflow.job.WorkflowJob
import java.util.Calendar
import java.text.SimpleDateFormat
import java.util.Date
import com.cloudbees.hudson.plugins.folder.AbstractFolder;

//import java.util.GregorianCalendar

jenkins = Hudson.instance
controller = Jenkins.getInstance().getComputer('').getHostName()
println "===================================================================================================================="
println "Counts of all builds and their respective age including whether they are disabled, frozen/patch titled or never run!"
println "Report collected on the Controller with hostname - ${controller}"
println "Display builds along with their respective age based upon top-level-folder name provided."
println "===================================================================================================================="

def sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss.SSS")
def startDate = new Date()
println "Start time : ${sdf.format(startDate)}"

def folderName = 'JenkinsPlatformTeam'

Calendar c = Calendar.getInstance()
Calendar sevenDays = Calendar.getInstance()
Calendar fifteenDays = Calendar.getInstance()
Calendar thirtyDays = Calendar.getInstance()
Calendar fortyFiveDays = Calendar.getInstance()
Calendar ninetyDays = Calendar.getInstance()
Calendar oneHundredEightyDays = Calendar.getInstance()
Calendar oneYear = Calendar.getInstance()

now = c.instance
def date = new Date()
c.setTime(date);

sevenDays.add(Calendar.DATE, -7)
fifteenDays.add(Calendar.DATE, -15)
thirtyDays.add(Calendar.DATE, -30)
fortyFiveDays.add(Calendar.DATE, -45)
ninetyDays.add(Calendar.DATE, -90)
oneHundredEightyDays.add(Calendar.DATE, -180)
oneYear.add(Calendar.DATE, -365)

println("Starting report generation at - ${now.time}")
println "++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++"
//def allItems = Jenkins.instance.getAllItems()
def allItems = Jenkins.instance.getItemByFullName(folderName, AbstractFolder).getAllItems();
def iItemCount = 0

//println("item count=${allItems.size()}")

// counts is an array to collect stats
//      0       1        2       3       4       5      6       7       8    9    10
// |one year|180 days|90 days|45 days|30 days|15 days|7 days|0-7 days|disabled|freeze|never
def counts = [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0] as int[]
def build_time;

for (item in allItems) {
    //println("\t ${iItemCount++} - ${item.name}")
    try {
        if (item.name ==~ /(?i)(freeze|patch).*/) {
            counts[9]++
            continue
        }
        if (item.disabled) {
            counts[8]++
            continue
        }

    //Skip if item is say folder name
    try{
        build_time = item.getLastBuild()    
    }catch (Exception e){
        //Do nothing as this maybe a folder
        continue;
    }

        if (build_time != null) {
        build_time = item.getLastBuild().getTimestamp()    
            
            if (build_time.compareTo(oneYear) < 1) {
                counts[0]++
				println"one year, ${item.getAbsoluteUrl()}";
				//println"one year, ${item.getUrl()}, /*${item.getRootDir},*/ ${item.getFullDisplayName()}, ${item.getFullName()}, ${item.getName()}, ${item.getParent()}";
                continue
            }
            if (build_time.compareTo(oneHundredEightyDays) < 1 && build_time.compareTo(oneYear) >= 1) {
                counts[1]++
				println"oneHundredEightyDays, ${item.getAbsoluteUrl()}";
                continue
            }
            if (build_time.compareTo(ninetyDays) < 1 && build_time.compareTo(oneHundredEightyDays) >= 1) {
                counts[2]++
				println"ninetyDays, ${item.getAbsoluteUrl()}";
                continue
            }
            if (build_time.compareTo(fortyFiveDays) < 1 && build_time.compareTo(ninetyDays) >= 1) {
                counts[3]++
				println"fortyFiveDays, ${item.getAbsoluteUrl()}";
                continue
            }
            if (build_time.compareTo(thirtyDays) < 1 && build_time.compareTo(fortyFiveDays) >= 1) {
                counts[4]++
				println"thirtyDays, ${item.getAbsoluteUrl()}";
                continue
            }
            if (build_time.compareTo(fifteenDays) < 1 && build_time.compareTo(thirtyDays) >= 1) {
                counts[5]++
				println"fifteenDays, ${item.getAbsoluteUrl()}";
                continue
            }
        if (build_time.compareTo(sevenDays) < 0 && build_time.compareTo(fifteenDays) >= 1) {
                counts[6]++
				println"less than sevenDays, ${item.getAbsoluteUrl()}";
                continue
            }
        if (build_time.compareTo(sevenDays) >= 1) {
                counts[7]++
				println"more than sevenDays, ${item.getAbsoluteUrl()}";
                continue
            }
        } else {
            //never built
            counts[10]++
			println"never, ${item.getAbsoluteUrl()}";
        }
    }
    catch (Exception e) {
        println "Exception raised ... - ${e}"
    }
}

println("\t|one year\t|180 days\t|90 days\t|45 days\t|30 days\t|15 days\t|7 days\t\t|0-7 days\t|disabled\t\t|frozen\t\t|never\t\t|")
println("\t=======================================================================================================================")
println("\t|one year+ : ${counts[0]}\t|180+ days : ${counts[1]}\t|90+ days : ${counts[2]}\t|45+ days : ${counts[3]}\t|30+ days : ${counts[4]}\t|\
15+ days : ${counts[5]}\t|7+ days : ${counts[6]}\t|0-7 days : ${counts[7]}\t|disabled : ${counts[8]}\t|freeze : ${counts[9]}\t|never : ${counts[10]}\t|")
println("Total all time builds in ${controller} are : ${counts.sum() - (counts[10] + counts[8] + counts[9])} where total item count is : ${allItems.size}")

println "################################################################################"
println("\tOne year, 180 days, 90 days, 45 days, 30 days, 15 days, 7 days, 0-7 days, disabled, frozen, never")
println("\t${counts[0]}, ${counts[1]}, ${counts[2]}, ${counts[3]}, ${counts[4]}, ${counts[5]}, ${counts[6]}, ${counts[7]}, ${counts[8]}, ${counts[9]}, ${counts[10]}");

//--------------------------------------------------
println "++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++"
def endDate = new Date()
println "End time: ${sdf.format(endDate)}"


/*
class org.jenkinsci.plugins.workflow.job.WorkflowJob functions:
equals(); getClass(); hashCode(); notify(); notifyAll(); toString(); wait(); getSearch(); getSearchIndex(); getSearchName(); addAction(); addOrReplaceAction(); doContextMenu(); getAction(); getActions(); getAllActions(); getDynamic(); removeAction(); removeActions(); replaceAction(); replaceActions(); delete(); doCheckNewName(); doConfigDotXml(); doConfirmRename(); doDoDelete(); doReload(); doSubmitDescription(); getACL(); getAbsoluteUrl(); getAllJobs(); getApi(); getConfigFile(); getDescription(); getDisplayName(); getDisplayNameOrNull(); getFullDisplayName(); getFullName(); getName(); getParent(); getPronoun(); getRelativeDisplayNameFrom(); getRelativeNameFromGroup(); getRootDir(); getSearchUrl(); getShortUrl(); getTarget(); getTaskNoun(); getUrl(); isNameEditable(); movedTo(); onCopiedFrom(); onLoad(); resolveForCLI(); save(); setDescription(); setDisplayName(); setDisplayNameOrNull(); updateByXml(); writeConfigDotXml(); addProperty(); assignBuildNumber(); doBuildStatus(); doChildrenContextMenu(); doConfigSubmit(); doDescription(); doDoRename(); doRssAll(); doRssChangelog(); doRssFailed(); getAllProperties(); getBuild(); getBuildByNumber(); getBuildDir(); getBuildDiscarder(); getBuildForCLI(); getBuildHealth(); getBuildHealthReports(); getBuildStatusIconClassName(); getBuildStatusUrl(); getBuildTimeGraph(); getBuilds(); getBuildsAsMap(); getBuildsByTimestamp(); getCharacteristicEnvVars(); getEnvironment(); getEstimatedDuration(); getFirstBuild(); getIconColor(); getLastBuild(); getLastBuildsOverThreshold(); getLastCompletedBuild(); getLastFailedBuild(); getLastStableBuild(); getLastSuccessfulBuild(); getLastUnstableBuild(); getLastUnsuccessfulBuild(); getLogRotator(); getNearestBuild(); getNearestOldBuild(); getNewBuilds(); getNextBuildNumber(); getOverrides(); getPermalinks(); getProperties(); getProperty(); getQueueItem(); getTimeline(); getWidgets(); isBuildable(); isBuilding(); isHoldOffBuildUntilSave(); isInQueue(); isKeepDependencies(); isLogUpdated(); logRotate(); onCreatedFromScratch(); removeProperty(); renameTo(); setBuildDiscarder(); setLogRotator(); supportsLogRotator(); updateNextBuildNumber(); addTrigger(); addTriggersJobPropertyWithoutStart(); alias(); asItem(); checkAbortPermission(); getAssignedLabel(); getAuthToken(); getCauseOfBlockage(); getDefinition(); getDescriptor(); getHasCustomQuietPeriod(); getLastBuiltOn(); getLazyBuildMixIn(); getQuietPeriod(); getSCMTrigger(); getSCMs(); getSameNodeConstraint(); getSubTasks(); getTriggers(); getTriggersJobProperty(); getTypicalSCM(); hasAbortPermission(); isConcurrentBuild(); isDisabled(); isResumeBlocked(); poll(); scheduleBuild2(); setConcurrentBuild(); setDefinition(); setDisabled(); setQuietPeriod(); setResumeBlocked(); setTriggers(); supportsMakeDisabled(); 
*/