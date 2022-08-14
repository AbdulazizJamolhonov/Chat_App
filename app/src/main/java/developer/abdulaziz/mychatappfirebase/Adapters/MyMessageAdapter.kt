package developer.abdulaziz.mychatappfirebase.Adapters

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import developer.abdulaziz.mychatappfirebase.Class.MyMessage
import developer.abdulaziz.mychatappfirebase.databinding.ItemMessageFromBinding
import developer.abdulaziz.mychatappfirebase.databinding.ItemMessageToBinding

class MyMessageAdapter(
    private val context: Context,
    private val auth: FirebaseAuth,
    private val list: ArrayList<MyMessage>
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    inner class ToVh(private val binding: ItemMessageToBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBindTo(message: MyMessage) {
            binding.apply {
                when (message.type) {
                    "text" -> {
                        myVisibleTo(binding, View.VISIBLE, View.GONE, View.GONE, View.GONE)
                        text.text = message.message
                        name.text = message.name
                    }
                    "image" -> {
                        myVisibleTo(binding, View.GONE, View.VISIBLE, View.GONE, View.GONE)
                        imageName.text = message.name
                        Glide.with(context).load(message.path).into(image)
                    }
                    "audio" -> {
                        myVisibleTo(binding, View.GONE, View.GONE, View.VISIBLE, View.GONE)
                        audioName.text = message.name
                        val media = MediaPlayer.create(context, Uri.parse(message.path))
                        audioPlay.setOnClickListener { media.start() }
                        audioPause.setOnClickListener { media.pause() }
                    }
                    "video" -> {
                        myVisibleTo(binding, View.GONE, View.GONE, View.GONE, View.VISIBLE)
                        videoName.text = message.name
                        video.setVideoPath(message.path)
                        video.setOnClickListener { video.start() }
                        video.setOnLongClickListener { video.pause(); true }
                    }
                }
            }
        }
    }

    inner class FromVh(private val binding: ItemMessageFromBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBindFrom(message: MyMessage) {
            binding.apply {
                when (message.type) {
                    "text" -> {
                        myVisibleFrom(binding, View.VISIBLE, View.GONE, View.GONE, View.GONE)
                        text.text = message.message
                    }
                    "image" -> {
                        myVisibleFrom(binding, View.GONE, View.VISIBLE, View.GONE, View.GONE)
                        Glide.with(context).load(message.path).into(image)
                    }
                    "audio" -> {
                        myVisibleFrom(binding, View.GONE, View.GONE, View.VISIBLE, View.GONE)
                        val media = MediaPlayer.create(context, Uri.parse(message.path))
                        audioPlay.setOnClickListener { media.start() }
                        audioPause.setOnClickListener { media.pause() }
                    }
                    "video" -> {
                        myVisibleFrom(binding, View.GONE, View.GONE, View.GONE, View.VISIBLE)
                        video.setVideoPath(message.path)
                        video.setOnClickListener { video.start() }
                        video.setOnLongClickListener { video.pause(); true }
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == 1) {
            FromVh(
                ItemMessageFromBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        } else {
            ToVh(
                ItemMessageToBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == 1) (holder as FromVh).onBindFrom(list[position])
        else (holder as ToVh).onBindTo(list[position])
    }

    override fun getItemCount(): Int = list.size

    override fun getItemViewType(position: Int): Int =
        if (list[position].fromUid == auth.currentUser?.uid) 1
        else 0

    private fun myVisibleFrom(binding: ItemMessageFromBinding, t: Int, i: Int, a: Int, v: Int) {
        binding.text.visibility = t
        binding.image.visibility = i
        binding.linearAudio.visibility = a
        binding.linearVideo.visibility = v
    }

    private fun myVisibleTo(binding: ItemMessageToBinding, t: Int, i: Int, a: Int, v: Int) {
        binding.linearText.visibility = t
        binding.linearImage.visibility = i
        binding.linearAudio.visibility = a
        binding.linearVideo.visibility = v
    }
}