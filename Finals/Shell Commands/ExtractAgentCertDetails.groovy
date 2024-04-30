//This command may be executed directly from the linux bash shell on the target Linux agent
openssl s_client -showcerts -servername globalrepository.mclocal.int -connect globalrepository.mclocal.int:443

//Another approach navigate to the target URL and using IE or Chrome open the certificates directly on the browser to check validity

//// Following scripts may be executed from script console of the target Linux agent from the controller directly

//Print all certificates defined in the cacerts for the target linux agent
println "keytool -list -v -keystore /etc/pki/ca-trust/extracted/java/cacerts -storepass changeit".execute().text

//Prints all expiry dates for cacerts in Linux agent
def sout = new StringBuilder(), serr = new StringBuilder()
def cmd = 'keytool -list -v -keystore /etc/pki/ca-trust/extracted/java/cacerts -storepass changeit |grep \"Valid from:\"'
def proc = ['bash', '-c', cmd].execute()

proc.consumeProcessOutput(sout, serr)
proc.waitForOrKill(1000)
println "$sout"
println " ------- ****************************** --------------- "
println "$serr"

//Prints alias name along with cert validity for linux agents
def sout = new StringBuilder(), serr = new StringBuilder()
def cmd = 'keytool -list -v -keystore /etc/pki/ca-trust/extracted/java/cacerts -storepass changeit |grep -e \"Alias name:\" -e \"Valid from:\"'
def proc = ['bash', '-c', cmd].execute()

proc.consumeProcessOutput(sout, serr)
proc.waitForOrKill(1000)
println "$sout"
println " ------- ****************************** --------------- "
println "$serr"