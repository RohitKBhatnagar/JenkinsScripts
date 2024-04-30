import java.text.SimpleDateFormat

def sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss.SSS")
def startDate = new Date()
println "Start time : ${sdf.format(startDate)}"
//-------------------------------------------------

String strMaster = java.net.InetAddress.getLocalHost();
println "================================================================================================="
println "Displays all members behind ALL Top level folders"
println ">>>>> CD Master - ${strMaster} and Groups, Roles and Members of all TOP LEVEL Folders. Script will iterate through sub-folders as well. <<<<<<<<<<<"
println "================================================================================================="

//===================================================================
//Displays all groups, roles and members behind all top level folder
//===================================================================
import nectar.plugins.rbac.groups.*;
import java.util.*;
import com.cloudbees.hudson.plugins.folder.AbstractFolder;

def topItems = Jenkins.instance.getItems()
topItems.eachWithIndex {
	itTop, iTopCount ->
	Map containers = new TreeMap();
	println "${iTopCount} :: ${itTop.name}";
	//println "${iTopCount} :: ${itTop.name} :: ${itTop.fullDisplayName} :: ${itTop.fullName} :: ${itTop.shortUrl} :: ${itTop.url} :: ${itTop.rootDir}";
	//1 :: Builder-Tools-Engine :: Builder-Tools-Engine :: Builder-Tools-Engine :: job/Builder-Tools-Engine/ :: job/Builder-Tools-Engine/ :: /sys_apps_01/jenkins_nfs/jobs/Builder-Tools-Engine
	println("-----------------------------------------------");
	if(Jenkins.instance.getItemByFullName(itTop.name, AbstractFolder) != null)
	{
		containers.put(Jenkins.instance.displayName, GroupContainerLocator.locate(Jenkins.instance.getItemByFullName(itTop.name, AbstractFolder)));
		// Add all the items that are be containers
		  for (i in Jenkins.instance.getItemByFullName(itTop.name, AbstractFolder).allItems) {
		  if (GroupContainerLocator.isGroupContainer(i.getClass())) {
			GroupContainer g = GroupContainerLocator.locate(i);
			//println g.url
            if (g != null) 
			{
				//println "${Jenkins.instance.displayName}/${i.fullDisplayName}, ${g}"
				containers.put(Jenkins.instance.displayName + "/" + i.fullDisplayName, g);
			}
		  }
		}
	}
	else
		println "${itTop.name} is not a container with any groups or roles!";

	// Display group containers now...
	
	containers.eachWithIndex {
		itC, itCount ->
		//for (c in containers) {
		for (c in itC) {
	     if(c.value.groups.size() > 0)
		  {
			//println(c.key); //Key here is 'Jenkins'
			  //println(c.value.url)
              println("Folder Group: ${c.value.containerTarget.fullName}")
              //Folder Group: OpenBankingConnect/Deploy_to_Dev
			  for (g in c.value.groups) {
				println("  " + g.name);
				println("    Roles:");
				for (r in g.roles) {
				  println("      " + r + (g.doesPropagateToChildren(r) ? " (and children)" : " (pinned)"));
				}
				println("    Members:");
				// Listing all members has an extremely high runtime cost. Enable the below portion at your own risk! --Rohit
				for (a in g.membership) {
				  //println("      " + a.id + " <" + a.fullName + "> (" + a.description + " : " +a.getClass().getName() + ")");
				  println("      " + a.id);
				  //Enable this if condition, only if you wish to list all members in any external groups
				  if(a instanceof nectar.plugins.rbac.assignees.ExternalGroupAssignee) {
						def iCount = 1;
                    	if(a.hasProperty('members'))
                        {
                          println "            Members count - ${a.members.size()}"
                          //for (b in a.members) {
                            //println "            ${iCount++}: ${b}"
                          //}
                        }
					}
				}
			  }
		  }
		}
	}
}

//--------------------------------------------------
println "================================================================================================="
def endDate = new Date()
println "End time: ${sdf.format(endDate)}"

use(groovy.time.TimeCategory) 
{
    def duration = endDate - startDate
  	print "Elapsed time: Days: ${duration.days}, Hours: ${duration.hours}, Minutes: ${duration.minutes}, Seconds: ${duration.seconds}"
}

return null;