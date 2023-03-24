package com.example.university_app

import android.app.AlertDialog
import android.content.ContentValues
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
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
}