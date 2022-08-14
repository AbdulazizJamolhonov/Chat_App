package developer.abdulaziz.mychatappfirebase.Fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import developer.abdulaziz.mychatappfirebase.Adapters.MyMessageAdapter
import developer.abdulaziz.mychatappfirebase.Class.MyMessage
import developer.abdulaziz.mychatappfirebase.Class.User
import developer.abdulaziz.mychatappfirebase.Object.MyObject
import developer.abdulaziz.mychatappfirebase.databinding.FragmentMessageBinding
import developer.abdulaziz.mychatappfirebase.databinding.SendBottomItemBinding
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

@SuppressLint("SimpleDateFormat", "NotifyDataSetChanged")
class MessageFragment : Fragment() {
    private lateinit var binding: FragmentMessageBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference
    private lateinit var firebaseStorage: FirebaseStorage
    private lateinit var storageReference: StorageReference
    private lateinit var myMessageAdapter: MyMessageAdapter
    private lateinit var list: ArrayList<MyMessage>
    private var user: User? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMessageBinding.inflate(layoutInflater)
        binding.apply {
            auth = FirebaseAuth.getInstance()
            firebaseDatabase = FirebaseDatabase.getInstance()
            databaseReference = firebaseDatabase.getReference("messages")
            firebaseStorage = FirebaseStorage.getInstance()
            storageReference = firebaseStorage.getReference("images")
            list = ArrayList()

            myClick()

            if (MyObject.key) {
                user = MyObject.user
                title.text = user?.name
                Glide.with(root.context).load(user!!.image).into(image)
                online.text = user!!.online
            } else {
                online.visibility = View.GONE
            }
            myMessageAdapter = MyMessageAdapter(root.context, auth, list)
            rv.adapter = myMessageAdapter

            databaseReference.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    list.clear()
                    for (child in snapshot.children) {
                        val v = child.getValue(MyMessage::class.java)
                        if (v != null) {
                            if (MyObject.key) {
                                if ((v.fromUid == auth.uid && user?.uid == v.toUid) || (v.toUid == auth.uid && user?.uid == v.fromUid)) {
                                    v.name = user!!.name
                                    list.add(v)
                                }
                            } else {
                                if ((v.toUid == "group") || (v.fromUid == "group")) {
                                    list.add(v)
                                }
                            }
                        }
                    }
                    myMessageAdapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(context, error.message, Toast.LENGTH_SHORT).show()
                }
            })
        }
        return binding.root
    }

    private fun myClick() {
        binding.apply {
            back.setOnClickListener { findNavController().popBackStack() }
            imageSend.setOnClickListener {
                if (edtMessage.text.toString().isNotEmpty()) {
                    val message =
                        if (MyObject.key) MyMessage(
                            edtMessage.text.toString(),
                            auth.uid,
                            user!!.uid,
                            user!!.name
                        )
                        else {
                            var a = "Group"
                            for (i in MyObject.list) {
                                if (i.uid == auth.uid) {
                                    a = i.name!!
                                    break
                                }
                            }
                            MyMessage(edtMessage.text.toString(), auth.uid, "group", a)
                        }
                    databaseReference.child(databaseReference.push().key!!).setValue(message)
                    edtMessage.text?.clear()
                } else Toast.makeText(context, "Message is Empty", Toast.LENGTH_SHORT).show()
            }
            sendFile.setOnClickListener {
                val bottom = BottomSheetDialog(root.context)
                val item = SendBottomItemBinding.inflate(layoutInflater).apply {
                    image.setOnClickListener {
                        bottom.cancel()
                        getImage.launch("image/*")
                    }
                    audio.setOnClickListener {
                        bottom.cancel()
                        getAudio.launch("audio/*")
                    }
                    video.setOnClickListener {
                        bottom.cancel()
                        getVideo.launch("video/*")
                    }
                }
                bottom.setContentView(item.root)
                bottom.show()
            }
        }
    }

    private val getImage =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri ?: return@registerForActivityResult
            binding.progress.visibility = View.VISIBLE
            val task =
                storageReference.child(SimpleDateFormat("yyyy/MM/dd-hh:mm:ss").format(Date()))
                    .putFile(uri)
            task.addOnSuccessListener { my ->
                val downloadUrl = my.metadata?.reference?.downloadUrl
                downloadUrl?.addOnSuccessListener { url ->
                    val message = if (MyObject.key) {
                        MyMessage(
                            fromUid = auth.uid,
                            toUid = user!!.uid,
                            name = user!!.name,
                            type = "image",
                            path = "$url"
                        )
                    } else {
                        var a = "Group"
                        for (i in MyObject.list) {
                            if (i.uid == auth.uid) {
                                a = i.name!!
                                break
                            }
                        }
                        MyMessage(
                            fromUid = auth.uid,
                            toUid = "group",
                            name = a,
                            type = "image",
                            path = "$url"
                        )
                    }
                    databaseReference.child(databaseReference.push().key!!).setValue(message)
                    binding.progress.visibility = View.INVISIBLE
                }
            }
        }

    private val getAudio =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri ?: return@registerForActivityResult
            binding.progress.visibility = View.VISIBLE
            val task =
                storageReference.child(SimpleDateFormat("yyyy/MM/dd-hh:mm:ss").format(Date()))
                    .putFile(uri)
            task.addOnSuccessListener { my ->
                val downloadUrl = my.metadata?.reference?.downloadUrl
                downloadUrl?.addOnSuccessListener { url ->
                    val message = if (MyObject.key) {
                        MyMessage(
                            fromUid = auth.uid,
                            toUid = user!!.uid,
                            name = user!!.name,
                            type = "audio",
                            path = "$url"
                        )
                    } else {
                        var a = "Group"
                        for (i in MyObject.list) {
                            if (i.uid == auth.uid) {
                                a = i.name!!
                                break
                            }
                        }
                        MyMessage(
                            fromUid = auth.uid,
                            toUid = "group",
                            name = a,
                            type = "audio",
                            path = "$url"
                        )
                    }
                    databaseReference.child(databaseReference.push().key!!).setValue(message)
                    binding.progress.visibility = View.INVISIBLE
                }
            }
        }

    private val getVideo =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri ?: return@registerForActivityResult
            val task =
                storageReference.child(SimpleDateFormat("yyyy/MM/dd-hh:mm:ss").format(Date()))
                    .putFile(uri)
            task.addOnSuccessListener { my ->
                val downloadUrl = my.metadata?.reference?.downloadUrl
                downloadUrl?.addOnSuccessListener { url ->
                    val message = if (MyObject.key) {
                        MyMessage(
                            fromUid = auth.uid,
                            toUid = user!!.uid,
                            name = user!!.name,
                            type = "video",
                            path = "$url"
                        )
                    } else {
                        var a = "Group"
                        for (i in MyObject.list) {
                            if (i.uid == auth.uid) {
                                a = i.name!!
                                break
                            }
                        }
                        MyMessage(
                            fromUid = auth.uid,
                            toUid = "group",
                            name = a,
                            type = "video",
                            path = "$url"
                        )
                    }
                    databaseReference.child(databaseReference.push().key!!).setValue(message)
                    binding.progress.visibility = View.INVISIBLE
                }
            }
        }
}