import org.jenkinsci.plugins.workflow.job.WorkflowJob;

def printScm(project, scm)
{
    if (scm instanceof hudson.plugins.git.GitSCM) 
    {
        scm.getRepositories().eachWithIndex 
	{
	    it, itCount ->
	    println "${itCount} :: ${scm} \t Name - ${it.getName()} \t Key - ${scm.getKey()} \t SCM Name - ${scm.getScmName()}"
	    scm.getBranches().eachWithIndex
	    {
		itBranch, itBranchCount ->
		println "\t ${itBranchCount} :: Branch Name - ${itBranch.getName()}"
	    }
            it.getURIs().eachWithIndex 
	    {
	        itUri, itUriCount ->
                println "\t ${itUriCount} :: Project - ${project} \t URI - ${itUri.toString()}";
            }
        }
    }
}

Jenkins.instance.getAllItems(Job.class).each 
{
    project = it.getFullName()
    if (it instanceof AbstractProject)
    {
        printScm(project, it.getScm())
    } 
    else if (it instanceof WorkflowJob) 
    {
        it.getSCMs().each 
	{
            printScm(project, it)
        }
    } 
    else 
    {
        println("project type unknown: " + it)
    }

}

return null