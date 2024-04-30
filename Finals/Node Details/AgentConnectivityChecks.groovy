/*pwd
curl -v -m 30 -o 'sidekick-dev.v2.js' 'https://stage.asset.connect.mastercard.com/community/sidekick.v2.js'
ls -lah
curl -v -m 30 -o 'sidekick-stage.v1.js' 'https://stage.asset.connect.mastercard.com/community/sidekick.v1.js'
ls -lah
echo "Check chromium path and version"
which chromium-browser
chromium-browser --version
curl -v --connect-timeout 10 'https://creditor-self-service.apps.stl.bnw-dev-pas.mastercard.int/bsweb/'
curl -v --connect-timeout 10 'https://itf.mps.connect.mastercard.com/bsweb'
curl -v --connect-timeout 10 'https://creditor-self-service-stage-perf-stl.apps.stl.pcfstage00.mastercard.int/bsweb/'
*/

//////////////////////////////////////////////////////////////////////////////////////////////////////////////

println "HostName and IP Address - ${java.net.InetAddress.getLocalHost()}"
def sout = new StringBuilder(), serr = new StringBuilder()
println "=============================================================================================="
println "Checking connectivity to - https://stage.asset.connect.mastercard.com/community/sidekick.v2.js"
println "=============================================================================================="
def cmd = "curl -v -m 30 -o 'sidekick-dev.v2.js' 'https://stage.asset.connect.mastercard.com/community/sidekick.v2.js'"
def proc = ['bash', '-c', cmd].execute()
//Clear StringBuilder
sout.setLength(0)
serr.setLength(0)
proc.consumeProcessOutput(sout, serr)
proc.waitForOrKill(1000)
//println "out> $sout err> $serr"
println "$sout"
println " ------- ****************************** --------------- "
println "$serr"

/////////////////////////
sout = new StringBuilder()
serr = new StringBuilder()
println "=============================================================================================="
println "Checking connectivity to - https://stage.asset.connect.mastercard.com/community/sidekick.v1.js"
println "=============================================================================================="
proc = "curl -v -m 30 -o 'sidekick-stage.v1.js' 'https://stage.asset.connect.mastercard.com/community/sidekick.v1.js'".execute()
//proc = "curl -v -m 30 -o 'sidekick-stage.v1.js' 'http://stage.asset.connect.mastercard.com/community/sidekick.v1.js'".execute()
proc.consumeProcessOutput(sout, serr)
proc.waitForOrKill(1000)
//println "out> $sout err> $serr"
println "$sout"
println " ------- ****************************** --------------- "
println "$serr"

/////////////////////////
sout = new StringBuilder()
serr = new StringBuilder()
println "=============================================================================================="
println "Checking CHROMIUM path "
println "=============================================================================================="
proc = "which chromium-browser".execute()
proc.consumeProcessOutput(sout, serr)
proc.waitForOrKill(1000)
//println "out> $sout err> $serr"
println "$sout"
println " ------- ****************************** --------------- "
println "$serr"

//////////////////////////////////////////////////////////////////////
sout = new StringBuilder()
serr = new StringBuilder()
println "=============================================================================================="
println "Checking CHROMIUM "
println "=============================================================================================="
proc = "which chromium-browser".execute()
proc.consumeProcessOutput(sout, serr)
proc.waitForOrKill(1000)
//println "out> $sout err> $serr"
println "$sout"
println " ------- ****************************** --------------- "
println "$serr"

/////////////////////////
sout = new StringBuilder()
serr = new StringBuilder()
println "==================================================================================================="
println "Checking connectivity to - https://creditor-self-service.apps.stl.bnw-dev-pas.mastercard.int/bsweb/"
println "==================================================================================================="
proc = "curl -v --connect-timeout 10 'https://creditor-self-service.apps.stl.bnw-dev-pas.mastercard.int/bsweb/'".execute()
//proc = "curl -v --connect-timeout 10 'http://creditor-self-service.apps.stl.bnw-dev-pas.mastercard.int/bsweb/'".execute()
proc.consumeProcessOutput(sout, serr)
proc.waitForOrKill(1000)
//println "out> $sout err> $serr"
println "$sout"
println " ------- ****************************** --------------- "
println "$serr"

/////////////////////////
sout = new StringBuilder()
serr = new StringBuilder()
println "==================================================================================================="
println "Checking connectivity to - https://itf.mps.connect.mastercard.com/bsweb"
println "==================================================================================================="
proc = "curl -v --connect-timeout 10 'https://itf.mps.connect.mastercard.com/bsweb'".execute()
//proc = "curl -v --connect-timeout 10 'http://itf.mps.connect.mastercard.com/bsweb'".execute()
proc.consumeProcessOutput(sout, serr)
proc.waitForOrKill(1000)
//println "out> $sout err> $serr"
println "$sout"
println " ------- ****************************** --------------- "
println "$serr"

/////////////////////////
sout = new StringBuilder()
serr = new StringBuilder()
println "================================================================================================================="
println "Checking connectivity to - https://creditor-self-service-stage-perf-stl.apps.stl.pcfstage00.mastercard.int/bsweb/"
println "================================================================================================================="
proc = "curl -v --connect-timeout 10 'https://creditor-self-service-stage-perf-stl.apps.stl.pcfstage00.mastercard.int/bsweb/'".execute()
//proc = "curl -v --connect-timeout 10 'http://creditor-self-service-stage-perf-stl.apps.stl.pcfstage00.mastercard.int/bsweb/'".execute()
proc.consumeProcessOutput(sout, serr)
proc.waitForOrKill(1000)
//println "out> $sout err> $serr"
println "$sout"
println " ------- ****************************** --------------- "
println "$serr"

