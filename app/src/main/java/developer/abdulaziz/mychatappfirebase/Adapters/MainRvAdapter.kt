package developer.abdulaziz.mychatappfirebase.Adapters

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import developer.abdulaziz.mychatappfirebase.Class.User
import developer.abdulaziz.mychatappfirebase.databinding.ItemRvBinding

class MainRvAdapter(
    private val context: Context,
    private val list: ArrayList<User>,
    private val uid: String,
    private val myClick: MyClick
) :
    RecyclerView.Adapter<MainRvAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ItemRvBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun onBind(user: User) {
            binding.apply {
                Glide.with(context).load(user.image).into(image)
                name.text = user.name
                myRoot.setOnClickListener { myClick.onClick(user) }
                time.text = user.time
                online.visibility =
                    if (user.online == "online") View.VISIBLE
                    else View.GONE
                if (uid == user.uid) myRoot.setCardBackgroundColor(Color.parseColor("#009EFF"))
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(ItemRvBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(hol: ViewHolder, pos: Int) = hol.onBind(list[pos])
    override fun getItemCount(): Int = list.size
    interface MyClick {
        fun onClick(user: User)
    }
}