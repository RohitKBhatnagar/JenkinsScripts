import java.text.SimpleDateFormat
def iCount = 1

def sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss.SSS")
def startDate = new Date()
println "Start time : ${sdf.format(startDate)}"
//------------------------------------------------------------


println "---------------------------------------------------------------------------"
println "Display Job names and associated Config file name with path"
println "---------------------------------------------------------------------------"

 
import jenkins.model.Jenkins
import hudson.tasks.*

import java.io.InputStream;
import java.io.FileInputStream
import java.io.File;
import javax.xml.transform.stream.StreamSource

Jenkins.instance.getAllItems(Job.class).each 
{ Job j ->
    try 
    {
        String jc = j.class.simpleName
        println "${iCount++}:   ------------- ${jc} -------------"

        def configXMLFile = j.getConfigFile();
        def file = configXMLFile.getFile();
        println "${file}"
        println ""
    }
    catch(Exception exp)
    {
        println "Exception raised - ${exp.message}"
    }
}


//So that we don't print some unnecessary outputs to the console
null

//----------------------------------------------------------
def endDate = new Date()
println "End time: ${sdf.format(endDate)}"

use(groovy.time.TimeCategory) 
{
    def duration = endDate - startDate
  	print "Elapsed time: Days: ${duration.days}, Hours: ${duration.hours}, Minutes: ${duration.minutes}, Seconds: ${duration.seconds}"
}