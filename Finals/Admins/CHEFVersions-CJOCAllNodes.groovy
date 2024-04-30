//CJOC (Script Console and as Cluster Job) - Gets Node-Name, Label, Executors and CHEF-CLIENT version for all nodes against all controllers
import com.cloudbees.opscenter.server.model.*;
import com.cloudbees.opscenter.server.clusterops.steps.*;
import hudson.remoting.*;

def cjoc = getHost(new LocalChannel(), OperationsCenter.class.simpleName, OperationsCenter.class.simpleName)

cjoc.masters = []
Jenkins.instance.getAllItems(ConnectedMaster.class).each {
  cjoc.masters.add(getHost(it.channel, it.class.simpleName, it.encodedName))
}

try {
  cjoc.summary = [
    masters:cjoc.masters.size() + 1, //masters + cjoc
    masterCores:cjoc.masters*.cores.sum() + cjoc.cores,
    executors:cjoc.nodes*.executors.sum() + cjoc.masters*.nodes*.executors.sum().sum(),
    knownCloudExecutors:cjoc.masters*.clouds*.executorsCap.sum().findAll{it}.sum(0) + cjoc.clouds*.executorsCap.findAll{it}.sum(0)
  ]
}catch(e){}

def getHost(channel, type, name) {
  def host
  if(channel) {
    def stream = new ByteArrayOutputStream();
    def listener = new StreamBuildListener(stream);
    channel.call(new MasterGroovyClusterOpStep.Script('''
      def nodes = []
      Jenkins.instance.getNodes().each {
      try {
        import hudson.util.RemotingDiagnostics
        def isLinuxLabel = it.getLabelString().contains("LIN")
        def isWinLabel = it.getLabelString().contains("WIN")
    def chefClientCmd = "println(\"chef-client -version\".execute().text)";
        def chefStr = ""
        if (it.toComputer().isOnline() && isWinLabel) {
          chefStr = RemotingDiagnostics.executeGroovy("""
          def serr = new StringBuilder()
          def sout = new StringBuilder()
          def chefPSClientCmd = "chef-client -version"
          def proc = ["powershell", "-c", chefPSClientCmd].execute()
          proc.waitForProcessOutput(sout, serr);
          """, it.toComputer().getChannel());
        } 
        else if(it.toComputer().isOnline() && isLinuxLabel) {
           chefStr = RemotingDiagnostics.executeGroovy(chefClientCmd, it.toComputer().getChannel());
        }
      }
      catch(Exception ex) {
    chefStr = ""
      }
      nodes.add([type:it.class.simpleName, name:it.displayName, label:it.labelString, executors:it.numExecutors, chefVersion:chefStr])
     }

    def host = [type:'$type', name:'$name', url:Jenkins.instance.rootUrl, cores:Runtime.runtime.availableProcessors(), nodes:nodes, offline:false];

    return new groovy.json.JsonBuilder(host).toString()
    ''', listener, "host-script.groovy", [:]));
    host = new groovy.json.JsonSlurper().parseText(stream.toString().minus("Result: "));
  } else {
    host = [type:type, name:name, offline:true]
  }
  return host;
}

==============================================


//CJOC (Script Console and as Cluster Job) - Gets Node-Name, Label, Executors and CHEF-CLIENT version for all nodes against all controllers
import com.cloudbees.opscenter.server.model.*;
import com.cloudbees.opscenter.server.clusterops.steps.*;
import hudson.remoting.*;

def cjoc = getHost(new LocalChannel(), OperationsCenter.class.simpleName, OperationsCenter.class.simpleName)

cjoc.masters = []
Jenkins.instance.getAllItems(ConnectedMaster.class).each {
  cjoc.masters.add(getHost(it.channel, it.class.simpleName, it.encodedName))
}

try {
  cjoc.summary = [
    masters:cjoc.masters.size() + 1, //masters + cjoc
    masterCores:cjoc.masters*.cores.sum() + cjoc.cores,
    executors:cjoc.nodes*.executors.sum() + cjoc.masters*.nodes*.executors.sum().sum(),
    knownCloudExecutors:cjoc.masters*.clouds*.executorsCap.sum().findAll{it}.sum(0) + cjoc.clouds*.executorsCap.findAll{it}.sum(0)
  ]
}catch(e){}

