#!groovy
//Getting scripts approved on particular instance

import org.jenkinsci.plugins.scriptsecurity.scripts.ScriptApproval;
println "Permission to run scripts - " + Jenkins.get().hasPermission(Jenkins.RUN_SCRIPTS);
ScriptApproval sa = ScriptApproval.get();
println sa;
//println "Approved signatures - " + sa.getApprovedSignatures();
println "\nApproved signatures ========== ";
def ApprSigs = sa.getApprovedSignatures();
ApprSigs.eachWithIndex
{ it, i ->
    println "------------------------"
    println "\t$i: $it"
    println "++++++++++++++++++++++++"
}
println "Approved Classpath Entries - " + sa.getApprovedClasspathEntries();
println "URL - " + Jenkins.get().getRootUrl() + sa.getUrlName();
println "Dangerous Approved Signatures - " + sa.getDangerousApprovedSignatures();

println "ACL Approved Signatures - " + sa.getAclApprovedSignatures() 
println "Category - " + sa.getCategory();
def glblCat = sa.getCategory();
glblCat.eachWithIndex
{ it, i ->
    println "$i: Description - ${it.getShortDescription()}";
    println "------------------------------"
    println "All registered global configuration descriptors...\n\t ${it.getDisplayName()}"
    def glblConf = it.all();
    glblConf.eachWithIndex
    { itGlbl, itGlblCount ->
        println "$itGlblCount: $itGlbl";
        //println "Class - ${itGlbl.getClass()}"
        int iCount = 0;
        if(itGlbl instanceof jenkins.model.GlobalConfigurationCategory$Security)
        {
            println "${iCount++}:: Name - ${itGlbl.getDisplayName()} | Description - ${itGlbl.getShortDescription()}"
        }
    }
    println "++++++++++++++++++++++++++++++"
}
println "Classpath Render Info - " + sa.getClasspathRenderInfo();
println "Config File - " + sa.getConfigFile();
println "Pending Classpath Entries - " + sa.getPendingClasspathEntries();
//println "Pending Scripts - " + sa.getPendingScripts();
println "\nPending Scripts ========== ";
def pendScripts = sa.getPendingScripts();
pendScripts.eachWithIndex
{ it, i ->
    println "$i: HashCode - ${it.hashCode()}, Hash - ${it.getHash()}";
    println "------------------------"
    println "Script...\n\t $it.script"
    println "++++++++++++++++++++++++"
}
//println "Pending Signatures - " + sa.getPendingSignatures();
println "\nPending Signatures ========== ";
def pendSigs = sa.getPendingSignatures();
pendSigs.eachWithIndex
{ it, i ->
    println "$i: Dangerous - ${it.dangerous},  HashCode - ${it.hashCode()}, Hash - ${it.getHash()}";
    println "------------------------------"
    println "Signature...\n\t $it.signature"
    println "++++++++++++++++++++++++++++++"
}

return;