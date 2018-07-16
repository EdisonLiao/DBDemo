package com.example.liaoshouwang.dbdemo.greendaodb;

import android.content.Context;

/**
 * created by edison 2018/7/16
 */
public class GreenDBManager {

    private DaoSession daoSession;
    private static GreenDBManager mIns;

    public static GreenDBManager getInstance(){
        if (mIns == null){
            mIns = new GreenDBManager();
        }
        return mIns;
    }

    public void init(Context context){
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(context,"green-db");
        DaoMaster daoMaster = new DaoMaster(helper.getWritableDatabase());
        daoSession = daoMaster.newSession();
    }

    public DaoSession getDaoSession(){
        return daoSession;
    }




}
