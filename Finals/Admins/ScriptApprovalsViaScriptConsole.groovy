/*
String[]	getAclApprovedSignatures()	 
List<ScriptApproval.ApprovedClasspathEntry>	getApprovedClasspathEntries()	 
String[]	getApprovedScriptHashes()	 
String[]	getApprovedSignatures()	 
GlobalConfigurationCategory	getCategory()	 
net.sf.json.JSON	getClasspathRenderInfo()	 
protected XmlFile	getConfigFile()	 
String[]	getDangerousApprovedSignatures()	 
String	getIconFileName()	 
List<ScriptApproval.PendingClasspathEntry>	getPendingClasspathEntries()	 
Set<ScriptApproval.PendingScript>	getPendingScripts()	 
Set<ScriptApproval.PendingSignature>	getPendingSignatures()	 
String	getSpinnerIconClassName()	 
String	getUrlName()
*/
//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
def scriptApproval = org.jenkinsci.plugins.scriptsecurity.scripts.ScriptApproval.get()

//println scriptApproval.metaClass.methods
scriptApproval.metaClass.methods.eachWithIndex{ it, itCount ->
  println "${itCount}: ${it}"
  
}

println "--------------------"

println "--------- Approved Signatures ---------"
def approvedScripts = scriptApproval.getApprovedSignatures()

approvedScripts.each { approvedScriptEntry ->
    println(approvedScriptEntry)
}

println "--------- Approved Acl Signatures ---------"
def approvedAclScripts = scriptApproval.getAclApprovedSignatures()

approvedAclScripts.each { approvedScriptEntry ->
    println(approvedScriptEntry)
}

println "--------- Approved Class Path Entries ---------"
def approvedClassPathEntries = scriptApproval.getApprovedClasspathEntries()

approvedClassPathEntries.each { approvedScriptEntry ->
    println(approvedScriptEntry)
}

println "--------- Approved Script Hashes ---------"
def approvedScriptHashes = scriptApproval.getApprovedScriptHashes()

approvedScriptHashes.each { approvedScriptEntry ->
    println(approvedScriptEntry)
}

println "--------- Global Configuration Category ---------"
def globalConfCategory = scriptApproval.getCategory()

println(globalConfCategory)

println "--------- Class Path Render Info ---------"
def classPathRndrInfo = scriptApproval.getClasspathRenderInfo()

classPathRndrInfo.each { approvedScriptEntry ->
    println(approvedScriptEntry)
    approvedScriptEntry.each { it->
    println it
    }
}
//println(classPathRndrInfo)

println "--------- Script Approval Config File ---------"
def configFileName = scriptApproval.getConfigFile()

println(configFileName)
