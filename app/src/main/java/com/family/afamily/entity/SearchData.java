package com.family.afamily.entity;

import java.util.List;

/**
 * Created by hp2015-7 on 2018/1/15.
 */

public class SearchData {
    private List<String> search_word;
    private List<String> hot_word;

    public List<String> getSearch_word() {
        return search_word;
    }

    public void setSearch_word(List<String> search_word) {
        this.search_word = search_word;
    }

    public List<String> getHot_word() {
        return hot_word;
    }

    public void setHot_word(List<String> hot_word) {
        this.hot_word = hot_word;
    }
}
