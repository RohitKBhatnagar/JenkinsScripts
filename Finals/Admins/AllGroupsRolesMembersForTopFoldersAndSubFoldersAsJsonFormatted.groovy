import java.text.SimpleDateFormat

def sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss.SSS")
def startDate = new Date()
println "Start time : ${sdf.format(startDate)}"
//-------------------------------------------------

String strMaster = java.net.InetAddress.getLocalHost();
println "================================================================================================="
println "Displays all members behind ALL Top level folders"
println ">>>>> CD Master - ${strMaster} and Groups, Roles and Members of all TOP LEVEL Folders. "
println "Script will iterate through sub-folders as well. <<<<<<<<<<<"
println "================================================================================================="

//===================================================================
//Displays all groups, roles and members behind all top level folder
//===================================================================
import nectar.plugins.rbac.groups.*;
import java.util.*;
import com.cloudbees.hudson.plugins.folder.AbstractFolder;
import hudson.model.User;

def topItems = Jenkins.instance.getItems()
topItems.eachWithIndex {
    itTop, iTopCount ->
    Map containers = new TreeMap();
    if(Jenkins.instance.getItemByFullName(itTop.name, AbstractFolder) != null)
    {
        containers.put(Jenkins.instance.displayName, GroupContainerLocator.locate(Jenkins.instance.getItemByFullName(itTop.name, AbstractFolder)));
        // Add all the items that are be containers
          for (i in Jenkins.instance.getItemByFullName(itTop.name, AbstractFolder).allItems) {
          if (GroupContainerLocator.isGroupContainer(i.getClass())) {
            GroupContainer g = GroupContainerLocator.locate(i);
            if (g != null) 
            {
                containers.put(Jenkins.instance.displayName + "/" + i.fullDisplayName, g);
            }
          }
        }
    }

    containers.eachWithIndex 
    {
        itC, itCount ->
        for (c in itC) 
        {
            if(c.value.groups.size() > 0)
            {
                Boolean bIsParent = true;
                if(topItems.contains(c.value.containerTarget.getTarget()))
                    bIsParent = true
                else
                    bIsParent = false
                def lstGroups = []
                for (g in c.value.groups) 
                {
                    def lstRoles = []
                    for (r in g.roles) 
                    {
                        def lstMembers = []
                        for (a in g.membership) 
                        {
                            Boolean bIsUser = false
                            def lstADMembers = []
                            def total = 0
                            if(a instanceof nectar.plugins.rbac.assignees.UserAssignee)
                                bIsUser = true
                            if(a instanceof nectar.plugins.rbac.assignees.ExternalGroupAssignee) 
                            {
                                def iCount = 1;
                                if(a.hasProperty('members'))
                                {
                                    // for (b in a.members) {
                                    //     lstADMembers.add(b);
                                    // }
                                    total = a.members.size()
                                }
                                else 
                                    total = 0
                            }
                            else
                                total = 0;
                            if(bIsUser)
                                lstMembers.add(ADGroup:a.id, IsUser:"true");
                            else
                            {
                                // if(lstADMembers.size() > 0)
                                //     lstMembers.add(ADGroup:a.id, IsUser:"false", Members:lstADMembers, Count:total);
                                // else
                                    lstMembers.add(ADGroup:a.id, IsUser:"false", Count:total);
                            }
                        }
                        lstRoles.add(Role:r + (g.doesPropagateToChildren(r) ? " (and children)" : " (pinned)"), ADGroups:lstMembers)
                    }
                    if(lstRoles.size() > 0)
                        lstGroups.add(Group:g.name, Roles:lstRoles)
                    else
                        lstGroups.add(Group:g.name)
                }
              String sParent = ""
              try{
                 sParent = c.value.containerTarget.fullName.substring(0, c.value.containerTarget.fullName.indexOf("/"))
              }
              catch(Exception exp) {
                sParent = ""
              }
              def lstFolders;
                if(bIsParent)
                    lstFolders = [Folder:c.value.containerTarget.fullName, IsParentFolder:bIsParent, Groups:lstGroups]
                else 
                    lstFolders = [Folder:c.value.containerTarget.fullName, IsParentFolder:bIsParent, TopFolder:sParent, ParentFolder:c.value.containerTarget.getParent().fullName, Groups:lstGroups]
                println new groovy.json.JsonBuilder(lstFolders).toPrettyString()
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
