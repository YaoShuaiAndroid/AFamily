package com.family.afamily.adapters;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.family.afamily.R;
import com.family.afamily.entity.InnateIntelligenceModel;
import com.family.afamily.view.GlideRoundImage;
import com.superrecycleview.superlibrary.adapter.BaseViewHolder;
import com.superrecycleview.superlibrary.adapter.SuperBaseAdapter;

import java.util.List;
import java.util.Map;

/**
 * Created by hp2015-7 on 2018/1/14.
 */

public class PhysiqueAdapter extends SuperBaseAdapter<InnateIntelligenceModel> {

    private Context mContext;

    public PhysiqueAdapter(Context context, List<InnateIntelligenceModel> data) {
        super(context, data);
        this.mContext=context;
    }

    @Override
    protected void convert(BaseViewHolder holder, InnateIntelligenceModel item, int position) {
        TextView psysique_title=holder.getView(R.id.psysique_title);
        TextView physique_date=holder.getView(R.id.physique_date);
        ImageView psysique_img=holder.getView(R.id.psysique_img);

        physique_date.setText(item.getCreate_time());
        psysique_title.setText(item.getTitle());

        String url="";
        if(item.getImgs()!=null&&item.getImgs().size()>0){
            url= item.getImgs().get(0);
        }

        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.mipmap.no_img)
                .transform(new GlideRoundImage(mContext));

        Glide.with(mContext)
                .load(url)
                .apply(options)
                .into(psysique_img);
    }

    @Override
    protected int getItemViewLayoutId(int position, InnateIntelligenceModel item) {
        return R.layout.list_psysique_item;
    }
}
