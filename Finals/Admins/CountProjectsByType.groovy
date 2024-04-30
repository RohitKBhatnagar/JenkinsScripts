/*
   This script console script will count jobs by type.
 */
import hudson.model.Job
import jenkins.model.Jenkins

projects = [:]
Jenkins.instance.getAllItems(Job.class).each { Job j ->
    String jc = j.class.simpleName
    if(!(jc in projects)) {
        projects[jc] = 0
    }
    projects[jc]++
}
println "Count projects by type:"
projects.each { k, v ->
    println "    ${k}: ${v}"
}
null