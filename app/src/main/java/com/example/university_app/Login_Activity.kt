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
import androidx.appcompat.app.AlertDialog
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
        val forgotPasswordButton = findViewById<TextView>(R.id.forgot_password)
        forgotPasswordButton.setOnClickListener {
            showForgotPasswordDialog()
        }
    }
    private fun showForgotPasswordDialog() {
        val builder = AlertDialog.Builder(this)
        /*builder.setTitle("Forgot Password")*/

        val view = layoutInflater.inflate(R.layout.alertdialog_forgot_password, null)
        val emailEditText = view.findViewById<EditText>(R.id.forgot_password_email)
        builder.setView(view)
        val dialog = builder.create()

        val buttonReset = view.findViewById<Button>(R.id.forgot_password_button)
        val buttonCancel = view.findViewById<Button>(R.id.forgot_password_cancel_button)
        buttonReset.setOnClickListener {
            val email = emailEditText.text.toString()
            if (email.isNotEmpty()) {
                FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this, "Password reset email sent", Toast.LENGTH_SHORT).show()
                            dialog.cancel()
                        } else {
                            Toast.makeText(this, "Failed to send password reset email", Toast.LENGTH_SHORT).show()
                        }
                    }
            } else {
                Toast.makeText(this, "Please enter your email address", Toast.LENGTH_SHORT).show()
            }

        }

        buttonCancel.setOnClickListener{
            dialog.cancel()
        }
        val drawable = resources.getDrawable(R.drawable.shape7)
        dialog.window?.setBackgroundDrawable(drawable)
        dialog.show()

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