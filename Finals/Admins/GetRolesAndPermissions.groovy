import jenkins.model.Jenkins;
import nectar.plugins.rbac.strategy.*;
import hudson.security.*;
import nectar.plugins.rbac.groups.*;
import nectar.plugins.rbac.roles.*;

//Obtain security configuration
RoleMatrixAuthorizationStrategyImpl strategy = RoleMatrixAuthorizationStrategyImpl.getInstance()
RoleMatrixAuthorizationConfig config = RoleMatrixAuthorizationPlugin.getConfig()

/*****
println 'Groups'
config.getGroups().each{ g ->
    println '\t' + g.name
    println '\t\t Group Roles'
    g.getAllRoles().each{rg -> println '\t\t\t' + rg}
    
    println '\t\t Group User Members'
    g.getUsers().each{mg -> println '\t\t\t' + mg}
    println '\t\t Group Group Members'
    g.getGroups().each{mg -> println '\t\t\t' + mg}
    println '\t\t Group Ambiguous Members'
    g.getMembers().each{mg -> println '\t\t\t' + mg}
}
***/
println '*Roles*'
config.getRoles().each{r ->
    println '\t' + r
    //println '\t\t Role Permissions'
    Role rc = new Role(r)
    rc.getPermissionProxies().each{p -> println '\t\t' + p.id + " - " + p.name}
  	//rc.getPermissionProxies().each{p -> println '\t\t' + p.name}
    }

println '*Permissions*'
Permission.getAll().each{p -> println '\t' + p.id + " - " + p.name}

return null;