def getHost(channel, type, name) {
  def host
  if(channel) {
    def stream = new ByteArrayOutputStream();
    def listener = new StreamBuildListener(stream);
    channel.call(new MasterGroovyClusterOpStep.Script('''
      def nodes = []
      Jenkins.instance.getNodes().each {
      try {
        import hudson.util.RemotingDiagnostics
        def isLinuxLabel = it.getLabelString().contains("LIN")
        def isWinLabel = it.getLabelString().contains("WIN")
    def chefClientCmd = "println(\"chef-client -version\".execute().text)";
        def chefStr = ""
        if (it.toComputer().isOnline() && isWinLabel) {
          chefStr = RemotingDiagnostics.executeGroovy("""
          def serr = new StringBuilder()
          def sout = new StringBuilder()
          def chefPSClientCmd = "chef-client -version"
          def proc = ["powershell", "-c", chefPSClientCmd].execute()
          proc.waitForProcessOutput(sout, serr);
          """, it.toComputer().getChannel());
        } 
        else if(it.toComputer().isOnline() && isLinuxLabel) {
           chefStr = RemotingDiagnostics.executeGroovy(chefClientCmd, it.toComputer().getChannel());
        }
      }
      catch(Exception ex) {
    chefStr = ""
      }
    if(chefStr.length > 0)
        nodes.add([type:it.class.simpleName, name:it.displayName, label:it.labelString, executors:it.numExecutors, chefVersion:chefStr])
     }

    def host = [type:'$type', name:'$name', url:Jenkins.instance.rootUrl, cores:Runtime.runtime.availableProcessors(), nodes:nodes, offline:false];

    return host.toString()
    ''', listener, "host-script.groovy", [:]));
    host = new groovy.json.JsonSlurper().parseText(stream.toString().minus("Result: "));
   //host = stream.toString().minus("Result: ");
  } else {
    host = [type:type, name:name, offline:true]
  }
  return host;
}


=============================== +++++++++++++++++++++++++++++++++ ==================


/*** BEGIN META {
 "name" : "Push Config File Provider configuration to Client Masters",
 "comment" : "Run from the Script Console. This script read the global configuration of the Config File Provider in CJOC and push it to all Client Masters. If a config file with identical ID already exists in the Client Master, it will be overridden.",
 "parameters" : [],
 "core": "2.73.2.1",
 "authors" : [
 { name : "Allan Burdajewicz" }
 ]
 } END META**/

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
        it.channel.call(new MasterGroovyClusterOpStep.Script('''
        def nodes = []
      Jenkins.instance.getNodes().each {
        def chefStr = ""
      try {
        def isLinuxLabel = it.getLabelString().contains("LIN")
        def isWinLabel = it.getLabelString().contains("WIN")
        if (it.toComputer().isOnline() && isWinLabel) {
          chefStr = hudson.util.RemotingDiagnostics.executeGroovy("""
          def serr = new StringBuilder()
          def sout = new StringBuilder()
          def chefPSClientCmd = "chef-client -version"
          def proc = ["powershell", "-c", chefPSClientCmd].execute()
          proc.waitForProcessOutput(sout, serr);
          println sout;
          """, it.toComputer().getChannel());
        } 
        else if(it.toComputer().isOnline() && isLinuxLabel) {
           chefStr = hudson.util.RemotingDiagnostics.executeGroovy(chefClientCmd, it.toComputer().getChannel());
        }
      }
      catch(Exception ex) {
      }
      nodes.add([type:it.class.simpleName, name:it.displayName, label:it.labelString, executors:it.numExecutors, chefVersion:chefStr])
     }

    def host = [type:'$type', name:'$name', url:Jenkins.instance.rootUrl, cores:Runtime.runtime.availableProcessors(), nodes:nodes, offline:false];

    return new groovy.json.JsonBuilder(host).toString()
        ''', listener, "configFileProviderPush.groovy", [:]))
        result = result << "Master ${it.name}:\n${stream.toString()}"

        stream.toString().eachLine { line, count ->
            print line + "\n"
        }
    }
}

============================================================ LAST ONE ============
import com.cloudbees.opscenter.server.model.*;
import com.cloudbees.opscenter.server.clusterops.steps.*;
import hudson.remoting.*;
import com.cloudbees.opscenter.server.clusterops.steps.MasterGroovyClusterOpStep
import com.cloudbees.opscenter.server.model.ConnectedMaster
import hudson.model.StreamBuildListener
import jenkins.model.Jenkins

def result = '\n'

def cjoc = getHost(new LocalChannel(), OperationsCenter.class.simpleName, OperationsCenter.class.simpleName)

cjoc.masters = []
Jenkins.instance.getAllItems(ConnectedMaster.class).each {
  cjoc.masters.add(getHost(it.channel, it.class.simpleName, it.encodedName))
}

