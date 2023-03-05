package com.example.university_app.onboarding

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.example.university_app.R
import com.example.university_app.onboarding.days.*
import me.relex.circleindicator.CircleIndicator3

class ViewPagerFragment : Fragment() {

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_view_pager, container, false)

        val fragmentlist = arrayListOf<Fragment>(
            MondayFragment(),
            TuesdayFragment(),
            WednesdayFragment(),
            ThursdayFragment(),
            FridayFragment()
        )
        val adapter = ViewPagerAdapter(fragmentlist, requireActivity().supportFragmentManager, lifecycle)

        val viewPager2 = view.findViewById<ViewPager2>(R.id.viewPager)
        viewPager2.adapter = adapter

        val indicator = view.findViewById<CircleIndicator3>(R.id.indicator)
        indicator.setViewPager(viewPager2)

        return view
    }

}