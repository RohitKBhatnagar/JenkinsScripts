import java.text.SimpleDateFormat

def folderName="PrepaidManagementServicesBizOps" // change value `folder-a` for the full name of the folder you want to disable all jobs in

def sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss.SSS")
def startDate = new Date()
println "Start time : ${sdf.format(startDate)}"
//-------------------------------------------------

String strMaster = java.net.InetAddress.getLocalHost();
println "================================================================================================="
println "Displays all members behind a top level folder and also marks the top level folder as DISABLED"
println ">>>>> CD Master - ${strMaster} and the TOP LEVEL Folder Name - ${folderName} <<<<<<<<<<<<<<<<<<<<"
println "================================================================================================="

//==============================================================
//Displays all members behind a top level folder
//==============================================
import nectar.plugins.rbac.groups.*;
import java.util.*;
import com.cloudbees.hudson.plugins.folder.AbstractFolder;

Map containers = new TreeMap();

// Add the root container
containers.put(Jenkins.instance.displayName, GroupContainerLocator.locate(Jenkins.instance.getItemByFullName(folderName, AbstractFolder)));
// Add all the items that are be containers
//for (i in Jenkins.instance.allItems) {
  for (i in Jenkins.instance.getItemByFullName(folderName, AbstractFolder).allItems) {
  if (GroupContainerLocator.isGroupContainer(i.getClass())) {
    GroupContainer g = GroupContainerLocator.locate(i);
    if (g != null) containers.put(Jenkins.instance.displayName + "/" + i.fullDisplayName, g);
  }
}

// Add all the nodes, as they are containers also (but be safe about it)
for (i in Jenkins.instance.nodes) {
  if (GroupContainerLocator.isGroupContainer(i.getClass())) {
    GroupContainer g = GroupContainerLocator.locate(i);
    if (g != null) containers.put(Jenkins.instance.displayName + "/" + i.displayName, g);
  }
}
// There may be other group containers if somebody has written additional
// extension points in additional plugins, but at this point in time this
// is the full set of places where group containers can be hiding

for (c in containers) {
  println(c.key);
  for (g in c.value.groups) {
    println("  " + g.name);   //https://javadoc.jenkins.io/plugin/cloudbees-folder/com/cloudbees/hudson/plugins/folder/Folder.html
    println("    Roles:");
    for (r in g.roles) {
      println("      " + r + (g.doesPropagateToChildren(r) ? " (and children)" : " (pinned)"));

    }
    println("    Members:");
    // g.members is the String names
    // g.membership is the corresponding AbstractAssignee objects (so this may involve an LDAP lookup)
    // but g.membership is the only way to determine what the String name corresponds to
    // listing here so you can see what can be done, but up to you to judge the runtime cost
    for (a in g.membership) {
      println("      " + a.id + " <" + a.fullName + "> (" + a.description + " : " +a.getClass().getName() + ")");
    }
  }
}

//=================================================================
//Marks the top level folder DISABLED
//------------------------------------
//def allItems = Jenkins.instance.getAllItems()
//allItems.eachWithIndex{ it, i ->
//  println "$i: $it"
//}

//folderName="CaaS_Testing" // change value `folder-a` for the full name of the folder you want to disable all jobs in

Jenkins.instance.getItemByFullName(folderName, AbstractFolder).getAllItems()
    .findAll { it instanceof ParameterizedJobMixIn.ParameterizedJob || it instanceof AbstractFolder }
    .each {
        it.makeDisabled(true)
        println("Disabled job: [$it.fullName]")
    }

//--------------------------------------------------
def endDate = new Date()
println "End time: ${sdf.format(endDate)}"

use(groovy.time.TimeCategory) 
{
    def duration = endDate - startDate
  	print "Elapsed time: Days: ${duration.days}, Hours: ${duration.hours}, Minutes: ${duration.minutes}, Seconds: ${duration.seconds}"
}

return null;

