///////////////////////////////////////////////////////////////////////////////////////
//Following Script Console Script adds 'Credential Provisioner' group to the controller
///////////////////////////////////////////////////////////////////////////////////////

import nectar.plugins.rbac.groups.*;

// For the following example the group is modified at root container (locationRoot) 
String groupName = "Credential Provisioner"
String roleName = "Credential Provisioner"
Group grp = new Group(GroupContainerLocator.locate(Jenkins.get()), groupName)

//////////////////////////

Group groupToModify = new Group(GroupContainerLocator.locate(Jenkins.get()), grp.name)
grp.doAddUser('pcfsvc2')
grp.doGrantRole(roleName, 0, Boolean.TRUE)
GroupContainerLocator.locate(Jenkins.get()).addGroup(grp)

////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////
//Following Script Console Script adds 'Credential Provisioner' group to the controller
///Longer version of the same script from above////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////
import jenkins.model.Jenkins;
import nectar.plugins.rbac.groups.*;

println 'Create/Modify existing group at container levels'

// Get location at Root Level 
locationRoot = Jenkins.get()

// For the following example the group is modified at root container (locationRoot) 
String groupName = "Credential Provisioner"
String roleName = "Credential Provisioner"
GroupContainer container = GroupContainerLocator.locate(locationRoot)
Group grp = new Group(container, groupName)
println grp

//////////////////////////

Group groupToModify = new Group(container, grp.name)
grp.doAddUser('pcfsvc2')
grp.doGrantRole(roleName, 0, Boolean.TRUE)
container.addGroup(grp)