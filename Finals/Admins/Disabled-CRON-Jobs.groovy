//CRON Jobs marked disabled by Users
//==================================
import hudson.triggers.*
TriggerDescriptor TIMER_TRIGGER_DESCRIPTOR = Hudson.instance.getDescriptorOrDie(TimerTrigger.class)

Jenkins.instance.getAllItems(Job).each{
  def jobBuilds=it.getBuilds()

      // Check the last build only
      jobBuilds[0].each { build ->
        def runningSince = groovy.time.TimeCategory.minus( new Date(), build.getTime() )
        def currentStatus = build.buildStatusSummary.message
        def cause = build.getCauses()[0] //we keep the cause

      //triggered by a user
      def user = cause instanceof Cause.UserIdCause? cause.getUserId():null;

      if( !user ) {
        println "[AUTOMATION] ${build}"
      }
      else
      {
        println "[${user}] ${build}"
      }

      //Timer Triggers
      def timerTrigger = it.getTriggers().get(TIMER_TRIGGER_DESCRIPTOR)

      if(( user ) && ( timerTrigger) && (it.disabled)) {
        println "${it.name} is disabled CRON job ${user}" 
      }
    }
}
return

////////////////////////////////////////////////////////////
//https://cd.mastercard.int/jenkins/job/JenkinsPlatformTeam/job/DisabledCronJobsNotifier/configure
import hudson.model.*    
import hudson.triggers.*

pipeline {
    agent any


    stages {
        stage('Disabled Cron Jobs') {
            steps {
                getdisabledcronjobs()
            }
        }
       
    }
}


void getdisabledcronjobs() {
    
    TriggerDescriptor TIMER_TRIGGER_DESCRIPTOR = Hudson.instance.getDescriptorOrDie(TimerTrigger.class)

    Jenkins.instance.getAllItems(Job).each{
   
        def jobBuilds=it.getBuilds()

        // Check the last build only
        jobBuilds[0].each { build ->
            def runningSince = groovy.time.TimeCategory.minus( new Date(), build.getTime() )
            def currentStatus = build.buildStatusSummary.message
            def cause = build.getCauses()[0] //we keep the cause

            //triggered by a user, is a cron job, and disabled
            def user = cause instanceof Cause.UserIdCause? cause.getUserId():null;      
            def timertrigger = it.getTriggers().get(TIMER_TRIGGER_DESCRIPTOR)
      
            if(( user) && (timertrigger) && (it.disabled)) {
                def recipient = user + '@mastercard.com'
                def controller = env.BUILD_URL.split('/')[2].split(':')[0]
                
                def body = """
                CURRENTLY DISABLED JENKINS CRON JOB
                Controller : ${controller}
                Job Name   : ${it.name}
                Job Path   : ${it.fullName}
                """
                println(body + " Last ran by" + " [${user}]");
                
                emailext (to: user, replyTo:'jenkins@mastercard.com', subject: "Disabled Jenkins Cron Job Reminder", body: body);
            }
        }
    }
}
