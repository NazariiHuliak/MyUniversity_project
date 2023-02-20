package com.example.university_app

import android.R
import android.R.attr.button
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.PopupWindow
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment


class HomeFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
//        val root_view = inflater.inflate(R.layout.fragment_home, container, false)
//        val btn_contacts = root_view.findViewById<ImageButton>(R.id.profile_photo)
//        btn_contacts.setOnClickListener {
//            val dialogBining = layoutInflater.inflate(R.layout.account_dialog, null)
//
//            val context = requireActivity()
//            val account_dialog = Dialog(context)
//            account_dialog.setContentView(dialogBining)
//
//            account_dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//            account_dialog.show()
//        }
//        return root_view
        return inflater.inflate(com.example.university_app.R.layout.fragment_home, container, false)
    }
}