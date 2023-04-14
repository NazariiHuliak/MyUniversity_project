package com.example.university_app

import android.app.AlertDialog
import android.content.ContentValues
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.university_app.databinding.FragmentHomeBinding
import com.google.android.material.materialswitch.MaterialSwitch
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import org.w3c.dom.Text
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.*

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var myView: View

    private lateinit var auth: FirebaseAuth
    private val KEY_EMAIL = "email"
    private val KEY_PASSWORD = "password"
    private lateinit var switch: MaterialSwitch

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = Firebase.auth
        myView = view.findViewById<View>(R.id.popup_background)
        val sharedPreferences = requireActivity().getSharedPreferences("myPrefs", MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        fun showPopupWindow(view: View) {
            val inflater = requireContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val popupView = inflater.inflate(R.layout.account_dialog, null)

            val currentUser = FirebaseAuth.getInstance().currentUser
            val database = FirebaseDatabase.getInstance().reference
            val usersRef = database.child("users").child(currentUser!!.uid)

            usersRef.child("nickname").addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val nickname = dataSnapshot.getValue(String::class.java)
                    val nicknameTextView = popupView.findViewById<TextView>(R.id.nickname)
                    nicknameTextView.setText(nickname)
                }
                override fun onCancelled(databaseError: DatabaseError) {
                }
            })
            usersRef.child("group").addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val group = dataSnapshot.getValue(String::class.java)
                    val groupTextView = popupView.findViewById<TextView>(R.id.group)
                    groupTextView.setText(group)
                }
                override fun onCancelled(databaseError: DatabaseError) {
                }
            })


            val width = LinearLayout.LayoutParams.WRAP_CONTENT
            val height = LinearLayout.LayoutParams.WRAP_CONTENT
            val focusable = true
            val popupWindow = PopupWindow(popupView, width, height, focusable)
            popupWindow.setOnDismissListener {
                myView.visibility = View.INVISIBLE
            }

            val newPasswordButton = popupView.findViewById<TextView>(R.id.new_password)
            newPasswordButton.setOnClickListener {
                val builder = AlertDialog.Builder(context)
                builder.setTitle("Змінити пароль")

                val inputLayout = LinearLayout(context)
                inputLayout.orientation = LinearLayout.VERTICAL

                val inputNewPassword = EditText(context)
                inputNewPassword.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                inputNewPassword.hint = "Новий пароль"

                val inputConfirmPassword = EditText(context)
                inputConfirmPassword.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                inputConfirmPassword.hint = "Підтвердити пароль"

                inputLayout.addView(inputNewPassword)
                inputLayout.addView(inputConfirmPassword)
                builder.setView(inputLayout)
                builder.setPositiveButton("Ok") { dialog, which ->
                    val newPassword = inputNewPassword.text.toString()
                    val confirmPassword = inputConfirmPassword.text.toString()

                    if (newPassword == confirmPassword) {
                        // виклик функції зміни пароля
                        changePassword(newPassword)
                    } else {
                        Toast.makeText(context, "Паролі не співпадають", Toast.LENGTH_SHORT).show()
                    }
                }
                builder.setNegativeButton("Скасувати") { dialog, which -> dialog.cancel() }
                builder.show()
            }


            val logoutButton = popupView.findViewById<TextView>(R.id.sign_out_btn)
            logoutButton.setOnClickListener {
                FirebaseAuth.getInstance().signOut()
                auth.signOut()
                editor.clear()
                editor.commit()
                val intent = Intent(requireContext(), Login_Activity::class.java)
                startActivity(intent)
                popupWindow.dismiss()
                requireActivity().finish()
            }

            popupWindow.showAsDropDown(view)
        }

        val button = view.findViewById<ImageButton>(R.id.profile_photo)
        button.setOnClickListener {
            myView.visibility = View.VISIBLE
            showPopupWindow(button)
        }

        val currentUser = FirebaseAuth.getInstance().currentUser
        val database = FirebaseDatabase.getInstance().reference
        val usersRef = database.child("users").child(currentUser!!.uid)
        usersRef.child("group").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val group = dataSnapshot.getValue(String::class.java)
                val sharedPref =
                    requireActivity().getSharedPreferences("HomeFragment", Context.MODE_PRIVATE)
                val editor = sharedPref.edit()
                editor.putString("group", group)
                editor.apply()
            }

            override fun onCancelled(databaseError: DatabaseError) {
            }
        })

        val databaseAccess: DatabaseAccess = DatabaseAccess.getInstance(requireContext())
        databaseAccess.open()
        val sharedPref = requireActivity().getSharedPreferences("HomeFragment", Context.MODE_PRIVATE)
        val group_ = sharedPref.getString("group", "")
        val todays_lessons = group_?.let { databaseAccess.getData(getCurrentDayOfWeek(), it) }
        databaseAccess.close()

        val currentTime = getCurrentTime()

        var currentLessonFlag: Boolean = false
        var nextLessonFlag: Boolean = false
        if (todays_lessons != null) {
            for (i in 0 until todays_lessons.size) {
                val parts = todays_lessons[i].starttime.split("  ")
                val start = parts[0]
                val end = parts[1]

                if (compareTime(currentTime, start) &&  compareTime(end, currentTime)) {
                    view.findViewById<TextView>(R.id.current_lesson_time).text = todays_lessons[i].starttime
                    view.findViewById<TextView>(R.id.current_lesson).text = todays_lessons[i].subject
                    view.findViewById<TextView>(R.id.current_tutor).text = todays_lessons[i].tutor
                    if ('#' in todays_lessons[i].auditory) {
                        if(todays_lessons[i].auditory.replace("#", "") == "-1"){
                            view.findViewById<TextView>(R.id.current_auditory).text = "Лек."
                        } else {
                            view.findViewById<TextView>(R.id.current_auditory).text =
                                todays_lessons[i].auditory.replace("#", "")
                        }
                    } else if (todays_lessons[i].auditory == "0") {
                        view.findViewById<TextView>(R.id.current_auditory).text = "Ут."
                    } else {
                        view.findViewById<TextView>(R.id.current_auditory).text =
                            todays_lessons[i].auditory
                    }
                    currentLessonFlag = true

                    if (i != (todays_lessons.size - 1)) {
                        view.findViewById<TextView>(R.id.current_lesson_time_2).text = todays_lessons[i + 1].starttime
                        view.findViewById<TextView>(R.id.current_lesson_2).text = todays_lessons[i + 1].subject
                        nextLessonFlag = true
                    }
                    break
                } else if (i != (todays_lessons.size - 1)) {
                    val parts_n = todays_lessons[i + 1].starttime.split("  ")
                    if (compareTime(currentTime, end) && compareTime(parts_n[0], currentTime)) {
                        view.findViewById<TextView>(R.id.current_lesson_time).text = ""
                        view.findViewById<TextView>(R.id.current_lesson).text = "Перерва"
                        view.findViewById<TextView>(R.id.current_lesson).setTextColor(ContextCompat.getColor(requireContext(), R.color.dark_green))
                        view.findViewById<TextView>(R.id.current_tutor).text = ""
                        view.findViewById<TextView>(R.id.current_auditory).text = ""
                        currentLessonFlag = true

                        view.findViewById<TextView>(R.id.current_lesson_time_2).text = todays_lessons[i + 1].starttime
                        view.findViewById<TextView>(R.id.current_lesson_2).text = todays_lessons[i + 1].subject
                        nextLessonFlag = true
                        break
                    }
                }
            }

            if (todays_lessons.size == 0){
                view.findViewById<TextView>(R.id.current_lesson_time).text = ""
                view.findViewById<TextView>(R.id.current_lesson).text = "Пар немає"
                view.findViewById<TextView>(R.id.current_lesson).setTextColor(ContextCompat.getColor(requireContext(), R.color.dark_green))
                view.findViewById<TextView>(R.id.current_tutor).text = ""
                view.findViewById<TextView>(R.id.current_auditory).text = ""

                view.findViewById<TextView>(R.id.current_lesson_time_2).text = ""
                view.findViewById<TextView>(R.id.current_lesson_2).text = "Пар немає"

            } else {
                val parts = todays_lessons[0].starttime.split("  ")
                if (compareTime(parts[0], currentTime) && !currentLessonFlag) {
                    view.findViewById<TextView>(R.id.current_lesson_time_2).text = todays_lessons[0].starttime
                    view.findViewById<TextView>(R.id.current_lesson_2).text = todays_lessons[0].subject
                } else if(!nextLessonFlag){
                    view.findViewById<TextView>(R.id.current_lesson_time_2).text = ""
                    view.findViewById<TextView>(R.id.current_lesson_2).text = "На сьогодні все"
                }
                if (!currentLessonFlag) {
                    view.findViewById<TextView>(R.id.current_lesson_time).text = ""
                    view.findViewById<TextView>(R.id.current_lesson).text = "Пари немає"
                    view.findViewById<TextView>(R.id.current_lesson).setTextColor(ContextCompat.getColor(requireContext(), R.color.dark_green))
                    view.findViewById<TextView>(R.id.current_tutor).text = ""
                    view.findViewById<TextView>(R.id.current_auditory).text = ""
                }
            }

            view.findViewById<View>(R.id.rectangle_6).setOnClickListener {
                Log.d("test", "works")
            }
        }
    }

    fun changePassword(newPassword_: String){
        val user = FirebaseAuth.getInstance().currentUser

        val newPassword = newPassword_

        user?.updatePassword(newPassword)
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(ContentValues.TAG, "Password updated")
                } else {
                    Log.e(ContentValues.TAG, "Error updating password", task.exception)
                }
            }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getCurrentDayOfWeek(): String {
        val currentDay = LocalDate.now().dayOfWeek
        val currentDayName = currentDay.getDisplayName(TextStyle.FULL, Locale("uk", "UA"))
        return currentDayName.substring(0, 1).toUpperCase() + currentDayName.substring(1).replace("ʼ", "'")
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun getCurrentTime(): String {
        val currentTime = LocalTime.now()
        val formatter = DateTimeFormatter.ofPattern("HH:mm")
        return currentTime.format(formatter)
    }
    fun compareTime(time1: String, time2: String): Boolean {
        val (hours1, minutes1) = time1.split(':').map { it.toInt() }
        val (hours2, minutes2) = time2.split(':').map { it.toInt() }

        if (hours1 != hours2) {
            return hours1 >= hours2
        }

        return minutes1 >= minutes2
    }
}