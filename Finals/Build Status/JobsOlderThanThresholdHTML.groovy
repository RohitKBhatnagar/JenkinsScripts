#!groovy
//Collects and display list of projects that were built more than X days ago.

import java.text.SimpleDateFormat
def iCount = 0

def sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss.SSS")
def startDate = new Date()
println "Start time : ${sdf.format(startDate)}"
//-------------------------------------------------

String strMaster = java.net.InetAddress.getLocalHost();
println "==================================================================================================================="
println "Find builds older than 'X' days to mark as 'Disabled'/'Deleted' or simply identify that they are already 'Disabled'"
println ">>>>>>>>> CD Master - ${strMaster} <<<<<<<<<<<<<<<<<<<<<<<<<<<<<"
println "==================================================================================================================="

import hudson.slaves.*
import java.util.concurrent.*
import org.jenkinsci.plugins.workflow.job.WorkflowJob

import hudson.model.*
import hudson.maven.*
import hudson.tasks.*
import jenkins.model.Jenkins
import hudson.maven.reporters.*
import hudson.plugins.emailext.*

//Sending email from script console
import javax.mail.Session
import javax.mail.Message
import javax.mail.Transport
import javax.mail.internet.MimeMessage
import javax.mail.internet.InternetAddress

jenkins = Hudson.instance
def now=Calendar.instance;

def Date before30Days
def Date before90Days
def Date currentTime
use( groovy.time.TimeCategory ) 
{
  currentTime = new Date()
  before30Days = new Date().plus(-30)
  before90Days = new Date().plus(-90)
}

println "Current Time - ${currentTime}, Threshold to disable - ${before30Days}, Threshold to delete - ${before90Days}";

def list2Disable=[];
def list2Delete=[];
def listAlreadyDisabled=[];
 
def allItems = Jenkins.instance.getAllItems(WorkflowJob) 
def iItemCount = 0;

for (item in allItems)
{
  try
  {
    // Ignore project that contains freeze or patch case insensitive
    if (item.name ==~ /(?i)(freeze|patch).*/)
    {
        //println("\t Ignored as it is a freeze or patch build");
    }
    else if (!item.disabled && item.getLastBuild() != null)
    {
        if (new Date(item.getLastBuild().startTimeInMillis).before(before90Days)) //Builds older than 30 days
        {
            list2Delete<< item;
        }
        if (new Date(item.getLastBuild().startTimeInMillis).before(before30Days)) //Builds older than 30 days
        {
            list2Disable<< item;
        }
    }
    else if (item.disabled)
    {
        listAlreadyDisabled<< item;
    }
  }
  catch(Exception e)
  {
      println "Exception raised ... - ${e.message}";
  }
}

String strHTMLReportStart = "<!doctype html><html lang=\"en\"> <body>";
String strHTMLTableStart = "<table style=100% border=\"1\">";
String strHTMLTableEnd = "</table>";
String strHTMLCaptionStart = "<caption><b>";
String strHTMLCaptionEnd = "</b></caption>";
String strHTMLCaption = "";
String strHTMLTableHeader = "<tr><th>S.No.</th><th>Name</th><th>Job#</th><th>Build_Time</th><th>Result</th><th>Executed_By</th><th>Build_Folder</th></tr>";
String strHTMLReportEnd = "</body></html>"
String strHTMLLine = "<hr style=\"height:2px;border-width:0;color:gray;background-color:gray\">";
String strHTMLData = "";
String strHTMLBlankRow = "<tr/>"
String strHTMLReport = "";

//------------------ Lets begin with HTML report -----------
strHTMLReport = strHTMLReportStart;
if (list2Delete.size()>0)
{
  strHTMLCaption = "";
  println("Please take a look at following projects which need to be deleted as they are over 90 days old!");
  strHTMLCaption = "Builds older than ${before90Days}";
  def i = 0;
  println("\t S.No., Name, Job#, Build_Time, Result, Executed_By, Build_Folder");
  strHTMLTableRow = "";
  for (item in list2Delete)
  {
      println("\t ${i++}, ${item.name}, ${item.displayName}, ${item.getLastBuild().getNumber()}, ${item.getLastBuild().time}, ${item.getLastBuild().getResult()}, ${findCause(item.getLastBuild())}, ${item.getLastBuild().getRootDir()}");
      strHTMLTableRow += "<tr><td>${i++}</td><td>${item.name}</td><td>${item.getLastBuild().getNumber()}</td><td>${item.getLastBuild().time}</td><td>${item.getLastBuild().getResult()}</td><td>${findCause(item.getLastBuild())}</td><td>${item.getLastBuild().getRootDir()}</td></tr>";
  }

  strHTMLReport += strHTMLCaptionStart + strHTMLCaption + strHTMLCaptionEnd + strHTMLTableStart + strHTMLTableHeader + strHTMLTableRow + strHTMLTableEnd + strHTMLBlankRow + strHTMLLine + strHTMLBlankRow;
}

