/// Workspace cleaner script to be run as a pipeline job
/// Author: Jacque Olson
/// Modified By: Rohit K. Bhatnagar
/// Modified On: June 21, 2020
/// Modified Reason: Script keeps bringing healthy nodes offline for workspace cleanup but is never able to make them online.
///                  Usually reasons for the same is some hung workspace. Inserting the deletion task inside a try-catch

import hudson.util.RemotingDiagnostics
import jenkins.model.Jenkins
import hudson.maven.MavenModule
import hudson.matrix.MatrixConfiguration
import hudson.slaves.OfflineCause

import org.jenkinsci.plugins.workflow.steps.FlowInterruptedException

def jenkinsMasterItems = Jenkins.get().getAllItems(Job)
def jenkinsMasterNodes = Jenkins.get().getNodes()
def jenkinsMasterHostName = Jenkins.get().getComputer('').getHostName()
def offlineCause = new OfflineCause.ByCLI("Agent Workspace Cleaner Triggered") 
def onlineCause = new OfflineCause.ByCLI("Agent Workspace Cleaner Completed") 

if (jenkinsMasterItems && jenkinsMasterNodes) 
{
  def spaceConsumedCommands = '''println("df -kh | grep jenkins ; df -kh | grep tmp ; df -kh /cygdrive/d".execute().text)'''
  
  def iNodeCount = 0

  for (def node in jenkinsMasterNodes) 
  {
    def computer = node.toComputer()
    def isWindowsLabel = node.getLabelString().contains("WIN")
    def isLinuxLabel = node.getLabelString().contains("LIN")
    
    if (computer?.isOnline() && isLinuxLabel || computer?.isOnline() && isWindowsLabel) 
    {
      try 
      {
        //println(RemotingDiagnostics.executeGroovy(spaceComsumedCommands, computer.getChannel()))
        println("----------- #${++iNodeCount} ${computer.name}[${node.getLabelString()}] ----------")
        setComputerToTemporaryOffline(computer, offlineCause)
        deleteAgentWorkspaces(node, jenkinsMasterItems, spaceConsumedCommands)
        setComputerToOnline(computer, onlineCause)
        //println(RemotingDiagnostics.executeGroovy(spaceComsumedCommands, computer.getChannel()))
      } 
      catch (IOException ex) 
      {
        println("There was an IO issue/problem communicating with ${node.getNodeName()}. \n${ex.printStackTrace()}")
      } 
      catch (FlowInterruptedException | InterruptedException ex) 
      {
        println("The agent workspace cleaner job on ${node.getNodeName()} was interrupted/manually aborted. \n${ex.printStackTrace()}")
      } 
      catch (Exception ex) 
      {
        println("An unexpected exception type has been caught.")
        throw ex
      } 
      finally 
      {
        if (computer?.isOffline()) 
        {
          println("${node.getNodeName()} had an issue during the cleanup process, and must be brought back online manually as a result.")
        }
      }
    } 
    else 
    {
      println("The node: ${node.getNodeName()} is either not online, or is not a Linux or Windows label.")
    }

  }

} 
else 
{
        println("There were no jobs retrieved from ${jenkinsMasterHostName}, no Agents were online for ${jenkinsMasterHostName} to delegate work to, or both of the aforementioned conditions are true. Job cancelled.")
}
 
//arguement 1 = Node - Name of the agent to clean workspace
//arguement 2 = List<Item> - Jenkins Items List specific to any Job
//argument 3 - String - Shell command to execute to collect disk space
void deleteAgentWorkspaces(def node, def jenkinsMasterItems, def spaceConsumedCommands) 
{
    try
    {
        for (def item in jenkinsMasterItems) 
        { 
            if (!item?.isBuilding() && !(item instanceof MatrixConfiguration) && !(item instanceof MavenModule) && node?.getWorkspaceFor(item)?.exists()) 
            {
                def computer = node.toComputer()
                println(" BEFORE: ${RemotingDiagnostics.executeGroovy(spaceConsumedCommands, computer.getChannel())} ")
                node.getWorkspaceFor(item).deleteRecursive()
                println("Deleted workspace: ${node.getWorkspaceFor(item)} on node: ${node.getNodeName()}")
                println("AFTER: ${RemotingDiagnostics.executeGroovy(spaceConsumedCommands, computer.getChannel())} ")
            }
        }
    }
    catch(Exception exp)
    {
        println "Exception raised when deleting workspace - ${exp.message}"
    }
}

//arguement 1 = Computer - Node name to make offline
//arguement 2 = OfflineCause - Reason for node to go offline
void setComputerToTemporaryOffline(def computer, def offlineCause) 
{
    try
    {
        computer.setTemporarilyOffline(true, offlineCause)
        computer.waitUntilOffline()
        println(">>>> ${computer.getName()} is Offline!")
    }
    catch(Exception exp)
    {
        println "Exception raised when trying to make node offline - ${exp.message}"
    }
}

//arguement 1 = Computer - Node name to make online
//arguement 2 = OnlineCause - Reason for node to come online
void setComputerToOnline(def computer, def onlineCause) 
{
    try
    {
        computer.setTemporarilyOffline(false, onlineCause)
        computer.waitUntilOnline()
        println(">>>> ${computer.getName()} is Online!")
    }
    catch(Exception exp)
    {
        println "Exception raised when trying to make node online - ${exp.message}"
    }
}