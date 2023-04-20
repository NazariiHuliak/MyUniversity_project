package com.example.university_app

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Rect
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.DisplayMetrics
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.view.inputmethod.EditorInfo
import androidx.appcompat.widget.ActivityChooserView
import androidx.navigation.NavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import com.google.android.material.textfield.TextInputEditText
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.textfield.TextInputLayout

class AuditoryFragment : Fragment() {

    private lateinit var newRecyclerView: RecyclerView
    private lateinit var newArraylist : ArrayList<Audiences>
    private lateinit var newDatalist : MutableList<AudienseModel>
    private lateinit var tempArraylist : ArrayList<Audiences>
    lateinit var imageid : MutableList<Int>
    lateinit var heading : MutableList<String>
    public lateinit var dataList : MutableList<AudienseModel>
    public lateinit var input: TextInputEditText
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var prefs: SharedPreferences
    private var isInputEmpty = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root_view = inflater.inflate(R.layout.fragment_auditory, container, false)
        input = root_view.findViewById<TextInputEditText>(R.id.serach_aud)
        val window = requireActivity().window
        val activityView = window.decorView.findViewById<View>(android.R.id.content)


        val bottomNavigationView = activityView?.findViewById<BottomNavigationView>(R.id.bottomNavigationView)

        input.setText(if (isInputEmpty) "Номер аудиторії" else "")

        input.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus && input.text.toString() == "Номер аудиторії") {
                input.setText("")
                isInputEmpty = false
            } else if (!hasFocus && input.text.toString().isEmpty()) {
                input.setText("Номер аудиторії")
                isInputEmpty = true
            }
        }
        activityView?.viewTreeObserver?.addOnGlobalLayoutListener {
            val heightDiff = activityView.rootView.height - activityView.height
            val keyboardOpen = heightDiff > dpToPx(root_view.context, 200f)
            if (keyboardOpen) {
                bottomNavigationView?.visibility = View.GONE
            } else {
                bottomNavigationView?.visibility = View.VISIBLE
            }
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
            heading.add(i.audience);

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
    override fun onResume() {
        super.onResume()
        this.input = input
        if (isInputEmpty) {
            input.setText("Номер аудиторії")
        }
    }
    private fun dpToPx(context: Context, dp: Float): Float {
        val resources = context.resources
        val metrics = resources.displayMetrics
        return dp * (metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT)
    }
    private fun getDayOfWeekString(dayOfWeek: Int): String {
    return when (dayOfWeek) {
        Calendar.SUNDAY -> "Неділя"
        Calendar.MONDAY -> "Понеділок"
        Calendar.TUESDAY -> "Вівторок"
        Calendar.WEDNESDAY -> "Середа"
        Calendar.THURSDAY -> "Четвер"
        Calendar.FRIDAY -> "П'ятниця"
        Calendar.SATURDAY -> "Субота"
        else -> throw IllegalArgumentException("Invalid day of week: $dayOfWeek")
    }
}
    private fun getData(dataList : MutableList<AudienseModel>, input : TextInputEditText){

        for(i in imageid.indices){
            val rooms = Audiences(imageid[i],heading[i])

            newArraylist.add(rooms)

        }



        input.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                newRecyclerView.adapter = AuditoryAdapter(newArraylist, dataList)
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            @SuppressLint("SuspiciousIndentation")
            override fun afterTextChanged(p0: Editable?) {
                tempArraylist = arrayListOf<Audiences>()
                    if(p0.toString() == "Номер аудиторії"){
                        tempArraylist.addAll(newArraylist)

                    }
                for(i in newArraylist){
                    if(i.heading.lowercase().contains(p0.toString().lowercase())){
                        tempArraylist.add(i)
                    }
                }
                if(tempArraylist.isEmpty()){
                    tempArraylist.add(Audiences(R.drawable.sad,"Збігів не знайдено"))
                }
                newRecyclerView.adapter = AuditoryAdapter(tempArraylist , dataList)
            }

        })


        newRecyclerView.adapter = AuditoryAdapter(newArraylist, dataList)
    }

}



