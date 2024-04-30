import hudson.security.AuthorizationStrategy;
//import com.michelin.cio.hudson.plugins.rolestrategy.RoleBasedAuthorizationStrategy;

AuthorizationStrategy strategy = Jenkins.get().getAuthorizationStrategy();
println ("All Roles - ${strategy.getGroups()}");
println ("All SIDs - ${strategy.getAllSIDs()}");
println ("All Assignable Roles - ${strategy.getAssignableRoles()}");
println ("All Filterable Roles - ${strategy.getFilterableRoles()}");
println ("Local Groups - ${strategy.getLocalGroups()}");
println ("Role ACLs - ${strategy.getACL()}");
printAllMethods(strategy.getACL())
println ("Role Filters - ${strategy.getRoleFilters()}");


/**
class nectar.plugins.rbac.strategy.RoleMatrixAuthorizationStrategyImpl functions:
equals(); getClass(); hashCode(); notify(); notifyAll(); toString(); wait(); getDescriptor(); all(); getACL(); getGroups(); getRootACL(); 
addFilterableRole(); addRole(); addRoleFilter(); deleteRoleFilter(); 
getAllSIDs(); getAssignableRoles(); getFilterableRoles(); getInstance(); getLocalGroups(); getRoleFilters(); getRootGroupsUrl(); 
grantPermissions(); hasExplicitPermission(); hasPermission(); isEnabled(); isImplicitGroup(); 
removeFilterableRole(); removeRole(); revokePermissions(); setLocalGroups(); setRoleFilters(); 
**/

/**
//if (strategy instanceof RoleBasedAuthorizationStrategy) {
  com.michelin.cio.hudson.plugins.rolestrategy.RoleBasedAuthorizationStrategy rbas = (com.michelin.cio.hudson.plugins.rolestrategy.RoleBasedAuthorizationStrategy) strategy;
  RoleMap roleMap = rbas.getRoleMap(roleType);
  printAllMethods(roleMap)
//}
/**/


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
//////////////////////////////////////////***************************
org.codehaus.groovy.runtime.metaclass.MethodSelectionException: Could not find which method getACL() to invoke from this list:
  public hudson.security.ACL nectar.plugins.rbac.strategy.RoleMatrixAuthorizationStrategyImpl#getACL(hudson.model.AbstractItem)
  public hudson.security.ACL hudson.security.AuthorizationStrategy#getACL(hudson.model.AbstractProject)
  public hudson.security.ACL nectar.plugins.rbac.strategy.RoleMatrixAuthorizationStrategyImpl#getACL(hudson.model.Computer)
  public hudson.security.ACL nectar.plugins.rbac.strategy.RoleMatrixAuthorizationStrategyImpl#getACL(hudson.model.Job)
  public hudson.security.ACL nectar.plugins.rbac.strategy.RoleMatrixAuthorizationStrategyImpl#getACL(hudson.model.Node)
  public hudson.security.ACL nectar.plugins.rbac.strategy.RoleMatrixAuthorizationStrategyImpl#getACL(hudson.model.User)
  public hudson.security.ACL nectar.plugins.rbac.strategy.RoleMatrixAuthorizationStrategyImpl#getACL(hudson.model.View)
  public hudson.security.ACL nectar.plugins.rbac.strategy.RoleMatrixAuthorizationStrategyImpl#getACL(hudson.slaves.Cloud)
/////////////////////////////////////////////////////////////////////

import com.michelin.cio.hudson.plugins.rolestrategy.AuthorizationType
import com.michelin.cio.hudson.plugins.rolestrategy.PermissionEntry
import com.michelin.cio.hudson.plugins.rolestrategy.RoleBasedAuthorizationStrategy
import com.michelin.cio.hudson.plugins.rolestrategy.Role
import com.synopsys.arc.jenkins.plugins.rolestrategy.RoleType
import hudson.security.Permission
import jenkins.model.Jenkins


Jenkins jenkins = Jenkins.get()
def rbas = new RoleBasedAuthorizationStrategy()

/* create admin role */
Set<Permission> permissions = new HashSet<>();
permissions.add(Jenkins.ADMINISTER)
Role adminRole = new Role("admin", permissions)

globalRoleMap = rbas.getRoleMap(RoleType.Global)
globalRoleMap.addRole(adminRole)
/* assign admin role to user 'admin' */
globalRoleMap.assignRole(adminRole, new PermissionEntry(AuthorizationType.USER, 'admin'))
/* assign admin role to group 'administrators' */
globalRoleMap.assignRole(adminRole, new PermissionEntry(AuthorizationType.GROUP, 'administrators'))
jenkins.setAuthorizationStrategy(rbas)

jenkins.save()



////////////////////////////////
https://raw.githubusercontent.com/cloudbees/jenkins-scripts/master/RBAC_Example.groovy

//////////////////=======================================


import jenkins.model.Jenkins;
import nectar.plugins.rbac.strategy.*;
import hudson.security.*;
import nectar.plugins.rbac.groups.*;
import nectar.plugins.rbac.roles.*;

//Obtain security configuration
RoleMatrixAuthorizationStrategyImpl strategy = RoleMatrixAuthorizationStrategyImpl.getInstance()
RoleMatrixAuthorizationConfig config = RoleMatrixAuthorizationPlugin.getConfig()

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