package com.example.liaoshouwang.dbdemo.liteormdb;

import android.content.Context;

import com.litesuits.orm.LiteOrm;

/**
 * created by edison 2018/7/16
 */
public class LiteDBManager {

    private LiteOrm liteOrm;
    private static LiteDBManager mIns;

    public static LiteDBManager getInstance(){
        if (mIns == null){
            mIns = new LiteDBManager();
        }
        return mIns;
    }

    public void init(Context context){
        liteOrm = LiteOrm.newSingleInstance(context,"liteorm-db");
    }

    public LiteOrm getLiteOrm() {
        return liteOrm;
    }

    public void setLiteOrm(LiteOrm liteOrm) {
        this.liteOrm = liteOrm;
    }
}
