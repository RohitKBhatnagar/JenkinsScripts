import jenkins.*
import jenkins.model.*
import hudson.*
import hudson.model.*

import hudson.scm.*
import hudson.tasks.*
import com.cloudbees.hudson.plugins.folder.*


jen = Jenkins.instance

jen.getItems().each {
    if(it instanceof Folder)
	{
		println "Top Level Folder - ${it.name}"
        processFolder(it)
    }else{
        processJob(it)
    }
}

void processJob(Item job){
}

void processFolder(Item folder)
{
    folder.getItems().each {
        if(it instanceof Folder)
		{
			println "Sub Folder - ${it.name}"
            processFolder(it)
        }
		else
		{
            processJob(it)
        }
    }
}