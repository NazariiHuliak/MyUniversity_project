package com.example.university_app


import android.annotation.SuppressLint
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper

class AudienseHelper(context: Context) : SQLiteAssetHelper(context, DATABASE_NAME, null, DATABASE_VERSION){
    companion object {
        private const val DATABASE_NAME = "aud_db.db"
        private const val DATABASE_VERSION = 3
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
        val cursor: Cursor = database.rawQuery("SELECT * FROM audienses_db", null)

        if (cursor.moveToFirst()) {
            do {
                val id: Int = cursor.getInt(cursor.getColumnIndex("id"))
                val lesson: String = cursor.getString(cursor.getColumnIndex("lesson"))
                val audiense: String = cursor.getString(cursor.getColumnIndex("audiense"))
                val day: String = cursor.getString(cursor.getColumnIndex("day"))
                val start_lesson: String = cursor.getString(cursor.getColumnIndex("start_lesson"))
                val end_lesson: String = cursor.getString(cursor.getColumnIndex("end_lesson"))
                val teacher: String = cursor.getString(cursor.getColumnIndex("teacher"))
                dataList.add(AudienseModel(id, lesson, audiense, day, start_lesson, end_lesson, teacher))
            } while (cursor.moveToNext())
        }
        cursor.close()
        return dataList
    }
}