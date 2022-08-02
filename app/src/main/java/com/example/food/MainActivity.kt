package com.example.food

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.food.navigation.homeFragment
import com.example.food.navigation.mypageFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {

    //로그인 정보 (id)
    lateinit var login_id : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bottom_navigation.setOnNavigationItemSelectedListener(this)

        //로그인 정보 (id) 받기
        login_id = intent.getStringExtra("ID").toString()

        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 1)

    }

    //하단 탭 기능 구현 (프래그먼트 or 액티비티) 전달 + 로그인 정보(id) 전달
    override fun onNavigationItemSelected(p0 : MenuItem): Boolean {
        when(p0.itemId) {
            R.id.action_home -> {
                var homeFragment = homeFragment()
                supportFragmentManager.beginTransaction().replace(R.id.main_content,homeFragment).commit()
                return true
            }
            R.id.action_upload -> {
                if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)==PackageManager.PERMISSION_GRANTED) {
                    var intent = Intent(this, uploadActivity::class.java)
                    intent.putExtra("ID", login_id)
                    startActivity(intent)
                }
                return true
            }
            R.id.action_mypage   -> {
                var myFragment = mypageFragment()
                var bundle = Bundle()
                bundle.putString("ID", login_id)
                myFragment.arguments = bundle
                supportFragmentManager.beginTransaction().replace(R.id.main_content,myFragment).commit()
                return true
            }
        }
        return false
    }
}