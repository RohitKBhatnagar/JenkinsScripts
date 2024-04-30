#!Groovy
import java.text.SimpleDateFormat
def iCount = 1
def iExecutors = 0
def iAgents = 0

def sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss.SSS")
def startDate = new Date()
println "Start time : ${sdf.format(startDate)}"
//--------------------------------------------------


/*** BEGIN META {
  "name" : "Show labels overview and executors count",
  "comment" : "Show an overview of all labels defined & executors count for each slave",
  "author" : "Rohit K. Bhatnagar"
} END META**/
import jenkins.model.Jenkins;


println "=========================================================================================="
println "Displays Labels defined and Slaves which have these Labels alongwith their total executors"
println "=========================================================================================="
println ">>>>>>>>> CD Master - ${java.net.InetAddress.getLocalHost()} <<<<<<<<<<<<<<<<<<<<<<<<<<<<<"
 
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

slave_label_map.eachWithIndex{
    entry, index ->
  println "${index} : ${entry.key} - ${entry.value}"
}


/**----------------- Play ---------------------------
*/
println "------------------------------"
println "------------------------------"
println ""
//=================
def mymap = [name:"Gromit", id:1234]
def x = mymap.find{ it.key == "name" }?.value
if(x)
    println "x value: ${x}"
def y = mymap.find{ it.value == "Gromit" }?.key
if(y)
    println "y value: ${y}"

println x.getClass().name
println y.getClass().name
return