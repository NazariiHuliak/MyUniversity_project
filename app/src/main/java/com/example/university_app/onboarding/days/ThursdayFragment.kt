package com.example.university_app.onboarding.days

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.example.university_app.DatabaseAccess
import com.example.university_app.LessonModel
import com.example.university_app.R
import com.example.university_app.TimetableFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ThursdayFragment : Fragment() {

    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view =  inflater.inflate(R.layout.fragment_thursday, container, false)

        var databaseAccess: DatabaseAccess = DatabaseAccess.getInstance(requireContext())
        databaseAccess.open()

        val sharedPref = requireActivity().getSharedPreferences("onBoarding", Context.MODE_PRIVATE)
        var group_ = sharedPref.getString("group", "")

        var rectangleList = TimetableFragment.getRectangle()
        var objectsList = TimetableFragment.getObjectLists()
        var dataList: MutableList<LessonModel> = databaseAccess.getData("Четвер","$group_")
        var type = databaseAccess.getTypeOfWeek()
        databaseAccess.close()

        for ((iterator, i) in dataList.withIndex()) {
            if (i.type == type || i.type == 0) {
                view.findViewById<TextView>(objectsList[0][iterator]).text = i.starttime
                view.findViewById<TextView>(objectsList[1][iterator]).text = i.subject
                if(i.auditory == "0"){
                    view.findViewById<TextView>(objectsList[2][iterator]).text = "Невідомо"
                } else if('#' in i.auditory){
                    var auditory = i.auditory.replace("#", "")
                    if(auditory == "-1"){
                        view.findViewById<TextView>(objectsList[2][iterator]).text = "Лекція (дист.)"
                    }else{
                        view.findViewById<TextView>(objectsList[2][iterator]).text = "${i.auditory} (Лекція)"
                    }
                } else {
                    view.findViewById<TextView>(objectsList[2][iterator]).text = i.auditory
                }
                view.findViewById<TextView>(objectsList[3][iterator]).text = i.tutor
            }
        }
        var IcoList = TimetableFragment.getIcoList()
        if(dataList.size != 4){
            for(i in dataList.size..3){
                view.findViewById<TextView>(objectsList[0][i]).text = ""
                view.findViewById<TextView>(objectsList[1][i]).text = ""
                view.findViewById<TextView>(objectsList[2][i]).text = ""
                view.findViewById<TextView>(objectsList[3][i]).text = ""
                for(j in 0..2){
                    view.findViewById<ImageView>(IcoList[i][j]).visibility = View.INVISIBLE
                }
                view.findViewById<RelativeLayout>(rectangleList[i]).visibility = View.INVISIBLE
            }
        }
        return view
    }

}