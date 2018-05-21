package com.family.afamily.entity;

/**
 * Created by bt on 2018/4/14.
 */

public class CommentModel {
    private  String id;//": "13",
    private  String uid;//": "5567",
    private  String comment;//": "新华网北京4月12日电 12日下午，习近平总书记来到位于海南三亚的中国科学院深海科学与工程研究所，仔细察看“海翼”号深海滑翔机、“天涯”深渊着陆器等设备，并同“深海勇士”号载人潜水器设计师和深潜团队亲切交谈。（据新华社“新华视点”微博报道，记者张晓松，摄影李学仁、谢环驰）",
    private  String commenttime;//": "2018-04-13 15:29:46",
    private  String commentedid;//": "0",
    private  String nick_name;//": "admin",
    private  String img;//": "http:\/\/yjlx.oss-cn-shenzhen.aliyuncs.com\/Uploads\/Picture\/2017-12-29\/5a45f342c21fb.jpg",
    private  String parent_name;//": 0

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getCommenttime() {
        return commenttime;
    }

    public void setCommenttime(String commenttime) {
        this.commenttime = commenttime;
    }

    public String getCommentedid() {
        return commentedid;
    }

    public void setCommentedid(String commentedid) {
        this.commentedid = commentedid;
    }

    public String getNick_name() {
        return nick_name;
    }

    public void setNick_name(String nick_name) {
        this.nick_name = nick_name;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getParent_name() {
        return parent_name;
    }

    public void setParent_name(String parent_name) {
        this.parent_name = parent_name;
    }
}
