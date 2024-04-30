#!Groovy

import java.text.SimpleDateFormat
def iCount = 1

def sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss.SSS")
def startDate = new Date()
println "Start time : ${sdf.format(startDate)}"

//Master HostName and IP Address
println "Master HostName and IP Address - ${java.net.InetAddress.getLocalHost()}"

//---------------------------------------------------------------------------
//All Nodes - Java Version
//---------------------------------------------------------------------------

println "---------------------------------------------------------------------------"
println "All Nodes - Java Instances installed as well as default version"
println "---------------------------------------------------------------------------"

import hudson.model.Node
import hudson.model.Slave
import jenkins.model.Jenkins
import hudson.util.RemotingDiagnostics

Jenkins jenkins = Jenkins.instance
for (Node node in jenkins.nodes) 
{
  // Make sure slave is online
  if (!node.toComputer().online) 
  {
    println "Node '$node.nodeName' is currently offline - skipping check"
    continue;
  } 
  else 
  {
    //String agent_name = node.nodeName;
	if(node.nodeName == 'jnk2stl1')
	{
		props = node.toComputer().getSystemProperties();
	    println "Node '$node.nodeName' is running " + props.get('java.runtime.version');
		try {
			//groovy script you want executed on an agent
			// String groovy_script = '''
			// println System.getenv("PATH")
			// println "uname -a".execute().text
			// '''.trim()

			boolean isUnix = Boolean.TRUE.equals(node.toComputer().isUnix());
			//println "Linux instance - ${isUnix}"
			String groovy_script, tuxedo_script, tux111130_script, tux111130_jre_script, tux111130_java_version_script
			if(isUnix)
			{
				groovy_script = '''println "ls -latrch /sys_apps_01/java/".execute().text'''.trim()
				tuxedo_script = '''println "ls -latrch /sys_apps_01/tuxedo11/".execute().text'''.trim()
				tux111130_script = '''println "ls -latrch /sys_apps_01/tuxedo11/tuxedo111130/".execute().text'''.trim()
				tux111130_jre_script = '''println "ls -latrch /sys_apps_01/tuxedo11/tuxedo111130/jre/bin/".execute().text'''.trim()
				tux111130_java_version_script = '''println "/sys_apps_01/tuxedo11/tuxedo111130/jre/bin/java -version".execute().text'''.trim()
			}
			else
			{
				groovy_script = '''println "dir /cygdrive/c/'Program Files'/Java".execute().text'''.trim()
			}

			String result = RemotingDiagnostics.executeGroovy(groovy_script, node.channel);
			println "Directory search on /sys_apps_01/java/ - " + result;
			println "========================================="
			result = RemotingDiagnostics.executeGroovy(tuxedo_script, node.channel);
			println "Directory search on /sys_apps_01/tuxedo11/ - " + result;
			println "========================================="
			result = RemotingDiagnostics.executeGroovy(tux111130_script, node.channel);
			println "Directory search on /sys_apps_01/tuxedo11/tuxedo111130/ - " + result;
			println "========================================="
			result = RemotingDiagnostics.executeGroovy(tux111130_jre_script, node.channel);
			println "Directory search on /sys_apps_01/tuxedo11/tuxedo111130/jre/bin/ - " + result;
			println "========================================="
			result = RemotingDiagnostics.executeGroovy(tux111130_java_version_script, node.channel);
			println "Java Version of /sys_apps_01/tuxedo11/tuxedo111130/jre/bin/ - " + result;
			println "========================================="
		}
		catch(Exception exp)
		{
			println "${exp.message}"
		}
	  }
  }
}

//---------------------------------------------------------------------------
def endDate = new Date()
println "End time: ${sdf.format(endDate)}"

use(groovy.time.TimeCategory) 
{
    def duration = endDate - startDate
  	print "Elapsed time: Days: ${duration.days}, Hours: ${duration.hours}, Minutes: ${duration.minutes}, Seconds: ${duration.seconds}"
}