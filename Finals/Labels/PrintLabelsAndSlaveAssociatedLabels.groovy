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
 
def uniqueLabels = []
def slave_label_map = [:]
def executors_map = [:]
for (slave in Jenkins.instance.slaves) 
{
    println "${iCount++},${slave.nodeName},${slave.labelString},${slave.numExecutors}" //Prints the LABELS associated and EXECUTORS count in a single line for each slave
    words = slave.labelString.split()
    def labelListForSlave = []
    words.each() 
    {
        labelListForSlave.add(it);
        uniqueLabels.add(it)
    }
    slave_label_map.put(slave.name, labelListForSlave)
    executors_map.put(slave.name, slave.numExecutors)
}
//Adding this line to add 'EXECUTORS' as the last column entry to LABELS
uniqueLabels.add('EXECUTORS')

uniqueLabels.unique()

maxLen=0
uniqueLabels.each() 
{
    if (it.length() > maxLen) 
    {
        maxLen=it.length()
    } 
}

def vertLabels = []

for (int idx=0; idx < maxLen; idx++) 
{
    def vertLabel="|"
    uniqueLabels.each() 
    {
        if (idx < it.length()) 
        { 
            vertLabel+="${it[idx]}|" 
        } 
        else 
        {
            vertLabel+=" |"
        }
    }
    vertLabels.add(vertLabel)
}
    

def FIXED_FIRST_COLUMN = 40
 
vertLabels.each() 
{
    printSign(FIXED_FIRST_COLUMN-1, " ")
    print "${it}\n" 
}
printLine()
 
//Loop to display Slave-Name and associated label and executors
for ( entry in slave_label_map ) 
{
    def slaveName = entry.key
    print "${slaveName}"
    iAgents++
    printSign(FIXED_FIRST_COLUMN - entry.key.size()-1, " ")
    print "|"
    uniqueLabels.each() 
    { 
        lab ->
        boolean found = false
        entry.value.each() 
        { 
            valueList ->
            if(lab.equals(valueList)) 
            {
                found = true
            }
        }
        if(found) 
        {
            print "X"
        } 
        else 
        {
            if(!lab.equals('EXECUTORS')) //Added to adjust for 'EXECUTORS' entry
                print " "
        }
        if(!lab.equals('EXECUTORS')) //Added to adjust for 'EXECUTORS' entry
            print "|"
    }
    //Added to print the executors against each slave
    for( execEntry in executors_map )
    {
        if(execEntry.key == slaveName)
        {
            //println "${execEntry.key} - ${slaveName}"
            iExecutors += execEntry.value
            print "${execEntry.value}"
            print "|"
        }
    }
    printLine()
}
 
 
def printSign(int count, String sign) 
{
    for (int i = 0; i < count; i++) 
    {
        print sign
    }
}
 
def printLine() 
{
    print "\n";
    //printSign(120, "-")
    //print "\n";
}


println "${java.net.InetAddress.getLocalHost()} - Total Agents - ${iAgents} and total Executors - ${iExecutors}"


//--------------------------------------------------
def endDate = new Date()
println "End time: ${sdf.format(endDate)}"

use(groovy.time.TimeCategory) 
{
    def duration = endDate - startDate
  	print "Elapsed time: Days: ${duration.days}, Hours: ${duration.hours}, Minutes: ${duration.minutes}, Seconds: ${duration.seconds}"
}