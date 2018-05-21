package com.family.afamily.adapters;

import android.content.Context;
import android.graphics.Color;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.family.afamily.R;
import com.family.afamily.entity.CommentModel;
import com.family.afamily.utils.GlideCircleTransform;
import com.family.afamily.view.GlideRoundImage;
import com.superrecycleview.superlibrary.adapter.BaseViewHolder;
import com.superrecycleview.superlibrary.adapter.SuperBaseAdapter;

import java.util.List;
import java.util.Map;

/**
 * Created by hp2015-7 on 2018/1/14.
 */

public class DetectCommentAdapter extends SuperBaseAdapter<CommentModel> {
    private Context mContext;
    public DetectCommentAdapter(Context context, List<CommentModel> data) {
        super(context, data);
        this.mContext=context;
    }

    @Override
    protected void convert(BaseViewHolder holder, CommentModel item, int position) {
        ImageView item_comm_head=holder.getView(R.id.item_comm_head);
        TextView item_comm_nick=holder.getView(R.id.item_comm_nick);
        TextView item_comm_time=holder.getView(R.id.item_comm_time);
        TextView item_comm_decs=holder.getView(R.id.item_comm_decs);

        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.mipmap.no_img)
                .transform(new GlideCircleTransform(mContext));

        Glide.with(mContext)
                .load(item.getImg())
                .apply(options)
                .into(item_comm_head);

        item_comm_nick.setText(item.getNick_name());
        item_comm_time.setText(item.getCommenttime());
        if(item.getCommentedid()!=null&&!item.getCommentedid().equals("0")){
            item_comm_decs.setText("回复"+item.getParent_name()+":"+item.getComment());

            SpannableStringBuilder mSpannable = new SpannableStringBuilder(item_comm_decs.getText().toString());
            mSpannable.setSpan(new ForegroundColorSpan(Color.BLUE), 2,2+item.getParent_name().length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
            item_comm_decs.setText(mSpannable);
        }else{
            item_comm_decs.setText(item.getComment());
        }
    }

    @Override
    protected int getItemViewLayoutId(int position, CommentModel item) {
        return R.layout.list_comment_item;
    }
}