try {
  cjoc.summary = [
    masters:cjoc.masters.size() + 1, //masters + cjoc
    masterCores:cjoc.masters*.cores.sum() + cjoc.cores,
    executors:cjoc.nodes*.executors.sum() + cjoc.masters*.nodes*.executors.sum().sum(),
    knownCloudExecutors:cjoc.masters*.clouds*.executorsCap.sum().findAll{it}.sum(0) + cjoc.clouds*.executorsCap.findAll{it}.sum(0)
  ]
}catch(e){}

def getHost(channel, type, name) {
    if(channel) {
        def stream = new ByteArrayOutputStream();
        def listener = new StreamBuildListener(stream);
        // Execute remote Groovy script in the Client Master
        // Result of the execution must be a String
        channel.call(new MasterGroovyClusterOpStep.Script('''
        def nodes = []
        Jenkins.instance.getNodes().each {
         def chefStr = ""
         try {
            def isLinuxLabel = it.getLabelString().contains("LIN")
            def isWinLabel = it.getLabelString().contains("WIN")
            if (it.toComputer().isOnline() && isWinLabel) {
              chefStr = hudson.util.RemotingDiagnostics.executeGroovy("""
              def serr = new StringBuilder()
              def sout = new StringBuilder()
              def chefPSClientCmd = "chef-client -version"
              def proc = ["powershell", "-c", chefPSClientCmd].execute()
              proc.waitForProcessOutput(sout, serr);
              println sout;
              """, it.toComputer().getChannel());
            } 
            else if(it.toComputer().isOnline() && isLinuxLabel) {
               chefStr = hudson.util.RemotingDiagnostics.executeGroovy(chefClientCmd, it.toComputer().getChannel());
            }
          }
          catch(Exception ex) { }       
      nodes.add([type:it.class.simpleName, name:it.displayName, label:it.labelString, executors:it.numExecutors, chefVersion:chefStr])
         }
    
        def host = [type:'$type', name:'$name', url:Jenkins.instance.rootUrl, cores:Runtime.runtime.availableProcessors(), nodes:nodes, offline:false];
    
        return new groovy.json.JsonBuilder(host).toString()
        ''', listener, "configFileProviderPush.groovy", [:]))
        result = new groovy.json.JsonSlurper().parseText(stream.toString().minus("Result: "));
      
      result = result << "Master ${it.name}:\n${stream.toString()}"
      stream.toString().eachLine { line, count ->
        print line + "\n"
      }
    }
    else {
      result = [type:type, name:name, offline:true]
    }
    return result
  }

============================= NOT WORKING =================
import com.cloudbees.opscenter.server.model.*;
import com.cloudbees.opscenter.server.clusterops.steps.*;
import hudson.remoting.*;
import com.cloudbees.opscenter.server.clusterops.steps.MasterGroovyClusterOpStep
import com.cloudbees.opscenter.server.model.ConnectedMaster
import hudson.model.StreamBuildListener
import jenkins.model.Jenkins

def result = '\n'

def cjoc = getHost(new LocalChannel(), OperationsCenter.class.simpleName, OperationsCenter.class.simpleName)

cjoc.masters = []
Jenkins.instance.getAllItems(ConnectedMaster.class).each {
  cjoc.masters.add(getHost(it.channel, it.class.simpleName, it.encodedName))
}

try {
  cjoc.summary = [
    masters:cjoc.masters.size() + 1,
    masterCores:cjoc.masters*.cores.sum() + cjoc.cores,
    executors:cjoc.nodes*.executors.sum() + cjoc.masters*.nodes*.executors.sum().sum(),
    knownCloudExecutors:cjoc.masters*.clouds*.executorsCap.sum().findAll{it}.sum(0) + cjoc.clouds*.executorsCap.findAll{it}.sum(0)
  ]
}catch(e){}

