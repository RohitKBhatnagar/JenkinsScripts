
//https://support.cloudbees.com/hc/en-us/articles/360055283971-Find-What-Triggered-Caused-a-Job-to-Build

//Find What Triggered/Caused a Job to Build

import hudson.model.Fingerprint.RangeSet
import hudson.model.Fingerprint.Range

Jenkins.instance.getAllItems(Job).each {
  try
  {
    it.getBuilds(new RangeSet(new Range(0,2))).each { build ->
      build.getCauses().each {
        if(it instanceof Cause.UserIdCause)
        {
          println "[${cause.getUserId()}] ${build}"
        }
        else
        {
          //println "[${'AUTOMATION'}] ${build}"
        }
      }
    }
  }
  catch(Exception ex)
  {
  }
}
return