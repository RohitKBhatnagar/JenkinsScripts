//Displays all users who have authenticated into Jenkins
import org.acegisecurity.*
import hudson.model.User
import jenkins.security.*
import java.util.Date

User.getAll().each{ u ->
  def prop = u.getProperty(LastGrantedAuthoritiesProperty)
  def realUser = false
  def timestamp = null
  if (prop) {
    realUser=true
    timestamp = new Date(prop.timestamp).toString()
  }

  if (realUser){
    println u.getId() + ':' + u.getDisplayName() + ':Jenkins-User:' + u?.getDescription() + ':timestamp=' + timestamp
  } else if (realUser==false){
    println u.getId() + ':' + u.getDisplayName() + ':No-Jenkins-User:' + u?.getDescription()
  }
}
return