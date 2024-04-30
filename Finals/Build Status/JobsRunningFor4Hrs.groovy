/* Jobs runnign more than 4 HRS
We had to write this script several times. Time to have it stored, it is a very simple approach but will serve as starting point for more refined approaches. 
*/


println "Job Name, Job URL, Execution Time (HH:MM)"
Jenkins.instance.getAllItems(Job).each(){ job -> job.isBuildable()

  if (job.isBuilding()){

    def myBuild= job.getLastBuild()

    def runningSince= groovy.time.TimeCategory.minus( new Date(), myBuild.getTime() )

    if (runningSince.hours >= 4){

      println "${job.name}, ${job.url}, ${runningSince.hours}:${runningSince.minutes}"
    }
  }
}
return null