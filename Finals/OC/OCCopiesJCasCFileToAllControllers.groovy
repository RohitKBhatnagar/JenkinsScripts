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
        println "cp /sys_apps_01/jenkins_utils/io.jenkins.plugins.casc.CasCGlobalConfig.xml /sys_apps_01/jenkins_nfs/io.jenkins.plugins.casc.CasCGlobalConfig.xml".execute().text
        """, listener, "configFileProviderPush.groovy", [:]))
        result = result << "Master ${it.name}:\n${stream.toString()}"

        stream.toString().eachLine { line, count ->
            print line + "\n"
        }
    }
}