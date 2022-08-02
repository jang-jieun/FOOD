package com.example.food

import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class Login : AppCompatActivity() {

    lateinit var dbManager : DBManager_member
    lateinit var sqlDB : SQLiteDatabase

    lateinit var login_id : EditText
    lateinit var login_pw : EditText
    lateinit var login_btn : Button
    lateinit var join_go_btn : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        login_id = findViewById(R.id.login_id)
        login_pw = findViewById(R.id.login_pw)
        login_btn = findViewById(R.id.login_btn)
        join_go_btn = findViewById(R.id.join_go_btn)

        dbManager = DBManager_member(this)

        //회원가입하러가기
        join_go_btn.setOnClickListener{
            var intent = Intent(this, Join::class.java)
            startActivity(intent)
        }

        //로그인하기 + 로그인 정보(id) 전달
       login_btn.setOnClickListener{
            if(login_id.text.toString()=="" || login_pw.text.toString()=="") {
                Toast.makeText(this, "아이디 또는 비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show()
            } else {
                sqlDB = dbManager.readableDatabase
                var cursor : Cursor
                cursor = sqlDB.rawQuery("SELECT password FROM member WHERE id = '${login_id.text.toString()}';", null)

                if(cursor.moveToFirst()==false) {
                    Toast.makeText(this, "아이디가 존재하지 않습니다.", Toast.LENGTH_SHORT).show()
                    cursor.close()
                    sqlDB.close()
                } else if(cursor.getString(0)==login_pw.text.toString()) {
                    cursor.close()
                    sqlDB.close()
                    Toast.makeText(this, "로그인하였습니다.", Toast.LENGTH_SHORT).show()
                    var intent = Intent(this, MainActivity::class.java)
                    intent.putExtra("ID",login_id.text.toString())
                    startActivity(intent)
                } else {
                    Toast.makeText(this, "비밀번호가 틀렸습니다.", Toast.LENGTH_SHORT).show()
                    cursor.close()
                    sqlDB.close()
                }

            }
        }
    }
}