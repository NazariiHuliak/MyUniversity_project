package com.example.university_app

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.widget.ViewPager2
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.*

class TimetableFragment : Fragment() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.fragment_timetable, container, false)

        Log.d("current", "${getCurrentDayOfWeek()}")
//        getCurrentDayOfWeek()
//        Log.d("current", "${getCurrentDayOfWeek()}")
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
        fun getIcoList():MutableList<MutableList<Int>>{
            var icoList1 = mutableListOf<Int>(
                R.id.ico_1_1_1,
                R.id.ico_1_1_2,
                R.id.ico_1_1_3
            )
            var icoList2 = mutableListOf<Int>(
                R.id.ico_1_2_1,
                R.id.ico_1_2_2,
                R.id.ico_1_2_3
            )
            var icoList3 = mutableListOf<Int>(
                R.id.ico_1_3_1,
                R.id.ico_1_3_2,
                R.id.ico_1_3_3
            )
            var icoList4 = mutableListOf<Int>(
                R.id.ico_1_4_1,
                R.id.ico_1_4_2,
                R.id.ico_1_4_3
            )
            return mutableListOf(icoList1,icoList2,icoList3,icoList4)
        }
        @RequiresApi(Build.VERSION_CODES.O)
        fun getCurrentDayOfWeek(): Int {
            val dayOfWeek = DayOfWeek.from(java.time.LocalDate.now())
            val formatter = DateTimeFormatter.ofPattern("EEEE", Locale.ENGLISH)
            when(dayOfWeek.getDisplayName(TextStyle.FULL, Locale.ENGLISH)){
                "Monday" -> return 0
                "Tuesday" -> return 1
                "Wednesday" -> return 2
                "Thursday" -> return 3
                "Friday" -> return 4
            }
            return 0
        }
    }
}