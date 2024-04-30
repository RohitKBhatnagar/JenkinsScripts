//----------------------- BROKEN --------------------
import java.text.SimpleDateFormat
def iCount = 1

def sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss.SSS")
def startDate = new Date()
println "Start time : ${sdf.format(startDate)}"
//------------------------------------------------------------


println "---------------------------------------------------------------------------"
println "Check Disk Usage - put disk usage info and check used percentage."
println "---------------------------------------------------------------------------"

 
/*** BEGIN META {
  "name" : "Check Disk Usage",
  "comment" : "put disk usage info and check used percentage."
} END META**/
columns = ["path","size","used","free","used%"]
println columns.join("\t")

result = true;

File.listRoots().each{
  if (!"".equals(root) && !root.equals(it.getAbsolutePath())) 
    return
  percent = Math.ceil((it.getTotalSpace() - it.getFreeSpace()) * 100 / it.getTotalSpace())
  columns = []
  columns << iCount++ << ": "
  columns << it.getAbsolutePath()
  columns << Math.ceil(it.getTotalSpace() /1024/1024)
  columns << Math.ceil((it.getTotalSpace() - it.getFreeSpace()) /1024/1024)
  columns << Math.ceil(it.getFreeSpace() /1024/1024)
  columns << percent + "%"

  println columns.join("\t")
  
  if (percent >= Double.valueOf(threshold)) { result = false }
}

return result


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