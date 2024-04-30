import java.text.SimpleDateFormat
def iCount = 1

def sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss.SSS")
def startDate = new Date()
println "Start time : ${sdf.format(startDate)}"
//-------------------------------------------------

println "========================================================"
println "Find all builds performed within that past 24 hours whose console output contains the text listed in the search binding."
println "========================================================"


/*
   Find all builds performed within that past 24 hours whose console output
   contains the text listed in the search binding.
 */
import hudson.console.PlainTextConsoleOutputStream
import java.io.ByteArrayOutputStream
import java.util.Date
import jenkins.model.Jenkins
import org.apache.commons.io.IOUtils
import org.jenkinsci.plugins.workflow.job.WorkflowJob
import org.jenkinsci.plugins.workflow.job.WorkflowRun

if(binding.hasVariable('out') && binding.hasVariable('build')) {
    search = build.buildVariableResolver.resolve('search')
}
int HOURS_IN_MS = 3600000
Date oneDayAgo = new Date(System.currentTimeMillis() - (24 * HOURS_IN_MS))

println Jenkins.instance.getAllItems(WorkflowJob).findAll {
    WorkflowRun build = it.builds.first()
    (new Date(build.startTimeInMillis)).after(oneDayAgo)
}.collect {
    it.builds.findAll { WorkflowRun build ->
        (new Date(build.startTimeInMillis)).after(oneDayAgo)
    }
}.flatten().findAll { WorkflowRun build ->
    ByteArrayOutputStream os = new ByteArrayOutputStream()
    InputStreamReader is = build.logText.readAll()
    PlainTextConsoleOutputStream pos = new PlainTextConsoleOutputStream(os)
    IOUtils.copy(is, pos)
    String text = os.toString()
    Boolean result = text.contains(search)
    os.close()
    is.close()
    pos.close()
    result
}*.absoluteUrl.join('\n')



//--------------------------------------------------
def endDate = new Date()
println "End time: ${sdf.format(endDate)}"

use(groovy.time.TimeCategory) 
{
    def duration = endDate - startDate
  	print "Elapsed time: Days: ${duration.days}, Hours: ${duration.hours}, Minutes: ${duration.minutes}, Seconds: ${duration.seconds}"
}