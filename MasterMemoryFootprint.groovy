import net.bull.javamelody.*;
import net.bull.javamelody.internal.model.*;
import net.bull.javamelody.internal.common.*;
 
import java.text.SimpleDateFormat
def iCount = 1

def sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss.SSS")
def startDate = new Date()
println "Start time : ${sdf.format(startDate)}"

memory = new MemoryInformations();
println "\nused memory:\n    " + Math.round(memory.usedMemory / 1024 / 1024) + " Mb";
println "\nmax memory:\n    " + Math.round(memory.maxMemory / 1024 / 1024) + " Mb";
println "\nused perm gen:\n    " + Math.round(memory.usedPermGen / 1024 / 1024) + " Mb";
println "\nmax perm gen:\n    " + Math.round(memory.maxPermGen / 1024 / 1024) + " Mb";
println "\nused non heap:\n    " +       Math.round(memory.usedNonHeapMemory / 1024 / 1024) + " Mb";
println "\nused physical memory:\n    " +       Math.round(memory.usedPhysicalMemorySize / 1024 / 1024) + " Mb";
println "\nused swap space:\n    " +       Math.round(memory.usedSwapSpaceSize / 1024 / 1024) + " Mb";

def endDate = new Date()
println "End time: ${sdf.format(endDate)}"

use(groovy.time.TimeCategory) 
{
    def duration = endDate - startDate
  	print "Elapsed time: Days: ${duration.days}, Hours: ${duration.hours}, Minutes: ${duration.minutes}, Seconds: ${duration.seconds}"
}