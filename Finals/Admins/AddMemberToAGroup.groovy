/***
 * The script creates a new group and adds a member to the group based upon existing role name 
 * Please note this script doesn't modifies any existing role permissions ********************
 * Author - Rohit K Bhatnagar
 * ***************/

import jenkins.model.Jenkins;
import nectar.plugins.rbac.strategy.*;
import hudson.security.*;
import nectar.plugins.rbac.groups.*;
import nectar.plugins.rbac.roles.*;

//Obtain security configuration
RoleMatrixAuthorizationStrategyImpl strategy = RoleMatrixAuthorizationStrategyImpl.getInstance()
RoleMatrixAuthorizationConfig config = RoleMatrixAuthorizationPlugin.getConfig()

println "^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^"
printAllMethods(config)
println "^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^"

println 'Modify existing group at container levels'

// Get location for ClientMaster
locationCM = Jenkins.get().getAllItems().find{it.name.equals("ClientMaster")}
// Get location at Root Level 
locationRoot = Jenkins.get()

// For the following example the group is modified at root container (locationRoot) 
String groupName = "Credential Provisioner"
String roleName = "Credential Provisioner"
GroupContainer container = GroupContainerLocator.locate(locationRoot)
Group grp = new Group(container, groupName)
println grp

println '\t' + grp.name
println "\t\t ${grp.name} - Roles"
grp.getAllRoles().each{rg -> println '\t\t\t' + rg}

// RBAC Plugin < 5.66
 println '\t\t Group Members'
 grp.getMembers().each{mg -> println '\t\t\t' + mg}

// RBAC Plugin >= 5.66
println "\t\t ${grp.name} - User Members"
grp.getUsers().each{mg -> println '\t\t\t' + mg}
println "\t\t ${grp.name} - Group Members"
grp.getGroups().each{mg -> println '\t\t\t' + mg}
println "\t\t ${grp.name} - Ambiguous Members"
grp.getMembers().each{mg -> println '\t\t\t' + mg}


//////////////////////////

Group groupToModify = new Group(container, grp.name)
// RBAC Plugin < 5.66
//group.doAddMember('userToDelete')
//group.doRemoveMember('userToDelete')
//group.doAddMember(groupToDelete.name)
//group.doRemoveMember(groupToDelete.name)
// RBAC Plugin >= 5.66
grp.doAddUser('pcfsvc2')
//group.doRemoveUser('userToDelete')
//grp.doAddGroup(grp.name)
/**group.doRemoveGroup(groupToDelete.name)
group.doAddMember('ambiguousMember')
group.doRemoveMember('ambiguousMember')
group.doGrantRole('roleToRevoke', 0, Boolean.TRUE)
group.doRevokeRole('roleToRevoke')
**/
grp.doGrantRole(roleName, 0, Boolean.TRUE)
container.addGroup(grp)

println "=============================================="
/** Code below is to display all groups, roles and members at root level **/
println 'Groups'
config.getGroups().each{ g ->
    println '\t' + g.name
    println '\t\t Group Roles'
    g.getAllRoles().each{rg -> println '\t\t\t' + rg}
    
    // RBAC Plugin < 5.66
    // println '\t\t Group Members'
    // g.getMembers().each{mg -> println '\t\t\t' + mg}
    
    // RBAC Plugin >= 5.66
    println '\t\t Group User Members'
    g.getUsers().each{mg -> println '\t\t\t' + mg}
    println '\t\t Group Group Members'
    g.getGroups().each{mg -> println '\t\t\t' + mg}
    println '\t\t Group Ambiguous Members'
    g.getMembers().each{mg -> println '\t\t\t' + mg}
}

println '*Roles*'
config.getRoles().each{r ->
    println '\t' + r
    println '\t\t Role Permissions'
    Role rc = new Role(r)
    rc.getPermissionProxies().each{p -> println '\t\t' + p.id + " - " + p.name}
    }

println '*Permissions*'
Permission.getAll().each{p -> println '\t' + p.id + " - " + p.name}

return null
/* **/

void printAllMethods( obj ){
    if( !obj ){
    println( "Object is null\r\n" );
    return;
    }
  if( !obj.metaClass && obj.getClass() ){
        printAllMethods( obj.getClass() );
    return;
    }
  def str = "class ${obj.getClass().name} functions:\r\n";
  obj.metaClass.methods.name.unique().each{ 
    str += it+"(); "; 
  }
  println "${str}\r\n";
}
