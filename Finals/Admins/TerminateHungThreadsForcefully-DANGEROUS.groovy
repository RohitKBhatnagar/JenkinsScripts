def iRunCount = 0;
def runState = []
def termState = []
def waitState = []
jenkins.model.Jenkins.instanceOrNull.toComputer().allExecutors.eachWithIndex { it, iCount ->
  //println it.getClass()
  //println "${iCount}, ${it}, ${it.getState()}"
  if(it.getState().toString().contains('RUNNABLE'))
    runState.add(it)
   //{
     //println "${iRunCount++}, * Branch Indexing still running for ${it.currentExecutable.parent.fullDisplayName} that spent ${it.elapsedTime}ms scanning on ${it.owner.displayName} #${it.number}..."
   //}
  if(it.getState().toString().contains('TERMINATED'))
    termState.add(it)
  if(it.getState().toString().contains('WAITING'))
    waitState.add(it)
}

println "------------ Runnable count - " + runState.size
runState.eachWithIndex { it, iCount ->
  try {
	println "\t ${iCount}, * Branch Indexing still running for ${it.currentExecutable.parent.fullDisplayName} that spent ${it.elapsedTime}ms scanning on ${it.owner.displayName} #${it.number}..."
    //if(it.currentExecutable.parent.fullDisplayName.contains("StrategicGrowth"))
    //if(it.currentExecutable.parent.fullDisplayName.contains("Mastercard Digital Enablement Service"))
    //if(it.currentExecutable.parent.fullDisplayName.contains("fsp-cyber-secure-riskrecon-batch"))
    //if(it.currentExecutable.parent.fullDisplayName.contains("Heracles"))
    if(it.currentExecutable.parent.fullDisplayName.contains("MCCoreQETS"))
    {
      println "\t\t STOPPING - ${iCount}, * Branch Indexing running for ${it.currentExecutable.parent.fullDisplayName} that spent ${it.elapsedTime}ms scanning on ${it.owner.displayName} #${it.number}..."
      it.stop()
    }
  }
  catch(Exception exp)
  {
  }
}
println "------------ Terminated count - " + termState.size
termState.eachWithIndex { it, iCount ->
  try {
	println "\t ${iCount}, * Branch Indexing already terminated for ${it.currentExecutable.parent.fullDisplayName} that spent ${it.elapsedTime}ms scanning on ${it.owner.displayName} #${it.number}..."
  }
  catch(Exception exp)
  {
  }
}
println "------------ Waiting count - " + waitState.size
waitState.eachWithIndex { it, iCount ->
  try {
	println "\t ${iCount}, * Branch Indexing waiting for ${it.currentExecutable.parent.fullDisplayName} that spent ${it.elapsedTime}ms scanning on ${it.owner.displayName} #${it.number}..."
    //if(it.currentExecutable.parent.fullDisplayName.contains("StrategicGrowth"))
    //if(it.currentExecutable.parent.fullDisplayName.contains("Mastercard Digital Enablement Service"))
    //if(it.currentExecutable.parent.fullDisplayName.contains("fsp-cyber-secure-riskrecon-batch"))
    //if(it.currentExecutable.parent.fullDisplayName.contains("Heracles"))
    if(it.currentExecutable.parent.fullDisplayName.contains("MCCoreQETS"))
    {
      println "\t\t STOPPING - ${iCount}, * Branch Indexing waiting for ${it.currentExecutable.parent.fullDisplayName} that spent ${it.elapsedTime}ms scanning on ${it.owner.displayName} #${it.number}..."
      it.stop()
    }
  }
  catch(Exception exp)
  {
  }
}
return