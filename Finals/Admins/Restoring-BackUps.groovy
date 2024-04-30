Approved for Jenkins prod agents on CD0 Controller



//Run multiple commands sequentially say upto 3 commands
println "Master HostName and IP Address - ${java.net.InetAddress.getLocalHost()}"

//-------------------- Command #1 --------------------
def sout = new StringBuilder(), serr = new StringBuilder()
def proc = 'ls -latrch /sys_apps_01/jenkins_nfs/jobs/SCSS/jobs/DMP-DevOps/jobs/QE_Integration/jobs/BRMS/jobs'.execute()
proc.consumeProcessOutput(sout, serr)
proc.waitForOrKill(1000)
println "$sout"
println " ------- ****************************** --------------- "
println "$serr"

sout.setLength(0)
serr.setLength(0)
//-------------------- Command #2 --------------------
proc = 'ls -latrch /sys_apps_01/jenkins_nfs/.snapshot/stl_nztools_daily_2022-05-18_00:45/jobs/SCSS/jobs/DMP-DevOps/jobs/QE_Integration/jobs/BRMS/jobs'.execute()
proc.consumeProcessOutput(sout, serr)
proc.waitForOrKill(1000)
println "$sout"
println " ------- ****************************** --------------- "
println "$serr"

sout.setLength(0)
serr.setLength(0)
//--------------------- Command #3 --------------------
def cmd = 'ls -latrch /sys_apps_01/jenkins_nfs/.snapshot/stl_nztools_daily_2022-05-18_00:45/jobs/SCSS/jobs/DMP-DevOps/jobs/QE_Integration/jobs/BRMS/jobs/POC/*'
proc = ['bash', '-c', cmd].execute()

proc.consumeProcessOutput(sout, serr)
proc.waitForOrKill(1000)
println "$sout"
println " ------- ****************************** --------------- "
println "$serr"

sout.setLength(0)
serr.setLength(0)

/*
//--------------------- Command #4 --------------------
cmd = ""
cmd = 'rsync -av /sys_apps_01/jenkins_nfs/.snapshot/stl_nztools_daily_2022-05-18_00:45/jobs/SCSS/jobs/DMP-DevOps/jobs/QE_Integration/jobs/BRMS/jobs/POC /sys_apps_01/jenkins_nfs/jobs/SCSS/jobs/DMP-DevOps/jobs/QE_Integration/jobs/BRMS/jobs'
proc = ['bash', '-c', cmd].execute()

proc.consumeProcessOutput(sout, serr)
proc.waitForOrKill(10000)
println "$sout"
println " ------- ****************************** --------------- "
println "$serr"

sout.setLength(0)
serr.setLength(0)

*/