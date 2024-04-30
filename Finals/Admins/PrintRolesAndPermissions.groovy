import jenkins.model.Jenkins;
import nectar.plugins.rbac.strategy.*;
import hudson.security.*;
import nectar.plugins.rbac.groups.*;
import nectar.plugins.rbac.roles.*;

//Obtain security configuration
RoleMatrixAuthorizationStrategyImpl strategy = RoleMatrixAuthorizationStrategyImpl.getInstance()
RoleMatrixAuthorizationConfig config = RoleMatrixAuthorizationPlugin.getConfig()

println ("Roles by permissions ID....") // ${config.getRolesByPermissionIdMap()}")
config.getRolesByPermissionIdMap().each{p -> p.each{v -> println '\t' + v.key + "," + v.value}}
println "+++++++++++++++++++++++++++++++++++++++++"
println ("Roles by permissions....") // - ${config.getRolesByPermissionMap()}")
config.getRolesByPermissionMap().each{p -> p.each{v -> println '\t' + v.key + "," + v.value}}

println '***************Roles******************'
config.getRoles().each{r ->
    println '\t' + r
    //println '\t\t Role Permissions'
    Role rc = new Role(r)
    rc.getPermissionProxies().each{p -> println '\t\t' + r + "," + p.id + "," + p.name}
    }

println '******************Permissions*****************'
Permission.getAll().each{p -> println '\t' + p.id + "," + p.name}

return null;
