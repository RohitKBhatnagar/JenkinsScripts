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

===============

// NOTES:
// dryRun: to only list the jobs which would be changed
// daysToKeep:  If not -1, history is only kept up to this day.
// numToKeep: If not -1, only this number of build logs are kept.
// artifactDaysToKeep: If not -1 nor null, artifacts are only kept up to this day.
// artifactNumToKeep: If not -1 nor null, only this number of builds have their artifacts kept.

import jenkins.model.Jenkins

Jenkins.instanceOrNull.allItems(hudson.model.Job).eachWithIndex { job, jobCount ->
    if (job.isBuildable() && job.supportsLogRotator() && job.getProperty(jenkins.model.BuildDiscarderProperty) == null) {
      println "${jobCount}, Processing ,\"${job.fullDisplayName}\""
        /*if (!"true".equals(dryRun)) {
            // adding a property implicitly saves so no explicit one
            try {
                job.setBuildDiscarder(new hudson.tasks.LogRotator ( daysToKeep, numToKeep, artifactDaysToKeep, artifactNumToKeep))
                println "${job.fullName} is updated"
            } catch (Exception e) {
                // Some implementation like for example the hudson.matrix.MatrixConfiguration supports a LogRotator but not setting it
                println "[WARNING] Failed to update ${job.fullName} of type ${job.class} : ${e}"
            }
        }*/
    }
}
return;

=========================

import jenkins.model.Jenkins

Jenkins.instanceOrNull.allItems(hudson.model.Job).eachWithIndex 
{ 
    job, jobCount ->
    if (job.isBuildable() && job.supportsLogRotator() && job.getProperty(jenkins.model.BuildDiscarderProperty) == null) 
    {
        if (!"true".equals(dryRun)) 
        {
            try 
            {
                /*int idaysToKeep = 1;
                int inumToKeep = 1;
                int iartifactDaysToKeep = 1;
                int iartifactNumToKeep = 1;
                if(daysToKeep != null)
                    idaysToKeep = daysToKeep.toInteger()
                if(numToKeep != null)
                    inumToKeep = numToKeep.toInteger();
                if(artifactDaysToKeep != null)
                    iartifactDaysToKeep = artifactDaysToKeep.toInteger();
                if(artifactNumToKeep != null)
                    iartifactNumToKeep = artifactNumToKeep.toInteger();*/
              //job.setBuildDiscarder(new hudson.tasks.LogRotator ( daysToKeep.toInteger(), numToKeep, artifactDaysToKeep, artifactNumToKeep))
              job.setBuildDiscarder(new hudson.tasks.LogRotator ( daysToKeepStr, numToKeepStr, artifactDaysToKeepstr, artifactNumToKeepStr))
              println "${jobCount}, Updating Log Rotator settings... ,\"${job.fullDisplayName}\""
            } catch (Exception e) {
                // Some implementation like for example the hudson.matrix.MatrixConfiguration supports a LogRotator but not setting it
                println "[WARNING] Failed to update ${job.fullName} of type ${job.class} : ${e}"
            }
        }
        else
        {
            println "${jobCount}, Needs update to Log Rotator settings for ,\"${job.fullDisplayName}\""
        }
    }
}
return;

------------------------------------------------------------
++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
============================================================
/*
** Author : Rohit K. Bhatnagar
** Purpose : Discard old builds
*/
import jenkins.model.Jenkins

Jenkins.instanceOrNull.allItems(hudson.model.Job).eachWithIndex 
{ 
    job, jobCount ->
    if (job.isBuildable() && job.supportsLogRotator() && job.getProperty(jenkins.model.BuildDiscarderProperty) == null) 
    {
        if (!"true".equals(dryRun)) 
        {
            try 
            {
              job.setBuildDiscarder(new hudson.tasks.LogRotator ( daysToKeepStr, numToKeepStr, artifactDaysToKeepStr, artifactNumToKeepStr))
              println "${jobCount}, Updating Log Rotator settings... ,\"${job.fullDisplayName}\""
            } 
            catch (Exception e) 
            {
              // Some implementation like for example the hudson.matrix.MatrixConfiguration supports a LogRotator but not setting it
              println "[WARNING] Failed to update ${job.fullName} of type ${job.class} : ${e}"
              
              /***
              try
              {
                if(job.class == 'org.jenkinsci.plugins.workflow.job.WorkflowJob')
                {
                  job.setBuildDiscarder(new hudson.tasks.LogRotator ( daysToKeepStr, numToKeepStr))
                  println "${jobCount}, Updating workflow jobs Log Rotator settings... ,\"${job.fullDisplayName}\""
                }
              } 
              catch(Exception ex)
              {
                println "[WARNING] Failed to update workflow project for ${job.fullName} of type ${job.class} : ${e}"
              }
        ***/
            }
        }
        else
        {
            println "${jobCount}, Needs update to Log Rotator settings for ,\"${job.fullDisplayName}\""
        }
    }
}
return;



==================
import hudson.model.*

 

Hudson.instance.getAllItems(org.jenkinsci.plugins.workflow.job.WorkflowJob).eachWithIndex { job, jobCount ->

   if(job.getBuildDiscarder() == null) {
     println "${jobCount}, Updating Log Rotator settings... ,\"${job.fullDisplayName}\""
   }
}

=====================
CD8 Signatures Approved
=======================
method hudson.model.Item getFullDisplayName
method hudson.model.Item getFullName
method hudson.model.ItemGroup allItems java.lang.Class
method hudson.model.Job getProperty java.lang.Class
method hudson.model.Job isBuildable
method hudson.model.Job setBuildDiscarder jenkins.model.BuildDiscarder
method hudson.model.Job supportsLogRotator
new hudson.tasks.LogRotator java.lang.String java.lang.String java.lang.String java.lang.String
staticMethod jenkins.model.Jenkins getInstanceOrNull