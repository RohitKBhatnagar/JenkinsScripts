
import jenkins.model.Jenkins
import java.text.SimpleDateFormat

def sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss.SSS")
def startDate = new Date()
println "Start time : ${sdf.format(startDate)}"
//-------------------------------------------------

String strMaster = java.net.InetAddress.getLocalHost();

println "Multibranch Items\n-------"
Jenkins.instance.getAllItems(org.jenkinsci.plugins.workflow.multibranch.WorkflowMultiBranchProject.class).eachWithIndex { it, iCount ->
  //println "$iCount, $it.fullName, $it.disabled"
  //if(it.fullName.contains('ESSAuthSolR'))
  if(it.fullName.contains('MDES'))
  {
    if(!it.disabled)
    {
      it.makeDisabled(true)
      println "\t $iCount, $it.fullName, $it.name is DISABLED"
    }
  }
}

//--------------------------------------------------
def endDate = new Date()
println "End time: ${sdf.format(endDate)}"

use(groovy.time.TimeCategory) 
{
    def duration = endDate - startDate
      print "Elapsed time: Days: ${duration.days}, Hours: ${duration.hours}, Minutes: ${duration.minutes}, Seconds: ${duration.seconds}"
}

return

/***********************************
///////////////////////////
import jenkins.model.Jenkins

println "Multibranch Items\n-------"
Jenkins.instance.getAllItems(org.jenkinsci.plugins.workflow.multibranch.WorkflowMultiBranchProject.class).eachWithIndex { it, iCount ->
  println "$iCount, $it.fullName, $it.disabled"
  if(it.fullName.contains('ESSAuthSolR'))
  {
    if(it.disabled)
    {
      it.makeDisabled(true)
      println "\t $iCount, $it.fullName, $it.name is DISABLED"
    }
  }
}
return

/////////////////


//Disable MULTI BRANCH PIPELINES based upon name provided
import jenkins.model.Jenkins

println "Multibranch Items\n-------"
Jenkins.instance.getAllItems(org.jenkinsci.plugins.workflow.multibranch.WorkflowMultiBranchProject.class).eachWithIndex { it, iCount ->
  //println "$iCount, $it.fullName, $it.disabled"
  //if(it.fullName.contains('ESSAuthSolR'))
  if(it.fullName.contains('MastercardDigitalEnablementService'))
  {
    if(!it.disabled)
    {
      it.makeDisabled(true)
      println "\t $iCount, $it.fullName, $it.name is DISABLED"
    }
  }
}
return
/////////////
import jenkins.model.Jenkins

println "Multibranch Items\n-------"
Jenkins.instance.getAllItems(org.jenkinsci.plugins.workflow.multibranch.WorkflowMultiBranchProject.class).eachWithIndex { it, iCount ->
  //println "$iCount, $it.fullName, $it.disabled"
  //if(it.fullName.contains('ESSAuthSolR'))
  if(it.fullName.contains('MastercardDigitalEnablementService'))
  {
    if(!it.disabled)
    {
      it.makeDisabled(true)
      println "\t $iCount, $it.fullName, $it.name is DISABLED"
    }
  }
}
return
**********************/