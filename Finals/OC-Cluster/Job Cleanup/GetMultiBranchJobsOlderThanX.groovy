#!groovy
import java.io.*
import org.jenkinsci.plugins.github_branch_source.PullRequestSCMHead
import org.jenkinsci.plugins.workflow.multibranch.BranchJobProperty
import org.jenkinsci.plugins.workflow.multibranch.WorkflowMultiBranchProject
import java.util.*
import hudson.slaves.*
import java.util.concurrent.*
import org.jenkinsci.plugins.workflow.job.*
import java.text.SimpleDateFormat
import java.util.Date
import com.cloudbees.plugins.credentials.Credentials
import com.cloudbees.plugins.credentials.CredentialsProvider

/******************************************************************************
 * GetMultiBranchJobsOlderThanX
 Get and store all the multi-branch jobs with zero branches or no branches executed in the last X days.
 ******************************************************************************/
/* Job Setup */
String artifactory = "https://artifacts.mastercard.int/artifactory"
String artifactoryStorage = "https://artifacts.mastercard.int/artifactory/api/storage"
String artPath = "/backup/com/mastercard/pipe/jenkins/job-mgmt"
String artifactoryPasswordLabel = 'jnk-art-pass'
String gitRepo = "https://globalrepository.mclocal.int/stash/scm/pipe/job_cleanup_ignore_list.git"
String workspacePath = "/tmp/GetMultiBranchJobsOlderThanX"

// artifactory credentials
String username = 'dep-cicd'
String password = jjcMCGetPW(artifactoryPasswordLabel)

// Bitbucket credentials
Map bbCredMap = jjcGetBBCredentials()
if (bbCredMap == null) {
    println("failed to get credential from jenkins")
    println("Aborting Job")
    return
}

workspacePath = jjcCleanup(workspacePath)
String workspace = jjcMKDIR(workspacePath)

ignoreList = jjcGetIgnoreList(gitRepo, bbCredMap, workspace)
/* Job Setup End */


int itemCount = 0
def java.io.File outfile


/* Validate Parameter */
println("GetMultiBranchJobsOlderThan ${daysOlderThanParam} Days")
if (daysOlderThanParam.isInteger()) {
    daysOlderThan = daysOlderThanParam.toInteger()
} else {
    println("daysOlderThanParam must be a postive integer value")
    println("Exiting Job")
    return
}

Calendar expirationDate = Calendar.getInstance()
expirationDate.add(Calendar.DATE, daysOlderThan * -1)

/* Create file to store jobs found */
try {
    outfile = jjcCreateJobCleanupFile(jjcGetController(), "/tmp", "mbcleanup", "pending_disable")
}
catch (Exception e) {
    println "jjcCreateJobCleanupFile exception raised ... - ${e}"
}


/* Find Multi-Branch Jobs */
def allItems = Jenkins.instance.getAllItems(WorkflowMultiBranchProject)
int totalCount = 0

def boolean withinTimeLimit = false
def String line = null

outfile.newWriter().withWriter { w ->
    for (item in allItems) {
        try {
            totalCount++
            //if on ignore list skip this item
            if (jjcIgnoreJob(ignoreList, item.fullName)) continue

            withinTimeLimit = false
            int branchCount = 0
            def builds = []
            item.items.findAll { Job branch ->
                builds += branch.getBuilds()
                branchCount++
            }

            builds.each { build ->
            }
            //println "Multi-Branch Job Name: ${item.fullName} - Branch Count: ${branchCount}"
            for (build in builds) {
                if (build.isBuilding()) {
                    println "Skipping $build because it's building."
                    withinTimeLimit = true
                    break
                } else if (build.isKeepLog()) {
                    println "Skipping $build because it's marked as keep."
                    withinTimeLimit = true
                    break
                }
                build_time = build.time.toCalendar()
                if (build_time.compareTo(expirationDate) > 0) {
                    println "Keep: ${item.fullName} - Branch Count: ${branchCount}    ${build.time}"
                    withinTimeLimit = true
                    break
                }
            }
            if (branchCount == 0 || withinTimeLimit == false) {
                line = ""
                itemCount++
                println("Disable: ${item.fullName}")
                line = item.fullName + "\n"
                w << line
            }
        }
        catch (Exception e) {
            println "Exception raised ... - ${e}"
        }
    }
}

println("Multi-Branch Jobs That Should Be Disabled:  ${itemCount} out of ${totalCount}")
def List<String> ret = null
if (itemCount > 0) {
    println("-----------push to artifactory------------")
    sp = outfile.getAbsolutePath()
    ap = artifactory + artPath + "/" + outfile.getName()
    ret = jjcCurlToArtifactory(username, password, ap, sp)
    println("-----------end artifactory------------")
} else {
    println("No jobs found.")
}

