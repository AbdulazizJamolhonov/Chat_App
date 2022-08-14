package developer.abdulaziz.mychatappfirebase.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.database.*
import developer.abdulaziz.mychatappfirebase.Class.User
import developer.abdulaziz.mychatappfirebase.Object.MyObject
import developer.abdulaziz.mychatappfirebase.R
import developer.abdulaziz.mychatappfirebase.databinding.FragmentMainViewPagerItem2Binding

class MainViewPagerItemFragment2 : Fragment() {
    private lateinit var binding: FragmentMainViewPagerItem2Binding
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference
    private lateinit var list: ArrayList<User>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainViewPagerItem2Binding.inflate(layoutInflater)
        binding.apply {
            firebaseDatabase = FirebaseDatabase.getInstance()
            databaseReference = firebaseDatabase.getReference("users")
            list = ArrayList()

            myRoot.setOnClickListener {
                MyObject.list = list
                MyObject.key = false
                findNavController().navigate(R.id.messageFragment)
            }

            databaseReference.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    list.clear()
                    for (i in snapshot.children) {
                        val value = i.getValue(User::class.java)
                        list.add(value!!)
                    }
                }

                override fun onCancelled(error: DatabaseError) {}
            })
        }
        return binding.root
    }
}