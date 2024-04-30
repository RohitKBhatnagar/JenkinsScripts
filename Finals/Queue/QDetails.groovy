//https://javadoc.jenkins-ci.org/hudson/model/Queue.html

import hudson.model.*
import jenkins.model.Jenkins

def q = Jenkins.instance.queue

//Owner - ${qBIit.getOwner()}, 

println "================================"
println "Total Buildable Items - ${q.countBuildableItems()}"
println "================================"
def qBI = q.getBuildableItems()
//println "Buildable Items - ${qBI}"
//printAllMethods(qBI)
qBI.eachWithIndex { qBIit, iBICnt -> 
  //printAllMethods(qBIit)
  println "$iBICnt, ID - ${qBIit.getId()}, Search Name - ${qBIit.getSearchName()}, Label - ${qBIit.getAssignedLabel()}, Display Name - ${qBIit.getDisplayName()}, URL - ${qBIit.getUrl()}, \
  Blocked - ${qBIit.isBlocked()}, Buildable - ${qBIit.isBuildable()}, Stuck - ${qBIit.isStuck()}, Pending - ${qBIit.isPending()}, Q for - ${qBIit.getInQueueForString()}, In Q since - ${qBIit.getInQueueSince()}, Blockage Cause - ${qBIit.getCauseOfBlockage()}, ${qBIit.toString()}, Params - ${qBIit.getParams()}" 
}


//${qIit.getApi()}
//int iCount = 0;
//q.items.findAll {  println"${++iCount} - ${it.task.name}" }
println ""
println "================================"
println "Total Items - ${q.getItems().size()}"
println "================================"
def qI = q.getItems();
//println "Items - ${qI}"
qI.eachWithIndex { qIit, qICnt ->
  //printAllMethods(qIit)
  println "${qICnt}, ${qIit.getId()}, Search Name - ${qIit.getSearchName()}, Label - ${qIit.getAssignedLabel()}, Display Name - ${qIit.getDisplayName()}, URL - ${qIit.getUrl()}, \
  Blocked - ${qIit.isBlocked()}, Buildable - ${qIit.isBuildable()}, Stuck - ${qIit.isStuck()}, Why - ${qIit.getWhy()}, Q for - ${qIit.getInQueueForString()}, In Q since - ${qIit.getInQueueSince()}, Blockage Cause - ${qIit.getCauseOfBlockage()}, ${qIit.toString()}, Params - ${qIit.getParams()}" 
}

/*q.getLeftItems().eachWithIndex { iCnt, it -> 
  println "$iCnt - ${it.name}"
}*/

println ""
println "================================"
println "Left Items - ${q.getLeftItems().size()}"
println "================================"

println ""
println "================================"
println "Total Pending Items - ${q.getPendingItems().size()}"
println "================================"
//println "Total Blocked Items - ${q.getBlockedItem()}"
//println "Buildable Items - ${q.getBuildableItems()}"

println "==============="

//printAllMethods(q.getLeftItems())

// Filename: printAllMethodsExample.groovy
void printAllMethods( obj ){
    if( !obj ){
    println( "Object is null\r\n" );
    return;
    }
  if( !obj.metaClass && obj.getClass() ){
        printAllMethods( obj.getClass() );
    return;
    }
  def str = "class ${obj.getClass().name} functions:\r\n";
  obj.metaClass.methods.name.unique().each{ 
    str += it+"(); "; 
  }
  println "${str}\r\n";
}
