#!Groovy

import java.text.SimpleDateFormat
def iCount = 1

def sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss.SSS")
def startDate = new Date()
println "Start time : ${sdf.format(startDate)}"

//Master HostName and IP Address
println "Runnign on - ${java.net.InetAddress.getLocalHost()}"
//---------------------------------------------------------------------------
//Displays all available JDKs within Jenkins instance
//---------------------------------------------------------------------------

def jenkins = Jenkins.getInstance()
println "Jenkins version: ${jenkins.getVersion()}"
println "Available JDKs: ${jenkins.getInstance().getJDKs()}"

//---------------------------------------------------------------------------
def endDate = new Date()
println "End time: ${sdf.format(endDate)}"

use(groovy.time.TimeCategory) 
{
    def duration = endDate - startDate
  	print "Elapsed time: Days: ${duration.days}, Hours: ${duration.hours}, Minutes: ${duration.minutes}, Seconds: ${duration.seconds}"
}