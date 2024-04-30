import com.cloudbees.hudson.plugins.folder.AbstractFolder;
import nectar.plugins.rbac.groups.*;
import java.util.*;

def folderName="MarketingBusinessSolutionsDevOps"; //"PrepaidManagementServicesBizOps" // change value `folder-a` for the full name of the folder you want to disable all jobs in

Map containers = new TreeMap();
// Add the root container
containers.put(Jenkins.instance.displayName, GroupContainerLocator.locate(Jenkins.instance.getItemByFullName(folderName, AbstractFolder)));
// Add all the items that are be containers
for (i in Jenkins.instance.getItemByFullName(folderName, AbstractFolder).allItems) {
  if (GroupContainerLocator.isGroupContainer(i.getClass())) {
    GroupContainer g = GroupContainerLocator.locate(i);
    if (g != null) containers.put(Jenkins.instance.displayName + "/" + i.fullDisplayName, g);
  }
}

/****************************************************************************************
// Add all the nodes, as they are containers also (but be safe about it)
for (i in Jenkins.instance.nodes) {
  if (GroupContainerLocator.isGroupContainer(i.getClass())) {
    GroupContainer g = GroupContainerLocator.locate(i);
    if (g != null) containers.put(Jenkins.instance.displayName + "/" + i.displayName, g);
  }
}
****************************************************************************************/

// There may be other group containers if somebody has written additional
// extension points in additional plugins, but at this point in time this
// is the full set of places where group containers can be hiding

for (c in containers) {
  println(c.key);
  for (g in c.value.groups) {
    println("  " + g.name);
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
      //println "${g.membership}"
      println("      " + a.id + " <" + a.fullName + "> (" + a.description + " : " +a.getClass().getName() + ")");
      //Enable thie if condition, only if you wish to list all members in any external groups
      if(a instanceof nectar.plugins.rbac.assignees.ExternalGroupAssignee) {
						def iCount = 1;
						for (b in a.members) {
							println "            ${iCount++}: ${b}"
						}
					}
    }
  }
}