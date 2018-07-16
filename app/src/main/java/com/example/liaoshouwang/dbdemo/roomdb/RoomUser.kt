package com.example.liaoshouwang.dbdemo.roomdb

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

/**
created by edison 2018/7/16
 */
@Entity(tableName = "ROOM_USER")
data class RoomUser(
        @ColumnInfo(name = "name")
        var name:String,
        @ColumnInfo(name = "age")
        var age:Int) {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Long = 0

}