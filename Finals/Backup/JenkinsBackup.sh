# Change log
# ==========
# Created By - Rohit K. Bhatnagar
# Created On - Jan 30, 2023
# Purpose - Take individual folder level back up of all *.xml of Jenkins jobs

echo "-------- Starting on hostname - "$HOSTNAME" ----"

if [ -z $1]; then
    echo "USAGE: JenkinsBackup.sh -h [-s <Full-Path to Jenkins Job Folder>] [-b <Full-Path to to backup folder>] [-c <Controller Name>] [-d <Artifactory MMM-YY storage date>"
    echo "EXAMPLE: nohup ./JenkinsBackup.sh -s /sys_apps_01/jenkins_nfs/jobs -b /sys_apps_01/jenkins_utils/backups -c cd4 -d JAN23 &"
    echo "HINT: All arguments for source, destination, controller and date related arguments are mandatory and must be supplied"
    exit 1
fi

while getopts hs:b:c:d: option
do 
    case "${option}"
        in
        s)jobSrc=${OPTARG};;
        b)bkpSrc=${OPTARG};;
        c)ctlr=${OPTARG};;
        d)dt=${OPTARG};;
        h)help=true;;
        *)
          echo "Usage: $0 [-h] [-s <Full-Path to Jenkins Job Folder>] [-b <Full-Path to to backup folder>] [-c <Controller Name>] [-d <Artifactory MMM-YY storage date>" >&2
          exit 1
        ;;
    esac
done

if [ "$help" = true ]; then
    echo "USAGE: JenkinsBackup.sh -h [-s <Full-Path to Jenkins Job Folder>] [-b <Full-Path to to backup folder>] [-c <Controller Name>] [-d <Artifactory MMM-YY storage date>"
    echo "EXAMPLE: ./JenkinsBackup.sh -s /sys_apps_01/jenkins_nfs/jobs -b /sys_apps_01/jenkins_utils/backups/ -c cd4 -d JAN23"
    echo "HINT: All arguments for source, destination, controller and date related arguments are mandatory and must be supplied"
    exit 1
fi

echo "Job Location      : $jobSrc"
echo "Backup Location   : $bkpSrc"
echo "Controller Name   : $ctlr"

mkdir "$bkpSrc/$ctlr/$dt"
echo " ===== STEP #1 of 3 for backup creation for $ctlr ============"
for dir in $jobSrc/*
do
        echo "------ Copying contents of folder - $dir at $(date) ----------"
        d=${dir##*/}
        rsync -avm --include='config.xml' --include='nectar-rbac.xml' --include='*/' --exclude='*' "$jobSrc/$d" "$bkpSrc/$ctlr/$dt" >"$bkpSrc/$ctlr/$dt/$d"_copy.log
        echo "Contents copied for folder - $d on $(date)"
        #tar -czf "$bkpSrc/$ctlr/$d".tar.gz "$bkpSrc/$ctlr/$d"
        #echo "Contents compressed for folder - $d at $(date)"
        #curl -u dep-cicd:Fuw9tooL -T "$i" https://artifacts.mastercard.int:443/artifactory/archive-external-release/jenkins/$ctlr/bkup/$dt/"$bkpSrc/$ctlr/$d".tar.gz
        #echo "Contents uploaded to artifactory for folder - $d at $(date)"
done

echo " ===== STEP #2 of 3 for backup creation for $ctlr ============"
#for dir in $bkpSrc/$ctlr/$dt/*
find $bkpSrc/$ctlr/$dt/* -prune -type d | while IFS= read -r dir;
do
        echo "------ Compressing contents of folder - $dir at $(date) ----------"
        d=${dir##*/}
        tar -czvf "$bkpSrc/$ctlr/$dt/$d".tar.gz "$bkpSrc/$ctlr/$dt/$d" >"$bkpSrc/$ctlr/$dt/$d"_compress.log
        echo "Contents compressed for folder - $d at $(date)"
done

echo " ===== STEP #3 of 3 for backup creation for $ctlr ============"
#for dir in $bkpSrc/$ctlr/$dt/*
find $bkpSrc/$ctlr/$dt/* -prune -type d | while IFS= read -r dir;
do
        echo "------ Uploading contents of folder - $dir at $(date) ----------"
        d=${dir##*/}
        curl -u dep-cicd:Fuw9tooL -T "$bkpSrc/$ctlr/$dt/$d".tar.gz https://artifacts.mastercard.int:443/artifactory/archive-external-release/jenkins/$ctlr/bkup/$dt/"$d".tar.gz >"$bkpSrc/$ctlr/$dt/$d"_upload.log
        echo "Contents uploaded to artifactory for folder - $d at $(date)"
done

echo " ===== BACKUP completed SUCCESSFULLY for $ctlr ============"

exit 0