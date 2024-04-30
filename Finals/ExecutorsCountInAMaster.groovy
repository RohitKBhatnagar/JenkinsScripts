#!Groovy

import java.text.SimpleDateFormat
def iCount = 1

def sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss.SSS")
def startDate = new Date()
println "Start time : ${sdf.format(startDate)}"

//Master HostName and IP Address
println "Master HostName and IP Address - ${java.net.InetAddress.getLocalHost()}"

import hudson.util.RemotingDiagnostics

//---------------------------------------------------------------------------
//All Nodes - executor count for a Jenkins Master
//---------------------------------------------------------------------------

println "---------------------------------------------------------------------------"
println "All Nodes - executor count"
println "---------------------------------------------------------------------------"

println("All Nodes - executor count:")
Jenkins.instance.computers.eachWithIndex() { c, i -> println " [${i+1}] ${c.displayName}: ${c.numExecutors}" };
println("Total nodes: [" + Jenkins.instance.computers.size() + "]")
println("Total executors: [" + Jenkins.instance.computers.inject(0, {a, c -> a + c.numExecutors}) + "]")

//---------------------------------------------------------------------------
def endDate = new Date()
println "End time: ${sdf.format(endDate)}"

use(groovy.time.TimeCategory) 
{
    def duration = endDate - startDate
  	print "Elapsed time: Days: ${duration.days}, Hours: ${duration.hours}, Minutes: ${duration.minutes}, Seconds: ${duration.seconds}"
}