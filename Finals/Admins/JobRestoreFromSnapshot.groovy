println "----------------- Dec 15 Snapshot ---------------"
println "ls -latrch /sys_apps_01/jenkins_nfs/.snapshot/stl_nztools_daily_2022-12-15_00:45/jobs/ChefHabitat/jobs/mpms/jobs/PROD-SEGMENT/jobs/PROD-STL/jobs/ESB/jobs/DEPLOY/".execute().text
println "ls -latrch /sys_apps_01/jenkins_nfs/.snapshot/stl_nztools_daily_2022-12-15_00:45/jobs/ChefHabitat/jobs/mpms/jobs/PROD-SEGMENT/jobs/PROD-STL/jobs/ESB/jobs/DEPLOY/jobs".execute().text
println "ls -latrch /sys_apps_01/jenkins_nfs/.snapshot/stl_nztools_daily_2022-12-15_00:45/jobs/ChefHabitat/jobs/mpms/jobs/PROD-SEGMENT/jobs/PROD-STL/jobs/ESB/jobs/DEPLOY/jobs/ACCESS-ESB-1-PROMOTE-SPECIFIC".execute().text
println "cat /sys_apps_01/jenkins_nfs/.snapshot/stl_nztools_daily_2022-12-15_00:45/jobs/ChefHabitat/jobs/mpms/jobs/PROD-SEGMENT/jobs/PROD-STL/jobs/ESB/jobs/DEPLOY/config.xml".execute().text

println "----------------- Jan 02 - Direct Folder Path  ---------------"
println "ls -latrch /sys_apps_01/jenkins_nfs/jobs/ChefHabitat/jobs/mpms/jobs/PROD-SEGMENT/jobs/PROD-STL/jobs/ESB/jobs/DEPLOY/".execute().text
println "ls -latrch /sys_apps_01/jenkins_nfs/jobs/ChefHabitat/jobs/mpms/jobs/PROD-SEGMENT/jobs/PROD-STL/jobs/ESB/jobs/DEPLOY/jobs".execute().text

println "----------------- RSYNC for job restoration ----------"

def sout = new StringBuilder(), serr = new StringBuilder()
cmd = ""
cmd = 'rsync -av /sys_apps_01/jenkins_nfs/.snapshot/stl_nztools_daily_2022-12-15_00:45/jobs/ChefHabitat/jobs/mpms/jobs/PROD-SEGMENT/jobs/PROD-STL/jobs/ESB/jobs/DEPLOY/jobs /sys_apps_01/jenkins_nfs/jobs/ChefHabitat/jobs/mpms/jobs/PROD-SEGMENT/jobs/PROD-STL/jobs/ESB/jobs/DEPLOY/jobs'
proc = ['bash', '-c', cmd].execute()

proc.consumeProcessOutput(sout, serr)
proc.waitForOrKill(10000)
println "$sout"
println " ------- ****************************** --------------- "
println "$serr"




*********************************************************

println "----------------- Dec 15 Snapshot ---------------"
println "ls -latrch /sys_apps_01/jenkins_nfs/.snapshot/stl_nztools_daily_2022-12-15_00:45/jobs/ChefHabitat/jobs/mpms/jobs/PROD-SEGMENT/jobs/PROD-KSC/jobs/ESB/jobs/DEPLOY/".execute().text
println "ls -latrch /sys_apps_01/jenkins_nfs/.snapshot/stl_nztools_daily_2022-12-15_00:45/jobs/ChefHabitat/jobs/mpms/jobs/PROD-SEGMENT/jobs/PROD-KSC/jobs/ESB/jobs/DEPLOY/jobs".execute().text
//println "ls -latrch /sys_apps_01/jenkins_nfs/.snapshot/stl_nztools_daily_2022-12-15_00:45/jobs/ChefHabitat/jobs/mpms/jobs/PROD-SEGMENT/jobs/PROD-STL/jobs/ESB/jobs/ROLLBACK/jobs/ACCESS-ESB-1-PROMOTE-SPECIFIC".execute().text
//println "cat /sys_apps_01/jenkins_nfs/.snapshot/stl_nztools_daily_2022-12-15_00:45/jobs/ChefHabitat/jobs/mpms/jobs/PROD-SEGMENT/jobs/PROD-STL/jobs/ESB/jobs/DEPLOY/config.xml".execute().text

println "----------------- Jan 02 - Direct Folder Path  ---------------"
println "ls -latrch /sys_apps_01/jenkins_nfs/jobs/ChefHabitat/jobs/mpms/jobs/PROD-SEGMENT/jobs/PROD-KSC/jobs/ESB/jobs/DEPLOY/".execute().text
println "ls -latrch /sys_apps_01/jenkins_nfs/jobs/ChefHabitat/jobs/mpms/jobs/PROD-SEGMENT/jobs/PROD-KSC/jobs/ESB/jobs/DEPLOY/jobs".execute().text

/**/
println "----------------- RSYNC for job restoration ----------"

def sout = new StringBuilder(), serr = new StringBuilder()
cmd = ""
cmd = 'rsync -av /sys_apps_01/jenkins_nfs/.snapshot/stl_nztools_daily_2022-12-15_00:45/jobs/ChefHabitat/jobs/mpms/jobs/PROD-SEGMENT/jobs/PROD-KSC/jobs/ESB/jobs/DEPLOY/jobs /sys_apps_01/jenkins_nfs/jobs/ChefHabitat/jobs/mpms/jobs/PROD-SEGMENT/jobs/PROD-KSC/jobs/ESB/jobs/DEPLOY/'
proc = ['bash', '-c', cmd].execute()

proc.consumeProcessOutput(sout, serr)
proc.waitForOrKill(10000)
println "$sout"
println " ------- ****************************** --------------- "
println "$serr"
/**/

=========================
println "ls -latrch /sys_apps_01/jenkins_nfs/jobs/ChefHabitat/jobs/mpms/jobs/PROD-SEGMENT/jobs/PROD-STL/jobs/ESB/jobs/ROLLBACK/".execute().text
println "ls -latrch /sys_apps_01/jenkins_nfs/jobs/ChefHabitat/jobs/mpms/jobs/PROD-SEGMENT/jobs/PROD-STL/jobs/ESB/jobs/ROLLBACK/jobs".execute().text
//println "ls -latrch /sys_apps_01/jenkins_nfs/jobs/ChefHabitat/jobs/mpms/jobs/PROD-SEGMENT/jobs/PROD-STL/jobs/ESB/jobs/ROLLBACK/jobs/jobs".execute().text

/*
println "----------------- Job Deletion ----------"

def sout = new StringBuilder(), serr = new StringBuilder()
cmd = ""
cmd = 'rm -R -f /sys_apps_01/jenkins_nfs/jobs/ChefHabitat/jobs/mpms/jobs/PROD-SEGMENT/jobs/PROD-STL/jobs/ESB/jobs/DEPLOY/jobs'
proc = ['bash', '-c', cmd].execute()

proc.consumeProcessOutput(sout, serr)
proc.waitForOrKill(10000)
println "$sout"
println " ------- ****************************** --------------- "
println "$serr"
*/

=================================

//// Top Command /////////////
println "top -bcn1 -w 512 |grep rsync".execute().text

def sout = new StringBuilder(), serr = new StringBuilder()
def cmd = "top -bcn1 -w 512 |grep rsync"

proc = ['bash', '-c', cmd].execute()

proc.waitForProcessOutput(sout, serr)
//proc.consumeProcessOutput(sout, serr)
//proc.waitForOrKill(90000)
println "$sout"
println " ------- ****************************** --------------- "
println "$serr"