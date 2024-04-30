import java.text.SimpleDateFormat
def iCount = 1

def sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss.SSS")
def startDate = new Date()
println "Start time : ${sdf.format(startDate)}"

//---------------------------------------------------------------------------
//Displays all jobs and build parameters associated with the job
//---------------------------------------------------------------------------

//Master HostName and IP Address
println "Runnign on - ${java.net.InetAddress.getLocalHost()}"

//def allItems = Hudson.instance.items;
def allItems = Jenkins.instance.allItems;
println "Items Size - ${allItems.size()}"
for(item in allItems) 
{  
  try
  {
    prop = item.getProperty(ParametersDefinitionProperty.class)
    if(prop != null) 
    {
      println("--- Parameters for " + item.name + " ---")
      def strParams = ""
      for(param in prop.getParameterDefinitions()) 
      {
          try 
          {
              strParams += param.name + " - " + param.defaultValue + ", "
              println(param.name + " " + param.defaultValue)
          }
          catch(Exception e) 
          {
              println("Exception in " + param.name)
          }
      }
      println(iCount++ + ":   \"" + item.name + "\"   :   " + strParams)
      //println()
    }
  }
  catch(MissingMethodException mmexp)
  {
    //Do nothing in this case as we have scenarios when you could be just iterating a folder
    //No signature of method: com.cloudbees.hudson.plugins.folder.Folder.getProperty()
  }
  catch(Exception exp)
  {
    println "Exception raised - ${exp.message}" 
  }
}


def endDate = new Date()
println "End time: ${sdf.format(endDate)}"

use(groovy.time.TimeCategory) 
{
    def duration = endDate - startDate
  	print "Elapsed time: Days: ${duration.days}, Hours: ${duration.hours}, Minutes: ${duration.minutes}, Seconds: ${duration.seconds}"
}