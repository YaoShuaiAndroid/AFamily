package com.family.afamily.utils;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 文件工具类
 * Created by hp2015-7 on 2016/5/17.
 */
public class FileUtile {

    /**
     * 保存配置信息地址
     *
     * @param context
     * @return
     */
    public static String configPath(Context context) {
        return context.getCacheDir().getPath() + '/' + "configPath";
    }

    /**
     * 上传任务
     *
     * @param context
     * @return
     */
    public static String uploadPath(Context context) {
        return context.getCacheDir().getPath() + '/' + "uploadPath";
    }

    //获取SD卡存储路径
    private static String mSdRootPath = Environment.getExternalStorageDirectory().getPath();

    /**
     * 获取安装包路径
     */
    public static String getApkDirestory() {
        return mSdRootPath + "/Android/data/com.family.afamily/files/apk";
    }

    private static String SDPATH;

    public FileUtile() {
        //  getSDPATH();
    }

    public static String getSDPATH() {
        return SDPATH = mSdRootPath + "/afamily/";
    }


    /**
     * 在SD卡上创建文件
     *
     * @param fileName
     * @return
     * @throws IOException
     */
    public File createSDFile(String fileName) throws IOException {
        File file = new File(SDPATH + fileName);
        file.createNewFile();
        return file;
    }

    /**
     * 在SD卡上创建目录
     *
     * @param dirName 目录名字
     * @return 文件目录
     */
    public static File createDir(String dirName) {
        L.e("tag", "----------->" + getSDPATH());
        File dir = new File(getSDPATH() + dirName);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dir;
    }

    /**
     * 判断文件是否存在
     *
     * @param fileName
     * @return
     */
    public boolean isFileExist(String fileName) {
        File file = new File(SDPATH + fileName);
        L.e(file.getPath() + "--------------->");
        return file.exists();
    }

    public boolean isPause = false;

    public File write2SDFromInput(String path, String fileName, InputStream input) throws IOException {
        File file = null;
        OutputStream output = null;
        if (input == null)
            return null;
        try {
            createDir(path);
            file = createSDFile(path + fileName);
            L.e(file.getPath() + "---------ffff------>");

            FileOutputStream fos = new FileOutputStream(file);
            int count = 0;
            // 缓存
            byte buf[] = new byte[1024];
            // 写入到文件中
            do {
                int numread = input.read(buf);
                count += numread;
                if (numread <= 0) {
                    // 下载完成
                    L.e("正在下载", "-------ok-------------->");
                    break;
                }
                // 写入文件
                fos.write(buf, 0, numread);
            } while (!isPause);

            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }
}
