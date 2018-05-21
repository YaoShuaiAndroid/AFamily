package com.family.afamily.entity;

import java.io.Serializable;

/**
 * Created by hp2015-7 on 2018/1/22.
 */

public class UploadVideoData implements Serializable {
    private int id;
    private String userId;
    //视频标题
    private String title;
    //视频类型
    private String type;
    //1达人、2宝宝
    private int flag;
    //视频路径
    private String filePath;
    //视频上传ID
    private String uploadId;
    //上传到第几段
    private int currentIndex;
    //视频文件名称
    private String name;
    //上传状态，0未上传，1上传中 ,2当前为移动网络，3发布失败，4忽略移动网络继续上传
    private int uploadFlag;
    //视频路径
    private String videoPath;

    private int totalSize;

    private int currentSize;
    //宝宝模块字段
    private String create_time;
    private String child_id;
    private String address;


    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getChild_id() {
        return child_id;
    }

    public void setChild_id(String child_id) {
        this.child_id = child_id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(int totalSize) {
        this.totalSize = totalSize;
    }

    public int getCurrentSize() {
        return currentSize;
    }

    public void setCurrentSize(int currentSize) {
        this.currentSize = currentSize;
    }

    public String getVideoPath() {
        return videoPath;
    }

    public void setVideoPath(String videoPath) {
        this.videoPath = videoPath;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getUploadFlag() {
        return uploadFlag;
    }

    public void setUploadFlag(int uploadFlag) {
        this.uploadFlag = uploadFlag;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getUploadId() {
        return uploadId;
    }

    public void setUploadId(String uploadId) {
        this.uploadId = uploadId;
    }

    public int getCurrentIndex() {
        return currentIndex;
    }

    public void setCurrentIndex(int currentIndex) {
        this.currentIndex = currentIndex;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "UploadVideoData{" +
                "id=" + id +
                ", userId='" + userId + '\'' +
                ", title='" + title + '\'' +
                ", type='" + type + '\'' +
                ", flag=" + flag +
                ", filePath='" + filePath + '\'' +
                ", uploadId='" + uploadId + '\'' +
                ", currentIndex=" + currentIndex +
                ", name='" + name + '\'' +
                ", uploadFlag=" + uploadFlag +
                ", videoPath='" + videoPath + '\'' +
                '}';
    }
}
