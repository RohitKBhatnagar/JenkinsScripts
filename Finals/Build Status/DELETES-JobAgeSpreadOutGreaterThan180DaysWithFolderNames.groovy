//Deletes projects that were either built more than 180 days ago including disabled and never run pipeline jobs.

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
            println"Frozen, ${item.getClass()}, ${item.getRootDir()}, ${item.getFullName()}, ${item.getAbsoluteUrl()}";
            continue
        }
        if (item.disabled) {
            counts[2]++
            println"Disabled, ${item.getClass()}, ${item.getRootDir()}, ${item.getFullName()}, ${item.getAbsoluteUrl()}";

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
                println"365, ${item.getClass()}, ${item.getRootDir()}, ${item.getFullName()}, ${item.getAbsoluteUrl()}";
                println "Permanently deleting job - ${item.getAbsoluteUrl()}"
                item.delete();
                println "--- Deleted ---"
                continue
            }
            if (build_time.compareTo(oneHundredEightyDays) < 1 && build_time.compareTo(oneYear) >= 1) {
                counts[1]++
                println"180, ${item.getClass()}, ${item.getRootDir()}, ${item.getFullName()}, ${item.getAbsoluteUrl()}";
                println "Permanently deleting job - ${item.getAbsoluteUrl()}"
                item.delete();
                println "--- Deleted ---"
                continue
            }
        } else {
            //never built
            counts[4]++
            println"Never, ${item.getClass()}, ${item.getRootDir()}, ${item.getFullName()}, ${item.getAbsoluteUrl()}";
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


