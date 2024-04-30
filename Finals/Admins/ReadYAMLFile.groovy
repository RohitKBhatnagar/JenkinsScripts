
//Reading YAML file and interate through each value defined from Script Console
import jenkins.model.Jenkins

/* JENKINS_HOME environment variable is not reliable */
def jenkinsHome = Jenkins.instance.getRootDir().absolutePath
println jenkinsHome
def propertiesFile = new File("${jenkinsHome}/jenkins.yaml")
println propertiesFile
if (propertiesFile.exists()) {
    println("Loading system properties from ${propertiesFile.absolutePath}")
    propertiesFile.withReader { r ->
        /* Loading java.util.Properties as defaults makes empty Properties object */
        def props = new Properties()
        props.load(r)
        props.each { key, value ->
            println "$key - $value"
        }
    }
}

return null