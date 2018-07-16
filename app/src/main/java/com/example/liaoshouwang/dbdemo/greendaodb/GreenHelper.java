package com.example.liaoshouwang.dbdemo.greendaodb;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import org.greenrobot.greendao.database.Database;

/**
 * created by edison 2018/7/16
 */
public class GreenHelper extends DaoMaster.OpenHelper {


    public GreenHelper(Context context, String name) {
        super(context, name);
    }

    public GreenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
        super(context, name, factory);
    }

    @Override
    public void onUpgrade(Database db, int oldVersion, int newVersion) {
        super.onUpgrade(db, oldVersion, newVersion);
    }
}
