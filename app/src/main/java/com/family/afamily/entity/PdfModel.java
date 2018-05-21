package com.family.afamily.entity;

/**
 * Created by bt on 2018/4/13.
 */

public class PdfModel {
    private String id;//": "13",
    private String pdf_url;//": "Uploads\/2018-04-13\/5ad0595ae268f6.15476976.pdf",
    private String pdf_name;//": "一家老小App功能方案.pdf",
    private String urls;//": "http:\/\/win2.qbt8.com\/yjlx\/Public\/Uploads\/2018-04-13\/5ad0595ae268f6.15476976.pdf"
    private String time;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPdf_url() {
        return pdf_url;
    }

    public void setPdf_url(String pdf_url) {
        this.pdf_url = pdf_url;
    }

    public String getPdf_name() {
        return pdf_name;
    }

    public void setPdf_name(String pdf_name) {
        this.pdf_name = pdf_name;
    }

    public String getUrls() {
        return urls;
    }

    public void setUrls(String urls) {
        this.urls = urls;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
