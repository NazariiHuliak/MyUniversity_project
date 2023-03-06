package com.example.university_app

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import org.w3c.dom.Text

class TimetableFragment : Fragment() {
    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.fragment_timetable, container, false)
//        var group: String = "ПМІ14" //TODO:receiving information from firebase
//        var view_list: MutableList<View> = mutableListOf(
//            inflater.inflate(R.layout.fragment_monday, container, false),
//            inflater.inflate(R.layout.fragment_tuesday, container, false),
//            inflater.inflate(R.layout.fragment_wednesday, container, false),
//            inflater.inflate(R.layout.fragment_thursday, container, false),
//            inflater.inflate(R.layout.fragment_friday, container, false)
//        )
//        var time_List: MutableList<Int> = mutableListOf(
//            R.id.time_text_id_1,
//            R.id.time_text_id_2,
//            R.id.time_text_id_3,
//            R.id.time_text_id_4
//        )
//        var subject_List: MutableList<Int> = mutableListOf(
//            R.id.subject_text_id_1,
//            R.id.subject_text_id_2,
//            R.id.subject_text_id_3,
//            R.id.subject_text_id_4
//        )
//        var auditory_List: MutableList<Int> = mutableListOf(
//            R.id.auditory_text_id_1,
//            R.id.auditory_text_id_2,
//            R.id.auditory_text_id_3,
//            R.id.auditory_text_id_4
//        )
//        var tutor_List: MutableList<Int> = mutableListOf(
//            R.id.tutor_text_id_1,
//            R.id.tutor_text_id_2,
//            R.id.tutor_text_id_3,
//            R.id.tutor_text_id_4
//        )
//
//        var databaseAccess: DatabaseAccess = DatabaseAccess.getInstance(requireContext())
//        databaseAccess.open()
//        var dataList: MutableList<LessonModel> = databaseAccess.getData()
//        databaseAccess.close()
//        for(i in dataList){
//            Log.d("data:", "id: ${i.id} subject: ${i.subject} tutor:${i.tutor} auditory: ${i.auditory} day: ${i.day}  starttime: ${i.starttime} type: ${i.type}")
//        }
//        var iterator: Int = 0
//        var index: Int = 0
//        var old_index = -1
//
//        for (i in dataList){
//            when(i.day){
//                "Понеділок" -> index = 0
//                "Вівторок" -> index = 1
//                "Середа" -> index = 2
//                "Четвер" -> index = 3
//                "П'ятниця" -> index = 4
//            }
//            if(old_index == index){
//                iterator++
//            } else {
//                old_index = index
//                iterator = 0
//            }
////            view_list[index].findViewById<TextView>(time_List[iterator]).text = "1"
////            view_list[index].findViewById<TextView>(subject_List[iterator]).text = "1"
////            view_list[index].findViewById<TextView>(auditory_List[iterator]).text = "1"
////            view_list[index].findViewById<TextView>(tutor_List[iterator]).text = "1"
//        }
//
//        var view_Monday = inflater.inflate(R.layout.fragment_monday, container, false)
//        view_Monday.findViewById<TextView>(R.id.auditory_text_id_1).text = "999"

        return view
    }
}