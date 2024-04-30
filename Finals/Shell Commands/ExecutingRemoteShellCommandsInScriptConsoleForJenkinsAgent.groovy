///Executes the script in remote node
import hudson.util.RemotingDiagnostics
import jenkins.model.Jenkins


String agent_name = 'lstl2jnk7064' //lstl2jnk8645, lstl4jnk7451, lstl4jnk8526

groovy_script = '''
println System.getenv("PATH")
println "uname -a".execute().text
println "pwd".execute().text
println "ls -latrch".execute().text
'''.trim()

String result
Jenkins.instance.slaves.find { agent ->
    agent.name == agent_name
}.with { agent ->
    result = RemotingDiagnostics.executeGroovy(groovy_script, agent.channel)
}
println result


//////////////////////////////////
//Validate NodeJS version
 ///Executes the script in remote node
import hudson.util.RemotingDiagnostics
import jenkins.model.Jenkins


String agent_name = 'jnk4stl41' //lstl2jnk8645, lstl4jnk7451, lstl4jnk8526

groovy_script = '''
println System.getenv("PATH")
println "uname -a".execute().text
println "pwd".execute().text
println "ls -latrch /sys_apps_01/javascript/".execute().text
println "/sys_apps_01/javascript/node-v14.17.3-linux-x64/bin/node -v".execute().text
'''.trim()

String result
Jenkins.instance.slaves.find { agent ->
    agent.name == agent_name
}.with { agent ->
    result = RemotingDiagnostics.executeGroovy(groovy_script, agent.channel)
}
println result