if (list2Disable.size()>0)
{
  strHTMLCaption = "";
  println("Please take a look at following projects which need to be disabled as they are over 30 days old!");
  strHTMLCaption = "Builds older than ${before30Days}";
  def i = 0;
  //println("\t S.No., Name, Job#, Build_Time, Result, Executed_By");
  println("\t S.No., Name, Job#, Build_Time, Result, Executed_By, Build_Folder");
  strHTMLTableRow = "";
  for (item in list2Disable)
  {
    //println("\t ${i++}, ${item.name}, ${item.getLastBuild().getNumber()}, ${item.getLastBuild().time}, ${item.getLastBuild().getResult()}, ${findCause(item.getLastBuild())}");
    println("\t ${i++}, ${item.name}, ${item.getLastBuild().getNumber()}, ${item.getLastBuild().time}, ${item.getLastBuild().getResult()}, ${findCause(item.getLastBuild())}, ${item.getLastBuild().getRootDir()}");
    strHTMLTableRow += "<tr><td>${i++}</td><td>${item.name}</td><td>${item.getLastBuild().getNumber()}</td><td>${item.getLastBuild().time}</td><td>${item.getLastBuild().getResult()}</td><td>${findCause(item.getLastBuild())}</td><td>${item.getLastBuild().getRootDir()}</td></tr>";
  }

  strHTMLReport += strHTMLCaptionStart + strHTMLCaption + strHTMLCaptionEnd + strHTMLTableStart + strHTMLTableHeader + strHTMLTableRow + strHTMLTableEnd + strHTMLBlankRow + strHTMLLine + strHTMLBlankRow;
}

if (listAlreadyDisabled.size()>0)
{
  strHTMLCaption = "";
  println("Please take a look at following projects which are already disabled...");
  strHTMLCaption = "Jobs currently marked DISABLED!";
  def i = 0;
  strHTMLTableRow = "";
  String strHTMLTableDHdr = "<tr><th>S.No.</th><th>Name</th></tr>";
  for (item in list2Disable)
  {
    println("\t ${i++} - ${item.name}");
    strHTMLTableRow += "<tr><td>${i++}</td><td>${item.name}</td></tr>"
    //println("\t ${i++}, ${item.name}, ${item.getLastBuild().getNumber()}, ${item.getLastBuild().time}, ${item.getLastBuild().getResult()}, ${findCause(currentBuild)}");
  }

  strHTMLReport += strHTMLCaptionStart + strHTMLCaption + strHTMLCaptionEnd + strHTMLTableStart + strHTMLTableDHdr + strHTMLTableRow + strHTMLTableEnd + strHTMLBlankRow + strHTMLLine + strHTMLBlankRow;
}

