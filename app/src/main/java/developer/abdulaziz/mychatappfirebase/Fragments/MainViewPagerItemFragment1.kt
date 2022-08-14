package developer.abdulaziz.mychatappfirebase.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import developer.abdulaziz.mychatappfirebase.Adapters.MainRvAdapter
import developer.abdulaziz.mychatappfirebase.Class.User
import developer.abdulaziz.mychatappfirebase.Object.MyObject
import developer.abdulaziz.mychatappfirebase.R
import developer.abdulaziz.mychatappfirebase.databinding.FragmentMainViewPagerItem1Binding

class MainViewPagerItemFragment1 : Fragment() {
    private lateinit var binding: FragmentMainViewPagerItem1Binding
    private lateinit var auth: FirebaseAuth
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference
    private lateinit var list: ArrayList<User>
    private lateinit var mainRvAdapter: MainRvAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainViewPagerItem1Binding.inflate(layoutInflater)
        auth = FirebaseAuth.getInstance()
        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase.getReference("users")
        binding.apply {
            list = ArrayList()
            mainRvAdapter = MainRvAdapter(
                root.context,
                list,
                auth.currentUser?.uid!!,
                object : MainRvAdapter.MyClick {
                    override fun onClick(user: User) {
                        MyObject.user = user
                        MyObject.key = true
                        findNavController().navigate(R.id.messageFragment)
                    }
                })
            rv.adapter = mainRvAdapter

            databaseReference.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    try {
                        list.clear()
                        for (child in snapshot.children) {
                            val value = child.getValue(User::class.java)
                            if (value != null) list.add(value)
                        }
                        mainRvAdapter.notifyDataSetChanged()
                    } catch (e: Exception) {
                        println(e.message)
                    }
                }

                override fun onCancelled(error: DatabaseError) {

                }
            })
        }
        return binding.root
    }
}