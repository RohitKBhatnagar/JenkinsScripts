//Prints as JSON putput all nodes and executor counts
//Master HostName and IP Address
println "Master HostName and IP Address - ${java.net.InetAddress.getLocalHost()}"

def nodes = []
Jenkins.instance.getAllItems(com.cloudbees.opscenter.server.model.SharedSlave.class).each {
  nodes.add([type:it.class.simpleName, name:it.displayName, executors:it.numExecutors])
}


def host = [type:'$type', name:'$name', url:Jenkins.instance.rootUrl, cores:Runtime.runtime.availableProcessors(), nodes:nodes, offline:false]

return new groovy.json.JsonBuilder(host).toString()


//--------- ALTERNATIVE GROOVY SCRIPT ----------
///////////////////////////////////////////////////
//Executor Counts for a Controller
  /*** BEGIN META {
    "name" : "Count executors",
    "comment" : "Shows the total number of nodes and executors on Jenkins",
    "parameters" : [ ],
    "core": "1.350",
    "authors" : [
      { name : "Andy Pemberton" }
    ]
  } END META**/
  import jenkins.model.Jenkins
  
  println("====== Regular Slave Executors ======")
  println()
  
  // Jenkins Master and slaves
  def regularSlaves = Jenkins.instance.computers.grep{ 
    it.class.superclass?.simpleName != 'AbstractCloudComputer' &&
    it.class.superclass?.simpleName != 'AbstractCloudSlave' &&
    it.class.simpleName != 'EC2AbstractSlave'
  }
  int regularSlaveExecutorCount = regularSlaves.inject(0, {a, c -> a + c.numExecutors})
  //TODO perhaps filter other known cloud slaves; shame there isn't a cleaner way to know them

  println("| Node Name | Type | Executors |")
  regularSlaves.each {
   println "| ${it.displayName} | ${it.class.simpleName} | ${it.numExecutors} |" 
  }
  println()

  println("Total: " + regularSlaveExecutorCount + " executors")
  println()

  println("====== Shared Slave Executors ======")
  println()

  // CJOC Shared Slaves
  def sharedSlaves = Jenkins.instance.allItems.grep{
    it.class.name == 'com.cloudbees.opscenter.server.model.SharedSlave' 
  }
  int sharedSlaveExecutorCount = sharedSlaves.inject(0, {a, c -> a + c.numExecutors})

  println("| Node Name | Type | Executors |")
  sharedSlaves.each {
    println "| ${it.displayName} | ${it.class.simpleName} | ${it.numExecutors} |"
  }
  println()

  println("Total: " + sharedSlaveExecutorCount + " executors")
  println()

  println("====== Cloud Slave Executors ======")
  println()
  
  println("| Cloud Name | Type | Max. Executors |")
  int totalInstanceCaps
  Jenkins.instance.clouds.each { cloud ->
      Integer instanceCaps
      try{
        instanceCaps = cloud.templates?.inject(0, {a, c -> a + (c.numExecutors * c.instanceCap)})
        totalInstanceCaps += instanceCaps
      }catch(e){
      }finally{
      }
    println "| ${cloud.displayName} | ${cloud.descriptor.displayName} | ${instanceCaps ?: ''} |"
  }
  
  println()
  if(totalInstanceCaps != null && totalInstanceCaps > 0){
    println("Total: up to " + totalInstanceCaps + " executors")
  } else {
    println("Total: None")
  }
  
  return