//**********************************************************************************
// Reusable functions
//**********************************************************************************

def void NOOP() {
    return
}

def boolean jjcIgnoreJob(List<String> ignoreList, String name) {
    boolean isFound = false
    for (String excludeName in ignoreList) {
        if (name.startsWith(excludeName)) {
            isFound = true
            println("${name} excluded")
            break
        }
    }
    return(isFound)
}

def jjcGetJobInfo(String name) {
    def jenkins = Jenkins.getInstance()
    def job = jenkins.getItemByFullName(name)
    println "Job type: ${job.getClass()}"

    if (job.getLastBuild() != null) { //if job has run before
        println "Is building: ${job.isBuilding()}"
        println "Is in queue: ${job.isInQueue()}"
        println "Last successfull build: ${job.getLastSuccessfulBuild()}"
        println "Last failed build: ${job.getLastFailedBuild()}"
        println "Last build: ${job.getLastBuild()}"
        println "All builds: ${job.getBuilds().collect { it.getNumber() }}"
    } else {
        println("${job.fullName} has never run")
    }
    return job
}


def void jjcTrickleDelete(String name, long msBetweenDeletes) {
    def jenkins = Jenkins.getInstance()
    def job = jenkins.getItemByFullName(name)
    if (job != null) {
        try {
            job.delete()
        } catch (Exception e) {
            println("Severe Error on Delete of ${name}")
            println e.printStackTrace()
        }
        println("Deleted - " + name)
    } else {
        println("Error: Could not find job - " + name)
    }
    sleep(msBetweenDeletes)
}


def boolean jjcIsJobDisabled(String jobname) {
    def jenkins = Jenkins.getInstance()
    return (jenkins.getItemByFullName(jobname).disabled)
}


def String jjcGetControllerHostName() {
    def jenkins = Jenkins.getInstance()
    host = jenkins.getComputer('').getHostName()
    return (host.tokenize(".")[0])
}


def String jjcGetController() {
    def controllers = [jkm4stl0: "stage.cd", jkm4stl2: "stage.cd",
                       jkm4stl1: "stage.cd2",
                       jkm2stl0: "cd", jkm2stl1: "cd",
                       jkm9stl2: "cd2", jkm9stl3: "cd2",
                       jkm9stl4: "cd3", jkm9stl5: "cd3",
                       jkm9stl6: "cd4", jkm9stl7: "cd4",
                       jkm9stl8: "cd5", jkm9stl9: "cd5",
                       lstl9jkm8141: "cd0", lstl9jkm8781: "cd0",
                       lstl9jkm8720: "cd6", lstl9jkm8723: "cd6",
                       lstl9jkm8067: "cd7", lstl9jkm8271: "cd7",
                       lstl9jkm8282: "cd8", lstl9jkm8124: "cd8",
                       lstl9jkm8923: "cd9", lstl9jkm8739: "cd9",
                       lstl9jkm8993: "cd10", lstl9jkm8883: "cd10",
                       jkm9bel1: "bel.cd1", jkm9bel2: "bel.cd1",
                       jkm9bel3: "bel.cd2", jkm9bel4: "bel.cd2",
                       jkm9bel5: "bel.cd3", jkm9bel6: "bel.cd3"]
    return (controllers[jjcGetControllerHostName()])
}



def boolean jjcDeleteFile(String fullPath) {
    if (fullPath == null) {
        println("ERROR:  jjcDeleteFile Null path - ignored.")
        return false
    }
    def jenkins = Jenkins.getInstance()
    def boolean result
    result = new File(fullPath).delete()
    if (result) {
        println("File Deleted: " + fullPath)
    } else {
        println("File Deleted Failed: " + fullPath)
    }
    return (result)
}

def boolean jjcDisableJob(String name) {
    if (name == null) {
        println("ERROR:  jjcDisableJob Null name - ignored.")
        return false
    }
    def jenkins = Jenkins.getInstance()
    println("in jjcDisableJob: name=" + name)
    def job = jenkins.getItemByFullName(name)
    if (job != null) {
        return (job.doDisable())
    } else {
        println("Cannot find job " + name + " and disable failed.")
        return (false)
    }
}

