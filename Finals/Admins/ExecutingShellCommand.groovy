//Script console command to execute somethign directly on the controller

println "Master HostName and IP Address - ${java.net.InetAddress.getLocalHost()}"
def sout = new StringBuilder(), serr = new StringBuilder()
//def proc = 'ls -latrch /sys_apps_01/jenkins2/logs/custom/SAML-Logger.log'.execute()
//def proc = 'grep -A 250 -B 5 275e3950-b65f-4935-8150-02c1feace7d3 /apps_01/webapps/tomcat/logs/jenkins_server0/server.log'.execute() //grep 250 lines after and 5 lines before in the target file
//// ----------- To Use pipe command use this format -----------
//def cmd = 'df -h|grep jenkins'
def cmd = 'top -bc -o +%CPU | head -n 20'
def proc = ['bash', '-c', cmd].execute()
//Clear StringBuilder
sout.setLength(0)
serr.setLength(0)
proc.consumeProcessOutput(sout, serr)
proc.waitForOrKill(3000)
//println "out> $sout err> $serr"
println "$sout"
println " ------- ****************************** --------------- "
println "$serr"