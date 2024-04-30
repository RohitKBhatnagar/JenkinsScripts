#!Groovy
//Master HostName and IP Address
println java.net.InetAddress.getLocalHost()

TcpSlaveAgentListener.CLI_HOST_NAME=java.net.InetAddress.getLocalHost()