def getHost(channel, type, name) {
    if(channel) {
        def stream = new ByteArrayOutputStream();
        def listener = new StreamBuildListener(stream);
        // Execute remote Groovy script in the Client Master
        // Result of the execution must be a String
        channel.call(new MasterGroovyClusterOpStep.Script('''
def nodes = []
Jenkins.instance.getNodes().each {
  def chefStr = ""
  try {
    def isLinuxLabel = it.getLabelString().contains("LIN")
    def isWinLabel = it.getLabelString().contains("WIN")
    if (it.toComputer().isOnline() && isWinLabel) {
      chefStr = hudson.util.RemotingDiagnostics.executeGroovy("""
      def serr = new StringBuilder()
      def sout = new StringBuilder()
      def chefPSClientCmd = "chef-client -version"
      def proc = ["powershell", "-c", chefPSClientCmd].execute()
      proc.waitForOrKill(5000)
      proc.consumeProcessOutput(sout, serr)
      println sout;
      """, it.toComputer().getChannel());
    } 
    else if(it.toComputer().isOnline() && isLinuxLabel) {
      def chefClientCmd = "println('chef-client -version'.execute().text)";
      chefStr = hudson.util.RemotingDiagnostics.executeGroovy(chefClientCmd, it.toComputer().getChannel());
    }
  }
  catch(Exception ex) { }       
  nodes.add([type:it.class.simpleName, name:it.displayName, label:it.labelString, executors:it.numExecutors, chefVersion:chefStr])
}
def host = [type:'$type', name:'$name', url:Jenkins.instance.rootUrl, cores:Runtime.runtime.availableProcessors(), nodes:nodes, offline:false];
return new groovy.json.JsonBuilder(host).toString()
        ''', listener, "configFileProviderPush.groovy", [:]))
        result = new groovy.json.JsonSlurper().parseText(stream.toString().minus("Result: "));
      
      result = result << "Master ${name}:\n${stream.toString()}"
      stream.toString().eachLine { line, count ->
        print line + "\n"
      }
    }
    else {
      result = [type:type, name:name, offline:true]
    }
    return result
  }
return new groovy.json.JsonBuilder(cjoc).toPrettyString()

  =================== Script Console to be executed from CJOC Script Console for all controllers ============
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
        it.channel.call(new MasterGroovyClusterOpStep.Script('''
        def chefClientCmd = 'println("chef-client -version".execute().text)';
        def nodes = []
      Jenkins.instance.getNodes().each {
        def chefStr = ""
      try {
        def isLinuxLabel = it.getLabelString().contains("LIN")
        def isWinLabel = it.getLabelString().contains("WIN")
        if (it.toComputer().isOnline() && isWinLabel) {
          chefStr = hudson.util.RemotingDiagnostics.executeGroovy("""
          def serr = new StringBuilder()
          def sout = new StringBuilder()
          def chefPSClientCmd = "chef-client -version"
          def proc = ["powershell", "-c", chefPSClientCmd].execute()
          proc.consumeProcessOutput(sout, serr)
      proc.waitForOrKill(10000)
          println sout;
          """, it.toComputer().getChannel());
        } 
        else if(it.toComputer().isOnline() && isLinuxLabel) {
           chefStr = hudson.util.RemotingDiagnostics.executeGroovy(chefClientCmd, it.toComputer().getChannel());
        }
        else {
          chefStr = hudson.util.RemotingDiagnostics.executeGroovy("""
          def serr = new StringBuilder()
          def sout = new StringBuilder()
          def chefCmd = "chef-client -version"
          def proc = ["bash", "-c", chefCmd].execute()
          proc.consumeProcessOutput(sout, serr)
          proc.waitForOrKill(3000)
          println sout;
          """, it.toComputer().getChannel());
        }
      }
      catch(Exception ex) {
      }
      nodes.add([type:it.class.simpleName, name:it.displayName, label:it.labelString, executors:it.numExecutors, chefVersion:chefStr])
     }

    def host = [type:'$type', name:'$name', url:Jenkins.instance.rootUrl, cores:Runtime.runtime.availableProcessors(), nodes:nodes, offline:false];

    return new groovy.json.JsonBuilder(host).toString()
        ''', listener, "configFileProviderPush.groovy", [:]))
        result = result << "Master ${it.name}:\n${stream.toString()}"

        stream.toString().eachLine { line, count ->
            print line + "\n"
        }
    }
}

============ Individual Controller Script Console Script ================
import hudson.util.RemotingDiagnostics
def chefClientCmd = "println(\"chef-client -version\".execute().text)";
def chefStr = "";
def nodes = []
Jenkins.instance.getNodes().each {
  def isLinuxLabel = it.getLabelString().contains("LIN")
  def isWinLabel = it.getLabelString().contains("WIN")
  if (it.toComputer().isOnline() && isWinLabel) 
  {
    chefStr = RemotingDiagnostics.executeGroovy("""
def serr = new StringBuilder()
def sout = new StringBuilder()
def chefPSClientCmd = "chef-client -version"
def proc = ['powershell', '-c', chefPSClientCmd].execute()
proc.waitForProcessOutput(sout, serr);
""", it.toComputer().getChannel());
  } 
  else if(it.toComputer().isOnline() && isLinuxLabel) 
  {
     chefStr = RemotingDiagnostics.executeGroovy(chefClientCmd, it.toComputer().getChannel());
  }
  chefStr.replaceAll("[\n\r]", "");
  //println "name:${it.displayName}, label:${it.labelString}, executors:${it.numExecutors}, chefVersion:${chefStr}";
  nodes.add([type:it.class.simpleName, name:it.displayName, label:it.labelString, executors:it.numExecutors, chefVersion:chefStr])
 }
