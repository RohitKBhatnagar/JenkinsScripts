#!Groovy
import java.text.SimpleDateFormat
def iCount = 0

def sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss.SSS")
def startDate = new Date()
println "Start time : ${sdf.format(startDate)}"
//-------------------------------------------------

String strMaster = java.net.InetAddress.getLocalHost();
println "================================================================================================="
println "Find all builds performed within  past 24 hours and then prints the build log for each build job!"
println ">>>>>>>>> CD Master - ${strMaster} <<<<<<<<<<<<<<<<<<<<<<<<<<<<<"
println "================================================================================================="

import hudson.console.PlainTextConsoleOutputStream
import java.io.ByteArrayOutputStream
import java.util.Date
import jenkins.model.Jenkins
import org.apache.commons.io.IOUtils
import org.jenkinsci.plugins.workflow.job.WorkflowJob
import org.jenkinsci.plugins.workflow.job.WorkflowRun
//import org.groovy.json

class LabelBuild 
{
    long Count
    String Name
    String StartTime
    String StatusSummary
    String Duration
    String HostName
    String Label
}

def slave_label_map = [:]
for (slave in Jenkins.instance.slaves) 
{
    //println "${iCount++},${slave.nodeName},${slave.labelString},${slave.numExecutors}" //Prints the LABELS associated and EXECUTORS count in a single line for each slave
    words = slave.labelString.split()
    def labelListForSlave = []
    words.each() 
    {
        labelListForSlave.add(it);
    }
    slave_label_map.put(slave.name, labelListForSlave)
}

//println "slave_label_map - ${slave_label_map}"

// slave_label_map.eachWithIndex{
//     entry, index ->
//   println "${index} : ${entry.key} - ${entry.value}"
// }

def Date before60Minutes
use( groovy.time.TimeCategory ) 
{
    before60Minutes = new Date() - 60.minutes
}

