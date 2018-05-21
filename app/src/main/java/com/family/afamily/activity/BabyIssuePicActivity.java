package com.family.afamily.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.family.afamily.R;
import com.family.afamily.activity.base.BaseActivity;
import com.family.afamily.activity.mvp.interfaces.BabyIssueView;
import com.family.afamily.activity.mvp.presents.BabyIssuePresenter;
import com.family.afamily.utils.BaseViewHolder;
import com.family.afamily.utils.Log;
import com.family.afamily.utils.SPUtils;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by hp2015-7 on 2017/12/14.
 */

public class BabyIssuePicActivity extends BaseActivity<BabyIssuePresenter> implements BabyIssueView {
    @BindView(R.id.base_title_right_tv)
    TextView baseTitleRightTv;
    @BindView(R.id.issue_pic_content)
    EditText issuePicContent;
    @BindView(R.id.issue_pic_time)
    TextView issuePicTime;
    @BindView(R.id.issue_pic_time_rl)
    RelativeLayout issuePicTimeRl;
    @BindView(R.id.issue_pic_address)
    TextView issuePicAddress;
    @BindView(R.id.issue_pic_address_rl)
    RelativeLayout issuePicAddressRl;
    @BindView(R.id.issue_pic_list)
    GridView issuePicList;

    private List<String> list = new ArrayList<>();
    private MyAdapter adapder;
    private String id;
    private String token;
    private int address_index = -1;
    private String address_str = "不显示位置";

    @Override
    protected void onCreateActivity(Bundle savedInstanceState) {
        setContentView(R.layout.activity_issue_pic);
        token = (String) SPUtils.get(mActivity, "token", "");
        id = getIntent().getStringExtra("id");
    }

    @Override
    public void netWorkConnected() {

    }

    /**
     * 选择时间
     */
    @OnClick(R.id.issue_pic_time_rl)
    public void clickTime() {
        presenter.showDateDialog(issuePicTime);
    }

    /**
     * 选择地址
     */
    @OnClick(R.id.issue_pic_address_rl)
    public void clickAddress() {
        Intent intent = new Intent(mActivity, NearbySiteActivity.class);
        intent.putExtra("position", address_index);
        intent.putExtra("address", address_str);
        startActivityForResult(intent, 100);
    }

    @Override
    public BabyIssuePresenter initPresenter() {
        return new BabyIssuePresenter(this);
    }

    @Override
    public void initDataAsync() {
        super.initDataAsync();
        setTitle(this, "宝宝新变化");
        baseTitleRightTv.setText("发布");

        baseTitleRightTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = issuePicContent.getText().toString();
                String time = issuePicTime.getText().toString();
                String address = issuePicAddress.getText().toString();
                String addre = TextUtils.isEmpty(address) ? "不显示地址" : address;
                if (TextUtils.isEmpty(title)) {
                    toast("请输入图片描述");
                } else if (TextUtils.isEmpty(time)) {
                    toast("请选择图片拍摄时间");
                } else if (list.size() < 2) {
                    toast("请选择一张图片");
                } else {
                    File[] files = new File[list.size() - 1];
                    String[] fileKeys = new String[list.size() - 1];
                    for (int i = 1; i < list.size(); i++) {
                        files[i - 1] = new File(list.get(i));
                        fileKeys[i - 1] = "avatar" + i;
                    }
                    presenter.submitPic(token, id, title, time, addre, files, fileKeys);
                }
            }
        });
        list.add("add");
        adapder = new MyAdapter(list);
        issuePicList.setAdapter(adapder);
    }

    @Override
    public void successData() {
        setResult(100);
        finish();
    }

    class MyAdapter extends BaseAdapter {
        private List<String> data = new ArrayList<>();

        public MyAdapter(List<String> data) {
            this.data = data;
        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View view, ViewGroup parent) {
            if (view == null) {
                view = LayoutInflater.from(mActivity).inflate(R.layout.item_add_pic, null);
            }
            LinearLayout item_add_pic = BaseViewHolder.get(view, R.id.item_add_pic);
            RelativeLayout item_add_pic_rl = BaseViewHolder.get(view, R.id.item_add_pic_rl);
            ImageView item_add_pic_iv = BaseViewHolder.get(view, R.id.item_add_pic_iv);
            ImageView item_add_pic_c = BaseViewHolder.get(view, R.id.item_add_pic_c);

            if (position == 0) {
                item_add_pic.setVisibility(View.VISIBLE);
                item_add_pic_rl.setVisibility(View.GONE);
                item_add_pic.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (data.size() == 10) {
                            toast("最多可选9张图片");
                        } else {
                            getPic();
                        }
                    }
                });
            } else {
                item_add_pic.setVisibility(View.GONE);
                item_add_pic_rl.setVisibility(View.VISIBLE);
                Glide.with(mActivity).load(data.get(position)).into(item_add_pic_iv);
                item_add_pic_c.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        list.remove(position);
                        adapder.notifyDataSetChanged();
                    }
                });
            }
            return view;
        }
    }

    public void getPic() {
        // 进入相册 以下是例子：用不到的api可以不写
        PictureSelector.create(mActivity)
                .openGallery(PictureMimeType.ofImage())//全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                //.theme()//主题样式(不设置为默认样式) 也可参考demo values/styles下 例如：R.style.picture.white.style
                .maxSelectNum(10 - list.size())// 最大图片选择数量 int
                .minSelectNum(1)// 最小选择数量 int
                .imageSpanCount(4)// 每行显示个数 int
                .selectionMode(PictureConfig.MULTIPLE)// 多选 or 单选 PictureConfig.MULTIPLE or PictureConfig.SINGLE
                .previewImage(true)// 是否可预览图片 true or false
                .isCamera(true)// 是否显示拍照按钮 true or false
                .imageFormat(PictureMimeType.JPEG)// 拍照保存图片格式后缀,默认jpeg
                .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
                .sizeMultiplier(0.5f)// glide 加载图片大小 0~1之间 如设置 .glideOverride()无效
                //.setOutputCameraPath("/CustomPath")// 自定义拍照保存路径,可不填
                .enableCrop(false)// 是否裁剪 true or false
                .compress(true)
                .minimumCompressSize(100)// 小于100kb的图片不压缩
                .synOrAsy(true)//同步true或异步false 压缩 默认同步
                .forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    // 图片选择结果回调
                    List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
                    if (selectList != null) {
                        for (int i = 0; i < selectList.size(); i++) {
                            LocalMedia media = selectList.get(i);
                            String path;
                            if (media.isCompressed()) {
                                Log.e(media.getCompressPath());
                                path = media.getCompressPath();
                            } else if (media.isCut()) {
                                Log.e(media.getCutPath());
                                path = media.getCutPath();
                            } else {
                                Log.e(media.getPath());
                                path = media.getPath();
                            }
                            list.add(list.size(), path);
                        }
                        adapder.notifyDataSetChanged();
                    }
                    break;
            }
        } else if (requestCode == 100 && resultCode == 100) {
            if (data != null) {
                address_index = data.getIntExtra("position", -1);
                address_str = data.getStringExtra("address");
                issuePicAddress.setText(address_str);
            }
        }
    }

}
