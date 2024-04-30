//URL - https://github.com/cloudbees/jenkins-scripts/blob/master/count-cjoc-json.groovy
//Will work only in Script Console and NOT in CJOC hosted pipeline
//Result is JSON but all combined at CJOC itself!
//Modified to include active instance name in the output - https://www.tabnine.com/code/java/classes/hudson.model.Computer
import com.cloudbees.opscenter.server.model.*;
import com.cloudbees.opscenter.server.clusterops.steps.*;
import hudson.remoting.*;

def cjoc = getHost(new LocalChannel(), OperationsCenter.class.simpleName, OperationsCenter.class.simpleName)

cjoc.masters = []
Jenkins.instance.getAllItems(ConnectedMaster.class).each {
  cjoc.masters.add(getHost(it.channel, it.class.simpleName, it.encodedName))
}

try{
  cjoc.summary = [
      masters:cjoc.masters.size() + 1, //masters + cjoc
    ]
}catch(e){}

def getHost(channel, type, name){
  def host
  if(channel){
    def stream = new ByteArrayOutputStream();
    def listener = new StreamBuildListener(stream);
    channel.call(new MasterGroovyClusterOpStep.Script("""
      //master, regular slaves, and shared slaves
      def itmCounts = jenkins.model.Jenkins.instance.getAllItems()
      def topFolders = jenkins.model.Jenkins.instance.getItems()

def host = [type:'$type', name:'$name', url:Jenkins.instance.rootUrl, cores:Runtime.runtime.availableProcessors(), TotalItems:itmCounts.size, offline:false, hostname:Jenkins.getActiveInstance().getComputer("").getHostName(), TotalTopFolder:topFolders.size]

      return new groovy.json.JsonBuilder(host).toString()
    """, listener, "host-script.groovy", [:]));
    host = new groovy.json.JsonSlurper().parseText(stream.toString().minus("Result: "));
  } else {
    host = [type:type, name:name, offline:true]
  }
  return host;
}

return new groovy.json.JsonBuilder(cjoc).toPrettyString()

