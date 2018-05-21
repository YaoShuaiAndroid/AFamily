package com.family.afamily.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.family.afamily.R;

import java.util.List;
import java.util.Map;


/**
 * Created by chensuilun on 2016/11/15.
 */
public class Frag1GalleryAdapter extends RecyclerView.Adapter<Frag1GalleryAdapter.ViewHolder> implements View.OnClickListener {
    private static final String TAG = "DemoAdapter";
    private List<Map<String, String>> mItems;
    private OnItemClickListener mOnItemClickListener;
    private Context context;

    public Frag1GalleryAdapter(Context context, List<Map<String, String>> items) {
        this.mItems = items;
        this.context = context;
    }

    public Frag1GalleryAdapter setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
        return this;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_gallery_layout, parent, false);
        v.setOnClickListener(this);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Map<String, String> itemData = mItems.get(position);
        holder.itemView.setTag(position);
        /**
         * {
         "id": "1",
         "title": "你好",
         "picture": "\/Uploads\/Picture\/2017-12-14\/5a31fca2bba33.jpg",
         "add_time": "1513094400",
         "content": "<p>哈哈哈哈哈哈<\/p>"
         */
        holder.text.setText(itemData.get("title"));
        RequestOptions options = new RequestOptions();
        options.error(R.drawable.error_pic);
        Glide.with(context).load(itemData.get("picture")).apply(options).into(holder.item_gallery_img);

    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    @Override
    public void onClick(final View v) {
        if (mOnItemClickListener != null) {
            mOnItemClickListener.onItemClick(v, (int) v.getTag());
        }
    }

    /**
     * @author chensuilun
     */
    protected static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView text;
        public ImageView item_gallery_img;

        public ViewHolder(View itemView) {
            super(itemView);
            text = itemView.findViewById(R.id.item_gallery_title);
            item_gallery_img = itemView.findViewById(R.id.item_gallery_img);
        }
    }

    /**
     * @author chensuilun
     */
    public interface OnItemClickListener {

        void onItemClick(View view, int position);

    }
}
