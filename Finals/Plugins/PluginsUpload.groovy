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
                ['ace-editor', '1.1'],
['antisamy-markup-formatter', '159.v25b_c67cd35fb_'],
['apache-httpcomponents-client-4-api', '4.5.14-150.v7a_b_9d17134a_5'],
['authentication-tokens', '1.53.v1c90fd9191a_b_'],
['aws-credentials', '191.vcb_f183ce58b_9'],
['aws-java-sdk-ec2', '1.12.481-392.v8b_291cfcda_09'],
['aws-java-sdk-minimal', '1.12.481-392.v8b_291cfcda_09'],
['blueocean-commons', '1.27.11'],
['bootstrap5-api', '5.3.0-1'],
['bouncycastle-api', '2.29'],
['branch-api', '2.1128.v717130d4f816'],
['caffeine-api', '3.1.6-115.vb_8b_b_328e59d8'],
['checks-api', '2.0.0'],
['cloudbees-aborted-builds', '1.23'],
['cloudbees-administrative-monitors', '1.0.11'],
['cloudbees-analytics', '1.53'],
['cloudbees-assurance', '2.276.0.29'],
['cloudbees-casc-server', '2.13'],
['cloudbees-folder', '6.872.876.cb-vb_38131665686'],
['cloudbees-folders-plus', '3.29'],
['cloudbees-ha', '4.41'],
['cloudbees-jenkins-advisor', '336.v4d00382fe22c'],
['cloudbees-license', '9.78'],
['cloudbees-monitoring', '2.16'],
['cloudbees-nodes-plus', '1.25'],
['cloudbees-platform-common', '1.26'],
['cloudbees-platform-data', '1.35'],
['cloudbees-plugin-usage', '2.19'],
['cloudbees-quiet-start', '1.9'],
['cloudbees-ssh-slaves', '2.23'],
['cloudbees-support', '3.31'],
['cloudbees-uc-data-api', '4.57'],
['cloudbees-unified-ui', '1.26'],
['cloudbees-update-center-plugin', '4.8'],
['command-launcher', '100.102.v60240195db_50'],
['commons-lang3-api', '3.13.0-62.v7d18e55f51e2'],
['commons-text-api', '1.10.0-36.vc008c8fcda_7b_'],
['credentials-binding', '604.vb_64480b_c56ca_'],
['credentials', '1254.vb_96f366e7b_a_d'],
['data-tables-api', '1.13.5-1'],
['display-url-api', '2.3.7'],
['docker-commons', '434.v3dc53576ec32'],
['docker-workflow', '563.vd5d2e5c4007f'],
['durable-task', '510.v324450f8dca_4'],
['echarts-api', '5.4.0-5'],
['email-ext', '2.99'],
['font-awesome-api', '6.4.0-1'],
['git', '5.2.0'],
['git-client', '4.4.0'],
['git-server', '99.101.v720e86326c09'],
['handy-uri-templates-2-api', '2.1.8-22.v77d5b_75e6953'],
['infradna-backup', '3.38.65'],
['instance-identity', '173.va_37c494ec4e5'],
['ionicons-api', '56.v1b_1c8c49374e'],
['jackson2-api', '2.15.2-350.v0c2f3f8fc595'],
['jakarta-activation-api', '2.0.1-3'],
['jakarta-mail-api', '2.0.1-3'],
['javax-activation-api', '1.2.0-6'],
['javax-mail-api', '1.6.2-9'],
['jaxb', '2.3.8-1'],
['jdk-tool', '66.vd8fa_64ee91b_d'],
['jjwt-api', '0.11.5-77.v646c772fddb_0'],
['jquery3-api', '3.7.0-1'],
['jsch', '0.2.8-65.v052c39de79b_2'],
['junit', '1217.v4297208a_a_b_ce'],
['ldap', '682.v7b_544c9d1512'],
['lockable-resources', '1185.v0c528656ce04'],
['mailer', '457.v3f72cb_e015e5'],
['mapdb-api', '1.0.9-28.vf251ce40855d'],
['matrix-auth', '3.1.9'],
['matrix-project', '789.v57a_725b_63c79'],
['metrics', '4.2.18-439.v86a_20b_a_8318b_'],
['mina-sshd-api-common', '2.10.0-69.v28e3e36d18eb_'],
['mina-sshd-api-core', '2.10.0-69.v28e3e36d18eb_'],
['mina-sshd-api-scp', '2.10.0-69.v28e3e36d18eb_'],
['mina-sshd-api-sftp', '2.10.0-69.v28e3e36d18eb_'],
['nectar-license', '8.41'],
['nectar-rbac', '5.82'],
['node-iterator-api', '49.v58a_8b_35f8363'],
['okhttp-api', '4.11.0-145.vcb_8de402ef81'],
['operations-center-agent', '3.0.15'],
['operations-center-clusterops', '3.0.15'],
['operations-center-context', '3.0.15'],
['operations-center-jnlp-controller', '3.0.15'],
['operations-center-license', '3.0.15'],
['operations-center-monitoring', '3.0.15'],
['operations-center-rbac', '3.0.15'],
['operations-center-server', '3.0.15'],
['operations-center-sso', '3.0.15'],
['operations-center-updatecenter', '3.0.15'],
['pipeline-build-step', '539.v8c889169451f'],
['pipeline-graph-analysis', '202.va_d268e64deb_3'],
['pipeline-groovy-lib', '656.va_a_ceeb_6ffb_f7'],
['pipeline-input-step', '466.v6d0a_5df34f81'],
['pipeline-milestone-step', '111.v449306f708b_7'],
['pipeline-model-api', '2.2141.v5402e818a_779'],
['pipeline-model-definition', '2.2131.vb_9788088fdb_5'],
['pipeline-model-extensions', '2.2141.v5402e818a_779'],
['pipeline-rest-api', '2.32'],
['pipeline-stage-step', '305.ve96d0205c1c6'],
['pipeline-stage-tags-metadata', '2.2131.vb_9788088fdb_5'],
['pipeline-utility-steps', '2.16.0'],
['plain-credentials', '143.v1b_df8b_d3b_e48'],
['plugin-util-api', '3.3.0'],
['popper2-api', '2.11.6-2'],
['scm-api', '676.v886669a_199a_a_'],
['script-security', '1265.va_fb_290b_4b_d34'],
['snakeyaml-api', '1.33-95.va_b_a_e3e47b_fa_4'],
['ssh-agent', '333.v878b_53c89511'],
['ssh-credentials', '305.v8f4381501156'],
['sshd', '3.303.vefc7119b_ec23'],
['structs', '324.va_f5d6774f3a_d'],
['support-core', '1328.vb_4e66d4d525c'],
['token-macro', '359.vb_cde11682e0c'],
['trilead-api', '2.84.v72119de229b_7'],
['user-activity-monitoring', '1.11'],
['variant', '59.vf075fe829ccb'],
['workflow-aggregator', '590.v6a_d052e5a_a_b_5'],
['workflow-api', '1232.v1679fa_2f0f76'],
['workflow-basic-steps', '1042.ve7b_140c4a_e0c'],
['workflow-cps', '3740.v6d35b_4ed5f9f'],
['workflow-cps-checkpoint', '2.16'],
['workflow-cps-global-lib', '609.vd95673f149b_b'],
['workflow-durable-task-step', '1247.v7f9dfea_b_4fd0'],
['workflow-job', '1358.cb-v9d7288ddb_6a_8'],
['workflow-multibranch', '770.v1a_d0708dd1f6'],
['workflow-remote-loader', '1.6'],
['workflow-scm-step', '415.v434365564324'],
['workflow-step-api', '639.v6eca_cd8c04a_a_'],
['workflow-support', '839.v35e2736cfd5c']
            ]
            
            String strDnUrl = "https://jenkins-updates.cloudbees.com/download/plugins/";
            //String strUpUrl = "https://artifacts.mastercard.int/artifactory/archive-external-release/com/cloudbees/jenkins-oc/plugins3/";
            String strUpUrl = "https://artifacts.mastercard.int/artifactory/archive-external-release/com/cloudbees/jenkins-oc/CI-Trad/plugins/"
            //String strUpUrl = "https://artifacts.mastercard.int/artifactory/archive-external-release/com/cloudbees/jenkins-controller/plugins3/";
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
                //strUpUrl = "https://artifacts.mastercard.int/artifactory/archive-external-release/com/cloudbees/jenkins-controller/plugins3/";
                strUpUrl = "https://artifacts.mastercard.int/artifactory/archive-external-release/com/cloudbees/jenkins-oc/CI-Trad/plugins/";
                iPluginCnt++;
            }
        }
    }
  }
}