package com.family.afamily.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by yaos on 2018/4/13.
 */

public class BodyModel implements Parcelable{
    private String icon;//": "http:\/\/win2.qbt8.com\/yjlx\/Uploads\/avatar\/2018-04-13\/5ad053788a687.png",
    private String nickname;//": "哈哈",
    private String birthday;//": 1523548800,
    private String user_id;//": "5567",
    private String height;//": "160.0",
    private String weight;//": "50.0",
    private String sex;//": "1"
    private String id;
    private int month;


    protected BodyModel(Parcel in) {
        icon = in.readString();
        nickname = in.readString();
        birthday = in.readString();
        user_id = in.readString();
        height = in.readString();
        weight = in.readString();
        sex = in.readString();
        id = in.readString();
        month = in.readInt();
    }

    public static final Creator<BodyModel> CREATOR = new Creator<BodyModel>() {
        @Override
        public BodyModel createFromParcel(Parcel in) {
            return new BodyModel(in);
        }

        @Override
        public BodyModel[] newArray(int size) {
            return new BodyModel[size];
        }
    };

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

        parcel.writeString(icon);
        parcel.writeString(nickname);
        parcel.writeString(birthday);
        parcel.writeString(user_id);
        parcel.writeString(height);
        parcel.writeString(weight);
        parcel.writeString(sex);
        parcel.writeString(id);
        parcel.writeInt(month);
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }
}
