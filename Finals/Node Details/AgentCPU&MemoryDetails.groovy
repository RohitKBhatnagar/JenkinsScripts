//Windows Agents CPU and Memory details

println "WMIC CPU Get DeviceID,NumberOfCores,NumberOfLogicalProcessors".execute().text
println "systeminfo | findstr /C:\"Total Physical Memory\"".execute().text

println "wmic computersystem get totalphysicalmemory".execute().text
println "wmic memorychip get devicelocator, manufacturer, partnumber, serialnumber, capacity, speed, memorytype, formfactor".execute().text

//println "wmic memorychip get/format:list".execute().text

//Linux Agents CPU and Memory details

//println "cat /proc/cpuinfo".execute().text
println "lscpu".execute().text
println "free -ht".execute().text
//println "less /proc/cpuinfo".execute().text

//println "awk '/(Mem|Swap)Free:/ { print }' /proc/meminfo".execute().text


//println "awk '/MemFree:/ {print \$2}' /proc/meminfo".execute().text