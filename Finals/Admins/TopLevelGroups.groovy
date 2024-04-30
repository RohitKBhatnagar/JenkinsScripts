/**
Author: kuisathaverat
Description: list groups at top level on Master and CJOC from Master Console Script
**/
import hudson.model.*
import nectar.plugins.rbac.strategy.*;
import nectar.plugins.rbac.groups.*;
import com.cloudbees.opscenter.security.*
  
def rbac = RoleMatrixAuthorizationPlugin.getInstance()
println "---------------------"
println "Config - ${rbac.getConfig()}"
println "---------------------"
println "Configuration - ${rbac.getConfiguration()}"
println "---------------------"
printAllMethods(rbac)
println "---------------------"
def proxy = rbac.getRootProxyGroupContainer()
//Master Groups
proxy.getGroups().each{ println "Groups - ${it.displayName}, Members - ${it.getMembers()}" }
//CJOC groups
//proxy?.getParent().getGroups().each{ println "Parent Groups - ${it.displayName}" }

return null
// Filename: printAllMethodsExample.groovy
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