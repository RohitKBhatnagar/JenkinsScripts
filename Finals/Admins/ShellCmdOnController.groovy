#!groovy

//Run multiple commands sequentially say upto 3 commands
println "Master HostName and IP Address - ${java.net.InetAddress.getLocalHost()}"

//-------------------- Command #1 --------------------
def sout = new StringBuilder(), serr = new StringBuilder()
def proc = 'ls -latrch /sys_apps_01/jenkins_nfs/'.execute()
proc.consumeProcessOutput(sout, serr)
proc.waitForOrKill(1000)
//proc.waitForProcessOutput(sout, serr);
println "$sout"
println " ------- ****************************** --------------- "
println "$serr"

sout.setLength(0)
serr.setLength(0)

//--------------------- Command #2 --------------------
def cmd = 'dmesg -T|grep java'
proc = ['bash', '-c', cmd].execute()

proc.consumeProcessOutput(sout, serr)
proc.waitForOrKill(1000)
println "$sout"
println " ------- ****************************** --------------- "
println "$serr"

sout.setLength(0)
serr.setLength(0)



//////////////////////////////////////////////////////////
//Run multiple commands sequentially say upto 3 commands
println "Master HostName and IP Address - ${java.net.InetAddress.getLocalHost()}"

//-------------------- Command #1 --------------------
def sout = new StringBuilder(), serr = new StringBuilder()
def cmd = 'git config --list --show-origin'
proc = ['bash', '-c', cmd].execute()

proc.consumeProcessOutput(sout, serr)
proc.waitForOrKill(5000)
println "$sout"
println " ------- ****************************** --------------- "
println "$serr"

sout.setLength(0)
serr.setLength(0)