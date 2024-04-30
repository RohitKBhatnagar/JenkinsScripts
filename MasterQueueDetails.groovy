import net.bull.javamelody.*;
import net.bull.javamelody.internal.model.*;
import net.bull.javamelody.internal.common.*;
 
import java.text.SimpleDateFormat
def iCount = 1

def sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss.SSS")
def startDate = new Date()
println "Start time : ${sdf.format(startDate)}"

//https://javadoc.jenkins-ci.org/hudson/model/Queue.Item.html
def queue = Hudson.instance.queue
//println "API ${queue.items.api}"
println "Item ID ${queue.items.id}"
println "Params ${queue.items.params}"
println "Assigned Label ${queue.items.assignedLabel}"
println "Display Name ${queue.items.displayName}"
println "In queue since ${queue.items.inQueueSince}"
println "In queue since string  ${queue.items.inQueueForString}"
println "URL  ${queue.items.url}"
println "Search URL  ${queue.items.searchUrl}"
println "Why in queue  ${queue.items.why}"
println "Is it blocked  ${queue.items.blocked}"
println "Is it buildable  ${queue.items.buildable}"
println "Is it stuck  ${queue.items.stuck}"

println "Queue contains total ${queue.items.length} items"

def endDate = new Date()
println "End time: ${sdf.format(endDate)}"

use(groovy.time.TimeCategory) 
{
    def duration = endDate - startDate
  	print "Elapsed time: Days: ${duration.days}, Hours: ${duration.hours}, Minutes: ${duration.minutes}, Seconds: ${duration.seconds}"
}