ArrayList<LabelBuild> lstLblBlds = new ArrayList<LabelBuild>();
def allItems = Jenkins.instance.getAllItems(WorkflowJob) 
//def allItems = Jenkins.instance.getAllItems()
println "Total Items - ${allItems.size()}"
try
{
    def myItems = allItems.findAll 
    {
        try
        {
            WorkflowRun build = it.builds.first()
            new Date(build.startTimeInMillis).after(before60Minutes)
            //println "------------------ ${iCount++}:    ${build} -------------------------"
        }
        catch(Exception exp)
        {
            //println("${it.class.simpleName} - ${exp.message}")
        }
    }.collect 
    {
        it.builds.findAll 
        { 
            WorkflowRun build ->
            new Date(build.startTimeInMillis).after(before60Minutes)
        }
    }
    .flatten().findAll 
    { 
        WorkflowRun build ->
        iCount++
        //println "------------------ ${iCount}:    ${build} - Build-Time: ${build.time} - Build-Status: ${build.getBuildStatusSummary().message} -------------------------"
        ByteArrayOutputStream os = new ByteArrayOutputStream()
        InputStreamReader is = build.logText.readAll()
        PlainTextConsoleOutputStream pos = new PlainTextConsoleOutputStream(os)
        IOUtils.copy(is, pos)
        String text = os.toString()
        //println "${text}"
        def sHostName = ""
        def sLabel = ""
      	if(text.contains("Running on"))
        {
          def srchTxt = /jnk.*/
      	  def findNode = (text =~ /$srchTxt/) 
          String sHostString = findNode[0]//.toString()
          //println sHostString
          int iIndex = sHostString.indexOf('in', 0)
          if(iIndex > 0) {
            sHostName = sHostString.substring(0, iIndex - 1)
            sLabel = slave_label_map.find { it.key == sHostName }?.value
          }
          //sHostName = sHostString.substring(0, sHostString.indexOf('in', 0) - 1)

          //sLabel = slave_label_map.find{ it.key == sHostName }?.value
          //println "${sHostName} - ${sLabel}"
          	////=~ /jnk\w{6}/) //text.contains("JNK") //startsWith("JNK")
          //println "${findNode[0]} - ${sHostName}"
          //*.toString()
        }
      	//println ""
      	//println ""
        os.close()
        is.close()
        pos.close()

        //Add all details to the List<LabelBuild>
        //def myLabelBuild = new LabelBuild(Count:iCount, Name:build, StartTime: new Date(build.time), StatusSummary:build.getBuildStatusSummary().message)
      	def myLabelBuild = new LabelBuild(Count:iCount, Name:build, StartTime:build.time, StatusSummary:build.getBuildStatusSummary().message, HostName:sHostName, Label:sLabel, Duration: build.getDurationString());
      	//println "Class - ${myLabelBuild}"
        lstLblBlds.add(myLabelBuild)
      	//println "List - ${lstLblBlds}"
        //println "${iCount} - ${build} - ${build.time} - ${build.getBuildStatusSummary().message} - ${sHostName} - ${sLabel} - ${build.getDurationString()}"
    }.absoluteUrl.join('\n')

  //println "Hello"
  //println "Label builds so far are ... ${lstLblBlds}"
  	//[Count: it.Count, Name: it.Name, StartTime: it.StartTime, StatusSummary: it.StatusSummary]
  	//def jsonLabels = new groovy.json.JsonBuilder()//JsonSlurper()
    // jsonLabels {Labels(
    //     lstLblBlds.collect {
    //         [Count: it.Count, Name: it.Name, StartTime:it.StartTime, StatusSummary: it.StatusSummary, Duration:it.Duration, HostName: it.HostName, Label:it.Label, Master: strMaster]
    //     })
    // }
    
    // lstLblBlds.each{ it->
    //     println "${it.Count} - ${it.Name}"
    // }
    lstLblBlds.each{it->
        //jsonLabels."LabelStats" {Count: it.Count, Name: it.Name, StartTime:it.StartTime, StatusSummary: it.StatusSummary, Duration:it.Duration, HostName: it.HostName, Label:it.Label, Master: strMaster}
        //jsonLabels."LabelStats" {"Count: ${it.Count}, Name: $"{it.Name}", StartTime:${it.StartTime}, StatusSummary: ${it.StatusSummary}, Duration:${it.Duration}, HostName: ${it.HostName}, Label:${it.Label}, Master: ${strMaster}"}
        println "${it.Count} - ${it.Name} - ${it.StartTime}"
        def myCount = it.Count
        println myCount
        def myName = it.Name
        println myName
        def myStart = it.StartTime
        println myStart
        if(myCount)
        {
            def jsonLabels = new groovy.json.JsonBuilder()
            jsonLabels."LabelStats" {
                "Count" myCount 
                "Name" myName
                "StartTime" myStart
                }
            //jsonLabels."LabelStats" {"Count" $myCount, "Name" $myName} //EXCEPTION - No such property: $myCount for class: groovy.json.JsonDelegate
            println "${jsonLabels.toPrettyString()}"
            def sout = new StringBuilder(), serr = new StringBuilder()
            def proc = "curl -f -k \"https://hec.splunk.mclocal.int:13510/services/collector/raw\" -H \"Authorization: Splunk a0ec61b4-cb16-4a62-a159-9c3982d11153\" --data \"${jsonLabels}\"".execute()
            proc.consumeProcessOutput(sout, serr)
            proc.waitForOrKill(1000)
            println "out> $sout err> $serr"
        }
    }

    println "${jsonLabels.toPrettyString()}"

    //jsonLabel.("Count": iCount, "Build")
    println "Build count after - ${before60Minutes} are - ${iCount - 1}" //${myItems.size()}"
  
  //echo "curl -f --data \"${jsonLabels}\" -k https://hec.splunk.mclocal.int:13510/services/collector/raw -H \"Authorization: Splunk a0ec61b4-cb16-4a62-a159-9c3982d11153\""
  
//   def sout = new StringBuilder(), serr = new StringBuilder()
//   def proc = "curl -f --data \"${jsonLabels}\" -k \"https://hec.splunk.mclocal.int:13510/services/collector/raw\" -H \"Authorization: Splunk a0ec61b4-cb16-4a62-a159-9c3982d11153\"".execute()
//   proc.consumeProcessOutput(sout, serr)
//   proc.waitForOrKill(1000)
//   println "out> $sout err> $serr"
}
catch(Exception exp)
{
    println "EXCEPTION - ${exp.message}"
}

//--------------------------------------------------
def endDate = new Date()
println "End time: ${sdf.format(endDate)}"

use(groovy.time.TimeCategory) 
{
    def duration = endDate - startDate
  	print "Elapsed time: Days: ${duration.days}, Hours: ${duration.hours}, Minutes: ${duration.minutes}, Seconds: ${duration.seconds}"
}

// @NonCPS
// def before60Minutes ()
// {
//     use( groovy.time.TimeCategory )
//     {
//         return 60.minutes.ago
//     }
// }