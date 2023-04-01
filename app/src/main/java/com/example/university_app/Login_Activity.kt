package com.example.university_app

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.materialswitch.MaterialSwitch
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class Login_Activity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = Firebase.auth
        sharedPreferences = getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
        editor = sharedPreferences.edit()


        val email = sharedPreferences.getString("email", "")
        val password = sharedPreferences.getString("password", "")
        if (!email.isNullOrEmpty() && !password.isNullOrEmpty()) {
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        Toast.makeText(baseContext, "Success", Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(baseContext, "Authentication failed. ${it.localizedMessage}", Toast.LENGTH_SHORT).show()
                }
        }

        val switch = findViewById<MaterialSwitch>(R.id.keep_me_signed_in)
        switch.setOnCheckedChangeListener { _, isChecked ->
            editor.putBoolean("auto_login", isChecked)
            editor.apply()
        }

        val signInButton = findViewById<Button>(R.id.btn_Log_in)
        signInButton.setOnClickListener {
            performLogin()
        }
        val registerText: TextView = findViewById(R.id.register)
        registerText.setOnClickListener {
            val intent = Intent(this, Register_Activity::class.java)
            startActivity(intent)
        }
    }

    private fun performLogin() {
        val email: EditText = findViewById(R.id.email_login)
        val password: EditText = findViewById(R.id.password)

        if (email.text.isEmpty() || password.text.isEmpty()) {
            Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show()
            return
        }

        val emailInput = email.text.toString()
        val passwordInput = password.text.toString()

        auth.signInWithEmailAndPassword(emailInput, passwordInput)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val intent = Intent(this, MainActivity::class.java)

                    val autoLoginSwitch: MaterialSwitch = findViewById(R.id.keep_me_signed_in)
                    if (autoLoginSwitch.isChecked) {
                        val sharedPreferences = getSharedPreferences("myPrefs", MODE_PRIVATE)
                        val editor = sharedPreferences.edit()
                        editor.putString("email", emailInput)
                        editor.putString("password", passwordInput)
                        editor.apply()
                    } else {
                        val sharedPreferences = getSharedPreferences("myPrefs", MODE_PRIVATE)
                        val editor = sharedPreferences.edit()
                        editor.remove("email")
                        editor.remove("password")
                        editor.apply()
                    }

                    startActivity(intent)

                    Toast.makeText(baseContext, "Success", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener {
                Toast.makeText(
                    baseContext, "Authentication failed. ${it.localizedMessage}",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }
}