package com.family.afamily.entity;

import java.util.List;

/**
 * Created by hp2015-7 on 2017/12/21.
 */

public class BasePageBean<T> {
    private Integer totle_page;
    private Integer pages;
    private Integer page;
    private List<T> list_data;
    private List<T> review;
    private Integer count;

    public Integer getCount() {
        return count == null ? 0 : count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
//    public int getTotle_page() {
//        return totle_page == null ? 0 : totle_page;
//    }
//
//    public void setTotle_page(Integer total_page) {
//        this.totle_page = total_page;
//    }


    public Integer getTotle_page() {
        return totle_page == null ? 0 : totle_page;
    }

    public void setTotle_page(Integer totle_page) {
        this.totle_page = totle_page;
    }

    public int getPage() {
        return page == null ? 1 : page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public List<T> getList_data() {
        return list_data;
    }

    public void setList_data(List<T> list_data) {
        this.list_data = list_data;
    }

    public Integer getPages() {
        return pages;
    }

    public void setPages(Integer pages) {
        this.pages = pages;
    }

    public List<T> getReview() {
        return review;
    }

    public void setReview(List<T> review) {
        this.review = review;
    }
}
