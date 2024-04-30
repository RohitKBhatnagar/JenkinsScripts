def sout = new StringBuilder(), serr = new StringBuilder()
//def proc = 'ls -latrch /sys_apps_01/jenkins2/init.groovy.d/create_admin.groovy'.execute() //-rwx------ 1 jenkins jenkins 416 Aug 31 10:01 /sys_apps_01/jenkins2/init.groovy.d/create_admin.groovy
def proc = 'cat /sys_apps_01/jenkins2/init.groovy.d/create_admin.groovy'.execute() 
proc.consumeProcessOutput(sout, serr)
proc.waitForOrKill(1000)
//println "out> $sout err> $serr"
println "$sout"
println " ------- ****************************** --------------- "
println "$serr"




Result
=======
import jenkins.model.*
import hudson.security.*

def instance = Jenkins.getInstance()

def hudsonRealm = new HudsonPrivateSecurityRealm(false)
hudsonRealm.createAccount("cd-admin-user","iuy123$S")
instance.setSecurityRealm(hudsonRealm)

def strategy = new hudson.security.FullControlOnceLoggedInAuthorizationStrategy()
strategy.setAllowAnonymousRead(true)
instance.setAuthorizationStrategy(strategy)

instance.save()

 ------- ****************************** --------------- 
