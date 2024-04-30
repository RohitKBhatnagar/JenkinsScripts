//Clears build queue
Jenkins.instance.queue.clear()

//If you need to remove only some of them , you can use the below script to purge the build queue by build name.

import hudson.model.*
def q = Jenkins.instance.queue
q.items.findAll { it.task.name.startsWith('REPLACEME') }.each { q.cancel(it.task) }