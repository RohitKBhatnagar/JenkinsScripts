//Builds inside specific top-folder
import com.cloudbees.hudson.plugins.folder.AbstractFolder;
def jenkins = Jenkins.getInstance()


def folderName = 'Blockchain'

//def topItems = jenkins.getItems()
//topItems.findAll
//{
	//println "--------- ${topItems.name} ---------"
	//topItems.each 
	//{
		//topI ->
		//println " +++++++ ${topI.name} ++++++++"
		try
		  {
			def topFolderJobs = jenkins.getItemByFullName(folderName/*topI.name*/, AbstractFolder).getAllItems(Job);
            println topFolderJobs
			topFolderJobs.each 
			{
				def jobBuilds=it.getBuilds()

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
		  catch (Exception exp)
		  {
			  println "++++++++ Exception - ${exp.message} ++++++"
		  }
//	}
//}

//return null

/*

//////////////
- Name: Blockchain
  description: A team folder for the Blockchain project team.
  ESF: 52ff5031-11b3-397a-a9e7-7e304d09358e
  Duplicate: false
  maintainers:
  - Altin.Hoxha@mastercard.com
  - Shehar.Yar@mastercard.com
  - MiguelAngel.RojoFernandez@mastercard.com
  - Ciaran.Molloy@mastercard.com
  - Lola.Roberts@mastercard.com
  groups:
    BlockchainAdmin:
      members:
      - cd_BLOCKCHAIN_admin
      roles:
      - FolderAdmin
    BlockchainUser:
      members:
      - cd_BLOCKCHAIN_user
      roles:
      - Developer
    BlockchainReadOnly:
      members:
      - cd_BLOCKCHAIN_ro
      - authenticated
      roles:
      - ReadOnly
    BlockchainBuildOnly:
      members:
      - cd_BLOCKCHAIN_bo
      roles:
      - BuildOnly



*/