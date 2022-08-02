package com.example.food

import android.app.Activity
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteStatement
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import java.text.SimpleDateFormat
import kotlinx.android.synthetic.main.activity_upload.*
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.util.*

class uploadActivity : AppCompatActivity() {

    lateinit var dbManager : DBManager_allpost
    lateinit var sqlDB : SQLiteDatabase

    var PICK_IMAGE_FROM_ALBUM = 0
    var photoUrl : Uri? =null

    //로그인 정보 (id)
    lateinit var login_id : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload)

        //로그인 정보 (id) 받기
        login_id = intent.getStringExtra("ID").toString()

        dbManager = DBManager_allpost(this)

        //activity_upload.xml 파일을 import하여 변수를 바로 사용하였습니다. (ex. upload_gallery_btn)
        //갤러리버튼 - 갤러리에서 사진 선택
        upload_gallery_btn.setOnClickListener {
            openGallery()
        }
        //업로드버튼 - 데이터 저장
        upload_save_btn.setOnClickListener {
            contentUpload()
        }

    }

    //갤러리(앨범) 열기
    private fun openGallery() {
        val photoPickerIntent=Intent(Intent.ACTION_PICK)
        photoPickerIntent.type="image/*"
        startActivityForResult(photoPickerIntent, PICK_IMAGE_FROM_ALBUM)
    }

    //이미지 저장 : uri 사용
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode==PICK_IMAGE_FROM_ALBUM) {
            if (resultCode== Activity.RESULT_OK) {
                photoUrl = data?.data
                upload_photo_image.setImageURI(photoUrl)
            } else {
                finish()
            }
        }

    }

    //작성한 게시물 데이터베이스에 저장
    private fun contentUpload() {

        var timestamp = SimpleDateFormat("yyyy-MM-dd_HHmmss").format(Date())

        sqlDB=dbManager.writableDatabase

        if(photoUrl==null) {
            Toast.makeText(this,"사진을 선택해주세요", Toast.LENGTH_SHORT).show()
        } else {
            sqlDB.execSQL("INSERT INTO allpost (id, diary, timestamp) VALUES ('${login_id}', '"+upload_diary_text.text.toString()+"', '${timestamp}');")

            //이미지 바이트배열 저장
            var sql : String = "UPDATE ALLPOST SET image =? WHERE timestamp = '${timestamp}';"
            var insertStmt : SQLiteStatement = sqlDB.compileStatement(sql)
            insertStmt.clearBindings()
            insertStmt.bindBlob(1,getByteArray(photoUrl!!))
            insertStmt.execute()

            sqlDB.close()

            Toast.makeText(this,"업로드 성공했습니다.", Toast.LENGTH_SHORT).show()
        }
    }

    //이미지 저장 : (uri -> 비트맵 -> 바이트배열) 전환
    fun getByteArray(uri: Uri): ByteArray {
        var photo : InputStream = contentResolver.openInputStream(photoUrl!!)!!
        var bitmap : Bitmap = BitmapFactory.decodeStream(photo)
        var stream : ByteArrayOutputStream = ByteArrayOutputStream()

        bitmap.compress(Bitmap.CompressFormat.PNG,100,stream)

        var data = stream.toByteArray()

        return data;
    }
}