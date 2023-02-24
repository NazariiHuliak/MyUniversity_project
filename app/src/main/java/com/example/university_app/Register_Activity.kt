package com.example.university_app

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.google.android.material.textfield.TextInputEditText

class Register_Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)


        val signUpButton = findViewById<Button>(R.id.btn_Sign_up)
        signUpButton.setOnClickListener {
            val usernameEditText = findViewById<TextInputEditText>(R.id.username)
            val emailEditText = findViewById<TextInputEditText>(R.id.email)
            val groupEditText = findViewById<TextInputEditText>(R.id.group)
            val passwordEditText = findViewById<TextInputEditText>(R.id.password)

            val username = usernameEditText.text.toString()
            val email = emailEditText.text.toString()
            val group = groupEditText.text.toString()
            val password = passwordEditText.text.toString()

            dbAdapter.insertData(username,email,group,password)
            // обрабка даних бд
        }
    }
    val dbAdapter = MyDbAdapter(this)
}
class DatabaseHelper(context: Context?) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    override fun onCreate(db: SQLiteDatabase) {
        val sql = "CREATE TABLE UsersInfo " +
                "(id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "username TEXT, " +
                "email TEXT, "+
                "academic_group TEXT, "+
                "password TEXT)"
        db.execSQL(sql)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Оператор для видалення таблиці, якщо вона існує
        db.execSQL("DROP TABLE IF EXISTS UsersInfo")
        onCreate(db)
    }

    companion object {
        private const val DATABASE_NAME = "MyUniversity.db"
        private const val DATABASE_VERSION = 1
    }
}
class MyDbAdapter(private val context: Context) {

    private val database: SQLiteDatabase by lazy {
        DatabaseHelper(context).writableDatabase
    }

    fun insertData(username: String, email: String, academic_group: String, password: String): Long {
        val values = ContentValues().apply {
            put("username", username)
            put("email", email)
            put("academic_group", academic_group)
            put("password", password)
        }
        return database.insert("UsersInfo", null, values)
    }

    fun getAllData(): Cursor {
        val columns = arrayOf("id", "username", "email","academic_group","password")
        return database.query("UsersInfo", columns, null, null, null, null, null)
    }

//    fun updateData(id: Long, name: String, age: Int): Int {
//        val values = ContentValues().apply {
//            put("name", name)
//            put("age", age)
//        }
//        val whereClause = "id=?"
//        val whereArgs = arrayOf(id.toString())
//        return database.update("UsersInfo", values, whereClause, whereArgs)
//    }

    fun deleteData(id: Long): Int {
        val whereClause = "id=?"
        val whereArgs = arrayOf(id.toString())
        return database.delete("UsersInfo", whereClause, whereArgs)
    }
}