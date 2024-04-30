#!groovy
import java.io.*
import javax.mail.*
import javax.mail.internet.*
import com.cloudbees.plugins.credentials.Credentials
import com.cloudbees.plugins.credentials.CredentialsProvider

errormessage = ""
currentBuild = ""
String gitRepo = "https://globalrepository.mclocal.int/stash/scm/pipe/jenkins_utility_scripts.git"
String scriptPath = '/home/jenkins/test'
try {
  def file_path = '/home/jenkins/test'
  def folder = new File("${file_path}")
  def process = ""
  def processoutput = new StringBuilder(), processerror = new StringBuilder()
  
  if (folder.exists()) {
    // if folder already exists it will remove
    process = "rm -rf ${file_path}".execute()
    process.consumeProcessOutput(processoutput, processerror)
    process.waitForOrKill(1000)
    println "out> '${processoutput}' err> '${processerror}'"
  }
  def cmdArray = ["sh /home/jenkins/test/shell/create_backup_nfs.sh ${JENKINS_HOME_SHELL} ${BACKUP_DIR}", "sh /home/jenkins/test/shell/upload_backup.sh ${BACKUP_DIR}"]  

  // Fetch Bitbucket credentials
  Map bbCredMap = jjcGetBBCredentials()
  if (bbCredMap == null) {
    println("failed to get credential from jenkins")
    return
  }
  
  URL url = new URL(gitRepo)
  urlWithCreds = url.getProtocol() + "://" + bbCredMap['username'] + ":" + '\'' + bbCredMap['password'] + '\'' + "@" + url.getAuthority() + url.getPath()
    
  println(urlWithCreds)
  
  //println "mkdir ./test".execute().text
  println "ls -al /home/jenkins/test".execute().text
  cmd = "git clone --branch master ${gitRepo} /home/jenkins/test"
  resp = jjsExec(cmd)
  println "Responce of Git Clone operation is : ${resp}"
  println "ls -al /home/jenkins/test".execute().text
  
  //println "git clone https://globalrepository.mclocal.int/stash/scm/pipe/jenkins_utility_scripts.git /home/jenkins/test".execute().text
  
  for (i=0; i<cmdArray.size() ; i++) { 
    process = ""
    processoutput = new StringBuilder()
    processerror = new StringBuilder()
  
    process = cmdArray[i].execute()
    process.consumeProcessOutput(processoutput, processerror)
    process.waitForOrKill(700000)
    println " hangs out> '${processoutput}' hangs err> '${processerror}'" 
  }
  def date = new Date()

  def dateFormat = date.format("dd-MM-yyyy")
  def file = new File("/home/jenkins/uploadbackupnfs-${dateFormat}.log")
  file.write("out> '${processoutput}' err> '${processerror}'")
  currentBuild = "SUCCESS"
}
catch(e) {
  currentBuild = "FAILURE"
  errormessage = e.getMessage()
  throw e
}
finally {
  def currentResult = currentBuild
  if (currentResult == 'FAILURE') {
    mailmessage = errormessage
    sendMail('mailhost.mclocal.int', 'jenkins@mastercard.com', "Builder_tools_prod_support@mastercard.com", "Master NFS backup job failed", mailmessage)
  }
}

def sendMail(host, sender, receivers, subject, text) {
  Properties props = System.getProperties()
  props.put("mail.smtp.host", host)
  Session session = Session.getDefaultInstance(props, null)
  MimeMessage message = new MimeMessage(session)
  message.setFrom(new InternetAddress(sender))
  receivers.split(',').each {
    message.addRecipient(Message.RecipientType.TO, new InternetAddress(it))
  }
  message.setSubject(subject)
  message.setText(text)
  println 'Sending mail to ' + receivers + '.'
  Transport.send(message)
}


// *** Functions to Fetch Credentials *** //

def jjcGetBBCredentials() {
    // Bitbucket credentials
    return(jjcGetCredentialMap(jjcGetCredentialPasswordByController()))
}

def String jjcGetController() {
    def controllers = [jkm4stl0: "stage.cd", jkm4stl2: "stage.cd",
                       jkm4stl1: "stage.cd2",
                       jkm2stl0: "cd", jkm2stl1: "cd",
                       jkm9stl2: "cd2", jkm9stl3: "cd2",
                       jkm9stl4: "cd3", jkm9stl5: "cd3",
                       jkm9stl6: "cd4", jkm9stl7: "cd4",
                       jkm9stl8: "cd5", jkm9stl9: "cd5",
                       lstl9jkm8141: "cd0", lstl9jkm8781: "cd0",
                       lstl9jkm8720: "cd6", lstl9jkm8723: "cd6",
                       lstl9jkm8067: "cd7", lstl9jkm8271: "cd7",
                       lstl9jkm8282: "cd8", lstl9jkm8124: "cd8",
                       lstl9jkm8923: "cd9", lstl9jkm8739: "cd9",
                       lstl9jkm8993: "cd10", lstl9jkm8883: "cd10",
                       jkm9bel1: "bel.cd1", jkm9bel2: "bel.cd1",
                       jkm9bel3: "bel.cd2", jkm9bel4: "bel.cd2",
                       jkm9bel5: "bel.cd3", jkm9bel6: "bel.cd3"]
    return (controllers[jjcGetControllerHostName()])
}

def String jjcGetControllerHostName() {
    def jenkins = Jenkins.getInstance()
    host = jenkins.getComputer('').getHostName()
    return (host.tokenize(".")[0])
}

def String jjcGetCredentialPasswordByController() {
    String controller = jjcGetController()
    controller = controller.toUpperCase()
    if (controller.startsWith("CD")) {
        credId = "Jenkins-" + controller + "-BB"
    } else {
        if (controller.startsWith("BEL")) {
            credId = "Jenkins-BB-ID"
        } else {
            if (controller.startsWith("STAGE")) {
                credId = "Jenkins-CD-BB"
            }
        }
    }
    return (credId)
}

def jjcGetCredentialMap(String credentialId) {
    Map bbCredentialMap = [:]
    bbCredentialMap['credentialId'] = credentialId
    Set<Credentials> allCredentials = new HashSet<Credentials>();

    Jenkins.instance.getAllItems(com.cloudbees.hudson.plugins.folder.Folder.class).each { f ->
        creds = com.cloudbees.plugins.credentials.CredentialsProvider.lookupCredentials(
                com.cloudbees.plugins.credentials.Credentials.class, f)
        allCredentials.addAll(creds)

    }
    for (c in allCredentials) {
        if (c.id == credentialId) {
            //println(c.id + ": " + c.description)
            if (c instanceof com.cloudbees.plugins.credentials.common.StandardUsernamePasswordCredentials) {
                bbCredentialMap['username'] = c.username
                bbCredentialMap['password'] = c.getPassword()
                bbCredentialMap['description'] = c.description
                return(bbCredentialMap)
            }
        }
    }
    return null
}

def String[] jjsExec(java.lang.String cmd) {
    List<String> response = new ArrayList<>()
    response.add(0)
    //println("cmd=" + cmd)
    def process = new ProcessBuilder(["sh", "-c", cmd])
            .redirectErrorStream(true)
            .start()
    process.outputStream.close()
    process.inputStream.eachLine {
        //println it
        response.add(it)
    }
    process.waitFor();
    response[0] = process.exitValue()

    return response
}
