package com.family.afamily.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.family.afamily.R;
import com.family.afamily.utils.GlideCircleTransform;
import com.family.afamily.utils.L;
import com.family.afamily.utils.SPUtils;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.entity.LocalMedia;
import com.superrecycleview.superlibrary.adapter.BaseViewHolder;
import com.superrecycleview.superlibrary.adapter.SuperBaseAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by hp2015-7 on 2017/12/29.
 */

public class ZJCommcentAdapter extends SuperBaseAdapter<Map<String, String>> {
    private Context context;
    private CommentItemClick itemClick;
    List<LocalMedia> picList;
    public ZJCommcentAdapter(Context context, List<Map<String, String>> data, CommentItemClick itemClick) {
        super(context, data);
        this.context = context;
        this.itemClick = itemClick;
        picList = new ArrayList<>();
    }

    @Override
    protected void convert(BaseViewHolder holder, final Map<String, String> item, int position) {
        ImageView item_comm_head = holder.getView(R.id.item_comm_head);
        holder.setText(R.id.item_comm_nick, item.get("nick_name"));
        holder.setText(R.id.item_comm_time, item.get("comment_time"));
        TextView item_comm_decs = holder.getView(R.id.item_comm_decs);
        ImageView item_comm_pic = holder.getView(R.id.item_comm_pic);
        RelativeLayout item_comment_root_rl = holder.getView(R.id.item_comment_root_rl);
//        holder.setText(R.id.item_zj_time,item.get("play_time"));
//
        //查看大图
        item_comm_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                picList.clear();
                LocalMedia l = new LocalMedia();
                l.setPath(item.get("picture"));
                picList.add(l);
                PictureSelector.create((Activity) context).externalPicturePreview(0, "/custom_file", picList);
            }
        });



        RequestOptions options = new RequestOptions();
        //options.error(R.drawable.error_pic);
        options.transform(new GlideCircleTransform(context));
        Glide.with(context).load(item.get("images")).apply(options).into(item_comm_head);

        item_comment_root_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String item_user_id = item.get("user_id");
                String user_id = (String) SPUtils.get(context, "user_id", "");
                if (item_user_id.equals(user_id)) {
                    itemClick.clickItem("", "");
                } else {
                    itemClick.clickItem(item.get("nick_name"), item_user_id);
                }
            }
        });

        String picture = item.get("picture");
        String commented_id = item.get("commented_id");
        String commented_name = item.get("commented_name");
        String comment = item.get("comment");
        RequestOptions options2 = new RequestOptions();
        options2.error(R.drawable.error_pic);
        //正常评论
        if (TextUtils.isEmpty(commented_id) || commented_id.equals("0")) {
            if (TextUtils.isEmpty(picture)) {
                item_comm_decs.setText(comment);
                item_comm_decs.setVisibility(View.VISIBLE);
                item_comm_pic.setVisibility(View.GONE);
            } else {
                item_comm_decs.setVisibility(View.GONE);
                item_comm_pic.setVisibility(View.VISIBLE);
                Glide.with(context).load(item.get("picture")).apply(options2).into(item_comm_pic);
            }
        } else {//回复xx
            if (TextUtils.isEmpty(picture)) {
                String str = "回复 " + commented_name + ":" + comment;
                SpannableStringBuilder style = new SpannableStringBuilder();
                SpannableString sp1 = new SpannableString(str);
                sp1.setSpan(new Clickable(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //在这里添加点击事件
                        L.e("Tag", "---------------66--------------------------->");
                    }
                }), 3, 3 + commented_name.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                style.append(sp1);//添加


                item_comm_decs.setText(style);
                item_comm_decs.setMovementMethod(LinkMovementMethod.getInstance());
                item_comm_decs.setVisibility(View.VISIBLE);
                item_comm_pic.setVisibility(View.GONE);
            } else {
                String str = "回复 " + commented_name + ":";
                SpannableStringBuilder style = new SpannableStringBuilder(str);
                //style.setSpan(new ForegroundColorSpan(Color.parseColor("#6276B2")), 3, 3+commented_name.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                style.setSpan(new Clickable(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    }
                }), 3, 3 + commented_name.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                item_comm_decs.setText(style);
                item_comm_decs.setMovementMethod(LinkMovementMethod.getInstance());
                item_comm_decs.setVisibility(View.VISIBLE);
                item_comm_pic.setVisibility(View.VISIBLE);
                Glide.with(context).load(item.get("picture")).apply(options2).into(item_comm_pic);
            }
        }
    }


    class Clickable extends ClickableSpan implements View.OnClickListener {
        private final View.OnClickListener mListener;

        public Clickable(View.OnClickListener mListener) {
            this.mListener = mListener;
        }

        @Override
        public void onClick(View v) {
            mListener.onClick(v);
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            ds.setColor(Color.parseColor("#6276B2"));
            ds.setUnderlineText(false);    //去除超链接的下划线
        }
    }

    @Override
    protected int getItemViewLayoutId(int position, Map<String, String> item) {
        return R.layout.item_zaoj_comment_layout;
    }

    public interface CommentItemClick {

        void clickItem(String userName, String uid);
    }

}
