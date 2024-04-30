///Prints all jobs running against a node
//https://marslo.github.io/ibook/jenkins/script/agent.html
import hudson.model.*
import jenkins.model.*
import hudson.slaves.*

String name = 'jnk4stl4' //cd2
Computer computer = jenkins.model.Jenkins.instance.getNode(name)?.computer ?: null

/*
println computer.allExecutors.collect {
  it.currentWorkUnit?.work?.runId ?: ''
}.join(', ') ?: ''
*/

int i=0
println computer.allExecutors.collect {
  "\n${++i}, " + it.currentWorkUnit?.work?.url ?: ''
}.join(', ') ?: ''
/////////////////////////////////////////////////////////////////////////////////////////////////////////

============================================
///Displays jobs behind the label passed for all nodes
//https://marslo.github.io/ibook/jenkins/script/agent.html
String labelName = "DTL-LIN7"
getLabel(labelName)
println "Label - $labelName"

def getLabel( String label ){
  int i=0
  for ( node in jenkins.model.Jenkins.instance.nodes ) {
     if ( node.getLabelString().contains(label) ) {
      println "${++i}, " + node.name + ", " + node.getLabelString()

      String name = node.name //cd2
      Computer computer = jenkins.model.Jenkins.instance.getNode(name)?.computer ?: null

      int j=0
      computer.allExecutors.each { it->
        try
        {
          println "\t${++j}, ${it.currentWorkUnit.work.url}, ${it.getNumber()}, ${it.getElapsedTime()}"
        }
        catch(Exception e)
        {
          println e.message
        }
      }
    }
  }
}


///////////////////////// BELOW ARE ALL MISCELLANEOUS SCRIPTS ////////////////////////////

============
getLabel('DTL-LIN7')
//println "Label - " + getLabel('jnk4stl4')

def getLabel( String label ){
  println label
  println "======="
  int i=0
  for ( node in jenkins.model.Jenkins.instance.nodes ) {
    println "${++i}, " + node.name + ", " + node.getLabelString()
    if ( node.getNodeName().toString().equals(label) ) {
      return node.getLabelString()
    }
  }
}

===========
getLabel('DTL-LIN7')
//println "Label - " + getLabel('jnk4stl4')

def getLabel( String label ){
  int i=0
  for ( node in jenkins.model.Jenkins.instance.nodes ) {
     if ( node.getLabelString().contains(label) ) {
      println "${++i}, " + node.name + ", " + node.getLabelString()
    }
  }
}

=============
getLabel('DTL-LIN7')
//println "Label - " + getLabel('jnk4stl4')

def getLabel( String label ){
  int i=0
  for ( node in jenkins.model.Jenkins.instance.nodes ) {
     if ( node.getLabelString().contains(label) ) {
      println "${++i}, " + node.name + ", " + node.getLabelString()

      String name = node.name //cd2
      Computer computer = jenkins.model.Jenkins.instance.getNode(name)?.computer ?: null

      int j=0
      println computer.allExecutors.collect {
        "\n\t${++j}, " + it.currentWorkUnit?.work?.url ?: ''
        }.join(', ') ?: ''
    }
  }
}

//==================
///Prints all jobs running against a node
import hudson.model.*
import jenkins.model.*
import hudson.slaves.*

String name = 'jnk4stl4' //cd2
Computer computer = jenkins.model.Jenkins.instance.getNode(name)?.computer ?: null

int i=0
computer.allExecutors.each { it->
  println "${++i}, ${it.currentWorkUnit.work.url}, ${it.getNumber()}, ${it.getElapsedTime()}"
}


//==================
String labelName = "DTL-LIN7"
getLabel(labelName)
println "Label - $labelName"

def getLabel( String label ){
  int i=0
  for ( node in jenkins.model.Jenkins.instance.nodes ) {
     if ( node.getLabelString().contains(label) ) {
      println "${++i}, " + node.name + ", " + node.getLabelString()

      String name = node.name //cd2
      Computer computer = jenkins.model.Jenkins.instance.getNode(name)?.computer ?: null

      int j=0
      computer.allExecutors.each { it->
        println "\t${++j}, ${it.currentWorkUnit.work.url}, ${it.getNumber()}, ${it.getElapsedTime()}"
      }
    }
  }
}