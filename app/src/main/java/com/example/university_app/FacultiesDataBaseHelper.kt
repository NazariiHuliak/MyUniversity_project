package com.example.university_app

import android.annotation.SuppressLint
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper

class FacultiesDataBaseHelper(context: Context) : SQLiteAssetHelper(context, DATABASE_NAME, null, DATABASE_VERSION){
    companion object {
        private const val DATABASE_NAME = "faculties.db"
        private const val DATABASE_VERSION = 1
    }
}
class DatabaseAccessFacult private constructor(context: Context) {
    private val openHelper: SQLiteOpenHelper = FacultiesDataBaseHelper(context)
    private lateinit var database: SQLiteDatabase

    companion object {
        private var instance: DatabaseAccessFacult? = null

        @Synchronized
        fun getInstance(context: Context): DatabaseAccessFacult {
            if (instance == null) {
                instance = DatabaseAccessFacult(context)
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
    fun getAllData(): MutableList<FacultiesModel> {
        var dataList: MutableList<FacultiesModel> = mutableListOf()
        val cursor: Cursor = database.rawQuery("SELECT * FROM faculties", null)

        if (cursor.moveToFirst()) {
            do {
                val id: Int = cursor.getInt(cursor.getColumnIndex("id"))
                val name: String = cursor.getString(cursor.getColumnIndex("name"))
                val adress: String = cursor.getString(cursor.getColumnIndex("adress"))
                val phone: String = cursor.getString(cursor.getColumnIndex("phone"))
                val email: String = cursor.getString(cursor.getColumnIndex("email"))
                val site: String = cursor.getString(cursor.getColumnIndex("site"))
                val logo: String = cursor.getString(cursor.getColumnIndex("logo"))
                val map: String = cursor.getString(cursor.getColumnIndex("map"))
                val url: String = cursor.getString(cursor.getColumnIndex("mapURL"))
                dataList.add(FacultiesModel(id, name, adress, phone, email, site,logo,map,url))
            } while (cursor.moveToNext())
        }
        cursor.close()
        return dataList
    }
}