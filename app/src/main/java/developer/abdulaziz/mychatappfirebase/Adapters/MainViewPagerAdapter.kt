package developer.abdulaziz.mychatappfirebase.Adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import developer.abdulaziz.mychatappfirebase.Fragments.MainViewPagerItemFragment1
import developer.abdulaziz.mychatappfirebase.Fragments.MainViewPagerItemFragment2

class MainViewPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return if (position == 1) MainViewPagerItemFragment2()
        else MainViewPagerItemFragment1()
    }
}