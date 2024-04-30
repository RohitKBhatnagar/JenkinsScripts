#!Groovy

//Display timer triggers - This scripts displays the timer triggers for all the jobs in order to better arrange them.

import hudson.model.*
import hudson.triggers.*
 
for(item in Hudson.instance.allItems) {
  try
  {
    for(trigger in item.triggers.values()) {
        if(trigger instanceof TimerTrigger) {
            println("--- Timer trigger for " + item.name + " ---")
            println(trigger.spec + '\n')
        }
    }
  }
  catch(Exception e)
  {
    //Do Nothing
  }
}