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
//Displays disk space on all connected nodes to a Jenkins Master
//---------------------------------------------------------------------------

println "---------------------------------------------------------------------------"
println "Displays disk space on all connected nodes to a Jenkins Master"
println "---------------------------------------------------------------------------"

def spaceConsumedCommands = '''println("df -kh | grep jenkins ; df -kh | grep tmp ; df -kh /cygdrive/d".execute().text)'''

def jenkinsMasterNodes = Jenkins.get().getNodes()
for (def node in jenkinsMasterNodes) 
{
  def computer = node.toComputer()
  def isWindowsLabel = node.getLabelString().contains("WIN")
  def isLinuxLabel = node.getLabelString().contains("LIN")
  if (computer?.isOnline() && isLinuxLabel || computer?.isOnline() && isWindowsLabel) 
  {
    try 
    {
      println("${computer.name}[${node.getLabelString()}] - ${RemotingDiagnostics.executeGroovy(spaceConsumedCommands, computer.getChannel())} ")
    }
    catch (Exception ex) 
    {
      println("Exception - ${ex.message}")
    }
  }
}

//---------------------------------------------------------------------------
def endDate = new Date()
println "End time: ${sdf.format(endDate)}"

use(groovy.time.TimeCategory) 
{
    def duration = endDate - startDate
  	print "Elapsed time: Days: ${duration.days}, Hours: ${duration.hours}, Minutes: ${duration.minutes}, Seconds: ${duration.seconds}"
}