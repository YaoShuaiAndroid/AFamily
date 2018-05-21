package com.family.afamily.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

/**
 * 继成scrollview 重写滑动监听
 * Created by hp2015-7 on 2017/3/20.
 */
public class MyScrollView extends ScrollView {
    ScrollViewChanged scrollViewChanged;

    public MyScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (scrollViewChanged != null) {
            scrollViewChanged.onScrollChanged(l, t, oldl, oldt);
        }
    }


    public void setScrollViewChanged(ScrollViewChanged scrollViewChanged) {
        this.scrollViewChanged = scrollViewChanged;
    }

    public interface ScrollViewChanged {
        void onScrollChanged(int l, int t, int oldl, int oldt);
    }
}