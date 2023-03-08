package com.example.university_app

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.content.ContentProviderCompat.requireContext
import org.w3c.dom.Text
import java.time.LocalDate
import java.util.Objects

class TimetableFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.fragment_timetable, container, false)

//        var databaseAccess: DatabaseAccess = DatabaseAccess.getInstance(requireContext())
//        databaseAccess.open()
//        var dataList: MutableList<LessonModel> = databaseAccess.getData("ALL")
//        databaseAccess.close()
//        for(i in dataList){
//            Log.d("data:", "id: ${i.id} subject: ${i.subject} tutor:${i.tutor} auditory: ${i.auditory} day: ${i.day}  starttime: ${i.starttime} type: ${i.type}")
//        }
        return view
    }

    companion object {
        fun getObjectLists():MutableList<MutableList<Int>>{
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
            var objectsList = mutableListOf(time_List, subject_List, auditory_List, tutor_List)
            return objectsList
        }
    }
}