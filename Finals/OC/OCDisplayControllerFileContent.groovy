/*** OC Script Console script to - Display Server defined file without the Controller Name but in iterative loop order**/

import com.cloudbees.opscenter.server.clusterops.steps.MasterGroovyClusterOpStep
import com.cloudbees.opscenter.server.model.ConnectedMaster
import hudson.model.StreamBuildListener
import jenkins.model.Jenkins
//import org.jenkinsci.plugins.configfiles.GlobalConfigFiles

def result = '\n'

// Loop over all online Client Masters
Jenkins.instance.getAllItems(ConnectedMaster.class).eachWithIndex{ it, index ->
    if(it.channel) {
        def stream = new ByteArrayOutputStream();
        def listener = new StreamBuildListener(stream);
        // Execute remote Groovy script in the Client Master
        // Result of the execution must be a String
        it.channel.call(new MasterGroovyClusterOpStep.Script("""
        println "cat /sys_apps_01/jenkins_nfs/io.jenkins.plugins.casc.CasCGlobalConfig.xml".execute().text
        """, listener, "configFileProviderPush.groovy", [:]))
        result = result << "Master ${it.name}:\n${stream.toString()}"

        stream.toString().eachLine { line, count ->
            print line + "\n"
        }
    }
}

//==========

//OC Script Console script to display server defined script using the cat command but against each controller name display

import com.cloudbees.opscenter.server.model.*
import com.cloudbees.opscenter.server.clusterops.steps.*
import hudson.remoting.*

controllers = []
scriptToRun = """
println "cat /sys_apps_01/jenkins_nfs/io.jenkins.plugins.casc.CasCGlobalConfig.xml".execute().text
x=null
"""

Jenkins.instance.getAllItems(ConnectedMaster.class).each {
  controllers.add(getHost(it.channel, it.class.simpleName, it.encodedName))
}

def getHost(channel, type, name){
  def hostReturnStr
  if(channel){
    def stream = new ByteArrayOutputStream()
    def listener = new StreamBuildListener(stream)
    channel.call(new MasterGroovyClusterOpStep.Script(
        scriptToRun,
        listener,
        "host-script.groovy",
        [:]))
    hostReturnStr = stream.toString()

  }
  return channel ? "${name} - ${hostReturnStr}" : "${name} - NOTFOUND"
}

controllers.each { if (it) { println it } }
x=null