println "Lets send the email notification out..."
try
{
    // String strMessage = "Build counts before ${before30Days} - ${list2Disable.size()}    \n\
    // Build counts before ${before90Days} - ${list2Delete.size()}    \n\
    // Jobs counts which are already disabled - ${listAlreadyDisabled.size()}"
    String strHTMLTableCHdr = "<tr><th>Build count before ${before90Days}</th></tr>"
    String strHTMLTableCRow = "<tr><td>${list2Delete.size()}</td></tr>";
    String strHTMLCounts = strHTMLCaptionStart + "Builds counts..." + strHTMLCaptionEnd + strHTMLTableStart + strHTMLTableCHdr + strHTMLTableCRow + strHTMLTableEnd + strHTMLBlankRow;
    strHTMLTableCHdr = "<tr><th>Build count before ${before30Days}</th></tr>"
    strHTMLTableCRow = "<tr><td>${list2Disable.size()}</td></tr>";
    strHTMLCounts += strHTMLTableStart + strHTMLTableCHdr + strHTMLTableCRow + strHTMLTableEnd + strHTMLBlankRow;
    strHTMLTableCHdr = "<tr><th>Jobs disabled</th></tr>"
    strHTMLTableCRow = "<tr><td>${listAlreadyDisabled.size()}</td></tr>";
    strHTMLCounts += strHTMLTableStart + strHTMLTableCHdr + strHTMLTableCRow + strHTMLTableEnd + strHTMLBlankRow;

    strHTMLReport += strHTMLCounts + strHTMLReportEnd;
    //Send final email for the controller
    //sendEmail("Rohit.Bhatnagar@mastercard.com", "Build details for " + strMaster, strMessage);
    sendEmail("Rohit.Bhatnagar@mastercard.com", "Build details for " + strMaster, strHTMLReport);
}
catch(Exception e)
{
  println "Exception raised when sending email... - ${e.message}";
}

//--------------------------------------------------
def endDate = new Date()
println "End time: ${sdf.format(endDate)}"

use(groovy.time.TimeCategory) 
{
    def duration = endDate - startDate
      print "Elapsed time: Days: ${duration.days}, Hours: ${duration.hours}, Minutes: ${duration.minutes}, Seconds: ${duration.seconds}"
}

return;

//------------ https://medium.com/faun/how-to-get-jenkins-build-job-details-b8c918087030 ---------
def findCause(upStreamBuild) {
    //Check if the build was triggered by SCM change
    scmCause = upStreamBuild.getCause(hudson.triggers.SCMTrigger.SCMTriggerCause)
    if (scmCause != null) {
        return scmCause.getShortDescription()
    }//Check if the build was triggered by timer
    timerCause = upStreamBuild.getCause(hudson.triggers.TimerTrigger.TimerTriggerCause)
    if (timerCause != null) {
        return timerCause.getShortDescription()
    }//Check if the build was triggered by some jenkins user
    usercause = upStreamBuild.getCause(hudson.model.Cause.UserIdCause.class)
    if (usercause != null) {
        return usercause.getUserId()
    }//Check if the build was triggered by some jenkins project(job)
    upstreamcause = upStreamBuild.getCause(hudson.model.Cause.UpstreamCause.class)
    if (upstreamcause != null) {
        job = Jenkins.getInstance().getItemByFullName(upstreamcause.getUpstreamProject(), hudson.model.Job.class)
        if (job != null) {
            upstream = job.getBuildByNumber(upstreamcause.getUpstreamBuild())
            if (upstream != null) {
                return upstream
            }
        }
    }
    return;
}

//---------------------- Send email to recipient -------------
def sendEmail(def sTo, def sSubject, def sMsg)
{
    try
    {
        println "${sTo} - ${sSubject} - ${sMsg}";
        def descriptor = Jenkins.instance.getDescriptor("hudson.tasks.Mailer")

        Session session = descriptor.createSession();
        MimeMessage msg = new MimeMessage(session);

        InternetAddress fromAddress = new InternetAddress(descriptor.getAdminAddress());

        msg.setFrom(fromAddress)

        //msg.setRecipients(MimeMessage.RecipientType.TO, (InternetAddress[]) [new InternetAddress('Rohit.Bhatnagar@mastercard.com')].toArray());
        msg.setRecipients(MimeMessage.RecipientType.TO, (InternetAddress[]) [new InternetAddress("${sTo}")].toArray());

        String charset = descriptor.getCharset();
        //msg.setSubject("Test", charset);
        msg.setSubject("${sSubject}", charset);
        //msg.setText("Hello from Jenkins!", charset)
        //msg.setText("${sMsg}", charset)
        msg.setText("${sMsg}", "utf-8", "html");
        //msg.setContent("${sMsg}", "text/html") //we should invoke the setContent(Object obj, String type) method of the MimeMessage object. The setContent() method specifies the mime type of the content explicitly, and for HTML format, the type parameter must be "text/html".

        Transport transporter = session.getTransport("smtp");
        transporter.connect();
        transporter.send(msg);
    }
    catch(Exception  e)
    {
        println "Exception raised when preparing email content... - ${e.message}";
    }

    return;
}