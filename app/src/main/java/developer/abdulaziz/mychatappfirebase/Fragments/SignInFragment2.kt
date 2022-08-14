package developer.abdulaziz.mychatappfirebase.Fragments

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import developer.abdulaziz.mychatappfirebase.Class.User
import developer.abdulaziz.mychatappfirebase.R
import developer.abdulaziz.mychatappfirebase.databinding.FragmentSignIn2Binding
import java.util.*
import kotlin.collections.ArrayList

class SignInFragment2 : Fragment() {
    private lateinit var binding: FragmentSignIn2Binding
    private lateinit var auth: FirebaseAuth
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference
    private lateinit var firebaseStorage: FirebaseStorage
    private lateinit var storageReference: StorageReference
    private lateinit var list: ArrayList<User>
    private lateinit var user: User
    private var imageUri: Uri? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignIn2Binding.inflate(layoutInflater)
        binding.apply {
            auth = FirebaseAuth.getInstance()
            firebaseDatabase = FirebaseDatabase.getInstance()
            databaseReference = firebaseDatabase.getReference("users")
            firebaseStorage = FirebaseStorage.getInstance()
            storageReference = firebaseStorage.getReference("images")
            list = ArrayList()

            image.setOnClickListener {
                getImageContent.launch("image/*")
            }

            btnSign.setOnClickListener {
                val name = edtName.text.toString()
                val email = "${edtEmail.text}@gmail.com"
                val password = edtPassword.text.toString()
                if (name.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()) {
                    signUp(name, email, password)
                } else Toast.makeText(context, "Info is Empty", Toast.LENGTH_SHORT).show()
            }

            return root
        }
    }

    private fun signUp(name: String, email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    if (imageUri != null) {
                        binding.progress.visibility = View.VISIBLE
                        val task =
                            storageReference.child("${System.currentTimeMillis()}")
                                .putFile(imageUri!!)
                        task.addOnSuccessListener { my ->
                            val downloadUrl = my.metadata?.reference?.downloadUrl
                            downloadUrl?.addOnSuccessListener { url ->
                                user =
                                    User(
                                        auth.currentUser?.uid,
                                        url.toString(),
                                        name,
                                        email,
                                        password
                                    )
                                databaseReference.child(auth.currentUser?.uid!!).setValue(user)
                                binding.progress.visibility = View.INVISIBLE
                                findNavController().popBackStack()
                                findNavController().popBackStack()
                                findNavController().navigate(R.id.mainFragment)
                                Toast.makeText(context, "Successful", Toast.LENGTH_SHORT).show()
                            }
                        }
                    } else Toast.makeText(context, "Image is Empty", Toast.LENGTH_SHORT).show()
                } else Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
            }
    }

    private val getImageContent =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri ?: return@registerForActivityResult
            binding.image.setImageURI(uri)
            imageUri = uri
        }
}