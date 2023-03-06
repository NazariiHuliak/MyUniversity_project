package com.example.university_app.onboarding.days

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.university_app.DatabaseAccess
import com.example.university_app.LessonModel
import com.example.university_app.R

class MondayFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.fragment_monday, container, false)
        var databaseAccess: DatabaseAccess = DatabaseAccess.getInstance(requireContext())

        databaseAccess.open()
        var dataList: MutableList<LessonModel> = databaseAccess.getData("Понеділок")
        databaseAccess.close()

        var time_List: MutableList<Int> = mutableListOf(
            R.id.time_text_id_1,
            R.id.time_text_id_2,
            R.id.time_text_id_3,
            R.id.time_text_id_4
        )
        var subject_List: MutableList<Int> = mutableListOf(
            R.id.subject_text_id_1,
            R.id.subject_text_id_2,
            R.id.subject_text_id_3,
            R.id.subject_text_id_4
        )
        var auditory_List: MutableList<Int> = mutableListOf(
            R.id.auditory_text_id_1,
            R.id.auditory_text_id_2,
            R.id.auditory_text_id_3,
            R.id.auditory_text_id_4
        )
        var tutor_List: MutableList<Int> = mutableListOf(
            R.id.tutor_text_id_1,
            R.id.tutor_text_id_2,
            R.id.tutor_text_id_3,
            R.id.tutor_text_id_4
        )
        var iterator = 0
        for(i in dataList){
            view.findViewById<TextView>(time_List[iterator]).text = i.tutor
            view.findViewById<TextView>(subject_List[iterator]).text = i.subject
            view.findViewById<TextView>(auditory_List[iterator]).text = i.auditory.toString()
            view.findViewById<TextView>(tutor_List[iterator]).text = i.starttime
            iterator++
        }
        return view
    }

}