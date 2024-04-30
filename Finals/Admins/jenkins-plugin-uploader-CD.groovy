//Jenkins Plugin Uploader - https://cd.mastercard.int/jenkins/job/JenkinsPlatformTeam/job/jenkins-plugin-uploader/configure

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
            //Updated plugins versions
            //['branch-api', '2.1045.v4ec3ed07b_e4f']
            //['workflow-durable-task-step', '1128.v8c259d125340']
            //['workflow-job', '1174.vdcb_d054cf74a_']
            //['workflow-multibranch', '707.v71c3f0a_6ccdb_']
            //['email-ext', '2.86']
            //['pipeline-utility-steps', '2.12.0']
            
            //Deprecated plugin workflow-cps-global-lib plugin has dependency on these plugins
            //['pipeline-groovy-lib', '593.va_a_fc25d520e9']

            //Suddenly went missing .... https://artifacts.mastercard.int/artifactory/archive-release/com/cloudbees/jenkins-oc/plugins3/
            //Were successfully uploaded to - https://artifacts.mastercard.int/artifactory/archive-external-release/com/cloudbees/jenkins-oc/plugins3/
            //['aws-java-sdk-ec2', '1.12.163-315.v2b_716ec8e4df'],
            //['aws-java-sdk-minimal', '1.12.163-315.v2b_716ec8e4df']
            //['durable-task', '495.v29cd95ec10f2']
            //['matrix-project', '758.v7a_ea_491852f3']
            
            //Missing or Forced
            //['ssh-slaves','1.821.vd834f8a_c390e']
            //['compuware-common-configuration','1.0.14']
            //['compuware-scm-downloader','2.0.13']
            //['compuware-topaz-utilities','1.0.9']
            
            //['operations-center-cloud','2.332.0.3']
            def plugins = [
                ['operations-center-client','2.332.0.1']
            ]
            
            String strDnUrl = "https://jenkins-updates.cloudbees.com/download/plugins/";
            //String strUpUrl = "https://artifacts.mastercard.int/artifactory/archive-external-release/com/cloudbees/jenkins-oc/plugins3/";
            String strUpUrl = "https://artifacts.mastercard.int/artifactory/archive-external-release/com/cloudbees/jenkins-controller/plugins3/";
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
                //strUpUrl = "https://artifacts.mastercard.int/artifactory/archive-external-release/com/cloudbees/jenkins-oc/plugins3/";
                strUpUrl = "https://artifacts.mastercard.int/artifactory/archive-external-release/com/cloudbees/jenkins-controller/plugins3/";
                iPluginCnt++;
            }
        }
    }
  }
}