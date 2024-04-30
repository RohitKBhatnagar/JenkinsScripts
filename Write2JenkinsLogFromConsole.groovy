#!groovy

//Master HostName and IP Address
println java.net.InetAddress.getLocalHost()

TcpSlaveAgentListener.CLI_HOST_NAME=java.net.InetAddress.getLocalHost()

//Following section writes to Jenkins log for persistence
import java.util.logging.Level;
import java.util.logging.Logger;
def Logger LOGGER = Logger.getLogger(this.class.getName());
println "${LOGGER.getName()}"
LOGGER.info("Logger Name: " + LOGGER.getName());

LOGGER.warning("Can cause ArrayIndexOutOfBoundsException");

//Using REST API call - you may now check/validate/view your log entry directly into the Jenkins logs
//https://cd4.mastercard.int/jenkins/log/all