//Run commands on controller
println "Master HostName and IP Address - ${java.net.InetAddress.getLocalHost()}"

//-------------------- Command #1 --------------------
def sout = new StringBuilder(), serr = new StringBuilder()
def proc = 'ls -latrch /sys_apps_01/jenkins_nfs/hdump'.execute()
proc.consumeProcessOutput(sout, serr)
proc.waitForOrKill(1000)
println "$sout"
println " ------- ****************************** --------------- "
println "$serr"