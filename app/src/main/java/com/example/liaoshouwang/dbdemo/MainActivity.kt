package com.example.liaoshouwang.dbdemo

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.liaoshouwang.dbdemo.greendaodb.GreenDBManager
import com.example.liaoshouwang.dbdemo.greendaodb.GreenUser
import com.example.liaoshouwang.dbdemo.liteormdb.LiteDBManager
import com.example.liaoshouwang.dbdemo.liteormdb.LiteUser
import com.example.liaoshouwang.dbdemo.roomdb.RoomDBManager
import com.example.liaoshouwang.dbdemo.roomdb.RoomUser
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class MainActivity : AppCompatActivity() ,View.OnClickListener{

    val TAG = "DBDemo"
    private val mGreenList = ArrayList<GreenUser>()
    private val mRoomList = ArrayList<RoomUser>()
    private val mLiteList = ArrayList<LiteUser>()
    private val mContext = this
    private var mGreenSelectList =  ArrayList<GreenUser>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        room_delete.setOnClickListener(this)
        room_insert.setOnClickListener(this)
        room_select.setOnClickListener(this)

        green_delete.setOnClickListener(this)
        green_insert.setOnClickListener(this)
        green_select.setOnClickListener(this)

        lite_select.setOnClickListener(this)
        lite_insert.setOnClickListener(this)
        lite_delete.setOnClickListener(this)

        room_delete.isClickable = false
        room_insert.isClickable = false
        room_select.isClickable = false

        green_select.isClickable = false
        green_insert.isClickable = false
        green_delete.isClickable = false

        lite_delete.isClickable = false
        lite_insert.isClickable = false
        lite_select.isClickable = false

        Toast.makeText(this,"正在生成数据。。",Toast.LENGTH_LONG).show()
        initData()
    }

    private fun initData() = doAsync {

        for (i in 0..99999){
            mGreenList.add(GreenUser("green$i",i))
        }

        for (j in 0..99999){
            mRoomList.add(RoomUser("room$j",j))
        }

        for (k in 0..99999){
            mLiteList.add(LiteUser("lite$k",k))
        }

        uiThread {
            room_delete.isClickable = true
            room_insert.isClickable = true
            room_select.isClickable = true

            green_select.isClickable = true
            green_insert.isClickable = true
            green_delete.isClickable = true

            lite_delete.isClickable = true
            lite_insert.isClickable = true
            lite_select.isClickable = true

            Toast.makeText(mContext,"数据生成完毕。。",Toast.LENGTH_LONG).show()
        }
    }


    override fun onClick(p0: View?) {
        when(p0?.id){

            R.id.green_delete -> {
                val b = System.currentTimeMillis()
                val dao = GreenDBManager.getInstance().daoSession.greenUserDao
                dao.deleteInTx(mGreenSelectList)
                val e = System.currentTimeMillis()
                val t = e - b
                Log.e(TAG,"green_delete_time: $t ms")
                Toast.makeText(mContext,"green_delete_time: $t ms",Toast.LENGTH_LONG).show()
            }

            R.id.green_insert -> {
                val b = System.currentTimeMillis()
                val dao = GreenDBManager.getInstance().daoSession.greenUserDao
                dao.insertInTx(mGreenList)
                val e = System.currentTimeMillis()
                val t = e - b
                Log.e(TAG,"green_insert_time: $t ms")
                Toast.makeText(mContext,"green_insert_time: $t ms",Toast.LENGTH_LONG).show()
            }

            R.id.green_select -> {
                val b = System.currentTimeMillis()
                val dao = GreenDBManager.getInstance().daoSession.greenUserDao
                val list = dao.queryBuilder().list()
                val e = System.currentTimeMillis()
                val t = e - b
                Log.e(TAG,"green_select_time: $t ms")
                Toast.makeText(mContext,"green_select_time: $t ms",Toast.LENGTH_LONG).show()

                mGreenSelectList.addAll(list)
            }

            R.id.room_delete -> doAsync{
                val b = System.currentTimeMillis()
                val dao = RoomDBManager.getInstances(mContext).roomUserDao()
                dao.deleteAll()
                val e = System.currentTimeMillis()
                val t = e - b
                Log.e(TAG,"room_delete_time: $t ms")

                uiThread {
                    Toast.makeText(mContext,"room_delete_time: $t ms",Toast.LENGTH_LONG).show()
                }
            }

            R.id.room_insert -> doAsync{
                val b = System.currentTimeMillis()
                val dao = RoomDBManager.getInstances(mContext).roomUserDao()
                dao.insertAll(mRoomList)
                val e = System.currentTimeMillis()
                val t = e - b
                Log.e(TAG,"room_insert_time: $t ms")
                uiThread {
                    Toast.makeText(mContext,"room_insert_time: $t ms",Toast.LENGTH_LONG).show()
                }
            }

            R.id.room_select -> doAsync{
                val b = System.currentTimeMillis()
                val dao = RoomDBManager.getInstances(mContext).roomUserDao()
                dao.getRoomUsers()
                val e = System.currentTimeMillis()
                val t = e - b
                Log.e(TAG,"room_delete_time: $t ms")
                uiThread {
                    Toast.makeText(mContext,"room_delete_time: $t ms",Toast.LENGTH_LONG).show()
                }
            }

            R.id.lite_delete -> {
                val b = System.currentTimeMillis()
                LiteDBManager.getInstance().liteOrm.deleteAll(LiteUser::class.java)
                val e = System.currentTimeMillis()
                val t = e - b
                Log.e(TAG,"lite_delete_time: $t ms")
                Toast.makeText(mContext,"lite_delete_time: $t ms",Toast.LENGTH_LONG).show()
            }

            R.id.lite_insert -> {
                val b = System.currentTimeMillis()
                LiteDBManager.getInstance().liteOrm.insert(mLiteList)
                val e = System.currentTimeMillis()
                val t = e - b
                Log.e(TAG,"lite_insert_time: $t ms")
                Toast.makeText(mContext,"lite_insert_time: $t ms",Toast.LENGTH_LONG).show()
            }

            R.id.lite_select -> {
                val b = System.currentTimeMillis()
                LiteDBManager.getInstance().liteOrm.query(LiteUser::class.java)
                val e = System.currentTimeMillis()
                val t = e - b
                Log.e(TAG,"lite_query_time: $t ms")
                Toast.makeText(mContext,"lite_query_time: $t ms",Toast.LENGTH_LONG).show()
            }
        }

    }

}
