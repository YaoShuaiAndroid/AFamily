
package com.family.afamily.entity;

import com.superrecycleview.superlibrary.sidebar.bean.BaseIndexPinyinBean;

/**
 * Created by super南仔 on 2017-05-04. 类备注： 需要传入的参数:
 */
public class WeChatBean extends BaseIndexPinyinBean {
    private String label;
    private String pinyin;
    private String zip;
    private String name;// 城市的名称
    private boolean isTop;// 是否是最上面 不需要被转化成拼音

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isTop() {
        return isTop;
    }

    public void setTop(boolean top) {
        isTop = top;
    }

    @Override
    public String getTarget() {
        return name;
    }

    @Override
    public boolean isNeedToPinyin() {
        return !isTop;
    }

    @Override
    public boolean isShowSuspension() {
        return !isTop;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getPinyin() {
        return pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    @Override
    public String toString() {
        return "WeChatBean{" +
                "label='" + label + '\'' +
                ", pinyin='" + pinyin + '\'' +
                ", zip='" + zip + '\'' +
                ", name='" + name + '\'' +
                ", isTop=" + isTop +
                '}';
    }
}
