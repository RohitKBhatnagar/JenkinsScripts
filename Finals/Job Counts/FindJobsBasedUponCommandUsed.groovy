/**
  Search all freestyle jobs in a Jenkins instance.  Find any job containing any
  one of the commands being used in a list of strings.
 */
import hudson.model.FreeStyleProject
import hudson.tasks.Shell
import jenkins.model.Jenkins

//find any of the following strings existing in a freestyle job in Jenkins
//List<String> strings = ['git clone', 'echo', 'sh', 'bat']
List<String> strings = ['bat']
//require a freestyle job to contain all of the strings in the list within shell steps?
Boolean containsAll = false

println Jenkins.instance.getAllItems(FreeStyleProject.class).findAll { job ->
    job.builders.findAll { it in Shell } &&
    job.builders.findAll {
        it in Shell
    }.collect { shell ->
        shell.command
    }.join('\n').with { String script ->
        Boolean found = ((!containsAll) in strings.collect { script.contains(it) })
        //XOR the found result
        found ^ containsAll
    }
}.collect { job ->
    job.absoluteUrl
}.join('\n')