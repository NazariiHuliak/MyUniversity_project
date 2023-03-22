package com.example.university_app

import android.annotation.SuppressLint
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.os.Build
import androidx.annotation.RequiresApi
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper
import java.time.LocalDate

class LessonDatabaseHelper(context: Context) : SQLiteAssetHelper(context, DATABASE_NAME, null, DATABASE_VERSION){
    companion object {
        private const val DATABASE_NAME = "mynewuniversity1.db"
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

    @SuppressLint("Range")
    fun getData(day_: String, group: String): MutableList<LessonModel> {
        if(group.isEmpty()){
           return mutableListOf()
        }
        var dataList: MutableList<LessonModel> = mutableListOf()
        var group_ = group.replace("-", "")
        val cursor: Cursor = database.rawQuery("SELECT * FROM $group_", null)
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
                } else if(day_ == "ALL"){
                    dataList.add(LessonModel(id, subject, starttime, tutor, auditory, day, type))
                }
            } while (cursor.moveToNext())
        }
        cursor.close()
        return dataList
    }
    @SuppressLint("Range")
    private fun getListOfTypes():MutableList<MutableList<String>>{
        var dataList: MutableList<MutableList<String>> = mutableListOf()
        val cursor: Cursor = database.rawQuery("SELECT * FROM weeksType", null)
        if (cursor.moveToFirst()) {
            do {
                val begin: String = cursor.getString(cursor.getColumnIndex("begin"))
                val end: String = cursor.getString(cursor.getColumnIndex("end"))
                dataList.add(mutableListOf(begin, end))
            } while (cursor.moveToNext())
        }
        cursor.close()
        return dataList
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun getTypeOfWeek(): Int{
        var typesList = this.getListOfTypes()
        val current = LocalDate.now().toString()
        for (i in 0 until typesList.size){
            return if (current>=typesList[i][0] && current<=typesList[i][1]){
                1
            } else {
                2
            }
        }
        return 0
    }
}