def boolean jjcDeleteJob(String name) {
    if (name == null) {
        println("ERROR:  jjcDeleteJob Null name - ignored.")
        return false
    }
    def jenkins = Jenkins.getInstance()
    def job = jenkins.getItemByFullName(name)
    if (job != null) {
        try {
            job.delete()
        } catch (Exception e) {
            println("Severe Error on Delete of ${name}")
            println e.printStackTrace()
        }
        println("Deleted - " + name)
    } else {
        println("Cannot find job " + name + " and delete failed.")
    }
}

def List<String> jjcGetIgnoreList(String gitRepo, Map bbCredMap, String workspace) {
    List<String> iList = new ArrayList<>()
    URL url = new URL(gitRepo)
    urlWithCreds = url.getProtocol() + "://" + bbCredMap['username'] + ":" + '\'' + bbCredMap['password'] + '\'' + "@" +
            url.getAuthority() + url.getPath()
    println(urlWithCreds)
    //get the directory name to clone to
    tokens = url.getPath().tokenize('/')
    repoName = tokens[tokens.size() - 1].tokenize('.')[0]

    cmd = "git clone --branch master ${urlWithCreds} ${workspace}/${repoName}"
    response = jjsExec(cmd)
    println("jjcGetIgnoreList response=${response}")
    if (response[response.length - 1] == "404") {
        println("Repository not found")
        return (null)
    }

    ignoreFilePath = "${workspace}/${repoName}/.ignore_cleanup"
    ignoreFile = jjcCreateFileHandle(ignoreFilePath)
    println('-------------------------------------------------------')
    println('----P R I N T  C L E A N U P  I G N O R E  L I S T-----')
    println('-------------------------------------------------------')
    ignoreFile.eachLine { line ->
        iList.add(line)
        println(line)
    }
    println('-------------------------------------------------------\n')
    return (iList)
}


//curl -u<USERNAME>:<PASSWORD> -O "https://artifacts.mastercard.int/artifactory/backup/<TARGET_FILE_PATH>"

def List<String> jjcDownloadFromArtifactory(String username, String password, String artifactoryPath, String destinationPath) {
    cmd = "curl -sSf -u\'" + username + ":" + password + "\'" + " -O \"" + artifactoryPath + "\""
    response = jjsExec(cmd)
    if (response[response.length - 1] == "404") {
        println("File not found")
    }
    return (response)
}

def List<String> jjcCurlToArtifactory(String username, String password, String artifactoryPath, String sourcePath) {
    cmd = "curl -u\'" + username + ":" + password + "\'" + " -T \"" + sourcePath + "\" \"" + artifactoryPath + "\""
    response = jjsExec(cmd)
    if (response[response.length - 1] == "502") {
        println("File already exists")
    }
    return (response)
}

def boolean jjcArtifactExists(String username, String password, String artifactoryPath) {
    def boolean isExists = false
    cmd = "curl -X GET -u\'" + username + ":" + password + "\'  -s -f -o /dev/null -w \"%{http_code}\" \"${artifactoryPath}\""
    response = jjsExec(cmd)
    println("jjcArtifactExists response:  ")
    response.each { println it }
    if (response[1] == "200") isExists = true
    return (isExists)
}


def File jjcCreateJobCleanupFile(String controller = null, String targetDir = null,
                                 String typeOfFile = null, String statusOfFile = null) {
    // File name format:  <controller>.<type>.<status>.<date>
    def String fileName
    def String fullPath
    fileName = controller + "." + typeOfFile + "." + statusOfFile + "." + jjcGetDate()
    fullPath = targetDir + "/" + fileName
    fileHandle = new File(fullPath)
    println("Output file created: " + fullPath)
    return (fileHandle)
}

def java.io.File jjcCreateFileHandle(String fullPath) {
    return (new File(fullPath))
}

def File jjcFileCompare(String path1, String path2) {
    List<String> l1 = new ArrayList<>()
    List<String> l2 = new ArrayList<>()
    List<String> commons = new ArrayList<>()
    List<String> differences = new ArrayList<>()
    fh1 = new File(path1)
    fh2 = new File(path2)
    if (!fh1.exists()) {
        println(path1 + " does not exist")
        return (null)
    }
    if (!fh2.exists()) {
        println(path2 + " does not exist")
        return (null)
    }
    // Load the two files into Lists
    fh1.eachLine { line ->
        l1.add(line)
    }
    fh2.eachLine { line ->
        l2.add(line)
    }
    println("l1 size = " + l1.size())
    println("l2 size = " + l2.size())
    results = jjcCreateJobCleanupFile(jjcGetController(), "/tmp", "neverrun", "pending_disable")
    commons = l1.intersect(l2)
    println("commons size = " + commons.size())
    differences = l1.plus(l2)
    differences.removeAll(commons)
    println("differences size = " + differences.size() + "\n")
    // write the pending disables/deletes to a file
    int itemCount = 0
    String line = null
    results.newWriter().withWriter { w ->
        if (commons.size()) {
            commons.each { job ->
                line = ""
                itemCount++
                line = job + "\n"
                w << line
            }
        }
    }
    println(results.getAbsolutePath() + " created with ${itemCount} entries")
    return (results)
}