println nodes
return null

================= Final on CJOC for all Controllers ===========
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
        it.channel.call(new MasterGroovyClusterOpStep.Script('''
    def chefClientCmd = 'println("chef-client -version".execute().text)';
        def nodes = []
      Jenkins.instance.getNodes().each {
        def chefStr = ""
      try {
        def isWinLabel = it.getLabelString().contains("WIN")
        if (it.toComputer().isOnline() && isWinLabel) {
          chefStr = hudson.util.RemotingDiagnostics.executeGroovy("""
          def serr = new StringBuilder()
          def sout = new StringBuilder()
          def chefPSClientCmd = "chef-client -version"
          def proc = ["powershell", "-c", chefPSClientCmd].execute()
          proc.consumeProcessOutput(sout, serr)
      proc.waitForOrKill(10000)
          println sout;
          """, it.toComputer().getChannel());
        } 
        else if(it.toComputer().isOnline()) {
       chefStr = hudson.util.RemotingDiagnostics.executeGroovy(chefClientCmd, it.toComputer().getChannel());
        }
    else {
          chefStr = hudson.util.RemotingDiagnostics.executeGroovy("""
          def serr = new StringBuilder()
          def sout = new StringBuilder()
          def chefCmd = "chef-client -version"
          def proc = ["bash", "-c", chefCmd].execute()
          proc.consumeProcessOutput(sout, serr)
          proc.waitForOrKill(3000)
          println sout;
          """, it.toComputer().getChannel());
        }
      }
      catch(Exception ex) {
      }
      nodes.add([type:it.class.simpleName, name:it.displayName, label:it.labelString, executors:it.numExecutors, chefVersion:chefStr])
     }

    def host = [type:'$type', name:'$name', url:Jenkins.instance.rootUrl, cores:Runtime.runtime.availableProcessors(), nodes:nodes, offline:false];

    return new groovy.json.JsonBuilder(host).toString()
        ''', listener, "configFileProviderPush.groovy", [:]))
        result = result << "Master ${it.name}:\n${stream.toString()}"

        stream.toString().eachLine { line, count ->
            print line + "\n"
        }
    }
}

=========== Final on all Controllers ==============
import hudson.util.RemotingDiagnostics
def chefClientCmd = """println('chef-client -version'.execute().text)""";
def chefStr = "";
def nodes = []
Jenkins.instance.getNodes().each {
  def isLinuxLabel = it.getLabelString().contains("LIN")
  def isWinLabel = it.getLabelString().contains("WIN")
  try {
    if (it.toComputer().isOnline() && isWinLabel) 
    {
      chefStr = RemotingDiagnostics.executeGroovy("""
  def serr = new StringBuilder()
  def sout = new StringBuilder()
  def chefPSClientCmd = "chef-client -version"
  def proc = ['powershell', '-c', chefPSClientCmd].execute()
  proc.waitForProcessOutput(sout, serr);
  """, it.toComputer().getChannel());
    } 
    else if(it.toComputer().isOnline() && isLinuxLabel) 
    {
       chefStr = RemotingDiagnostics.executeGroovy(chefClientCmd, it.toComputer().getChannel());
    }
    else {
      chefStr = hudson.util.RemotingDiagnostics.executeGroovy("""
      def serr = new StringBuilder()
      def sout = new StringBuilder()
      def chefCmd = "chef-client -version"
      def proc = ["bash", "-c", chefCmd].execute()
      proc.consumeProcessOutput(sout, serr)
      proc.waitForOrKill(3000)
      println sout;
      """, it.toComputer().getChannel());
    }
    chefStr.replaceAll("[\n]", "");
    chefStr.replaceAll("[\r]", "");
    chefStr.replaceAll("[\n\n]", "");
}
  catch(e) { }
  //println "name:${it.displayName}, label:${it.labelString}, executors:${it.numExecutors}, chefVersion:${chefStr}";
  nodes.add([type:it.class.simpleName, name:it.displayName, label:it.labelString, executors:it.numExecutors, chefVersion:chefStr])
 }
println nodes
println "=================================================="
return new groovy.json.JsonBuilder(nodes).toString()