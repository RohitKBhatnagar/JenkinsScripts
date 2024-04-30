/** Pending Scripts or Signatures for Approvals 
Author: kuisathaverat
Description: list pending approvals, approve scripts/signatures pending on "In-process Script Approval" using the parameters method and signature, and add a signature tho the list.
Parameters: method and signature that you want to approve
NOTE: this is only for advanced users and for weird behaviours that does not have other workaround
**/

import org.jenkinsci.plugins.scriptsecurity.scripts.*
  
def method = "something"
def signature = "something"

ScriptApproval sa = ScriptApproval.get();

//list pending approvals
for (ScriptApproval.PendingScript pending : sa.getPendingScripts()) {
        println "Pending Script Approval : " + pending.script
}

for (ScriptApproval.PendingSignature pending : sa.getPendingSignatures()) {
        println "Pending Signature Approval : " + pending.signature
}  

// approve scripts
for (ScriptApproval.PendingScript pending : sa.getPendingScripts()) {
   if (pending.script.equals(method)) {
       	sa.approveScript(pending.getHash());
     	println "Approved Scripts : " + pending.script
      }
}

// approve signatures
for (ScriptApproval.PendingSignature pending : sa.getPendingSignatures()) {
   if (pending.equals(signature)) {
       	sa.approveSignature(pending.signature);
     	println "Approved Signatures : " + pending.signature
      }
}