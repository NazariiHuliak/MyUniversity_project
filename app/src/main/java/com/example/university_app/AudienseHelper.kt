package com.example.university_app


import android.annotation.SuppressLint
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper

class AudienseHelper(context: Context) : SQLiteAssetHelper(context, DATABASE_NAME, null, DATABASE_VERSION){
    companion object {
        private const val DATABASE_NAME = "LessonsDatabase.db"
        private const val DATABASE_VERSION = 1
    }
}
class DatabaseAccessAudiense private constructor(context: Context) {
    private val openHelper: SQLiteOpenHelper = AudienseHelper(context)
    private lateinit var database: SQLiteDatabase

    companion object {
        private var instance: DatabaseAccessAudiense? = null

        @Synchronized
        fun getInstance(context: Context): DatabaseAccessAudiense {
            if (instance == null) {
                instance = DatabaseAccessAudiense(context)
            }
            return instance!!
        }
    }
    fun open() {
        database = openHelper.writableDatabase
    }
    fun close() {
        database.close()
    }

    @SuppressLint("Range")
    fun getAllData(): MutableList<AudienseModel> {
        var dataList: MutableList<AudienseModel> = mutableListOf()

        val cursor: Cursor = database.rawQuery("SELECT * FROM Lessons", null)

        if (cursor != null && cursor.moveToFirst()) {
            do {
                val id: Int = cursor.getInt(cursor.getColumnIndex("id"))
                val subject: String = cursor.getString(cursor.getColumnIndex("subject"))
                val audience: String = cursor.getString(cursor.getColumnIndex("audience"))
                val day: String = cursor.getString(cursor.getColumnIndex("day"))
                val start_lesson: String = cursor.getString(cursor.getColumnIndex("start_lesson"))
                val end_lesson: String = cursor.getString(cursor.getColumnIndex("end_lesson"))
                val tutor: String = cursor.getString(cursor.getColumnIndex("tutor"))
                dataList.add(AudienseModel(id, subject, audience, day, start_lesson, end_lesson, tutor))
            } while (cursor.moveToNext())
        }

        cursor.close()
        return dataList
        Log.d("asd", "$dataList");
    }
}