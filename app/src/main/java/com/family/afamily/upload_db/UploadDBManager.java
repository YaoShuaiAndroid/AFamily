package com.family.afamily.upload_db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by hp2015-7 on 2018/1/24.
 */

public class UploadDBManager extends SQLiteOpenHelper {
    public static final String TABLE_NAME = "upload";
    public static final String AUDIO_TABLE_NAME = "audio";
    public static final int VERSION = 3;
    /**
     * 建表
     */
    public static final String CREATE_TABLE_SQL = "CREATE TABLE " + TABLE_NAME + " (id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "user_id TEXT,file_name TEXT,title TEXT,type TEXT,flag INTEGER,upload_Id TEXT,file_path TEXT,upload_flag INTEGER," +
            "video_path TEXT,total_size INTEGER,current_size INTEGER,create_time TEXT,child_id TEXT,address TEXT);";

    public static final String CREATE_AUDIO_TABLE_SQL = "CREATE TABLE " + AUDIO_TABLE_NAME + " (id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "user_id TEXT,img TEXT,order_sn TEXT,audio_path TEXT,audio_name TEXT,audio_download TEXT,download_flag INTEGER,total_size INTEGER,current_size INTEGER);";

    public UploadDBManager(Context context) {
        this(context, "upload_db", null, VERSION);
    }

    public UploadDBManager(Context context, int version) {
        this(context, "upload_db", null, version);
    }

    public UploadDBManager(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_SQL);
        db.execSQL(CREATE_AUDIO_TABLE_SQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 3) {
            db.execSQL(CREATE_AUDIO_TABLE_SQL);
        }
    }
}
