package com.example.liaoshouwang.dbdemo;

import android.app.Application;

import com.example.liaoshouwang.dbdemo.greendaodb.GreenDBManager;
import com.example.liaoshouwang.dbdemo.liteormdb.LiteDBManager;
import com.example.liaoshouwang.dbdemo.roomdb.RoomDBManager;

/**
 * created by edison 2018/7/16
 */
public class MyApplication extends Application {


    @Override
    public void onCreate() {
        super.onCreate();

        GreenDBManager.getInstance().init(this);
        RoomDBManager.Companion.getInstances(this);
        LiteDBManager.getInstance().init(this);
    }
}
