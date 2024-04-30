//################################ BROKEN ########################################
import java.text.SimpleDateFormat
def iCount = 1
def labelCount = 1

def sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss.SSS")
def startDate = new Date()
println "Start time : ${sdf.format(startDate)}"

println "---------------------------------------------------------------------------"
println "This reveals in Jenkins which projects are using a Jenkins agent with a particular label.  Useful for finding all projects using a particular set of agents."
println "---------------------------------------------------------------------------"

 
/*
   This reveals in Jenkins which projects are using a Jenkins agent with a
   particular label.  Useful for finding all projects using a particular set of
   agents.
*/
// import hudson.model.Job

// if(!binding.hasVariable('labels')) {
//     labels = ['language:shell', 'language:ruby']
// }
// if(!binding.hasVariable('evaluateOr')) {
//     //by default it searches for any of the labels from the list
//     //set evaluateAnd to true to require all labels much exist in the job
//     evaluateOr = true
// }
// if(!binding.hasVariable('formatJervis')) {
//     //Are Jenkins jobs organized when generated by Jervis?
//     //https://github.com/samrocketman/jervis
//     formatJervis = false
// }

// if(labels in String) {
//     labels = (labels.contains(',')) ? labels.split(',') : [labels]
// }
// if(evaluateOr in String) {
//     evaluateOr = (evaluateOr != 'false')
// }
// if(formatJervis in String) {
//     formatJervis = (formatJervis != 'false')
// }

// //type check user defined parameters/bindings
// if(!(labels in List) || (false in labels.collect { it in String } )) {
//     throw new Exception('PARAMETER ERROR: labels must be a list of strings.')
// }
// if(!(evaluateOr in Boolean)) {
//     throw new Exception('PARAMETER ERROR: evaluateOr must be a boolean.')
// }
// if(!(formatJervis in Boolean)) {
//     throw new Exception('PARAMETER ERROR: formatJervis must be a boolean.')
// }

projects = [] as Set
//getAllItems searches a global lookup table of items regardless of folder structure
Jenkins.instance.getAllItems(Job.class).each { Job job ->
    Boolean labelFound = false
    String jobLabelString
    if(job.class.simpleName == 'FreeStyleProject') {
        jobLabelString = job.getAssignedLabelString()
        println "${iCount++}: FreeStyle Project - ${jobLabelString}"
    } else if(job.class.simpleName == 'WorkflowJob') {
        //jobLabelString = job.getDefinition().getScript()
        jobLabelString = job.getDefinition().getDescriptor().getConfigFile()
        println "${iCount++}: WorkFlow Job Definition - ${job.getDefinition()} Descriptor - ${job.getDefinition().getDescriptor()} Config File - ${jobLabelString}"
    } else {
        //throw new Exception("Don't know how to handle class: ${job.getClass()}")
        println "${iCount++}:  Unknown Job class: ${job.getClass()}"
    }
    // List results = labels.collect 
    // { label ->
    //     try {
    //         jobLabelString.contains(label)
    //     }
    //     catch(Exception exp)
    //     {
    //         //Do nothing
    //     }
    // }

    // if(evaluateOr) {
    //     //evaluate if any of the labels exist in job
    //     labelFound = true in results
    // } else {
    //     //evaluate requiring all labels to exist in job
    //     labelFound = !(false in results)
    // }

    // if(labelFound) {
    //     if(formatJervis) {
    //         projects << "${labelCount}: ${job.fullName.split('/')[0]}/${job.displayName.split(' ')[0]}"
    //     } else {
    //         projects << "${labelCount}: ${job.fullName}"
    //     }
    //     labelCount++
    // }
}
println(projects.join('\n'))
//null so no result shows up
null


def endDate = new Date()
println "End time: ${sdf.format(endDate)}"

use(groovy.time.TimeCategory) 
{
    def duration = endDate - startDate
  	print "Elapsed time: Days: ${duration.days}, Hours: ${duration.hours}, Minutes: ${duration.minutes}, Seconds: ${duration.seconds}"
}