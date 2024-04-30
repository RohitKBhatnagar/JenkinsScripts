/**/
println "----------------- RSYNC for job restoration ----------"

def sout = new StringBuilder(), serr = new StringBuilder()
def cmd = "rsync -avm --include='config.xml' --include='nectar-rbac.xml' --include='*/' --exclude='*' /sys_apps_01/jenkins_nfs/.snapshot/stl_nztools_daily_2022-12-30_00:45/jobs/SimplifyCommerceBizOps/jobs /sys_apps_01/jenkins_nfs/jobs/SimplifyCommerceBizOps"

//cmd = 'rsync -av /sys_apps_01/jenkins_nfs/.snapshot/stl_nztools_daily_2023-01-04_00:45/jobs/AlbertaOps/jobs/SRC-SUPPORT-APPS/jobs/UTILS/config.xml /sys_apps_01/jenkins_nfs/jobs/AlbertaOps/jobs/SRC-SUPPORT-APPS/jobs/UTILS'
proc = ['bash', '-c', cmd].execute()

proc.waitForProcessOutput(sout, serr)
//proc.consumeProcessOutput(sout, serr)
//proc.waitForOrKill(90000)
println "$sout"
println " ------- ****************************** --------------- "
println "$serr"
/**/
