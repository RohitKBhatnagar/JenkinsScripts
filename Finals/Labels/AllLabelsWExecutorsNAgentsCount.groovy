
//Display all labels behind that controller, including currently available executors, total allocated executors and total agents
def allLabels = []

Jenkins.instance.nodes.each { node ->
  node.assignedLabels.each { label ->
    if (label as String != node.name) {
        allLabels += label
    }      
  }
}

//println allLabels.unique().join('\n')

println "Label, Available Executors, Total Executors, Total Agents"
allLabels.unique().each { it ->
  
  println "${it.name}, ${it.getTotalExecutors()}, ${it.getTotalConfiguredExecutors()}, ${it.getNodes().size()}"
  //printAllMethods(it)
  /*def nodes = jenkins.model.Jenkins.get().computers
  .findAll{ it.node.labelString.contains(it.name) }
  .collect{ it.node.selfLabel.name }

	println nodes*/
}

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


/*
class name functions: allLabels.unique() items via iterator 'it'
equals(); getClass(); hashCode(); notify(); notifyAll(); toString(); wait(); getSearch(); getSearchIndex(); getSearchName(); addAction(); addOrReplaceAction(); doContextMenu(); getAction(); getActions(); getAllActions(); getDynamic(); removeAction(); removeActions(); replaceAction(); replaceActions(); accept(); and(); compareTo(); contains(); doChildrenContextMenu(); get(); getApi(); getBusyExecutors(); getClouds(); getDescription(); getDisplayName(); getExpression(); getIdleExecutors(); getName(); getNodes(); getSearchUrl(); getSortedNodes(); getTiedJobCount(); getTiedJobs(); getTotalConfiguredExecutors(); getTotalExecutors(); getUrl(); iff(); implies(); isAssignable(); isAtom(); isEmpty(); isOffline(); isSelfLabel(); listAtoms(); matches(); not(); or(); paren(); parse(); parseExpression(); precedence(); doConfigSubmit(); doSubmitDescription(); escape(); findNearest(); getApplicablePropertyDescriptors(); getProperties(); getPropertiesList(); load(); needsEscape(); save(); setDescription(); 

*/