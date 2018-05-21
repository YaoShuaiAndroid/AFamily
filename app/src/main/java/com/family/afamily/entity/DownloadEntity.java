package com.family.afamily.entity;

import java.io.File;

/**
 * Created by bt on 2018/4/13.
 */

public class DownloadEntity {
    //文件总长度
    long fileSize;
    //下载链接
    String downloadUrl;
    //线程Id
    int threadId;
    //起始下载位置
    long startLocation;
    //结束下载的文章
    long endLocation;
    //下载文件
    File tempFile;

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public int getThreadId() {
        return threadId;
    }

    public void setThreadId(int threadId) {
        this.threadId = threadId;
    }

    public long getStartLocation() {
        return startLocation;
    }

    public void setStartLocation(long startLocation) {
        this.startLocation = startLocation;
    }

    public long getEndLocation() {
        return endLocation;
    }

    public void setEndLocation(long endLocation) {
        this.endLocation = endLocation;
    }

    public File getTempFile() {
        return tempFile;
    }

    public void setTempFile(File tempFile) {
        this.tempFile = tempFile;
    }
}
