package com.example.food

import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class Join : AppCompatActivity() {

    lateinit var dbManager : DBManager_member
    lateinit var sqlDB : SQLiteDatabase

    lateinit var join_id : EditText
    lateinit var join_pw : EditText
    lateinit var join_apply_btn : Button
    lateinit var join_checkid_btn : Button

    //아이디 중복 확인 여부 변수
    var flag : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_join)

        join_id = findViewById(R.id.join_id)
        join_pw = findViewById(R.id.join_pw)
        join_apply_btn = findViewById(R.id.join_apply_btn)
        join_checkid_btn = findViewById(R.id.join_checkid_btn)

        dbManager = DBManager_member(this)

        //아이디 중복 검사
        join_checkid_btn.setOnClickListener{
            if(join_id.text.toString()=="") {
                Toast.makeText(this, "아이디를 입력해주세요", Toast.LENGTH_SHORT).show()
            } else {
                sqlDB = dbManager.readableDatabase
                var cursor : Cursor
                cursor = sqlDB.rawQuery("SELECT id FROM member WHERE id = '${join_id.text.toString()} ';", null)

                if(cursor.moveToFirst()==false) {
                    flag=1
                    Toast.makeText(this, "사용 가능한 아이디입니다.", Toast.LENGTH_SHORT).show()
                    cursor.close()
                    sqlDB.close()
                } else {
                    Toast.makeText(this, "이미 존재하는 아이디입니다.", Toast.LENGTH_SHORT).show()
                    cursor.close()
                    sqlDB.close()
                }

            }
        }

        //회원가입
        join_apply_btn.setOnClickListener{
            if(join_pw.text.toString()=="") {
                Toast.makeText(this, "비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show()
            } else {
                if (flag==0){
                    Toast.makeText(this, "아이디 중복 검사를 해주세요.", Toast.LENGTH_SHORT).show()
                } else {
                    sqlDB = dbManager.writableDatabase
                    sqlDB.execSQL("INSERT INTO member VALUES ('${join_id.text.toString()}', '${join_pw.text.toString()}');")
                    sqlDB.close()

                    Toast.makeText(this, "회원가입이 완료되었습니다.", Toast.LENGTH_SHORT).show()
                    var intent = Intent(this, Login::class.java)
                    startActivity(intent)
                }

            }
        }
    }

}