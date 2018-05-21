package com.family.afamily.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.family.afamily.R;


/**
 * Created by hp2015-7 on 2016/8/30.
 */
public class TabButtonView extends RelativeLayout {

    private int tabImgWidth;//tab图标宽度
    private int tabImgHeight;//tab图标高度
    private int tabImgMarginTop;//距离头部距离
    private int tabImgMarginBottom;//距离底部距离
    private boolean isShowUnreadMsg;//是否显示未读消息
    private int unreadType;//未读消息类型，1红点 2，文本
    private int unreadBackgroundColor;//未读消息背景色
    private float unreadMsgTextSize;//未读消息字体大小
    private int unreadMsgTextColor;//未读消息文本颜色
    private int tabImgSelectImg;//tab图片选中图片
    private int tabImgUnSelectImg;//tab图片未选择图片
    private int tabTextSelectColor;//tab文本选中颜色
    private int tabTextUnSelectColor;//tab文本未选中颜色
    private int tabBackgroundColor = Color.parseColor("#00000000");
    private float tabTextSize;//文本字体大小
    private String tabText;//文本
    private String unreadMsgText;//未读消息文本
    private ImageView tab_iv;
    private ImageView tab_unread_iv;
    private TextView tab_tv;
    private TextView tab_unread_tv;

    public TabButtonView(Context context) {
        super(context);
    }

