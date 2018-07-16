package com.example.liaoshouwang.dbdemo.roomdb

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.migration.Migration
import android.content.Context
import android.arch.persistence.db.SupportSQLiteDatabase



/**
created by edison 2018/7/16
 */
@Database(entities = [RoomUser::class], version = 1)
abstract class RoomDBManager:RoomDatabase() {

    abstract fun roomUserDao(): RoomUserDao


    companion object {
        // For Singleton instantiation
        @Volatile private var instance: RoomDBManager? = null

        fun getInstances(context: Context): RoomDBManager {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context):RoomDBManager{
            return Room.databaseBuilder(context,RoomDBManager::class.java,"room-db").build()

            //处理数据库升级
//            return Room.databaseBuilder(context,RoomDBManager::class.java,"room-db").addMigrations(MIGRATION_1_2).build()

        }

        private val MIGRATION_1_2: Migration = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("CREATE TABLE `Fruit` (`id` INTEGER, " + "`name` TEXT, PRIMARY KEY(`id`))")
            }
        }

    }




}