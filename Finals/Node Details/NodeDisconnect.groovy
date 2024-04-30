https://javadoc.jenkins.io/hudson/model/Slave.html
https://javadoc.jenkins.io/hudson/slaves/SlaveComputer.html
https://javadoc.jenkins.io/hudson/slaves/SlaveComputer.html#disconnect-hudson.slaves.OfflineCause-
https://groups.google.com/forum/#!topic/jenkinsci-dev/ch2lQZvZdkw
def NodeDisconnect (NodeName) {
    println("Node Disconnect")
    def WorkNode = jenkins.model.Jenkins.instance.getNode(NodeName)
    if (WorkNode) {
        println ("GOT NODE")
        def WorkComputer = WorkNode.computer
        def WorkLouncher = WorkComputer.getLauncher();
        def WorkListener = TaskListener.NULL
        if (WorkComputer.isOnline()){
            println("Node is online")
            if (WorkComputer.isIdle()){
                println ("Current node status Online, Idle with ${WorkComputer.countExecutors()}:${WorkComputer.countBusy()} executors")
                println ("About to disconnect node")
                def WorkOfflineCause = new OfflineCause.UserCause(User.current(),'Disconnected for VirtualBox snapshot restore...')
                WorkLouncher.beforeDisconnect(WorkComputer,WorkListener)
                WorkComputer.disconnect(WorkOfflineCause);
                WorkLouncher.afterDisconnect(WorkComputer,WorkListener)
                println ("Wait for node to go offline")
                WorkComputer.waitUntilOffline()
            }
            else {
                println ("Current node status Online, Busy with ${WorkComputer.countExecutors()}:${WorkComputer.countBusy()} executors")
                //raise
            }
        }
        else {
            println("Node is offline with cause:${WorkComputer.getOfflineCauseReason()}")
            //raise
        }
    }
    else {
        println ("NODE NOT FOUND")
        //raise error
    }
}

https://javadoc.jenkins-ci.org/hudson/model/Computer.html#disconnect(hudson.slaves.OfflineCause)

===========================
import java.text.SimpleDateFormat
def iCount = 1

def sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss.SSS")
def startDate = new Date()
println "Start time : ${sdf.format(startDate)}"

//---------------------------------------------------------------------------
//Displays all nodes and information about each node
//---------------------------------------------------------------------------

for (aSlave in hudson.model.Hudson.instance.slaves) {
  println('====================');
  println('Name: ' + aSlave.name);
  println('getLabelString: ' + aSlave.getLabelString());
  println('getNumExectutors: ' + aSlave.getNumExecutors());
  println('getRemoteFS: ' + aSlave.getRemoteFS());
  println('getMode: ' + aSlave.getMode());
  println('getRootPath: ' + aSlave.getRootPath());
  println('getDescriptor: ' + aSlave.getDescriptor());
  println('getComputer: ' + aSlave.getComputer());
  println('\tcomputer.isAcceptingTasks: ' + aSlave.getComputer().isAcceptingTasks());
  println('\tcomputer.isLaunchSupported: ' + aSlave.getComputer().isLaunchSupported());
  println('\tcomputer.getConnectTime: ' + aSlave.getComputer().getConnectTime());
  println('\tcomputer.getDemandStartMilliseconds: ' + aSlave.getComputer().getDemandStartMilliseconds());
  println('\tcomputer.isOffline: ' + aSlave.getComputer().isOffline());
  println('\tcomputer.countBusy: ' + aSlave.getComputer().countBusy());
  //if (aSlave.name == 'NAME OF NODE TO DELETE') {
  //  println('Shutting down node!!!!');
  //  aSlave.getComputer().setTemporarilyOffline(true,null);
  //  aSlave.getComputer().doDoDelete();
  //}
  println('\tcomputer.getLog: ' + aSlave.getComputer().getLog());
  println('\tcomputer.getBuilds: ' + aSlave.getComputer().getBuilds());
}

def endDate = new Date()
println "End time: ${sdf.format(endDate)}"

use(groovy.time.TimeCategory) 
{
    def duration = endDate - startDate
  	print "Elapsed time: Days: ${duration.days}, Hours: ${duration.hours}, Minutes: ${duration.minutes}, Seconds: ${duration.seconds}"
}