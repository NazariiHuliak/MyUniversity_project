package com.example.university_app

import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController

class SplashScreen : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        Handler().postDelayed({
            findNavController().navigate(R.id.action_splashScreen_to_viewPagerFragment)
        }, 0)
//        Handler().postDelayed({
//            view.viewPager2.setCurrentItem(startPosition, false)
//        }, 100)
        return inflater.inflate(R.layout.fragment_splash_screen, container, false)
    }

}