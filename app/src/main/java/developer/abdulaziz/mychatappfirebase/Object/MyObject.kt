package developer.abdulaziz.mychatappfirebase.Object

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import developer.abdulaziz.mychatappfirebase.Class.User

object MyObject {
    var user = User()
    var list = ArrayList<User>()
    var key = false

    fun online(s: String) {
        val f = FirebaseAuth.getInstance().uid!!
        val m = HashMap<String, Any>()
        val d = FirebaseDatabase.getInstance().getReference("users").child(f)
        m["online"] = s
        d.updateChildren(m)
    }

    fun time(time: String) {
        val f = FirebaseAuth.getInstance().uid!!
        val m = HashMap<String, Any>()
        val d = FirebaseDatabase.getInstance().getReference("users").child(f)
        m["time"] = time
        d.updateChildren(m)
    }
}