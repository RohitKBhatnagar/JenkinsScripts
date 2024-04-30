import java.text.SimpleDateFormat

println "Master HostName and IP Address - ${java.net.InetAddress.getLocalHost()}"

def sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss.SSS")
def startDate = new Date()
println "Start time : ${sdf.format(startDate)}"

//---------------------------------------------------------------------------
//Displays plugins details for everything installed on the controller

println "---------------------------------------------------------------------------"
println ""

println "S.No., Short Name, Long Name, Version, Git URL"
Jenkins.instance.getPluginManager().getPlugins().eachWithIndex { it, i ->
  println "${i}, ${it.getShortName()}, ${it.getLongName()}, ${it.getVersion()}, ${it.getUrl()}"
};
  
println "---------------------------------------------------------------------------"
println ""
//---------------------------------------------------------
def endDate = new Date()
println "End time: ${sdf.format(endDate)}"

use(groovy.time.TimeCategory) 
{
    def duration = endDate - startDate
  	print "Elapsed time: Days: ${duration.days}, Hours: ${duration.hours}, Minutes: ${duration.minutes}, Seconds: ${duration.seconds}"
}

return null;