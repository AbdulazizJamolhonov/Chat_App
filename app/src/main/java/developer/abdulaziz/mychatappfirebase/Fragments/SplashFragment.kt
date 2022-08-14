package developer.abdulaziz.mychatappfirebase.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.navigation.fragment.findNavController
import developer.abdulaziz.mychatappfirebase.Object.MyObject
import developer.abdulaziz.mychatappfirebase.R
import developer.abdulaziz.mychatappfirebase.databinding.FragmentSplashBinding

class SplashFragment : Fragment() {
    private lateinit var binding: FragmentSplashBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSplashBinding.inflate(layoutInflater)
        binding.apply {
            val anim = AnimationUtils.loadAnimation(root.context, R.anim.my_splash_anim)
            splashText.startAnimation(anim)
            anim.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(p0: Animation?) {}
                override fun onAnimationEnd(p0: Animation?) {
                    val anim2 = AnimationUtils.loadAnimation(root.context, R.anim.my_splash_anim2)
                    splashText.startAnimation(anim2)
                    anim2.setAnimationListener(object : Animation.AnimationListener {
                        override fun onAnimationStart(p0: Animation?) {}
                        override fun onAnimationEnd(p0: Animation?) {
                            findNavController().popBackStack()
                            findNavController().navigate(R.id.signInFragment)
                        }

                        override fun onAnimationRepeat(p0: Animation?) {}
                    })
                }

                override fun onAnimationRepeat(p0: Animation?) {}
            })
        }
        return binding.root
    }
}