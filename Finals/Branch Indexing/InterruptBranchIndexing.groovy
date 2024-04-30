import groovy.time.TimeCategory

/**
 * Find all branch indexing that have been running for more than the **delay** specified and interrupt them.
 */
use(TimeCategory) {
  def delay = 2.hours /*minutes*/
  jenkins.model.Jenkins.instanceOrNull.toComputer().allExecutors
    .findAll { exec -> 
      exec.elapsedTime > delay.toMilliseconds()
      exec.currentExecutable != null && 
      exec.currentExecutable instanceof jenkins.branch.MultiBranchProject.BranchIndexing 
    }.each { exec ->
      println " * Stopping Branch Indexing of ${exec.currentExecutable.parent.fullDisplayName} that spent ${exec.elapsedTime}ms scanning on ${exec.owner.displayName} #${exec.number}..."
      exec.interrupt(hudson.model.Result.ABORTED, new jenkins.model.CauseOfInterruption() {
        @Override
        String getShortDescription() {
          return "Branch Indexing was running for too long. Contact Builder Tools – Prod Support <Builder_tools_prod_support@mastercard.com>"
       	}
      })
    }
}

return


/*************
 * Result on CD5 on July 6, 2022 @ 9:53 AM CST
 * Stopping Branch Indexing of Mastercard - Transaction Switching - Development » Application Pipelines » CustomerParameterManagement » Dev - CI » BFF that spent 1092045ms scanning on master #-1...
 * Stopping Branch Indexing of Mastercard Prepaid Management Services » MasterCard Assemble » BTE Gblobal Pipelines - Naga » Naga gdpr view » Bigdata Dev Deploy » mc_prepaid_naga_gdpr_view that spent 47882ms scanning on master #-1...
 * Stopping Branch Indexing of Mastercard - Network Services - Development » Application Pipelines » CryptogramSecurityServices_IS4 » CspMdesChipPrevalEventHandler » multibranchBuildScanCreateSnapshot that spent 15918ms scanning on master #-1...
 * Stopping Branch Indexing of HeraclesApplications » com-mastercard-mpam-acq-mea-csr_argus-validator-b2b-csr » com-mastercard-mpam-acq-mea-csr_argus-validator-b2b-csr-validator that spent 4760ms scanning on master #-1...
 * Stopping Branch Indexing of HeraclesApplications » com-mastercard-dgr_gdp-public » com-mastercard-dgr_gdp-public-validator that spent 2996ms scanning on master #-1...
 ************/
