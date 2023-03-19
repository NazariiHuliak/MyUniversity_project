package com.example.university_app

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import com.google.android.material.textfield.TextInputEditText

class AuditoryFragment : Fragment() {

    private lateinit var newRecyclerView: RecyclerView
    private lateinit var newArraylist : ArrayList<Audiences>
    private lateinit var newDatalist : MutableList<AudienseModel>
    private lateinit var tempArraylist : ArrayList<Audiences>
    lateinit var imageid : MutableList<Int>
    lateinit var heading : MutableList<String>
    public lateinit var dataList : MutableList<AudienseModel>


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        val root_view = inflater.inflate(R.layout.fragment_auditory, container, false)
        val input = root_view.findViewById<TextInputEditText>(R.id.serach_aud)
        input.onFocusChangeListener = View.OnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                input.setText("")
            }
//                else{
//                    input.setText("Назва деканату")
//                }
        }
        var databaseAccess: DatabaseAccessAudiense = DatabaseAccessAudiense.getInstance(requireContext())
        databaseAccess.open()
        var dataList: MutableList<AudienseModel> = databaseAccess.getAllData()

        databaseAccess.close()
        imageid = mutableListOf()
        heading = mutableListOf()

        val calendar = Calendar.getInstance()
        val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
        for (i in dataList){
            heading.add(i.audiense);

            val currentTime = Calendar.getInstance().timeInMillis

            val temp = getDayOfWeekString(dayOfWeek)
            if(i.day == getDayOfWeekString(dayOfWeek)) {

                val timeParts = i.start_lesson.split(":")
                val hours = timeParts[0].toInt()
                val minutes = timeParts[1].toInt()
                calendar.set(Calendar.HOUR_OF_DAY, hours)
                calendar.set(Calendar.MINUTE, minutes)
                val timeInMillis = calendar.timeInMillis

                val timeParts2 = i.end_lesson.split(":")
                val hours2 = timeParts2[0].toInt()
                val minutes2 = timeParts2[1].toInt()
                calendar.set(Calendar.HOUR_OF_DAY, hours2)
                calendar.set(Calendar.MINUTE, minutes2)
                val timeInMillis2 = calendar.timeInMillis

                if (currentTime >= timeInMillis && currentTime <= timeInMillis2) {
                    imageid.add(R.drawable.lock)




                } else {
                    imageid.add(R.drawable.enter)
                }
            }
            else{
                imageid.add(R.drawable.enter)
            }
        }
        newRecyclerView = root_view.findViewById(R.id.recycler)
        newRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        newRecyclerView.setHasFixedSize(true)

        newArraylist = arrayListOf<Audiences>()
        newDatalist = arrayListOf<AudienseModel>()
        getData(dataList, input)
        return root_view


    }


    private fun getDayOfWeekString(dayOfWeek: Int): String {
    return when (dayOfWeek) {
        Calendar.SUNDAY -> "Sunday"
        Calendar.MONDAY -> "Monday"
        Calendar.TUESDAY -> "Tuesday"
        Calendar.WEDNESDAY -> "Wednesday"
        Calendar.THURSDAY -> "Thursday"
        Calendar.FRIDAY -> "Friday"
        Calendar.SATURDAY -> "Saturday"
        else -> throw IllegalArgumentException("Invalid day of week: $dayOfWeek")
    }
}
    private fun getData(dataList : MutableList<AudienseModel>, input : TextInputEditText){

        for(i in imageid.indices){
            val rooms = Audiences(imageid[i],heading[i])

            newArraylist.add(rooms)

        }
      //  newRecyclerView.adapter = AuditoryAdapter(newArraylist, dataList)


        input.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                tempArraylist = arrayListOf<Audiences>()
//                    if(p0.toString() == "Назва деканату"){
//                        tempArraylist.addAll(newArraylist)
//                    }
                for(i in newArraylist){
                    if(i.heading.lowercase().contains(p0.toString().lowercase())){
                        tempArraylist.add(i)
                    }
                }
                if(tempArraylist.isEmpty()){
                    tempArraylist.add(Audiences(R.drawable.book,"Збігів не знайдено"))
                }
                newRecyclerView.adapter = AuditoryAdapter(tempArraylist , dataList)
            }

        })


        newRecyclerView.adapter = AuditoryAdapter(newArraylist, dataList)
    }

}







