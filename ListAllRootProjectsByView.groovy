//******************************* BROKEN *************************************************
import java.text.SimpleDateFormat
def iNodeCount = 1
def iQItemsCount = 1

def sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss.SSS")
def startDate = new Date()
println "Start time : ${sdf.format(startDate)}"

println "-----------------------------------------------------------------------------------------------------------------------------------------"
println "List all root projects by view and discover projects which are not associated with a view."
println "-----------------------------------------------------------------------------------------------------------------------------------------"


/*
   List all root projects by view and discover projects which are not
   associated with a view.
 */

import hudson.model.Job
import hudson.plugins.git.GitSCM
import hudson.scm.NullSCM
import hudson.scm.SCM
import jenkins.model.Jenkins
//import org.jenkinsci.plugins.multiplescms.MultiSCM

void printSCM(Job j, SCM scm) {
    switch(scm) {
        case NullSCM:
            println "    ${j.displayName} has no SCM configured."
            break
        case GitSCM:
            println "    ${j.displayName} -> ${scm.repositories[0].URIs[0]}"
            break
        // case MultiSCM:
        //     scm.configuredSCMs.each { s ->
        //         printSCM(j, s)
        //     }
        //     break
        default:
            //throw new Exception("SCM class not supported: ${scm.class}")
            println "EXCEPTION --- SCM class not supported: ${scm.class}"
            break
    }
}

j = Jenkins.instance

discovered_jobs = [].toSet()

Jenkins.instance.views.findAll 
{ v ->
    v.displayName != j.primaryView.displayName
}.each 
{ v ->
    println v.displayName
    v.items.each { j ->
        discovered_jobs << j
        printSCM(j, j.scm)
    }
}

println "Jobs under primary view '${j.primaryView.displayName}' not covered by other views"
((j.primaryView.items as Set) - discovered_jobs).each { j ->
    printSCM(j, j.scm)
}

//null because we don't want a return value in the Script Console
null


def endDate = new Date()
println "End time: ${sdf.format(endDate)}"

use(groovy.time.TimeCategory) 
{
    def duration = endDate - startDate
  	print "Elapsed time: Days: ${duration.days}, Hours: ${duration.hours}, Minutes: ${duration.minutes}, Seconds: ${duration.seconds}"
}