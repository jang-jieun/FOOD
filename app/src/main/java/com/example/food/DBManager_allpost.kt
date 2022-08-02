package com.example.food

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

//모든 게시물이 들어있는 테이블 allpost
class DBManager_allpost(context: Context) : SQLiteOpenHelper(context, "allpost", null, 1) {
    override fun onCreate(db: SQLiteDatabase?) {
        db!!.execSQL("CREATE TABLE allpost (id TEXT , diary TEXT, image BLOG, timestamp TEXT)")
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS allpost")
        onCreate(db)
    }
}