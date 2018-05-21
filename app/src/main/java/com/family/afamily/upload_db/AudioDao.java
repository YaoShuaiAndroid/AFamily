package com.family.afamily.upload_db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.family.afamily.entity.AudioData;
import com.family.afamily.utils.L;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hp2015-7 on 2018/1/26.
 */

public class AudioDao {
    UploadDBManager dbManager;

    public AudioDao(Context context) {
        dbManager = new UploadDBManager(context);
    }

    /**
     * 插入一条数据
     *
     * @param bean
     */
    public int insertDB(AudioData bean) {
//        "order_sn TEXT,audio_path TEXT,audio_name TEXT,audio_download TEXT,download_flag INTEGER);";
        int i = 0;
        SQLiteDatabase db = dbManager.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("user_id", bean.getUser_id());
        values.put("order_sn", bean.getOrder_sn());
        values.put("audio_path", bean.getAudioPath());
        values.put("audio_name", bean.getAudio_name());
        values.put("audio_download", bean.getAudioDownload());
        values.put("download_flag", bean.getDownloadFlag());
        values.put("total_size", bean.getTotalSize());
        values.put("img", bean.getImg());
        values.put("current_size", bean.getCurrentSize());
        if (db.isOpen()) {
            i = (int) db.insert(dbManager.AUDIO_TABLE_NAME, null, values);
            L.e("tag", "-------sql insert------------------->" + i);
        }
        return i;
    }

    /**
     * 查询一条数据
     *
     * @return
     */
    public AudioData selectData(String user_id, String order_sn) {
        SQLiteDatabase db = dbManager.getReadableDatabase();
        AudioData beans = null;
        if (db.isOpen()) {
            Cursor cursor = db.rawQuery("select * from " + dbManager.AUDIO_TABLE_NAME + " where user_id = " + "'" + user_id + "' and order_sn = " + "'" + order_sn + "'", null);
            while (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndex("id"));
                //  "order_sn TEXT,audio_path TEXT,audio_name TEXT,audio_download TEXT,download_flag INTEGER);";
                String userId = cursor.getString(cursor.getColumnIndex("user_id"));
                String orderSn = cursor.getString(cursor.getColumnIndex("order_sn"));
                String audioPath = cursor.getString(cursor.getColumnIndex("audio_path"));
                String audioName = cursor.getString(cursor.getColumnIndex("audio_name"));
                String audioDownload = cursor.getString(cursor.getColumnIndex("audio_download"));
                String img = cursor.getString(cursor.getColumnIndex("img"));
                int downloadFlag = cursor.getInt(cursor.getColumnIndex("download_flag"));
                int totalSize = cursor.getInt(cursor.getColumnIndex("total_size"));
                int currentSize = cursor.getInt(cursor.getColumnIndex("current_size"));
                beans = new AudioData();
                beans.setId(id);
                beans.setOrder_sn(orderSn);
                beans.setUser_id(userId);
                beans.setAudioPath(audioPath);
                beans.setAudio_name(audioName);
                beans.setAudioDownload(audioDownload);
                beans.setDownloadFlag(downloadFlag);
                beans.setTotalSize(totalSize);
                beans.setCurrentSize(currentSize);
                beans.setImg(img);
            }
            //  L.e("tag","---------ddddddddddd-------->"+beans);
            cursor.close();
        }

        return beans;
    }

    /**
     * 获取上传列表
     *
     * @param user_id
     * @return
     */
    public List<AudioData> getAudioList(String user_id) {
        SQLiteDatabase db = dbManager.getReadableDatabase();
        List<AudioData> msgs = new ArrayList<>();
        if (db.isOpen()) {
            Cursor cursor = db.rawQuery("select * from " + UploadDBManager.AUDIO_TABLE_NAME + " where user_id = " + "'" + user_id + "' order by id ASC", null);
            while (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndex("id"));
                //  "order_sn TEXT,audio_path TEXT,audio_name TEXT,audio_download TEXT,download_flag INTEGER);";
                String userId = cursor.getString(cursor.getColumnIndex("user_id"));
                String orderSn = cursor.getString(cursor.getColumnIndex("order_sn"));
                String audioPath = cursor.getString(cursor.getColumnIndex("audio_path"));
                String audioName = cursor.getString(cursor.getColumnIndex("audio_name"));
                String audioDownload = cursor.getString(cursor.getColumnIndex("audio_download"));
                int downloadFlag = cursor.getInt(cursor.getColumnIndex("download_flag"));
                int totalSize = cursor.getInt(cursor.getColumnIndex("total_size"));
                int currentSize = cursor.getInt(cursor.getColumnIndex("current_size"));
                String img = cursor.getString(cursor.getColumnIndex("img"));

                AudioData beans = new AudioData();
                beans.setId(id);
                beans.setOrder_sn(orderSn);
                beans.setUser_id(userId);
                beans.setAudioPath(audioPath);
                beans.setAudio_name(audioName);
                beans.setAudioDownload(audioDownload);
                beans.setDownloadFlag(downloadFlag);
                beans.setTotalSize(totalSize);
                beans.setCurrentSize(currentSize);
                beans.setImg(img);
                msgs.add(beans);
            }
            cursor.close();
        }

        return msgs;
    }

    /**
     * 更新一条数据
     *
     * @param values
     * @param str
     * @return
     */
    public int updateMsm(ContentValues values, int str) {
        int i = 0;
        SQLiteDatabase db = dbManager.getWritableDatabase();
        if (db.isOpen()) {
            i = db.update(dbManager.AUDIO_TABLE_NAME, values, "id = ?", new String[]{String.valueOf(str)});
            L.e("tag", "-------sql update------------------->" + str);
        }
        return i;
    }

    /**
     * 删除一条数据
     *
     * @param id
     * @return
     */
    public int delData(int id) {
        int i = 0;
        SQLiteDatabase db = dbManager.getWritableDatabase();
        if (db.isOpen()) {
            i = db.delete(dbManager.AUDIO_TABLE_NAME, "id = ?", new String[]{String.valueOf(id)});
            L.e("tag", "-------sql del------------------->" + id);
        }
        return i;
    }


}
