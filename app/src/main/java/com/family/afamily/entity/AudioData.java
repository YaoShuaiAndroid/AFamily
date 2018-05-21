package com.family.afamily.entity;

/**
 * Created by hp2015-7 on 2018/1/26.
 */

public class AudioData {
    private int id;
    private String img;
    private String user_id;
    private String audioPath;
    private String audioDownload;
    private String order_sn;
    private String audio_name;
    private int totalSize;
    private int currentSize;
    private int downloadFlag;

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAudioPath() {
        return audioPath;
    }

    public void setAudioPath(String audioPath) {
        this.audioPath = audioPath;
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

    public String getOrder_sn() {
        return order_sn;
    }

    public void setOrder_sn(String order_sn) {
        this.order_sn = order_sn;
    }

    public String getAudio_name() {
        return audio_name;
    }

    public void setAudio_name(String audio_name) {
        this.audio_name = audio_name;
    }


    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getAudioDownload() {
        return audioDownload;
    }

    public void setAudioDownload(String audioDownload) {
        this.audioDownload = audioDownload;
    }

    public int getDownloadFlag() {
        return downloadFlag;
    }

    public void setDownloadFlag(int downloadFlag) {
        this.downloadFlag = downloadFlag;
    }

    @Override
    public String toString() {
        return "AudioData{" +
                "id=" + id +
                ", user_id='" + user_id + '\'' +
                ", audioPath='" + audioPath + '\'' +
                ", audioDownload='" + audioDownload + '\'' +
                ", order_sn='" + order_sn + '\'' +
                ", audio_name='" + audio_name + '\'' +
                ", totalSize=" + totalSize +
                ", currentSize=" + currentSize +
                ", downloadFlag=" + downloadFlag +
                '}';
    }
}
