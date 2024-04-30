/**
 * Displays Roles and privileges defined behind that role
 * Additionally run this API call from curl or directly from the browser
 * Display All Roles
 * =================
 * cURL Call - curl -g --user "e059704:11b9097620f6d6269512114b94946dd55c" -XGET "https://cd7.mastercard.int/jenkins/roles/api/json?tree=filterableRoles" --ssl-no-revoke
 * Browser API Call - https://cd7.mastercard.int/jenkins/roles/api/json?pretty=true
 * Display privileges granted for the specific Role
 * ================================================
 * cURL Call -  curl -g --user "e059704:11b9097620f6d6269512114b94946dd55c" -XGET "https://cd7.mastercard.int/jenkins/roles/admin/api/json" --ssl-no-revoke
 * Browser API Call - https://cd7.mastercard.int/jenkins/roles/Cookbook-Pipeline-Job-Build/api/json?pretty=true
 *                    https://cd7.mastercard.int/jenkins/roles/Cookbook-Pipeline-Job-Build/api/json?tree=id,shortUrl,description,grantedPermissions
 **/
import jenkins.model.Jenkins;
import nectar.plugins.rbac.strategy.*;
import hudson.security.*;
import nectar.plugins.rbac.groups.*;
import nectar.plugins.rbac.roles.*;

//Obtain security configuration
RoleMatrixAuthorizationStrategyImpl strategy = RoleMatrixAuthorizationStrategyImpl.getInstance()
RoleMatrixAuthorizationConfig config = RoleMatrixAuthorizationPlugin.getConfig()

//println '***************Roles******************'
config.getRoles().each{r ->
    println '\t' + r
    Role rc = new Role(r)
    rc.getPermissionProxies().each{p -> println '\t\t' + r + "," + p.id + "," + p.name}
    }

return null;
