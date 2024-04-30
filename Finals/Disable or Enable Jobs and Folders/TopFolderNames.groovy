import java.text.SimpleDateFormat

def sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss.SSS")
def startDate = new Date()
println "Start time : ${sdf.format(startDate)}"

//--------------------------------------------------

String strMaster = java.net.InetAddress.getLocalHost();
println "==================================================="
println "Displays all TOP level folders on the controller"
println ">>>>> CD Master - ${strMaster} <<<<<<<<<<<<<<<<<<<<"
println "==================================================="


def topItems = Jenkins.instance.getItems()
topItems.eachWithIndex {
	itTop, iTopCount ->
		println "${iTopCount} :: ${itTop.name}";
}

//--------------------------------------------------
def endDate = new Date()
println "End time: ${sdf.format(endDate)}"

use(groovy.time.TimeCategory) 
{
    def duration = endDate - startDate
  	print "Elapsed time: Days: ${duration.days}, Hours: ${duration.hours}, Minutes: ${duration.minutes}, Seconds: ${duration.seconds}"
}

return null;