//VERY VERY Long running script almost 30-45 mins but will pull out all the details in a single JSON
/**************
Result: {
    "TotalItems": 103,
    "TotalTopFolder": 31,
    "cores": 8,
    "hostname": "joc9stl0",
    "masters": [
        {
            "TotalItems": 74980,
            "TotalTopFolder": 135,
            "cores": 12,
            "hostname": "jkm2stl0",
            "name": "cd.mastercard.int",
            "offline": false,
            "type": "ClientMaster",
            "url": "https://cd.mastercard.int/jenkins/"
        },
        {
            "TotalItems": 16,
            "TotalTopFolder": 4,
            "cores": 8,
            "hostname": "lstl9jkm8141.mastercard.int",
            "name": "cd0.mastercard.int",
            "offline": false,
            "type": "ClientMaster",
            "url": "http://cd0.mastercard.int/jenkins/"
        },
        {
            "TotalItems": 7212,
            "TotalTopFolder": 71,
            "cores": 8,
            "hostname": "lstl9jkm8883.mastercard.int",
            "name": "cd10.mastercard.int",
            "offline": false,
            "type": "ClientMaster",
            "url": "https://cd10.mastercard.int/jenkins/"
        },
        {
            "TotalItems": 32127,
            "TotalTopFolder": 11,
            "cores": 16,
            "hostname": "lstl9jkm7317.mastercard.int",
            "name": "cd11.mastercard.int",
            "offline": false,
            "type": "ClientMaster",
            "url": "https://cd11.mastercard.int/jenkins/"
        },
        {
            "TotalItems": 48562,
            "TotalTopFolder": 7,
            "cores": 16,
            "hostname": "lstl9jkm7690.mastercard.int",
            "name": "cd12.mastercard.int",
            "offline": false,
            "type": "ClientMaster",
            "url": "https://cd12.mastercard.int/jenkins/"
        },
        {
            "TotalItems": 3323,
            "TotalTopFolder": 18,
            "cores": 16,
            "hostname": "lstl9jkm7786.mastercard.int",
            "name": "cd13.mastercard.int",
            "offline": false,
            "type": "ClientMaster",
            "url": "https://cd13.mastercard.int/jenkins/"
        },
        {
            "TotalItems": 82205,
            "TotalTopFolder": 12,
            "cores": 16,
            "hostname": "lstl9jkm7910.mastercard.int",
            "name": "cd14.mastercard.int",
            "offline": false,
            "type": "ClientMaster",
            "url": "https://cd14.mastercard.int/jenkins/"
        },
        {
            "TotalItems": 68698,
            "TotalTopFolder": 14,
            "cores": 16,
            "hostname": "lstl9jkm8078.mastercard.int",
            "name": "cd15.mastercard.int",
            "offline": false,
            "type": "ClientMaster",
            "url": "https://cd15.mastercard.int/jenkins/"
        },
        {
            "TotalItems": 30028,
            "TotalTopFolder": 8,
            "cores": 16,
            "hostname": "lstl9jkm8190.mastercard.int",
            "name": "cd16.mastercard.int",
            "offline": false,
            "type": "ClientMaster",
            "url": "https://cd16.mastercard.int/jenkins/"
        },
        {
            "TotalItems": 185651,
            "TotalTopFolder": 157,
            "cores": 12,
            "hostname": "jkm9stl3",
            "name": "cd2.mastercard.int",
            "offline": false,
            "type": "ClientMaster",
            "url": "https://cd2.mastercard.int/jenkins/"
        },
        {
            "TotalItems": 111232,
            "TotalTopFolder": 101,
            "cores": 12,
            "hostname": "jkm9stl4.mastercard.int",
            "name": "cd3.mastercard.int",
            "offline": false,
            "type": "ClientMaster",
            "url": "https://cd3.mastercard.int/jenkins/"
        },
        {
            "TotalItems": 98441,
            "TotalTopFolder": 164,
            "cores": 12,
            "hostname": "jkm9stl6.mastercard.int",
            "name": "cd4.mastercard.int",
            "offline": false,
            "type": "ClientMaster",
            "url": "https://cd4.mastercard.int/jenkins/"
        },
        {
            "TotalItems": 245860,
            "TotalTopFolder": 168,
            "cores": 12,
            "hostname": "jkm9stl9.mastercard.int",
            "name": "cd5.mastercard.int",
            "offline": false,
            "type": "ClientMaster",
            "url": "https://cd5.mastercard.int/jenkins/"
        },
        {
            "TotalItems": 55524,
            "TotalTopFolder": 141,
            "cores": 8,
            "hostname": "lstl9jkm8720.mastercard.int",
            "name": "cd6.mastercard.int",
            "offline": false,
            "type": "ClientMaster",
            "url": "https://cd6.mastercard.int/jenkins/"
        },
        {
            "TotalItems": 4704,
            "TotalTopFolder": 20,
            "cores": 8,
            "hostname": "lstl9jkm8067.mastercard.int",
            "name": "cd7.mastercard.int",
            "offline": false,
            "type": "ClientMaster",
            "url": "https://cd7.mastercard.int/jenkins/"
        },
        {
            "TotalItems": 88,
            "TotalTopFolder": 3,
            "cores": 8,
            "hostname": "lstl9jkm8282.mastercard.int",
            "name": "cd8.mastercard.int",
            "offline": false,
            "type": "ClientMaster",
            "url": "https://cd8.mastercard.int/jenkins/"
        },
        {
            "TotalItems": 5670,
            "TotalTopFolder": 69,
            "cores": 8,
            "hostname": "lstl9jkm8739.mastercard.int",
            "name": "cd9.mastercard.int",
            "offline": false,
            "type": "ClientMaster",
            "url": "https://cd9.mastercard.int/jenkins/"
        },
        {
            "TotalItems": 1199,
            "TotalTopFolder": 5,
            "cores": 16,
            "hostname": "lstl9jkm7330.mastercard.int",
            "name": "cdmip.mastercard.int",
            "offline": false,
            "type": "ClientMaster",
            "url": "https://cdmip.mastercard.int/jenkins/"
        }
    ],
    "name": "OperationsCenter",
    "offline": false,
    "summary": {
        "masters": 19
    },
    "type": "OperationsCenter",
    "url": "https://oc.cd.mastercard.int/oc/"
}
**************/