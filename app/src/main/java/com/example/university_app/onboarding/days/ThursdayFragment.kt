package com.example.university_app.onboarding.days

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.example.university_app.DatabaseAccess
import com.example.university_app.LessonModel
import com.example.university_app.R
import com.example.university_app.TimetableFragment

class ThursdayFragment : Fragment() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view =  inflater.inflate(R.layout.fragment_thursday, container, false)

        var databaseAccess: DatabaseAccess = DatabaseAccess.getInstance(requireContext())
        databaseAccess.open()
        var dataList: MutableList<LessonModel> = databaseAccess.getData("Четвер")
        var type = databaseAccess.getTypeOfWeek()
        databaseAccess.close()

        var objectsList = TimetableFragment.getObjectLists()

        for((iterator, i) in dataList.withIndex()){
            if(i.type == type || i.type == 0){
                view.findViewById<TextView>(objectsList[0][iterator]).text = i.tutor
                view.findViewById<TextView>(objectsList[1][iterator]).text = i.subject
                view.findViewById<TextView>(objectsList[2][iterator]).text = i.auditory.toString()
                view.findViewById<TextView>(objectsList[3][iterator]).text = i.starttime
            }
        }
        return view
    }

}