    public TabButtonView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public TabButtonView(Context context, AttributeSet attrs) {
        super(context, attrs);
        getAttriButeSet(context, attrs);
        View view = LayoutInflater.from(context).inflate(R.layout.tab_button, this, true);
        tab_iv = (ImageView) view.findViewById(R.id.tab_iv);
        tab_unread_iv = (ImageView) view.findViewById(R.id.tab_unread_iv);
        tab_tv = (TextView) view.findViewById(R.id.tab_tv);
        tab_unread_tv = (TextView) view.findViewById(R.id.tab_unread_tv);
        tab_tv.setText(tabText);
        tab_tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, tabTextSize);
        tab_iv.setImageResource(tabImgUnSelectImg);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) tab_iv.getLayoutParams();
        params.setMargins(0, tabImgMarginTop, 0, tabImgMarginBottom);
        params.width = tabImgWidth;
        params.height = tabImgHeight;
        tab_iv.setLayoutParams(params);

        if (isShowUnreadMsg) {
            if (unreadType == 1) {
                tab_unread_iv.setVisibility(View.VISIBLE);
                tab_unread_tv.setVisibility(View.GONE);
                tab_unread_iv.setImageResource(unreadBackgroundColor);
            } else {
                tab_unread_tv.setVisibility(View.VISIBLE);
                tab_unread_iv.setVisibility(View.GONE);
                tab_unread_tv.setBackgroundResource(unreadBackgroundColor);
                tab_unread_tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, unreadMsgTextSize);
                tab_unread_tv.setText(unreadMsgText);
            }
        }
    }

    public void setSelectTab(boolean selecttab) {
        if (selecttab) {
            this.setSelected(true);
            this.setBackgroundColor(tabBackgroundColor);
            tab_iv.setImageResource(tabImgSelectImg);
            tab_tv.setTextColor(tabTextSelectColor);
            if (isShowUnreadMsg) {
                tab_unread_iv.setSelected(true);
                tab_unread_tv.setSelected(true);
            }
        } else {
            this.setSelected(false);
            this.setBackgroundColor(Color.parseColor("#00000000"));
            tab_iv.setImageResource(tabImgUnSelectImg);
            tab_tv.setTextColor(tabTextUnSelectColor);
            if (isShowUnreadMsg) {
                tab_unread_iv.setSelected(false);
                tab_unread_tv.setSelected(false);
            }
        }
    }

    public boolean isShowUnreadMsg() {
        return isShowUnreadMsg;
    }

    public void setShowUnreadMsg(boolean showUnreadMsg) {
        isShowUnreadMsg = showUnreadMsg;
    }

    public int getUnreadType() {
        return unreadType;
    }


    public int getTabTextSelectColor() {
        return tabTextSelectColor;
    }

    public void setTabTextSelectColor(int tabTextSelectColor) {
        this.tabTextSelectColor = tabTextSelectColor;
        tab_tv.setTextColor(tabTextSelectColor);
    }

    public int getTabImgSelectImg() {
        return tabImgSelectImg;
    }

    public void setTabImgSelectImg(int tabImgSelectImg) {
        this.tabImgSelectImg = tabImgSelectImg;
        tab_iv.setImageResource(tabImgSelectImg);
    }

    public int getTabImgUnSelectImg() {
        return tabImgUnSelectImg;
    }

    public void setTabImgUnSelectImg(int tabImgUnSelectImg) {
        this.tabImgUnSelectImg = tabImgUnSelectImg;
        tab_iv.setImageResource(tabImgUnSelectImg);
    }

    /**
     * 未读消息类型，1红点 2，文本
     *
     * @param unreadType
     */
    public void setUnreadType(int unreadType) {
        this.unreadType = unreadType;
        if (isShowUnreadMsg) {
            if (isShowUnreadMsg) {
                if (unreadType == 1) {
                    tab_unread_iv.setVisibility(View.VISIBLE);
                    tab_unread_tv.setVisibility(View.GONE);
                    tab_unread_iv.setImageResource(unreadBackgroundColor);
                } else {
                    tab_unread_tv.setVisibility(View.VISIBLE);
                    tab_unread_iv.setVisibility(View.GONE);
                    tab_unread_tv.setBackgroundResource(unreadBackgroundColor);
                    tab_unread_tv.setTextSize(unreadMsgTextSize);
                    tab_unread_tv.setText(unreadMsgText);
                    tab_unread_tv.setTextColor(unreadMsgTextColor);
                }
            }
        }
    }

    public void setUnreadMsgText(String unreadMsgText) {
        this.unreadMsgText = unreadMsgText;
        if (isShowUnreadMsg) {
            if (unreadType == 1) {
                tab_unread_iv.setVisibility(View.VISIBLE);
                tab_unread_tv.setVisibility(View.GONE);
                tab_unread_iv.setImageResource(unreadBackgroundColor);
            } else {
                tab_unread_tv.setVisibility(View.VISIBLE);
                tab_unread_iv.setVisibility(View.GONE);
                tab_unread_tv.setBackgroundResource(unreadBackgroundColor);
                tab_unread_tv.setTextSize(unreadMsgTextSize);
                tab_unread_tv.setText(unreadMsgText);
                tab_unread_tv.setTextColor(unreadMsgTextColor);
            }
        }
    }

    private void getAttriButeSet(Context context, AttributeSet attrs) {
        if (attrs == null) {
            return;
        }
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.TabButtonView);
        tabImgWidth = typedArray.getDimensionPixelSize(R.styleable.TabButtonView_tabImgWidth, 0);
        tabImgHeight = typedArray.getDimensionPixelSize(R.styleable.TabButtonView_tabImgHeight, 0);
        tabImgMarginTop = typedArray.getDimensionPixelSize(R.styleable.TabButtonView_tabImgMarginTop, 0);
        tabImgMarginBottom = typedArray.getDimensionPixelSize(R.styleable.TabButtonView_tabImgMarginBottom, 0);
        isShowUnreadMsg = typedArray.getBoolean(R.styleable.TabButtonView_isShowUnreadMsg, false);
        unreadType = typedArray.getInteger(R.styleable.TabButtonView_unreadType, 1);
        unreadBackgroundColor = typedArray.getResourceId(R.styleable.TabButtonView_unreadBackgroundColor, 0);
        unreadMsgTextSize = typedArray.getDimension(R.styleable.TabButtonView_unreadMsgTextSize, 0);
        unreadMsgTextColor = typedArray.getColor(R.styleable.TabButtonView_unreadMsgTextColor, 0);
        tabImgSelectImg = typedArray.getResourceId(R.styleable.TabButtonView_tabImgSelectImg, 0);
        tabImgUnSelectImg = typedArray.getResourceId(R.styleable.TabButtonView_tabImgUnSelectImg, 0);
        tabTextSelectColor = typedArray.getColor(R.styleable.TabButtonView_tabTextSelectColor, 0);
        tabTextUnSelectColor = typedArray.getColor(R.styleable.TabButtonView_tabTextUnSelectColor, 0);
        tabTextSize = typedArray.getDimension(R.styleable.TabButtonView_tabTextSize, 0);
        tabText = typedArray.getString(R.styleable.TabButtonView_tabText);
        unreadMsgText = typedArray.getString(R.styleable.TabButtonView_unreadMsgText);
        tabBackgroundColor = typedArray.getColor(R.styleable.TabButtonView_tabBackgroundColor, Color.parseColor("#00000000"));

        typedArray.recycle();
    }

}
