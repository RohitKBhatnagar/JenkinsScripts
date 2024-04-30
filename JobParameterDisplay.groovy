#!Groovy

//Display job parameters - This scripts displays the parameters for all the jobs along with their default values (if applicable).

import hudson.model.*
import hudson.triggers.*
 
for(item in Hudson.instance.allItems) {
  try
  {
    prop = item.getProperty(ParametersDefinitionProperty.class)
    if(prop != null) {
      println("--- Parameters for " + item.name + " ---")
      for(param in prop.getParameterDefinitions()) {
        try {
          println(param.name + " " + param.defaultValue)
        }
        catch(Exception e) {
          println(param.name)
        }
      }
      println()
    }
  }
  catch(Exception e)
  {
    //Do Nothing
  }
}