import java.text.SimpleDateFormat

def sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss.SSS")
def startDate = new Date()
println "Start time : ${sdf.format(startDate)}"
//-------------------------------------------------

String strMaster = java.net.InetAddress.getLocalHost();
println "================================================================================================="
println "Displays all members behind a top level folder"
println ">>>>> CD Master - ${strMaster} <<<<<<<<<<<<<<<<<<<<"
println "================================================================================================="

//==============================================================
//Displays all members behind a top level folder
//==============================================
import nectar.plugins.rbac.groups.*;
import java.util.*;
import com.cloudbees.hudson.plugins.folder.AbstractFolder;

Map containers = new TreeMap();

def allFolderGrps = []

def topItems = Jenkins.instance.getItems()
topItems.eachWithIndex 
{
  itTop, iTopCount ->
    println "${iTopCount} :: ${itTop.name}";
    println "=============================";
  
  try 
  {
    // Add the root container
    containers.put(Jenkins.instance.displayName, GroupContainerLocator.locate(Jenkins.instance.getItemByFullName(itTop.name, AbstractFolder)));
    // Add all the items that are be containers
    for (i in Jenkins.instance.getItemByFullName(itTop.name, AbstractFolder).allItems) 
    {
      if (GroupContainerLocator.isGroupContainer(i.getClass())) 
      {
        GroupContainer g = GroupContainerLocator.locate(i);
        if (g != null) 
        {
          containers.put(Jenkins.instance.displayName + "/" + i.fullDisplayName, g);
        }
      }
    }

    // There may be other group containers if somebody has written additional
    // extension points in additional plugins, but at this point in time this
    // is the full set of places where group containers can be hiding
    for (c in containers) {
      //println(c.key); //Displays sub-folder names
      for (g in c.value.groups) {
        String strFolderName = c.key.toString();
        boolean topFldr = false;
        String strTopFolderName = "";
        if(strFolderName.equals("Jenkins"))
        {
          strFolderName = "${itTop.name}";
          topFldr = true;
        }
        else
        {
          if(strFolderName.contains("Jenkins"))
            strFolderName = strFolderName.replaceAll("Jenkins", "")
          if(strFolderName.contains(" \u00bb "))
            strFolderName = strFolderName.replaceAll(" \u00bb ", "/")
          strTopFolderName = strFolderName.split("/")[1]
        }
        println(strFolderName); //Displays sub-folder names
        println("  " + g.name);   //https://javadoc.jenkins.io/plugin/cloudbees-folder/com/cloudbees/hudson/plugins/folder/Folder.html
        println("    Roles:");
        String fldrRoles = "";
        String fldrPropagation = "";
        for (r in g.roles) {
          println("      " + r + (g.doesPropagateToChildren(r) ? " (and children)" : " (pinned)"));
          //fldrRoles = r + (g.doesPropagateToChildren(r) ? " (and children)" : " (pinned)");
          if(fldrRoles?.trim())
            fldrRoles += ", " + r;
          else
            fldrRoles = r;
          fldrPropagation = g.doesPropagateToChildren(r) ? "children" : "pinned";
          //def fldrRoles = [Folder: strFolderName, Groups: g.name, Roles: r, Propagates: g.doesPropagateToChildren(r) ? " (children)" : " (pinned)"]
          //println (new groovy.json.JsonBuilder(fldrRoles).toString())
        }
        println("    Members:");

        String fldrRoleMembers = "";
        // g.members is the String names
        // g.membership is the corresponding AbstractAssignee objects (so this may involve an LDAP lookup)
        // but g.membership is the only way to determine what the String name corresponds to
        // listing here so you can see what can be done, but up to you to judge the runtime cost
        for (a in g.membership) {
          println("      " + a.id);
          //def fldrMembers = [Members: a.id]
          //println (new groovy.json.JsonBuilder(fldrMembers).toString())
          if(fldrRoleMembers?.trim())
            fldrRoleMembers += ", " + a.id
          else
            fldrRoleMembers = a.id;
          //println("      " + a.id + " <" + a.fullName + "> (" + a.description + ")");
          //println("      " + a.id + " <" + a.fullName + "> (" + a.description + " : " +a.getClass().getName() + ")");
        }
        //allFolderGrps.add([Folder: strFolderName, IsParent: topFldr, Group: g.name, Role: fldrRoles, Propagate: fldrPropagation, Member: fldrRoleMembers, TopFolder: itTop.name])
        if(topFldr)
          allFolderGrps.add([Folder: strFolderName, IsParent: topFldr, Group: g.name, Role: fldrRoles, Propagate: fldrPropagation, Member: fldrRoleMembers, TopFolder: itTop.name])
        else
          allFolderGrps.add([Folder: strFolderName, IsParent: topFldr, Group: g.name, Role: fldrRoles, Propagate: fldrPropagation, Member: fldrRoleMembers, TopFolder: strTopFolderName])
        
        strFolderName = "";
        strTopFolderName = "";
        fldrRoleMembers = "";
        fldrRoles = "";
        fldrPropagation = "";
      }
    }
  }
  catch(Exception e) {
    println (e.message);
  }
}

//Print the JSON collected data
println (new groovy.json.JsonBuilder(allFolderGrps).toString())

//--------------------------------------------------
def endDate = new Date()
println "End time: ${sdf.format(endDate)}"

use(groovy.time.TimeCategory) 
{
    def duration = endDate - startDate
    print "Elapsed time: Days: ${duration.days}, Hours: ${duration.hours}, Minutes: ${duration.minutes}, Seconds: ${duration.seconds}"
}

return null;

