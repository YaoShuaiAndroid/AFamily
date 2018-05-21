package com.family.afamily.adapters;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.family.afamily.R;
import com.family.afamily.entity.InnateIntelligenceModel;
import com.family.afamily.utils.AppUtil;
import com.family.afamily.view.GlideRoundImage;
import com.superrecycleview.superlibrary.adapter.BaseViewHolder;
import com.superrecycleview.superlibrary.adapter.SuperBaseAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by hp2015-7 on 2018/1/14.
 */

public class InnateIntelligenceAdapter extends SuperBaseAdapter<InnateIntelligenceModel> {
private Context mContext;
    private Handler handler;


    public InnateIntelligenceAdapter(Context context, List<InnateIntelligenceModel> data,Handler handler) {
        super(context, data);
        mContext=context;
        this.handler=handler;
    }

    @Override
    protected void convert(BaseViewHolder holder, final InnateIntelligenceModel item, int position) {
        RecyclerView intell_item_recy=holder.getView(R.id.intell_item_recy);
        TextView intelligence_title=holder.getView(R.id.intelligence_title);
        TextView intelligence_content=holder.getView(R.id.intelligence_content);
        TextView intelligence_comment_num=holder.getView(R.id.intelligence_comment_num);
        TextView intelligence_collect_num=holder.getView(R.id.intelligence_collect_num);
        ImageView intelligence_like_img=holder.getView(R.id.intelligence_like_img);
        ImageView conduct_play_img=holder.getView(R.id.conduct_play_img);

        intelligence_title.setText(item.getTitle());
        intelligence_content.setText(Html.fromHtml(item.getContent()));
        intelligence_comment_num.setText(item.getComments());
        intelligence_collect_num.setText(item.getLikes());
        if(item.getLike()!=null&&item.getLike().equals("1")){
            intelligence_like_img.setBackgroundResource(R.mipmap.like_true);
            intelligence_collect_num.setTextColor(Color.parseColor("#F99830"));
        }else{
            intelligence_like_img.setBackgroundResource(R.mipmap.like);
            intelligence_collect_num.setTextColor(Color.parseColor("#93959a"));
        }

        intell_item_recy.setHasFixedSize(true);
        intell_item_recy.setNestedScrollingEnabled(false);

        conduct_play_img.setVisibility(View.GONE);

        int gridNum=3;
        if(item.getImgs()!=null&&item.getImgs().size()>0&&item.getImgs().size()<4){
            gridNum=item.getImgs().size();

            if(!TextUtils.isEmpty(item.getVideo_url())){
                conduct_play_img.setVisibility(View.VISIBLE);
            }
        }
        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, gridNum);
        intell_item_recy.setLayoutManager(gridLayoutManager);

        CommonChaAdapter personCommonAdapter = new CommonChaAdapter<String>(R.layout.list_innate_item,item.getImgs()) {
            @Override
            public void convertViewItem(com.chad.library.adapter.base.BaseViewHolder baseViewHolder, String item) {
                ImageView innate_item_img=baseViewHolder.getView(R.id.innate_item_img);

                RelativeLayout.LayoutParams params= (RelativeLayout.LayoutParams) innate_item_img.getLayoutParams();
                //获取当前控件的布局对象
                if(this.getData().size()==1){
                    params.height= AppUtil.dip2px(mContext,195);
                }else if(this.getData().size()==1){
                    params.height= AppUtil.dip2px(mContext,95);
                }else{
                    params.height= AppUtil.dip2px(mContext,80);
                }
                innate_item_img.setLayoutParams(params);//将设置好的布局参数应用到控件中


                RequestOptions options = new RequestOptions()
                        .centerCrop()
                        .placeholder(R.mipmap.no_img)
                        .transform(new GlideRoundImage(mContext));

                Glide.with(mContext)
                        .load(item)
                        .apply(options)
                        .into(innate_item_img);
            }
        };

        personCommonAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Message message=new Message();
                message.obj=item.getId();
                message.what=1002;
                handler.sendMessage(message);
            }
        });

        intell_item_recy.setAdapter(personCommonAdapter);
    }

    @Override
    protected int getItemViewLayoutId(int position, InnateIntelligenceModel item) {
        return R.layout.item_intelligence_layout;
    }
}
