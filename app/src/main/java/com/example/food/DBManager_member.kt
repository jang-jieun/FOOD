package com.example.food

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

//유저 정보가 들어있는 테이블 memeber
class DBManager_member(context: Context) : SQLiteOpenHelper(context, "member", null, 1) {
    override fun onCreate(db: SQLiteDatabase?) {
        db!!.execSQL("CREATE TABLE member (id text PRIMARY KEY , password text)")
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS member")
        onCreate(db)
    }
}