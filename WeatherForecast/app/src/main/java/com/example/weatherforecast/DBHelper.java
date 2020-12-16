package com.example.weatherforecast;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    private static String DB_PATH = "/data/data/com.example.weatherforecase/databases";

    // 코드에서 만들어야 제대로 만들어짐
    // private static String DB_NAME = "highdb";

    public DBHelper(Context context) {
        super(context, "weatherdb", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // table을 만들 때
//        db.execSQL("CREATE TABLE groupTBL (gNAME char(20) PRIMARY KEY, gNumber INTEGER);");
        db.execSQL("CREATE TABLE weather (wID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "area VARCHAR(20), latitude VARCHAR(10), longitude VARCHAR(10));");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // table을 삭제할 때
        db.execSQL("drop table weather");
    }
}
