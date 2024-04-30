//https://support.cloudbees.com/hc/en-us/articles/235690727-Why-are-jobs-on-the-queue-when-there-are-dedicated-agents-with-free-executors
for (i in Jenkins.instance.queue.buildableItems) {
  println "considering ${i}"
  for (c in Jenkins.instance.computers) {
    println "found computer ${c}"
    EXEC: for (e in c.executors) {
      if (e.interrupted || !e.parking) continue
      println "with executor ${e}"
      def o = new Queue.JobOffer(e)
      if (!o.canTake(i)) {
        println "${o} refused ${i}"
        def node = o.node
        if (node == null) {
          println "no node associated with ${c}"
          continue
        }
        def cob = node.canTake(i)
        if (cob != null) {
          println "because of ${cob}"
          continue
        }
        for (d in hudson.model.queue.QueueTaskDispatcher.all()) {
          cob = d.canTake(node, i)
          if (cob != null) {
            println "because of ${cob} from ${d}"
            continue EXEC
          }
        }
        if (!o.available) {
          println "${o} not available"
          if (o.workUnit != null) println "has a workUnit ${o.workUnit}"
          if (c.offline) println "${c} is offline"
          if (!c.acceptingTasks) println "${c} is not accepting tasks"
        }
      }
    }
  }
}