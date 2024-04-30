/*
   Find all cron schedules in a Jenkins instance organized by frequency
   (weekly, daily, hourly, other).  This script is compatible with all Jenkins
   job types and recurses into Jenkins folders.
   This script was inspired by:
     https://gist.github.com/mgedmin/d6d271f4ee6ae82d9a86
 */

import hudson.model.Descriptor
import hudson.model.Job
import hudson.triggers.TimerTrigger
import java.util.regex.Pattern
import jenkins.model.Jenkins
import java.text.SimpleDateFormat
import java.util.Date

//import java.util.GregorianCalendar

jenkins = Hudson.instance
controller = Jenkins.getInstance().getComputer('').getHostName()
println "===================================================================================================================="
println "Find all cron schedules in a Jenkins instance organized by frequency   (weekly, daily, hourly, other)"
println "Report collected on the Controller with hostname - ${controller}"
println "===================================================================================================================="

def sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss.SSS")
def startDate = new Date()
println "Start time : ${sdf.format(startDate)}"

/*
   FUNCTIONS
 */

boolean patternMatchesAnyLine(String pattern, String text) {
    text.tokenize('\n').any { String line ->
        Pattern.compile(pattern).matcher(line.trim()).matches()
    }
}

boolean isWeeklySchedule(String cron) {
    patternMatchesAnyLine('^([^ ]+ +){2}\\* +\\* +[^*]+$', cron)
}

boolean isDailySchedule(String cron) {
    patternMatchesAnyLine('^([^ *]+ +){2}\\* +\\* +\\*+$', cron)
}

boolean isHourlySchedule(String cron) {
    patternMatchesAnyLine('^[^ ]+ +\\* +\\* +\\*+$', cron)
}

void displaySchedule(String frequency, Map schedules) {
    if(!schedules) {
        return
    }
    int half = ((80 - frequency.size()) / 2) - 1
    int padding = 80 - (2*half + frequency.size()) - 2
    println(['='*half, frequency, '='*half].join(' ') + '='*padding)
    schedules.each { k, v ->
        println(k)
        println('    ' + v.tokenize('\n').join('\n    '))
    }
    println('='*80)
}

/*
   MAIN LOGIC
 */


// Find all schedules.
Jenkins j = Jenkins.instance
Descriptor cron = j.getDescriptor(TimerTrigger)
Map<String, String> schedules = j.getAllItems(Job).findAll { Job job ->
  job?.triggers?.get(cron)
}.collect { Job job ->
  	//[ (job.fullName):  job.triggers.get(cron).spec ]
  	[ (job.url):  job.triggers.get(cron).spec ]
}.sum()


// Gather schedules organized by their frequency.
Map weekly = schedules.findAll { k, v ->
    isWeeklySchedule(v)
} ?: [:]
Map daily = schedules.findAll { k, v ->
    isDailySchedule(v)
} ?: [:]
Map hourly = schedules.findAll { k, v ->
    isHourlySchedule(v)
} ?: [:]
Map other = schedules.findAll { k, v ->
    !isWeeklySchedule(v) &&
    !isDailySchedule(v) &&
    !isHourlySchedule(v)
} ?: [:]

//Print totals
println "Total Weekly Schedules -  ${weekly.size()}"
println "Total Daily Schedules -  ${daily.size()}"
println "Total Hourly Schedules -  ${hourly.size()}"
println "Total Other Schedules -  ${other.size()}"

// Print out schedules after they're organized.
displaySchedule('Weekly Schedules', weekly)
displaySchedule('Daily Schedules', daily)
displaySchedule('Hourly Schedules', hourly)
displaySchedule('Other Schedules', other)


println "--------------------------------------------------"
def endDate = new Date()
println "End time: ${sdf.format(endDate)}"

use(groovy.time.TimeCategory) 
{
    def duration = endDate - startDate
  	print "Elapsed time: Days: ${duration.days}, Hours: ${duration.hours}, Minutes: ${duration.minutes}, Seconds: ${duration.seconds}"
}