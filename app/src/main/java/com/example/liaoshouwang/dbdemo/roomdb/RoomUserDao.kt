package com.example.liaoshouwang.dbdemo.roomdb

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query

/**
created by edison 2018/7/16
 */
@Dao
interface RoomUserDao {

    @Query("SELECT * FROM ROOM_USER")
    fun getRoomUsers(): List<RoomUser>

    @Insert
    fun insertAll(users:List<RoomUser>)

    @Query("DELETE FROM ROOM_USER")
    fun deleteAll()


}