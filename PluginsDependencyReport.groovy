import java.text.SimpleDateFormat
def iCount = 1

def sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss.SSS")
def startDate = new Date()
println "Start time : ${sdf.format(startDate)}"

//---------------------------------------------------------------------------
//Displays plugins dependency report
//Get information about direct and recursive (JSON object) dependencies of/to a specific plugin.
//---------------------------------------------------------------------------

//--------------------------------------------------------
println "---------------------------------------------------------------------------"
println "Get information about direct and recursive (JSON object) dependencies of/to a specific plugin."
println "---------------------------------------------------------------------------"


import hudson.PluginWrapper
import jenkins.model.Jenkins

/***********************************************************************************************
 * Following methods analyze dependencies of a particular plugin, for example the `git-plugin`.*
 ***********************************************************************************************/

// //def pluginByName = Jenkins.instance.getPluginManager().getPlugin(pluginShortName);
// def pluginsByName = Jenkins.instance.getPluginManager().getPlugins();

// //Iterating through all plugins
// pluginsByName.eachWithIndex 
// {
// /**
//  * Get the dependencies (direct) of a particular plugin.
//  */
// println "\nDEPENDENCIES (DIRECT) of ${pluginsByName.getShortName()} (${pluginsByName.getVersion()}):"
// /**
//  * Get the direct dependencies of a particular plugin.
//  */
// pluginByName.getDependencies().each {
//     println "${it.shortName} (${it.version})"
// };

// //println "\nDEPENDENCIES (Recursive JSON) of ${pluginByName.getShortName()} (${pluginByName.getVersion()}):"
// println "\nDEPENDENCIES (Recursive JSON) of ${pluginByName.getName()} (${pluginByName.getVersion()}):"

// }
// /**
//  * Get a complete JSON object of the dependencies (recursively) of a particular plugin.
//  * @param plugin The Plugin
//  */
// def void getDependenciesJSON(PluginWrapper plugin) 
// {
//     //print "{\"plugin\":\"${plugin.getShortName()}\", \"version\":\"${plugin.getVersion()}\"";
//     print "{\"plugin\":\"${plugin.getName()}\", \"version\":\"${plugin.getVersion()}\"";
//     def deps = plugin.getDependencies();
//     if (!deps.isEmpty()) {
//         def i;
//         print ", \"dependencies\":["
//         for (i = 0; i < deps.size() - 1; i++) {
//             getDependenciesJSON(Jenkins.instance.getPluginManager().getPlugin(deps.get(i).shortName));
//             print ","
//         }
//         getDependenciesJSON(Jenkins.instance.getPluginManager().getPlugin(deps.get(i).shortName));
//         print "]"
//     }
//     print "}"
// }

// getDependenciesJSON(pluginByName);

println "\n\nDEPENDANTS (DIRECT) of ${pluginByName.getShortName()} (${pluginByName.getVersion()}):"
/**
 * Get the plugins that depend (directly) on a particular plugin.
 */
Jenkins.instance.getPluginManager().getPlugins()
        .findAll { plugin ->
    plugin.getDependencies().find {
        dependency -> pluginShortName.equals(dependency.shortName)
    }
}.each {
    println "${it.getShortName()} (${it.getVersion()})"
};

println "\nDEPENDANTS (Recursive JSON) of ${pluginByName.getShortName()} (${pluginByName.getVersion()}):"

println(getDependantsJSON(pluginByName));

/**
 * Get a complete JSON object of the dependants (recursively) of a particular plugin.
 * @param plugin The Plugin
 */
def void getDependantsJSON(PluginWrapper plugin) {
    print "{\"plugin\":\"${plugin.getShortName()}\", \"version\":\"${plugin.getVersion()}\"";
    def deps = Jenkins.instance.getPluginManager().getPlugins().findAll { depCandidate ->
        depCandidate.getDependencies().find {
            dependency -> plugin.shortName.equals(dependency.shortName)
        }
    }
    if (!deps.isEmpty()) {
        def i;
        print ", \"dependants\":["
        for (i = 0; i < deps.size() - 1; i++) {
            getDependantsJSON(Jenkins.instance.getPluginManager().getPlugin(deps.get(i).shortName));
            print ","
        }
        getDependantsJSON(Jenkins.instance.getPluginManager().getPlugin(deps.get(i).shortName));
        print "]"
    }
    print "}"
}





//---------------------------------------------------------
def endDate = new Date()
println "End time: ${sdf.format(endDate)}"

use(groovy.time.TimeCategory) 
{
    def duration = endDate - startDate
  	print "Elapsed time: Days: ${duration.days}, Hours: ${duration.hours}, Minutes: ${duration.minutes}, Seconds: ${duration.seconds}"
}