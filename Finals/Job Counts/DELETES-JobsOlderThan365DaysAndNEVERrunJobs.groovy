//Deletes projects that were either built more than 365+ & 180+ days ago including never run pipeline jobs.

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
println "Deletes projects that were either built more than 365+ & 180+ days ago including never run pipeline jobs."
println "Run on the Controller with hostname - ${controller}"
println "===================================================================================================================="

def sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss.SSS")
def startDate = new Date()
println "Start time : ${sdf.format(startDate)}"


Calendar c = Calendar.getInstance()
Calendar oneYear = Calendar.getInstance()
Calendar oneHundredEightyDays = Calendar.getInstance()

now = c.instance
def date = new Date()
c.setTime(date);

oneYear.add(Calendar.DATE, -365)
oneHundredEightyDays.add(Calendar.DATE, -180)

println("Starting report generation at - ${now.time}")
println "++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++"
def allItems = Jenkins.instance.getAllItems()
def iItemCount = 0

//println("item count=${allItems.size()}")

// counts is an array to collect stats
//      0       1        2       3       4  
// |one year|never
def counts = [0, 0, 0] as int[]
int btCount = 0;
int mbCount = 0;
def build_time;

def folderName = ''
for (item in allItems) {
    try {
            //Skip if item is say folder name
            try {
              if(item instanceof Folder)
              {
              	//println "Folder, ${item.name}"
                folderName = item.name
                continue;
              }
              else
                build_time = item.getLastBuild()    
            }
            catch (Exception e){
                //Do nothing as this maybe a folder
                continue;
            }

            if (build_time != null) {
            build_time = item.getLastBuild().getTimestamp()    
                
                if (build_time.compareTo(oneYear) < 1) {
                    def sJobUrl = ''
                    counts[0]++
                    //println"365, ${item.getClass()}, ${item.getRootDir()}, ${item.getFullName()}, ${item.getAbsoluteUrl()}";
                    //println "Permanently deleting job - ${item.getAbsoluteUrl()}"
                    //sJobUrl = item.getAbsoluteUrl()
                    //item.delete();
                    //println "DELETED, 365, ${sJobUrl}"
                    continue
                }
                if (build_time.compareTo(oneHundredEightyDays) < 1 && build_time.compareTo(oneYear) >= 1)
                {
                    def sJobUrl = ''
                    counts[1]++
                    //println"180, ${item.getClass()}, ${item.getRootDir()}, ${item.getFullName()}, ${item.getAbsoluteUrl()}";
                    //println "Permanently deleting job - ${item.getAbsoluteUrl()}"
                    //sJobUrl = item.getAbsoluteUrl()
                    //item.delete();
                    //println "DELETED, 180, ${sJobUrl}"
                    continue
                }
            }
            else {
                try {
                    counts[2]++
                      //def numbuilds = item.builds.size()
                      //if (numbuilds == 0) {
                        //println "${++btCount} - ${item.getClass()}, ${item.getRootDir()}, ${item.getFullName()}, ${item.getAbsoluteUrl()}"
                        //println "${build_time.toZonedDateTime().toLocalDateTime()}}"
                        //never built ---------------------
                        def sJobUrl = ''
                        println"Never, ${item.getClass()}, ${item.getRootDir()}, ${item.getFullName()}, ${item.getAbsoluteUrl()}";
                        sJobUrl = item.getAbsoluteUrl()
                        item.delete();
                        println "DELETED, Never, ${sJobUrl}"
                      //}
                }
                catch (Exception e)
                {
                    mbCount++;
                }
                //if(build_time.compareTo(c) < 1)
                //{
                //    counts[2]++
                //    println "${++btCount} - ${item.getClass()}, ${item.getRootDir()}, ${item.getFullName()}, ${item.getAbsoluteUrl()}, ${build_time.toZonedDateTime().toLocalDateTime()}}"
                //}
                //never built ---------------------
                //def sJobUrl = ''
                //counts[2]++
                //println"Never, ${item.getClass()}, ${item.getRootDir()}, ${item.getFullName()}, ${item.getAbsoluteUrl()}";
                //sJobUrl = item.getAbsoluteUrl()
                //item.delete();
                //println "DELETED, Never, ${sJobUrl}"
            }
        }
    catch (Exception e) {
        println "Exception raised ... - ${e}"
    }
}

println("\t|one year\t\t|never")
println("\t=======================================================================================================================")
println("\t|one year+ : ${counts[0]}\t|180+ days : ${counts[1]}\t|never : ${counts[2]}\t|")
println("Total 365+ days old builds in ${controller} are : ${counts.sum() - (counts[1] + counts[2])} where total item count is : ${allItems.size}")
println("Total 180+ days old builds in ${controller} are : ${counts.sum() - (counts[0] + counts[2])} where total item count is : ${allItems.size}")

println("\t Total jobs with built time NOT null - ${btCount} \t or a multi-branch job - ${mbCount}")

//--------------------------------------------------
println "++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++"
def endDate = new Date()
println "End time: ${sdf.format(endDate)}"


/*********
class java.util.GregorianCalendar functions:
equals(); getClass(); hashCode(); notify(); notifyAll(); toString(); wait(); add(); after(); before(); clear(); clone(); compareTo(); get(); getActualMaximum(); getActualMinimum(); getAvailableCalendarTypes(); getAvailableLocales(); getCalendarType(); getDisplayName(); getDisplayNames(); getFirstDayOfWeek(); getGreatestMinimum(); getInstance(); getLeastMaximum(); getMaximum(); getMinimalDaysInFirstWeek(); getMinimum(); getTime(); getTimeInMillis(); getTimeZone(); getWeekYear(); getWeeksInWeekYear(); isLenient(); isSet(); isWeekDateSupported(); roll(); set(); setFirstDayOfWeek(); setLenient(); setMinimalDaysInFirstWeek(); setTime(); setTimeInMillis(); setTimeZone(); setWeekDate(); toInstant(); from(); getGregorianChange(); isLeapYear(); setGregorianChange(); toZonedDateTime(); 
**********/