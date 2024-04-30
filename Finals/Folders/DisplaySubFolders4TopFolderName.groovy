//Prints the folder names under a top-level-folder name provided. As an example in CD2 controller the list of SPMADMIN folders are shown in this example

import jenkins.*
import jenkins.model.*
import hudson.*
import hudson.model.*

import hudson.scm.*
import hudson.tasks.*
import com.cloudbees.hudson.plugins.folder.*
import com.cloudbees.hudson.plugins.folder.AbstractFolder;

def folderName="SPMADMIN";

jen = Jenkins.instance

//jen.getItems().each {
jen.getItemByFullName(folderName, AbstractFolder).getItems().each {
    if(it instanceof Folder)
	{
		println "Folder Name - ${it.name}"
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

return null;