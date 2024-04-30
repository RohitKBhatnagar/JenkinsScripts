//Enforces build discarder rules
def daysToKeep = 1
def numToKeep = 5
def artifactDaysToKeep = 1
def artifactNumToKeep = -1

Jenkins.instance.allItems.each { item ->
  println("=====================")
  println("JOB: " + item.name + ", Type: " + item.getClass() + ", URL: " + item.getAbsoluteUrl())
  //println("Job type: " + item.getClass())

  if(item.buildDiscarder == null) {
    println("\t\tNo BuildDiscarder setting found, enforcing via Log Rotator")
  } else {
    //println("BuildDiscarder: " + item.buildDiscarder.getClass())
    println("\tCurrent setting: " + "days to keep=" + item.buildDiscarder.daysToKeepStr + "; num to keep=" + item.buildDiscarder.numToKeepStr + "; artifact day to keep=" + item.buildDiscarder.artifactDaysToKeepStr + "; artifact num to keep=" + item.buildDiscarder.artifactNumToKeepStr)
    //println("Set new setting")
  }

  item.buildDiscarder = new hudson.tasks.LogRotator(daysToKeep,numToKeep, artifactDaysToKeep, artifactNumToKeep)
  item.save()
  //println("")

}