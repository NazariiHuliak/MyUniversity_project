package com.example.university_app

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton

class InfoFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root_view = inflater.inflate(R.layout.fragment_info, container, false)

        val btn_contacts = root_view.findViewById<Button>(R.id.contact_button)
        btn_contacts.setOnClickListener {
            val dialogBining = layoutInflater.inflate(R.layout.contacts_dialog, null)

            val context = requireActivity()
            val contact_dialog = Dialog(context)
            contact_dialog.setContentView(dialogBining)

            contact_dialog.setCancelable(true)
            contact_dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            contact_dialog.show()
            val cancel_btn = contact_dialog.findViewById<ImageButton>(R.id.button_close)
            cancel_btn.setOnClickListener {
                contact_dialog.dismiss()
            }
        }
        return root_view
    }
}