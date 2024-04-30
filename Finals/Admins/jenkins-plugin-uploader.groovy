node ('DEVCLD-LIN7')
{
  stage ('Plugin Download') 
  {
    withEnv(['ARTIFACTORY_BASE_URL=https://artifacts.mastercard.int/artifactory']) 
    {
      withCredentials([[
        $class: 'UsernamePasswordMultiBinding',
        credentialsId: 'dep-cicd',
        usernameVariable: 'ARTIFACTORY_USER',
        passwordVariable: 'ARTIFACTORY_PASSWORD']])
        {
            /**** Missing #1 ********
            def plugins = [
                ['javax-activation-api', '1.2.0-3'],
                ['workflow-api', '1144.v61c3180fa_03f']
                
                /////////
                ['pipeline-stage-tags-metadata', '1.9.3'],
                ['pipeline-model-extensions', '1.9.3'],
                ['pipeline-model-definition', '1.9.3'],
                ['pipeline-model-api', '1.9.3']
                
                ////////
                ['credentials', '2.6.2'],
                ['support-core', '2.81']
                
                ////////New Plugins
                ['lockable-resources', '2.15']
                ['operations-center-updatecenter','2.332.0.1']
                
                //Upgraded to
                ['workflow-cps', '2682.va_473dcddc941']
                
                ///These versions are available in core Jenkins
                ['credentials', '1074.1076.v39c30cecb_0e2']
                ['support-core', '1130.vb_eef6015fc37']
                
                ////// AWS related plugins
                ['aws-java-sdk-ec2','1.12.215-339.vdc07efc5320c']
                ['aws-java-sdk-minimal','1.12.215-339.vdc07efc5320c']
            ]*/
            def plugins = [
                ['aws-java-sdk-logs','1.12.215-339.vdc07efc5320c'],
                ['aws-java-sdk-elasticbeanstalk','1.12.215-339.vdc07efc5320c'],
                ['aws-java-sdk-codebuild','1.12.215-339.vdc07efc5320c'],
                ['aws-java-sdk-iam','1.12.215-339.vdc07efc5320c'],
                ['aws-java-sdk-ecr','1.12.215-339.vdc07efc5320c'],
                ['aws-java-sdk-cloudformation','1.12.215-339.vdc07efc5320c'],
                ['aws-java-sdk-ssm','1.12.215-339.vdc07efc5320c'],
                ['aws-java-sdk-ecs','1.12.215-339.vdc07efc5320c']
            ]
            
            String strDnUrl = "https://jenkins-updates.cloudbees.com/download/plugins/";
            String strUpUrl = "https://artifacts.mastercard.int/artifactory/archive-external-release/com/cloudbees/jenkins-oc/plugins3/";
            int iPluginCnt = 1;
            plugins.eachWithIndex 
            { item, iCount ->        
                //println "$iCount - ${item}"
                String strPluginName = "";
                String strPluginVersion = "";
                item.eachWithIndex 
                { i, iCnt ->
                    //println "\t${iCnt} - ${i}"
                    if(iCnt == 0)
                        strPluginName = i;
                    else
                        strPluginVersion = i;
                }
                //Download Artifact from CloudBees plugin website
                strDnUrl += strPluginName + "/" + strPluginVersion + "/" + strPluginName + ".hpi";
                println "\t\t ${iPluginCnt}, Artifactory Download, ${strDnUrl}";
                sh "curl -O ${strDnUrl}";
                
                //Upload Plugin to Artifactory
                strUpUrl += strPluginName + "/" + strPluginVersion + "/" + strPluginName + "-" + strPluginVersion + ".hpi";
                String strPluginFile = strPluginName + ".hpi";
                println "\t\t ${iPluginCnt}, Artifactory Upload, ${strUpUrl}"
                sh "curl -u $ARTIFACTORY_USER:$ARTIFACTORY_PASSWORD -T $strPluginFile $strUpUrl"

                //Bring both the download and upload url to base path settings again...
                strDnUrl = "https://jenkins-updates.cloudbees.com/download/plugins/";
                strUpUrl = "https://artifacts.mastercard.int/artifactory/archive-external-release/com/cloudbees/jenkins-oc/plugins3/";
                iPluginCnt++;
            }
        }
    }
  }
}