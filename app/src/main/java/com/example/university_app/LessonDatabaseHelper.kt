package com.example.university_app

//class LessonDatabaseHelper {
//
//}
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper

class LessonDatabaseHelper(context: Context) : SQLiteAssetHelper(context, DATABASE_NAME, null, DATABASE_VERSION){
    companion object {
        private const val DATABASE_NAME = "mynewuniversity.db"
        private const val DATABASE_VERSION = 1
    }
}
class DatabaseAccess private constructor(context: Context) {
    private val openHelper: SQLiteOpenHelper = LessonDatabaseHelper(context)
    private lateinit var database: SQLiteDatabase

    companion object {
        private var instance: DatabaseAccess? = null

        @Synchronized
        fun getInstance(context: Context): DatabaseAccess {
            if (instance == null) {
                instance = DatabaseAccess(context)
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

    fun getData(day_: String): MutableList<LessonModel> {
        var dataList: MutableList<LessonModel> = mutableListOf()
        val cursor: Cursor = database.rawQuery("SELECT * FROM ownlessons", null)

        if (cursor.moveToFirst()) {
            do {
                val id: Int = cursor.getInt(cursor.getColumnIndex("id"))
                val subject: String = cursor.getString(cursor.getColumnIndex("subject"))
                val tutor: String = cursor.getString(cursor.getColumnIndex("tutor"))
                val auditory: Int = cursor.getInt(cursor.getColumnIndex("auditory"))
                val starttime: String = cursor.getString(cursor.getColumnIndex("starttime"))
                val day: String = cursor.getString(cursor.getColumnIndex("day"))
                val type: Int = cursor.getInt(cursor.getColumnIndex("type"))
                if(day == day_){
                    dataList.add(LessonModel(id, subject, starttime, tutor, auditory, day, type))
                }
            } while (cursor.moveToNext())
        }
        cursor.close()
        return dataList
    }
}