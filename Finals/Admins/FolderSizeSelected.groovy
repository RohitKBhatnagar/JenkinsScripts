import java.text.SimpleDateFormat
////////////////////////////////////////////////////////
def sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss.SSS")
def startDate = new Date()
println "Start time : ${sdf.format(startDate)}"
///////////////////////////////////////////////////////

def cd5arr = ["jkm9stl9.mastercard.int","AcquiringSubsystem","CAS_Deploy_BizOps","ChefHabitat","ConsumerSharedComponents","CrossProductUI","CryptoSecure","CyberSecurityServices","DataFeedManager","DataServicesTechnologies","DataServicesTechnologiesRelease","DecisionProcessingPCF","DecisionStrategyEnrichment","DFDA","DGR","DockerImagePipeline","Event_Framework","FinancialSolutions","FLS-WatchTower","Franchise_and_Legal_Solutions","GPS-E_CI","HeraclesApplications","HeraclesOperations","IDVerificationHub","IMMEDIATE PAYMENT SERVICE","InfrastructureAutomation","ManagedServices","Mars","MastercardPrepaidManagementServices","MastercardProcessing","MCCLDev","MCCSDEV","MCNEBIZOPS","MCNEDEV","MCNSBIZOPS","MCNWSBIZOPS","MCTSBizOps","MCTSDev","mpgs_qualitygate","MPS_Development","NODES"]
def cd3arr = ["jkm9stl4.mastercard.int", "Alberta","AlbertaOps","AlbertaOpsKSC","AlbertaOpsStage","App Release Automation","CAS-Consumer_Authentication_Solutions","core_billpay","CS_DPS","CustomerDataManagement","DataDeliveryAPI","DataDeliveryAPIRelease","DecisionAdminPCF","DigitalFoundationsBizOps","DISBIZOPS","DISMIDS","DisputeBizOps","EDS","EDWPaymentTransactionServices","ESS","ESSAuthenticationSolutions","ESSDeployBizOps","ESSIDAssist","ESSMastercom","EventFramework","Guild_Pulse","Heracles","IdentityCheckMobile","Local Market Intelligence","MastercardDigitalEnablementService","MCAPI","MCCoreQETS","MCSend","MCSendBizops","MICAutomation","Sample Projects Using BTE","SIMBizOps","SimplifyCommerce","SimplifyCommerceBizOps","StrategicGrowth","TFCPCF","WalletSolutionsDev"]
def cd2arr = ["jkm9stl3", "ApplicationFrameworks","Builder Tools Automation","BulkProcessingService","CloudasaService","Commercial","ConsumerProgramSRE-OPS","DataGovernanceandRegulation","DataInsightsPlatform","Digital-Marketing","EDW-PTS","EServicesNew","ICS","ImmediatePaymentServices","LoyaltyBenefits","LoyaltyOffers","LoyaltyRewards","LoyaltyRewardsProd","Mangesh Ops","MCCDEVOPS","MerchantServices","MultiProxyPlatform","NextGenAPIGWProd","NextGenAPIGWStage","PresentationEnablement","ProductManagement","PTS","PTS-Americas","SCSS","SecurityIdentityManagement","SPMADMIN","Surya App Dev"]
def controller = Jenkins.getInstance().getComputer('').getHostName()
println controller

if(cd5arr[0].equals(controller))
{
    println "------- CD5 Controller --------"

    for (String item : cd5arr) 
    {
        def startFldrDate = new Date()
        println "Start time : ${sdf.format(startFldrDate)}"
        
        def sout = new StringBuilder(), serr = new StringBuilder()
        
            def cmd = "du -sh /sys_apps_01/jenkins_nfs/jobs/$item"
            println cmd
            def proc = ['bash', '-c', cmd].execute()

            //proc.consumeProcessOutput(sout, serr)
            //proc.waitForOrKill(1000)
            proc.waitForProcessOutput(sout, serr)
            println "$sout"
        
        def endFldrDate = new Date()
        println "End time: ${sdf.format(endFldrDate)}"
    }
}
else if(cd3arr[0].equals(controller))
{
    println "------- CD3 Controller --------"

    for (String item : cd3arr) 
    {
        def startFldrDate = new Date()
        println "Start time : ${sdf.format(startFldrDate)}"
        
        def sout = new StringBuilder(), serr = new StringBuilder()
        
            def cmd = "du -sh /sys_apps_01/jenkins_nfs/jobs/$item"
            println cmd
            def proc = ['bash', '-c', cmd].execute()

            //proc.consumeProcessOutput(sout, serr)
            //proc.waitForOrKill(1000)
            proc.waitForProcessOutput(sout, serr)
            println "$sout"
        
        def endFldrDate = new Date()
        println "End time: ${sdf.format(endFldrDate)}"
    }
}
else if(cd2arr[0].equals(controller))
{
    println "------- CD2 Controller --------"

    for (String item : cd2arr) 
    {
        def startFldrDate = new Date()
        println "Start time : ${sdf.format(startFldrDate)}"
        
        def sout = new StringBuilder(), serr = new StringBuilder()
        
            def cmd = "du -sh /sys_apps_01/jenkins_nfs/jobs/$item"
            println cmd
            def proc = ['bash', '-c', cmd].execute()

            //proc.consumeProcessOutput(sout, serr)
            //proc.waitForOrKill(1000)
            proc.waitForProcessOutput(sout, serr)
            println "$sout"
        
        def endFldrDate = new Date()
        println "End time: ${sdf.format(endFldrDate)}"
    }
}

//---------------------------------------------------------------------------
def endDate = new Date()
println "End time: ${sdf.format(endDate)}"

use(groovy.time.TimeCategory) 
{
    def duration = endDate - startDate
    print "Elapsed time: Days: ${duration.days}, Hours: ${duration.hours}, Minutes: ${duration.minutes}, Seconds: ${duration.seconds}"
}