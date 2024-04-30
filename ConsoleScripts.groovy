def strSplunk = "https://hec.splunk.mclocal.int:13510/services/collector/event";
def strToken = "a0ec61b4-cb16-4a62-a159-9c3982d11153";
String strJson = "{\"Script\":\"Hello World\"}";
//String strJson = "{Script:Hello World}";

def jsonLabels = new groovy.json.JsonBuilder()
jsonLabels (
  	"Script":"Hello World"
    )

String sFinal = "curl -f -u \"cd-admin-user:iuy123\$S\" -k ${strSplunk} -H 'Authorization: Splunk ${strToken}' --data '${jsonLabels}'"
println "${sFinal}"
//def out = sh(script: "curl -f --data \"${jsonLabels}\" -k \"${strSplunk}\" -H \"Authorization: Splunk ${strToken}\"", returnStdout: true)
//def out = sh "curl -k \"${strSplunk}\""
//def out = sh sFinal
//println "${out}"

//sh "curl -f --data \"${jsonLabels}\" -k \"${strSplunk}\" -H \"Authorization: Splunk ${strToken}\""

def sout = new StringBuilder(), serr = new StringBuilder();
//def proc = "curl -f -u \"cd-admin-user:iuy123\$S\" -k ${strSplunk} -H 'Authorization: Splunk ${strToken}' --data '${jsonLabels}'".execute()
def proc = "curl -f -k ${strSplunk} -H 'Authorization: Splunk ${strToken}' --data '${jsonLabels}'".execute()
//def proc = "curl -f -g --data \"${sFinal}\" -k \"${strSplunk}\" -H \"Authorization: Splunk ${strToken}\"".execute()
//def proc = "which curl".execute()

proc.consumeProcessOutput(sout, serr)
proc.waitForOrKill(1000) //1 sec
println "out> $sout err> $serr"
