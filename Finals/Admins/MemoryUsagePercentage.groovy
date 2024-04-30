//Memory % used by top 10 process and sorted in asc order
println "Master HostName and IP Address - ${java.net.InetAddress.getLocalHost()}"
def sout = new StringBuilder(), serr = new StringBuilder()
//def cmd = 'ps -eo user,pid,%mem,cmd --sort=-%mem | head -n 10'
def cmd = 'ps -eo size,pid,user,command --sort -size | head -n 10 | awk \'{ hr=$1/1024 ; printf("%13.2f Mb ",hr) } { for ( x=4 ; x<=NF ; x++ ) { printf("%s ",$x) } print "" }\''
def proc = ['bash', '-c', cmd].execute()
proc.consumeProcessOutput(sout, serr)
proc.waitForOrKill(1000)
println "$sout"
println " ------- ****************************** --------------- "
println "$serr"