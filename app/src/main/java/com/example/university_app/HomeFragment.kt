package com.example.university_app

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.university_app.databinding.FragmentHomeBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var myView: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        myView = view.findViewById<View>(R.id.popup_background)

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
            val focusable = true // lets taps outside the popup also dismiss it
            val popupWindow = PopupWindow(popupView, width, height, focusable)
            popupWindow.setOnDismissListener {
                myView.visibility = View.INVISIBLE
            }

            popupWindow.showAsDropDown(view)
        }

        val button = view.findViewById<ImageButton>(R.id.profile_photo)
        button.setOnClickListener {
            myView.visibility = View.VISIBLE
            showPopupWindow(button)
        }

    }
}