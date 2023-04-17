package com.example.university_app

import android.annotation.SuppressLint
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper

class QuotesDatabaseHelper(context: Context) : SQLiteAssetHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        private const val DATABASE_NAME = "quotes_db.db"
        private const val DATABASE_VERSION = 1
    }
}

class DatabaseAccessQuotes private constructor(context: Context){
    private val openHelper: SQLiteOpenHelper = QuotesDatabaseHelper(context)
    private lateinit var database: SQLiteDatabase

    companion object {
        private var instance: DatabaseAccessQuotes? = null

        @Synchronized
        fun getInstance(context: Context): DatabaseAccessQuotes {
            if (instance == null) {
                instance = DatabaseAccessQuotes(context)
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
    fun getAllData(): MutableList<QuotesModel> {
        var dataList: MutableList<QuotesModel> = mutableListOf()
        val cursor: Cursor = database.rawQuery("SELECT * FROM Quotes", null)

        if (cursor.moveToFirst()) {
            do {
                val id: Int = cursor.getInt(cursor.getColumnIndex("id"))
                val quote: String = cursor.getString(cursor.getColumnIndex("quote"))
                val author: String = cursor.getString(cursor.getColumnIndex("author"))
                dataList.add(QuotesModel(id, quote, author))
            } while (cursor.moveToNext())
        }
        cursor.close()
        return dataList
    }
}