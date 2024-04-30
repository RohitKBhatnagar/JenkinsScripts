//Encrypt or Decrypt values on particular controller
println (hudson.util.Secret.decrypt("{AQAAABAAAAAQi/PYikWtM/zgt6rmNNzGxSiWifjD+DuMj19BHovTdRs=}")) //"ecms_user"
println (hudson.util.Secret.decrypt("{AQAAABAAAAAwqJ4/l44TBmgBwjhpfr6273uJ7obtzlqAthhuhwprLQnCRRkxuKFoEMgreaNxykbMZe69xzg8IyiVAXp/7IA0BQ==}")) //bte_vault_prod

println " ------------ ecms_user ---------"
def mySecret = hudson.util.Secret.fromString("wfxRAnErn!")
def myEncString = mySecret.getEncryptedValue() //{AQAAABAAAAAQgipER9TQkipEmMe13SOZja0Wden4pj9Co+wg6g9O9Jw=}
println ("ecms_user encryption yields - ${myEncString}")

println ("ecms_user decryption yields - ${hudson.util.Secret.decrypt(myEncString)}")

println " ------------ bte_vault_prod ---------"
mySecret = hudson.util.Secret.fromString("245adfb2-aea0-e170-40b1-88cc5a299d76!")
myEncString = mySecret.getEncryptedValue() //{AQAAABAAAAAQgipER9TQkipEmMe13SOZja0Wden4pj9Co+wg6g9O9Jw=}
println ("bte_vault_prod encryption yields - ${myEncString}")

println ("bte_vault_prod decryption yields - ${hudson.util.Secret.decrypt(myEncString)}")

//---------------- New JCasC values --------------
println (hudson.util.Secret.decrypt("{AQAAABAAAAAQ31PXbGG2gMJgL/SMmB5fDnFvjOoYsBWqs6aRAo6BHtQ=}")) //"ecms_user"
println (hudson.util.Secret.decrypt("{AQAAABAAAAAwUJThJSgJTWB/70FTDs/Pld/GTwzKxA2WwAmArRl6Am2rDuA6M9d71hlq4Z5dtKPfJibfIBrfkDIPkqRdoJW9cQ==}")) //bte_vault_prod