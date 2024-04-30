import java.text.SimpleDateFormat
def iCount = 1

def sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss.SSS")
def startDate = new Date()
println "Start time : ${sdf.format(startDate)}"
//--------------------------------------------------------------------------------------

//Master HostName and IP Address
println "Master executed on - ${java.net.InetAddress.getLocalHost()}"

println "-------------------------------------------------------------------------------"
println "Iterates through all jenkins agents to find online instances and busy executors"
println "-------------------------------------------------------------------------------"

 
import jenkins.model.Jenkins

Jenkins j = Jenkins.instance

int active_builds = 0
int inactive_executors = 0
int total_executors = 0

int total_active_builds = 0
int total_inactive_executors = 0
int total_all_executors = 0
j.slaves.each 
{ 
    slave ->

    active_builds = 0
    inactive_executors = 0
    total_executors = 0

    if(!slave.computer.isOffline()) 
    {
        def executors = slave.computer.executors
        executors.each 
        { 
            executor ->
            if(executor.isBusy()) 
            {
                active_builds++
                total_active_builds++
            } 
            else 
            {
                inactive_executors++
                total_inactive_executors++
            }
            total_executors++
            total_all_executors++
        }
        println "${sdf.format(new Date())} :: ${iCount++} - ${slave.computer.name} [${slave.labelString}] is online with ${total_executors} executors, with ${active_builds} active and ${inactive_executors} inactive."
    }
    else
    {
        println "${sdf.format(new Date())} ::${iCount++} - ${slave.computer.name} [${slave.labelString}] is offline."
    }
}

println "-------------------------------------------------"
println "${sdf.format(new Date())} :: ${java.net.InetAddress.getLocalHost()} -> Queue Size: ${j.queue.items.size()}, Active: ${total_active_builds}, Free executors: ${total_inactive_executors}, All executors: ${total_all_executors}"


//-------------------------------------------------------
def endDate = new Date()
println "End time: ${sdf.format(endDate)}"

use(groovy.time.TimeCategory) 
{
    def duration = endDate - startDate
  	print "Elapsed time: Days: ${duration.days}, Hours: ${duration.hours}, Minutes: ${duration.minutes}, Seconds: ${duration.seconds}"
}