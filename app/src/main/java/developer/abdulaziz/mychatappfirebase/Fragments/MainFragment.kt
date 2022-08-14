package developer.abdulaziz.mychatappfirebase.Fragments

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import developer.abdulaziz.mychatappfirebase.Adapters.MainViewPagerAdapter
import developer.abdulaziz.mychatappfirebase.Object.MyObject
import developer.abdulaziz.mychatappfirebase.R
import developer.abdulaziz.mychatappfirebase.databinding.FragmentMainBinding
import developer.abdulaziz.mychatappfirebase.databinding.ItemTabBinding
import java.text.SimpleDateFormat
import java.time.*
import java.util.*

class MainFragment : Fragment() {
    private lateinit var binding: FragmentMainBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(layoutInflater)
        binding.apply {
            MyObject.online("online")
            MyObject.time("")
            listener()
            val listTitle = arrayListOf("Chats", "Groups")
            viewPager.adapter = MainViewPagerAdapter(this@MainFragment)
            TabLayoutMediator(tabLayout, viewPager) { tab, pos ->
                val tabView = ItemTabBinding.inflate(layoutInflater)
                tab.customView = tabView.root
                tabView.item.text = listTitle[pos]
                tabView.item.setBackgroundColor(Color.parseColor("#999999"))
            }.attach()
        }

        return binding.root
    }

    private fun listener() {
        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.customView?.findViewById<TextView>(R.id.item)
                    ?.setBackgroundColor(Color.parseColor("#0066FF"))
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                tab?.customView?.findViewById<TextView>(R.id.item)
                    ?.setBackgroundColor(Color.parseColor("#999999"))
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }

    override fun onPause() {
        super.onPause()
        MyObject.online("offline")
        val time = LocalTime.now().toString().substring(0, 5)
        val date = SimpleDateFormat("MM/dd").format(Date())
        MyObject.time("$date-$time")
    }
}