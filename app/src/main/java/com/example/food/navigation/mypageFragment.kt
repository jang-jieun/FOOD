package com.example.food.navigation


import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.food.DBManager_allpost
import com.example.food.R
import kotlinx.android.synthetic.main.item_detail.view.*
import kotlinx.android.synthetic.main.fragment_mypage.view.*

class mypageFragment : Fragment() {

    lateinit var dbManager : DBManager_allpost
    lateinit var sqlDB : SQLiteDatabase

    //로그인 정보 (id)
    lateinit var id : String

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        var view = LayoutInflater.from(activity).inflate(R.layout.fragment_mypage,container,false)

        dbManager= DBManager_allpost(requireContext())
        sqlDB=dbManager.readableDatabase

        //로그인 정보 (id) 받기
        id = arguments?.getString("ID").toString()

        view.mypageview_recyclerview.adapter = MyPageViewRecyclerViewAdapter()
        view.mypageview_recyclerview.layoutManager = LinearLayoutManager(activity)

        return view
    }

    inner class MyPageViewRecyclerViewAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        var cursor : Cursor = sqlDB.rawQuery("SELECT * FROM allpost WHERE ID = '${id}';", null)
        var length : Int = cursor.getCount()

        var user_post =Array(length){ArrayList<String>()}
        var user_image = ArrayList<Bitmap>()

        //데이터베이스에 접근하여 유저 레코드(게시물) ArrayList에 저장
        init {
            //새로고침
            notifyDataSetChanged()

            //2차원 ArrayList
            for (i in 0 until length-1) {
                user_post[i] = ArrayList()
            }

            cursor.moveToFirst()
            if(cursor.moveToFirst()==false) {
                Toast.makeText(requireContext(), "게시물이 없습니다.", Toast.LENGTH_SHORT).show()
                cursor.close()
                sqlDB.close()
            } else {
                for (i in 0..length-1) {
                    user_post[i].add(cursor.getString(0))
                    user_post[i].add(cursor.getString(1))
                    user_image.add(getImage(cursor.getBlob(2)))
                    cursor.moveToNext()
                }
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            var view = LayoutInflater.from(parent.context).inflate(R.layout.item_detail,parent,false)
            return CustomViewHolder(view)
        }

        //RecyclerView를 사용할 때 메모리를 적게 사용하기 위해 CustomViewHolder 사용
        inner class CustomViewHolder(view:View) : RecyclerView.ViewHolder (view)

        override fun getItemCount(): Int {
            return cursor.getCount()
        }

        //(에디트텍스트&이미지뷰 - 데이터)연결
        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

            var viewholder = (holder as CustomViewHolder).itemView

            if(cursor.moveToFirst()==false) {
                Toast.makeText(requireContext(), "게시물이 없습니다.", Toast.LENGTH_SHORT).show()
            } else {
                //id
                viewholder.postviewitem_profile_textview.text=user_post[position].get(0)

                //explain
                viewholder.postviewitem_explain_textview.text=user_post[position].get(1)

                //image
                //viewholder.detailviewitem_imageview_content.setImageBitmap(image.get(2)) 동일한 코드
                Glide.with(holder.itemView.context).load(user_image.get(position)).into(viewholder.postviewitem_imageview_content)
            }

        }

    }

    //이미지 출력 : 데이터베이스에 저장된 이미지 (바이트배열 -> 비트맵) 전환
    fun getImage(byteArray: ByteArray): Bitmap {
        var bitmap : Bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)

        return bitmap
    }

}