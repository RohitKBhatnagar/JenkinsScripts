[jenkins@lstl9jkm8141 config_store]$ cat test_master.sh
host=$(hostname)
echo "${host}"
cp -v /home/jenkins/config_store/cd0_prod_jenkins/"${host}".json /tmp/master.json


controller=($(grep -oP '(?<=jenkinsUrl>)[^<]+' "/sys_apps_01/jenkins_nfs/jenkins.model.JenkinsLocationConfiguration.xml"))
echo "${controller}"
for i in ${!controller[*]}
do
  controllerURL="${controller[$i]}"
  # instead of echo use the value directly
  echo ${controllerURL:7:3}
  name="${controllerURL%%.*}"
  echo ${name}
  #final=[["$name" =~ http:[^\/\//]+ ]]
  final="${name##*/}"
  echo ${final}
done
=============

[jenkins@lstl9jkm8141 config_store]$ cat test_master_new.sh
host=$(hostname)
echo "${host}"
cp -v /home/jenkins/config_store/cd0_prod_jenkins/"${host}".json /tmp/master.json


controller=($(grep -oP '(?<=jenkinsUrl>)[^<]+' "/sys_apps_01/jenkins_nfs/jenkins.model.JenkinsLocationConfiguration.xml"))
echo "${controller}"
name="${controller%%.*}"
#echo ${name}
final="${name##*/}"
echo ${final}
============

[root@lstl9jkm8141 jenkins_nfs]# find . -user root
./nodeMonitors.xml
./support/all_2023-09-14_22.25.32.log
./queue.xml
./plugins/cloudbees-platform-data.jpi
./hudson.model.UpdateCenter.xml
./logs/slaves/lstl2jnk8824
./logs/slaves/lstl2jnk8824/slave.log
./logs/slaves/lstl2jnk8704
./logs/slaves/lstl2jnk8704/slave.log
./logs/slaves/lstl4jnk7396
./logs/slaves/lstl4jnk7396/slave.log
./logs/slaves/wstl4jnk8429
./logs/slaves/lstl2jnk8167
./logs/slaves/lstl2jnk8167/slave.log
./logs/slaves/lstl4jnk7044
./logs/slaves/lstl4jnk7044/slave.log
./logs/slaves/wstl2jnk8430
./logs/slaves/lstl2jnk8245
./logs/slaves/lstl2jnk8245/slave.log
./logs/slaves/wstl2jnk8022
./logs/slaves/wstl2jnk8022/slave.log
./logs/slaves/lstl4jnk8437
./logs/slaves/lstl4jnk8437/slave.log
./logs/slaves/lstl2jnk8414
./logs/slaves/lstl2jnk8414/slave.log
./logs/slaves/wstl4jnk8549
./logs/slaves/lstl4jnk7921
./logs/slaves/lstl4jnk7921/slave.log
./cb-envelope/core-cm-2.332.3.4
./cb-envelope/envelope.json
./failed-boot-attempts.txt
[root@lstl9jkm8141 jenkins_nfs]# chown -R jenkins:jenkins ./logs/
[root@lstl9jkm8141 jenkins_nfs]# chown -R jenkins:jenkins ./nodeMonitors.xml
[root@lstl9jkm8141 jenkins_nfs]# chown -R jenkins:jenkins ./support/
[root@lstl9jkm8141 jenkins_nfs]# chown -R jenkins:jenkins ./plugins/cloudbees-platform-data.jpi
[root@lstl9jkm8141 jenkins_nfs]# chown -R jenkins:jenkins ./hudson.model.UpdateCenter.xml

===========
Install PLan for Jenkins CD0:
 Steps for CD0 controller Upgrade
1. Login to CD0 Controller (Primary and Secondary servers) lstl9jkm8141 and lstl9jkm8781.
2. cd /home/jenkins
3. git clone https://globalrepository.mclocal.int/stash/scm/pipe/config_store.git
4. cd config_store
5. update the sesudo_master.sh with the version 1.4.0 of the converge
6. Run the shell script ./sesudo_master.sh
=========
Backout Plan to Jenkins Downgrade

Steps for CD0 controller Rollback
1. Login to CD0 Controller (Primary and Secondary servers) lstl9jkm8141 and lstl9jkm8781.
2. cd /home/jenkins
3. git clone https://globalrepository.mclocal.int/stash/scm/pipe/config_store.git
4. cd config_store
5. update the sesudo_master.sh with the version 1.3.0 of the converge
6. Run the shell script ./sesudo_master.sh
 

Backout Plan for ESG team to rever the snaspshot 

Steps to revert CD0 NFS snapshot :

Login to Isi0091 device and Navigate to Cluster management > Job operations > Select Job type page > Scroll down till end of the page and select 'SnapRevert' > From left hand side click Start Job > From the message box we have to give the Snapshot ID (need to identify which snapshot we are going to use to restore) to revert > Start Job

 
Monitor the SnapRevert job under the background jobs (Login to Isi0091 device and Navigate to Cluster management > Job operations) to see the status whether it's completed or not.


===========================================


println "ls -latrch /sys_apps_01/tomcat/apache-tomcat-8.5.86/lib".execute().text

/**
println "Master HostName and IP Address - ${java.net.InetAddress.getLocalHost()}"
def sout = new StringBuilder(), serr = new StringBuilder()
def cmd = 'rm -f /sys_apps_01/tomcat/apache-tomcat-8.5.86/lib/javax.mail.jar'
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
**/
=============
println "ls -latrch /sys_apps_01/tomcat/apache-tomcat-8.5.86/lib".execute().text

/**
println "Master HostName and IP Address - ${java.net.InetAddress.getLocalHost()}"
def sout = new StringBuilder(), serr = new StringBuilder()
//def cmd = 'rm -f /sys_apps_01/tomcat/apache-tomcat-8.5.86/lib/javax.mail.jar'
//def cmd = 'rm -f /sys_apps_01/tomcat/apache-tomcat-8.5.86/lib/mc-SAML2.jar'
def cmd = 'cp /sys_apps_01/tomcat/apache-tomcat-8.5.86/lib/javax.mail.jar.remove /sys_apps_01/tomcat/apache-tomcat-8.5.86/lib/javax.mail.jar'
//def cmd = 'cp /sys_apps_01/tomcat/apache-tomcat-8.5.86/lib/mc-SAML2.jar.remove /sys_apps_01/tomcat/apache-tomcat-8.5.86/lib/mc-SAML2.jar'
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
**/