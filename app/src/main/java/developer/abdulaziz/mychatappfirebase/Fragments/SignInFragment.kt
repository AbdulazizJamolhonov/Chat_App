package developer.abdulaziz.mychatappfirebase.Fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import developer.abdulaziz.mychatappfirebase.Class.User
import developer.abdulaziz.mychatappfirebase.R
import developer.abdulaziz.mychatappfirebase.databinding.FragmentSignInBinding

@SuppressLint("SetTextI18n")
class SignInFragment : Fragment() {
    private lateinit var binding: FragmentSignInBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference
    private lateinit var list: ArrayList<User>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignInBinding.inflate(layoutInflater)
        binding.apply {
            auth = FirebaseAuth.getInstance()
            firebaseDatabase = FirebaseDatabase.getInstance()
            databaseReference = firebaseDatabase.getReference("users")
            list = ArrayList()

            btnSign.setOnClickListener { findNavController().navigate(R.id.signInFragment2) }

            btnLogin.setOnClickListener {
                val email = "${edtEmail.text}@gmail.com"
                val password = edtPassword.text.toString()
                if (email.isNotEmpty() && password.isNotEmpty()) {
                    login(email, password)
                } else Toast.makeText(context, "Info is Empty", Toast.LENGTH_SHORT).show()
            }

            return root
        }
    }


    private fun login(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    findNavController().popBackStack()
                    findNavController().navigate(R.id.mainFragment)
                } else Toast.makeText(context, "User doesn't exist", Toast.LENGTH_SHORT).show()
            }
    }
}