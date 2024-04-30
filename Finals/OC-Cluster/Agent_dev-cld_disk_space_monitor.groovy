
import hudson.util.RemotingDiagnostics
import jenkins.model.Jenkins

groovy_script = '''
import javax.mail.*
import javax.mail.internet.*

def sout = new StringBuilder(), sout1 = new StringBuilder(),serr = new StringBuilder(), hostname = new StringBuilder(), err = new StringBuilder(), disk_fs = new StringBuilder(), derr = new StringBuilder(), serr1 = new StringBuilder()
//println "rm -rf jenkins_utility_scripts".execute().text
println "git clone https://globalrepository.mclocal.int/stash/scm/PIPE/jenkins_utility_scripts.git".execute().text  
def proc = 'bash ./jenkins_utility_scripts/shell/docker_cleaner.sh'.execute()
proc.consumeProcessOutput(sout, serr)
proc.waitForOrKill(1000)
//def proc1 = 'println "df -h".execute()'
//proc1.consumeProcessOutput(sout1, serr1)
//println "PROC: $proc1"

def server = 'hostname'.execute()
server.consumeProcessOutput(hostname, err)
println "before df -h"
def disk_space = "df -Ph| awk {print "${5}"}'.execute()
//disk_space.consumeProcessOutput(disk_fs, derr)
disk_space.waitForProcessOutput(disk_fs, derr)
sout1 = disk_fs.split(' ')
usage=sout1[4]
println "current usage is $usage %"

if ( usage > 80) {
    println "Usage is higher than the threshold limit on this node: \\n $hostname"
    sendMail('mailhost.mclocal.int', 'noreply-cicd@mastercard.com', "Builder_tools_alerts@mastercard.com", " Disk Space-Alert:$hostname is more than 80% utilized", "Server:$hostname Usage on this node is $usage%. Please clean the File System on this node. \\n\\n$disk_fs")
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

'''.trim()

for (node in Jenkins.instance.nodes) {
  computer = node.toComputer()
  def isdevcloud = node.getLabelString().contains("DEVCLD-LIN7")
  def ischromium = node.getLabelString().contains("DEVCLD-CHROMIUM")
  if (!computer.online) continue
  if (isdevcloud || ischromium) {
    println ("###############################################\nRunning on " + node.name + "\n###############################################")
    println RemotingDiagnostics.executeGroovy(groovy_script, computer.channel)
    println ("###############################################\nDisk space check completed on " + node.name + "\n###############################################")

  }
}

