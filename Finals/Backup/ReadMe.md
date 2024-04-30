# Title - <span style="color:purple">Jenkins backup script for controllers</span>
---

#### Author - <span style="color:yellow">Rohit K Bhatnagar</span>
#### Created On - Jan 31, 2023
---

** <span style="color:orange">Use this backup shell script to take a full backup of configuration details for each and every job folder in the target controller</span> **
> Please note the shell script requires large target disk space and also must continue even after individual user session has expired as this is a long runnign script

---

### USAGE: <span style="color:brown">JenkinsBackup.sh -h [-s <Full-Path to Jenkins Job Folder>] [-b <Full-Path to to backup folder>] [-c <Controller Name>] [-d <Artifactory MMM-YY storage date>]</span>
#### EXAMPLE: <span style="color:green">nohup ./JenkinsBackup.sh -s /sys_apps_01/jenkins_nfs/jobs -b /sys_apps_01/jenkins_utils/backups -c cd4 -d JAN23 &>/dev/null &</span>
#### HINT: All arguments for source, destination, controller and date related arguments are mandatory and must be supplied

---

```codetype
nohup ./JenkinsBackup.sh -s /tmp/jenkins -b /tmp/backups -c cd4 -d JAN23 >backup_jan23.log &
```

---

##### Few tips to monitor for logs...
###### Look for logs in executable folder of the JenkinsBackup shell script itself
###### Every target folder would accompany 3 logs - <Folder-Name_copy.log>, <Folder-Name_compress.log> & <Folder-Name_upload.log> 