import com.cloudbees.hudson.plugins.folder.AbstractFolder;
def jenkins = Jenkins.getInstance()

def topItems = jenkins.getItems()
topItems.each
{
  	it ->
	println "--------- ${it.name} ---------"
  
  	try
    {
      def topFolderJobs = jenkins.getItemByFullName(it.name, AbstractFolder).getAllItems(Job);
      topFolderJobs.each 
      {
          fldrJobs ->
          println " +++++++ ${fldrJobs.name} ++++++++"
          def jobBuilds=fldrJobs.getBuilds()

          //for each of such jobs we can get all the builds (or you can limit the number at your convenience)
          jobBuilds.each 
          { 
            build ->
            def runningSince = groovy.time.TimeCategory.minus( new Date(), build.getTime() )
            def currentStatus = build.buildStatusSummary.message
            def cause = build.getCauses()[0] //we keep the first cause
            //This is a simple case where we want to get information on the cause if the build was 
            //triggered by an user
            def user = cause instanceof Cause.UserIdCause? cause.getUserId():""
            //This is an easy way to show the information on screen but can be changed at convenience
            println "Build: ${build} | Since: ${runningSince} | Status: ${currentStatus} | Cause: ${cause} | User: ${user}" 
            
            // You can get all the information available for build parameters.
            def parameters = build.getAction(ParametersAction)?.parameters
            parameters.each 
            {
              println "Type: ${it.class} Name: ${it.name}, Value: ${it.dump()}" 
            }
          }
      }
    }
  	catch(Exception exp)
  	{ }
}

return null;