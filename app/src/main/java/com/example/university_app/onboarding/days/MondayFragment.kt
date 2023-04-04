package com.example.university_app.onboarding.days

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.viewpager2.widget.ViewPager2
import com.example.university_app.DatabaseAccess
import com.example.university_app.LessonModel
import com.example.university_app.R
import com.example.university_app.TimetableFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase


class MondayFragment : Fragment() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.fragment_monday, container, false)
        var databaseAccess: DatabaseAccess = DatabaseAccess.getInstance(requireContext())
        databaseAccess.open()
        var type = databaseAccess.getTypeOfWeek()

        val currentUser = FirebaseAuth.getInstance().currentUser
        val database = FirebaseDatabase.getInstance().reference
        val usersRef = database.child("users").child(currentUser!!.uid)

        usersRef.child("group")?.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var group = dataSnapshot.getValue(String::class.java).toString()
                val sharedPref = requireActivity().getSharedPreferences("onBoarding", Context.MODE_PRIVATE)
                val editor = sharedPref.edit()
                editor.putString("group", group)
                editor.apply()
            }
            override fun onCancelled(databaseError: DatabaseError) {
            }
        })
        val sharedPref = requireActivity().getSharedPreferences("onBoarding", Context.MODE_PRIVATE)
        var group_ = sharedPref.getString("group", "")

        var dataList: MutableList<LessonModel> = databaseAccess.getData("Понеділок", "$group_")
        var objectsList = TimetableFragment.getObjectLists()
        var IcoList = TimetableFragment.getIcoList()
        for ((iterator, i) in dataList.withIndex()) {
            if (i.type == type || i.type == 0) {
                view.findViewById<TextView>(objectsList[0][iterator]).text = i.tutor
                view.findViewById<TextView>(objectsList[1][iterator]).text = i.subject
                if(i.auditory == "0"){
                    view.findViewById<TextView>(objectsList[2][iterator]).text = "Невідомо"
                } else {
                    view.findViewById<TextView>(objectsList[2][iterator]).text = i.auditory
                }
                view.findViewById<TextView>(objectsList[3][iterator]).text = i.starttime
            }
        }
        if (dataList.size != 4) {
            for(i in dataList.size..3){
                view.findViewById<TextView>(objectsList[0][i]).text = ""
                view.findViewById<TextView>(objectsList[1][i]).text = ""
                view.findViewById<TextView>(objectsList[2][i]).text = ""
                view.findViewById<TextView>(objectsList[3][i]).text = ""
                for(j in 0..2){
                    view.findViewById<ImageView>(IcoList[i][j]).visibility = View.INVISIBLE
                }
            }
        }
        databaseAccess.close()
        var viewPager = activity?.findViewById<ViewPager2>(R.id.viewPager)
        viewPager?.currentItem = TimetableFragment.getCurrentDayOfWeek()
        return view
    }
}