def String jjcGetDate() {
    SimpleDateFormat dateOfFile = new SimpleDateFormat("yyyyMMdd")
    def date = new Date()
    return (dateOfFile.format(date))
}

def String jjcPWD() {
    List<String> list = new ArrayList<>()
    ret = jjsExec('pwd')
    ret.each { it ->
        list.add(it)
    }
    if (list[0] == "0") { // found it
        return list[1]
    } else {
        println("pwd failed")
        return null
    }
}


def String jjcMKDIR(String absolutePath) {
    List<String> list = new ArrayList<>()
    String cmd = "mkdir ${absolutePath}"
    ret = jjsExec(cmd)
    ret.each { it ->
        list.add(it)
    }
    if (list[0] == "0") {
        return (absolutePath)
    } else {
        println("failed to make directory: ${absolutePath} Error ${list[0]}")
        return null
    }
}


def String jjcCleanup(String workspace) {
    List<String> list = new ArrayList<>()
    String cmd = "rm -rf ${workspace}"
    ret = jjsExec(cmd)
    ret.each { it ->
        list.add(it)
    }
    if (list[0] == "0") {
        return (workspace)
    } else {
        println("failed to remove directory: ${workspace} Error ${list[0]}")
        return null
    }
}

/***************************************************************************
 * jjsExec -
 * @param cmd
 * @return
 *************************************************************************   */
def String[] jjsExec(java.lang.String cmd) {
    List<String> response = new ArrayList<>()
    response.add(0)
    //println("cmd=" + cmd)
    def process = new ProcessBuilder(["sh", "-c", cmd])
            .redirectErrorStream(true)
            .start()
    process.outputStream.close()
    process.inputStream.eachLine {
        //println it
        response.add(it)
    }
    process.waitFor();
    response[0] = process.exitValue()

    return response
}


def String jjcGetDateXDaysAgo(int days) {
    days = days * -1  //make it negative to subtract
    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd")
    Calendar daysAgo = Calendar.getInstance()
    daysAgo.add(Calendar.DATE, days)
    return (sdf.format(daysAgo.getTime()))
}


def String jjcMCGetPW(String label) {
    List<String> list = new ArrayList<>()
    cmd = "/usr/local/bin/mcgetpw -label ${label}"
    ret = jjsExec(cmd)
    ret.each { it ->
        list.add(it)
    }
    if (list[0] == "0") { // found it
        return list[1]
    } else {
        return null
    }
}


/**************************************************************
 Get the Bitbucket Credential Information based on controller
 *************************************************************/

def String jjcGetCredentialPasswordByController() {
    String controller = jjcGetController()
    controller = controller.toUpperCase()
    if (controller.startsWith("CD")) {
        credId = "Jenkins-" + controller + "-BB"
    } else {
        if (controller.startsWith("BEL")) {
            credId = "Jenkins-CD-BB"
        } else {
            if (controller.startsWith("STAGE")) {
                credId = "Jenkins-CD-BB"
            }
        }
    }
    return (credId)
}

def jjcGetBBCredentials() {
    // Bitbucket credentials
    return(jjcGetCredentialMap(jjcGetCredentialPasswordByController()))
}


def jjcGetCredentialMap(String credentialId) {
    Map bbCredentialMap = [:]
    bbCredentialMap['credentialId'] = credentialId
    Set<Credentials> allCredentials = new HashSet<Credentials>();

    Jenkins.instance.getAllItems(com.cloudbees.hudson.plugins.folder.Folder.class).each { f ->
        creds = com.cloudbees.plugins.credentials.CredentialsProvider.lookupCredentials(
                com.cloudbees.plugins.credentials.Credentials.class, f)
        allCredentials.addAll(creds)

    }
    for (c in allCredentials) {
        if (c.id == credentialId) {
            //println(c.id + ": " + c.description)
            if (c instanceof com.cloudbees.plugins.credentials.common.StandardUsernamePasswordCredentials) {
                bbCredentialMap['username'] = c.username
                bbCredentialMap['password'] = c.getPassword()
                bbCredentialMap['description'] = c.description
                return(bbCredentialMap)
            }
        }
    }
    return null
}

println("---------end of job-----------")
