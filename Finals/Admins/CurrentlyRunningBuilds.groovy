//Currently running builds - https://stackoverflow.com/questions/40307037/how-to-get-a-list-of-running-jenkins-builds-via-groovy-script

runningBuilds = Jenkins.instance.getView('All').getBuilds().findAll() { it.getResult().equals(null) }

runningBuilds.eachWithIndex { it, itCount ->
  println "${itCount}, ${it.getAbsoluteUrl()}, ${it.getRootDir()}, ${it.getFullDisplayName()}"; //println "${it.name}"
}

return null