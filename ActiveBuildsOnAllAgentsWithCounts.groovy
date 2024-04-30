import java.text.SimpleDateFormat
def iCount = 1

def sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss.SSS")
def startDate = new Date()
println "Start time : ${sdf.format(startDate)}"
//----------------------------------------------------------------------

println "--------------------------------------------------------------------------------------"
println "This script counts up all of the active builds across all agents and prints the total."
println "--------------------------------------------------------------------------------------"

//----------------------------------------------------------------------------------------------
//   This script counts up all of the active builds across all agents and prints the total.
//----------------------------------------------------------------------------------------------

Jenkins j = Jenkins.instance

int active_builds = 0
int inactive_executors = 0
j.slaves.each 
{ slave ->
//for (slave in hudson.model.Hudson.instance.slaves) {
    if(!slave.computer.isOffline()) 
    {
        def executors = slave.computer.executors
        executors.each 
        { executor ->
            if(executor.isBusy()) 
            {
                active_builds++
            } 
            else 
            {
                inactive_executors++
            }
        }
    }

    println "${iCount++}: ------------ Name: ${slave.computer.name} ------------" 
}
println "${iCount++}: Queue-Size: ${j.queue.items.size()}, Active-Builds: ${active_builds}, Free-Executors: ${inactive_executors}" 



null

//--------------------------------------------------------------------
def endDate = new Date()
println "End time: ${sdf.format(endDate)}"

use(groovy.time.TimeCategory) 
{
    def duration = endDate - startDate
  	print "Elapsed time: Days: ${duration.days}, Hours: ${duration.hours}, Minutes: ${duration.minutes}, Seconds: ${duration.seconds}"
}