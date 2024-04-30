// turn off weather column health metrics for folders only
// try dry run first, set false to apply changes
def dryRun = false
controller = Jenkins.getInstance().getComputer('').getHostName()
folders_modified = 0


final metricToExclude = "com.cloudbees.hudson.plugins.folder.health.ProjectEnabledHealthMetric"
final verb = dryRun ? "Would remove" : "Removing"
Jenkins.instance.allItems(com.cloudbees.hudson.plugins.folder.AbstractFolder).each { folder ->
    def removed = folder.healthMetrics.removeIf { metric ->
        if (metric.class.name.equals(metricToExclude)) {
            return false
        }
        println "${verb} ${metric.class.simpleName} from ${folder.name}"
        return !dryRun
    }
    if (removed) {
        folder.save()
    }
    folders_modified = folders_modified + 1
}

println(controller + ":  " + folders_modified)

return null