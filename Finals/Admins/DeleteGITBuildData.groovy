//URL- https://plugins.jenkins.io/git/#remove-git-plugin-buildsbybranch-builddata-script
//The git plugin has an issue (JENKINS-19022) 'https://issues.jenkins.io/browse/JENKINS-19022' that sometimes causes excessive memory use and disc use in the build history of a job. The problem occurs because in some cases the git plugin copies the git build data from previous builds to the most recent build, even though the git build data from the previous build is not used in the most recent build. The issue can be especially challenging when a job retains a very large number of historical builds or when a job builds a wide range of commits during its history.

import hudson.matrix.*
import hudson.model.*

hudsonInstance = hudson.model.Hudson.instance
jobNames = hudsonInstance.getJobNames()
allItems = []
def allBuilds = 0
def allRuns = 0
for (name in jobNames) {
  allItems += hudsonInstance.getItemByFullName(name)
}

// Iterate over all jobs and find the ones that have a hudson.plugins.git.util.BuildData
// as an action.
//
// We then clean it by removing the useless array action.buildsByBranchName
//

for (job in allItems) {
  println("job: " + job.name);
  def counter = 0;
  for (build in job.getBuilds()) {
    // It is possible for a build to have multiple BuildData actions
    // since we can use the Mulitple SCM plugin.
    def gitActions = build.getActions(hudson.plugins.git.util.BuildData.class)
    if (gitActions != null) {
      for (action in gitActions) {
        action.buildsByBranchName = new HashMap<String, Build>();
        hudson.plugins.git.Revision r = action.getLastBuiltRevision();
        if (r != null) {
          for (branch in r.getBranches()) {
            action.buildsByBranchName.put(branch.getName(), action.lastBuild)
          }
        }
        //build.actions.remove(action)
        //build.actions.add(action)
        //build.save();
        println "ByBranch - ${counter} :: ${build}"
        counter++;
        allBuilds++;
      }
    }
    if (job instanceof MatrixProject) {
      def runcounter = 0;
      for (run in build.getRuns()) {
        gitActions = run.getActions(hudson.plugins.git.util.BuildData.class)
        if (gitActions != null) {
          for (action in gitActions) {
            action.buildsByBranchName = new HashMap<String, Build>();
            hudson.plugins.git.Revision r = action.getLastBuiltRevision();
            if (r != null) {
              for (branch in r.getBranches()) {
                action.buildsByBranchName.put(branch.getName(), action.lastBuild)
              }
            }
            //run.actions.remove(action)
            //run.actions.add(action)
            //run.save();
            println "ByRuns - ${runcounter} :: ${run}"
            runcounter++;
            allRuns++;
          }
        }
      }
      if (runcounter > 0) {
        println(" -->> cleaned: " + runcounter + " runs");
      }
    }
  }
  if (counter > 0) {
    println("-- cleaned: " + counter + " builds");
  }
}

println "Total by builds - ${allBuilds} and runs - ${allRuns}"