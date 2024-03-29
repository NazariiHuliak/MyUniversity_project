package com.example.university_app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class Register_Activity : AppCompatActivity() {
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var usersRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        firebaseAuth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        usersRef = database.getReference("users")

        val signUpButton = findViewById<Button>(R.id.btn_Sign_up)
        signUpButton.setOnClickListener {
            val usernameEditText = findViewById<TextInputEditText>(R.id.email_login)
            val emailEditText = findViewById<TextInputEditText>(R.id.email)
            val groupEditText = findViewById<TextInputEditText>(R.id.group)
            val passwordEditText = findViewById<TextInputEditText>(R.id.password)

            val username = usernameEditText.text.toString()
            val email = emailEditText.text.toString()
            val group = groupEditText.text.toString()
            val password = passwordEditText.text.toString()

            if (!isValidGroup(group)){
                groupEditText.error = "Введіть коректну групу!"
                groupEditText.requestFocus()
            }
            else {
                firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // Реєстрація успішна
                            val user = firebaseAuth.currentUser
                            val uid = user!!.uid
                            // Збереження інформації про користувача у Realtime Database
                            val userData = hashMapOf(
                                "nickname" to username,
                                "group" to group
                            )
                            usersRef.child(uid).setValue(userData)

                            Toast.makeText(
                                this,
                                "Реєстрація успішна: ${user.email}",
                                Toast.LENGTH_SHORT
                            ).show()
                            usersRef.child(uid).setValue(userData).addOnSuccessListener {
                                Log.d("RegisterActivity", "User added to database")
                            }.addOnFailureListener {
                                Log.e("RegisterActivity", "Error adding user to database", it)
                            }
                            user.sendEmailVerification()
                                .addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        // Лист для підтвердження пошти надіслано успішно
                                        Toast.makeText(
                                            this,
                                            "Лист для підтвердження пошти надіслано",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    } else {
                                        // Помилка під час відправки листа для підтвердження пошти
                                        Toast.makeText(
                                            this,
                                            "Помилка!",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                            val intent = Intent(this, Login_Activity::class.java)
                            startActivity(intent)
                        } else {
                            // Реєстрація не вдалась
                            Toast.makeText(
                                this,
                                "Помилка реєстрації: ${task.exception?.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
            }
        }
    }
}
    fun isValidGroup(input: String): Boolean {
        val pattern = Regex("[А-ЯІЇЄ]{3}-\\d{2}")
        return pattern.matches(input)
    }

//            if(!isValidUsername(username)){
//                usernameEditText.error = "Ім'я користувача не може бути порожнім"
//                usernameEditText.requestFocus()
//            }
//            else if (!isEmailValid(email)) {
//                emailEditText.error = "Введіть коректну адресу електронної пошти!"
//                emailEditText.requestFocus()
//            }
//            else if (dbAdapter.isEmailExists(email)) {
//                emailEditText.error = "Аккаунт з цією поштою вже існує!!"
//                emailEditText.requestFocus()
//            }
//            else if (!isValidGroup(group)){
//                groupEditText.error = "Введіть коректну групу!"
//                groupEditText.requestFocus()
//            }
//            else if (!isValidPassword(password)){
//                passwordEditText.error = "Пароль не може бути порожнім, та мусить містити щонайменше 8 символів!"
//                passwordEditText.requestFocus()
//            }
//            else {
//                Toast.makeText(this@Register_Activity, "Реєстрація пройшла успішно!", Toast.LENGTH_SHORT).show()
//                dbAdapter.insertData(username,email,group,password)
////                val intent = Intent(this, MainActivity::class.java)
////                startActivity(intent)
//            }
//
//            // обробка даних бд
//        }
//    }
//    val dbAdapter = MyDbAdapter(this)
//
//
//    fun isValidUsername(inputString: String): Boolean {
//        return inputString.isNotBlank()
//    }
//    fun isValidGroup(input: String): Boolean {
//        val pattern = Regex("[A-ZА-ЯІЇЄ]{3}-\\d{2}")
//        return pattern.matches(input)
//    }
//    private fun isEmailValid(email: String): Boolean {
//        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
//    }
//    fun isValidPassword(inputString: String): Boolean {
//        return inputString.isNotBlank() && inputString.length >= 8
//    }
//    //    fun emailResponder(result: Boolean){
////        if (result) {
////            Toast.makeText(this@Register_Activity, "Реєстрація успішна!", Toast.LENGTH_SHORT).show()
////        } else {
////            Toast.makeText(this@Register_Activity, "Помилка! Введено неправильну пошту", Toast.LENGTH_SHORT).show()
////        }
////    }
//}
////    private inner class SendEmailTask(val email: String) : AsyncTask<Void, Void, Boolean>() {
////
////        override fun doInBackground(vararg params: Void?): Boolean {
////            val props = Properties()
////            props.put("mail.smtp.auth", "true")
////            props.put("mail.smtp.starttls.enable", "true")
////            props.put("mail.smtp.host", "smtp.gmail.com")
////            props.put("mail.smtp.port", "587")
////
////            val session = Session.getInstance(props, object : Authenticator() {
////                override fun getPasswordAuthentication(): PasswordAuthentication {
////                    return PasswordAuthentication("myuniversity240@gmail.com", "myunivpass")
////                }
////            })
////
////            try {
////                val message = MimeMessage(session)
////                message.setFrom(InternetAddress("myuniversity24@proton.me"))
////                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email))
////                message.setSubject("Вітання від MyUniversity!")
////                message.setText("Привіт! Це автоматично згенерований лист, що підтверджує вашу реєстрацію у додатку MyUniversity.")
////
////                Transport.send(message)
////
////                return true
////            } catch (e: MessagingException) {
////                e.printStackTrace()
////                return false
////            }
////        }
////        override fun onPostExecute(result: Boolean) {
////            if (result) {
////                Toast.makeText(this@Register_Activity, "Реєстрація успішна! На введену пошту надіслано лист з підтвердженням", Toast.LENGTH_SHORT).show()
////            } else {
////                Toast.makeText(this@Register_Activity, "Помилка! Введено неправильну пошту", Toast.LENGTH_SHORT).show()
////            }
////        }
////    }
////}
//class DatabaseHelper(context: Context?) :
//    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
//    override fun onCreate(db: SQLiteDatabase) {
//        val sql = "CREATE TABLE UsersInfo " +
//                "(id INTEGER PRIMARY KEY AUTOINCREMENT, " +
//                "username TEXT, " +
//                "email TEXT, "+
//                "academic_group TEXT, "+
//                "password TEXT)"
//        db.execSQL(sql)
//    }
//
//    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
//        // Оператор для видалення таблиці, якщо вона існує
//        db.execSQL("DROP TABLE IF EXISTS UsersInfo")
//        onCreate(db)
//    }
//
//    companion object {
//        private const val DATABASE_NAME = "MyUniversity.db"
//        private const val DATABASE_VERSION = 1
//    }
//}
//class MyDbAdapter(private val context: Context) {
//
//    private val database: SQLiteDatabase by lazy {
//        DatabaseHelper(context).writableDatabase
//    }
//
//    fun insertData(username: String, email: String, academic_group: String, password: String): Long {
//        val values = ContentValues().apply {
//            put("username", username)
//            put("email", email)
//            put("academic_group", academic_group)
//            put("password", password)
//        }
//        return database.insert("UsersInfo", null, values)
//    }
//
//    fun getAllData(): Cursor {
//        val columns = arrayOf("id", "username", "email","academic_group","password")
//        return database.query("UsersInfo", columns, null, null, null, null, null)
//    }
//
////    fun updateData(id: Long, name: String, age: Int): Int {
////        val values = ContentValues().apply {
////            put("name", name)
////            put("age", age)
////        }
////        val whereClause = "id=?"
////        val whereArgs = arrayOf(id.toString())
////        return database.update("UsersInfo", values, whereClause, whereArgs)
////    }
//fun deleteData(id: Long): Int {
//    val whereClause = "id=?"
//    val whereArgs = arrayOf(id.toString())
//    return database.delete("UsersInfo", whereClause, whereArgs)
//}
//    fun clearData() {
//        database.execSQL("DELETE FROM UsersInfo")
//    }
//    fun isEmailExists(email: String): Boolean {
//        val columns = arrayOf("email")
//        val selection = "email = ?"
//        val selectionArgs = arrayOf(email)
//        val cursor = database.query("UsersInfo", columns, selection, selectionArgs, null, null, null)
//        val exists = cursor.count > 0
//        cursor.close()
//        return exists
//    }
//}
//123
//vagis95546@vootin.com