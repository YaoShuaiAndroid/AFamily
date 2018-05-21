package com.family.afamily.upload_db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.family.afamily.entity.UploadVideoData;
import com.family.afamily.utils.L;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hp2015-7 on 2018/1/24.
 */

public class UploadDao {

    UploadDBManager dbManager;

    public UploadDao(Context context) {
        dbManager = new UploadDBManager(context);
    }

    /**
     * 插入一条数据
     *
     * @param bean
     */
    public int insertDB(UploadVideoData bean) {
        int i = 0;
        SQLiteDatabase db = dbManager.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("user_id", bean.getUserId());
        values.put("file_name", bean.getName());
        values.put("title", bean.getTitle());
        values.put("type", bean.getType());
        values.put("flag", bean.getFlag());
        values.put("upload_Id", bean.getUploadId());
        values.put("file_path", bean.getFilePath());
        values.put("upload_flag", bean.getUploadFlag());
        values.put("total_size", bean.getTotalSize());
        values.put("create_time", bean.getCreate_time());
        values.put("child_id", bean.getChild_id());
        values.put("address", bean.getAddress());

        if (db.isOpen()) {
            i = (int) db.insert(dbManager.TABLE_NAME, null, values);
            L.e("tag", "-------sql insert------------------->" + i);
        }
        return i;
    }


    /**
     * 查询一条数据
     *
     * @return
     */
    public UploadVideoData selectData(String title_) {
        SQLiteDatabase db = dbManager.getReadableDatabase();
        UploadVideoData beans = null;
        if (db.isOpen()) {
            Cursor cursor = db.rawQuery("select * from " + dbManager.TABLE_NAME + " where title = " + "'" + title_ + "'", null);
            while (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndex("id"));
                // "user_id TEXT,file_name TEXT,title TEXT,type TEXT,flag INTEGER,upload_Id TEXT,file_path TEXT,upload_flag INTEGER);";
                String userId = cursor.getString(cursor.getColumnIndex("user_id"));
                String fileName = cursor.getString(cursor.getColumnIndex("file_name"));
                String title = cursor.getString(cursor.getColumnIndex("title"));
                String filePath = cursor.getString(cursor.getColumnIndex("file_path"));
                String upload_Id = cursor.getString(cursor.getColumnIndex("upload_Id"));
                String type = cursor.getString(cursor.getColumnIndex("type"));
                String videoPath = cursor.getString(cursor.getColumnIndex("video_path"));
                int flag = cursor.getInt(cursor.getColumnIndex("flag"));
                int upload_flag = cursor.getInt(cursor.getColumnIndex("upload_flag"));
                beans = new UploadVideoData();
                beans.setId(id);
                beans.setUploadFlag(upload_flag);
                beans.setUserId(userId);
                beans.setType(type);
                beans.setFilePath(filePath);
                beans.setName(fileName);
                beans.setTitle(title);
                beans.setUploadId(upload_Id);
                beans.setFlag(flag);
                beans.setVideoPath(videoPath);
            }
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
    public List<UploadVideoData> getUploadList(String user_id) {

        long l = System.currentTimeMillis();

        SQLiteDatabase db = dbManager.getReadableDatabase();
        List<UploadVideoData> msgs = new ArrayList<>();
        if (db.isOpen()) {
            Cursor cursor = db.rawQuery("select * from " + UploadDBManager.TABLE_NAME + " where user_id = " + "'" + user_id + "' order by id ASC", null);
            while (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndex("id"));
                String userId = cursor.getString(cursor.getColumnIndex("user_id"));
                String fileName = cursor.getString(cursor.getColumnIndex("file_name"));
                String title = cursor.getString(cursor.getColumnIndex("title"));
                String filePath = cursor.getString(cursor.getColumnIndex("file_path"));
                String upload_Id = cursor.getString(cursor.getColumnIndex("upload_Id"));
                String type = cursor.getString(cursor.getColumnIndex("type"));
                int flag = cursor.getInt(cursor.getColumnIndex("flag"));
                int upload_flag = cursor.getInt(cursor.getColumnIndex("upload_flag"));
                int totalSize = cursor.getInt(cursor.getColumnIndex("total_size"));
                int currentSize = cursor.getInt(cursor.getColumnIndex("current_size"));
                String videoPath = cursor.getString(cursor.getColumnIndex("video_path"));
                String create_time = cursor.getString(cursor.getColumnIndex("create_time"));
                String child_id = cursor.getString(cursor.getColumnIndex("child_id"));
                String address = cursor.getString(cursor.getColumnIndex("address"));

                UploadVideoData beans = new UploadVideoData();
                beans.setId(id);
                beans.setUploadFlag(upload_flag);
                beans.setUserId(userId);
                beans.setType(type);
                beans.setFilePath(filePath);
                beans.setName(fileName);
                beans.setTitle(title);
                beans.setUploadId(upload_Id);
                beans.setFlag(flag);
                beans.setVideoPath(videoPath);
                beans.setTotalSize(totalSize);
                beans.setCurrentSize(currentSize);
                beans.setCreate_time(create_time);
                beans.setChild_id(child_id);
                beans.setAddress(address);
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
            i = db.update(dbManager.TABLE_NAME, values, "id = ?", new String[]{String.valueOf(str)});
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
            i = db.delete(dbManager.TABLE_NAME, "id = ?", new String[]{String.valueOf(id)});
            L.e("tag", "-------sql del------------------->" + id);
        }
        return i;
    }
}
