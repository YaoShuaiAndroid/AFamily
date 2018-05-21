package com.family.afamily.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.family.afamily.R;
import com.family.afamily.activity.base.BaseActivity;
import com.family.afamily.activity.mvp.presents.BasePresent;
import com.family.afamily.utils.Log;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by hp2015-7 on 2017/12/8.
 */

public class ActionMoreActivity extends BaseActivity {
    @BindView(R.id.base_title_right_tv)
    TextView baseTitleRightTv;
    @BindView(R.id.act_more_bg)
    ImageView actMoreBg;
    @BindView(R.id.act_more_add_pic)
    LinearLayout actMoreAddPic;
    @BindView(R.id.act_more_decs_et)
    EditText actMoreDecsEt;

    @Override
    protected void onCreateActivity(Bundle savedInstanceState) {
        setContentView(R.layout.activity_action_more);
    }

    @Override
    public void netWorkConnected() {
    }

    @OnClick(R.id.act_more_add_pic)
    public void clickAddpic() {
        getPic();
    }

    @OnClick(R.id.act_more_bg)
    public void clickImgBg() {
        if (!actMoreAddPic.isShown()) {
            getPic();
        }
    }


    @Override
    public BasePresent initPresenter() {
        return null;
    }

    @Override
    public void initDataAsync() {
        super.initDataAsync();
        setTitle(this, "更多");
        baseTitleRightTv.setText("提交");

        baseTitleRightTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String path = (String) actMoreAddPic.getTag();
                String decs = actMoreDecsEt.getText().toString();
                if (TextUtils.isEmpty(path)) {
                    toast("请选择封面图");
                } else if (TextUtils.isEmpty(decs)) {
                    toast("请输入活动说明");
                } else {
                    Intent intent = new Intent();
                    intent.putExtra("path", path);
                    intent.putExtra("decs", decs);
                    setResult(100, intent);
                    finish();
                }
            }
        });
    }

    public void getPic() {
        // 进入相册 以下是例子：用不到的api可以不写
        PictureSelector.create(mActivity)
                .openGallery(PictureMimeType.ofImage())//全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                //.theme()//主题样式(不设置为默认样式) 也可参考demo values/styles下 例如：R.style.picture.white.style
                .maxSelectNum(1)// 最大图片选择数量 int
                .minSelectNum(1)// 最小选择数量 int
                .imageSpanCount(4)// 每行显示个数 int
                .selectionMode(PictureConfig.SINGLE)// 多选 or 单选 PictureConfig.MULTIPLE or PictureConfig.SINGLE
                .previewImage(true)// 是否可预览图片 true or false
                .isCamera(true)// 是否显示拍照按钮 true or false
                .imageFormat(PictureMimeType.JPEG)// 拍照保存图片格式后缀,默认jpeg
                .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
                .sizeMultiplier(0.5f)// glide 加载图片大小 0~1之间 如设置 .glideOverride()无效
                //.setOutputCameraPath("/CustomPath")// 自定义拍照保存路径,可不填
                .enableCrop(true)// 是否裁剪 true or false
                .compress(true)// 是否压缩 true or false
                //.glideOverride()// int glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
                .withAspectRatio(5, 3)// int 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
                // .hideBottomControls()// 是否显示uCrop工具栏，默认不显示 true or false
                // .isGif()// 是否显示gif图片 true or false
                // .compressSavePath(getPath())//压缩图片保存地址
                .freeStyleCropEnabled(true)// 裁剪框是否可拖拽 true or false
                .circleDimmedLayer(false)// 是否圆形裁剪 true or false
                .showCropFrame(false)// 是否显示裁剪矩形边框 圆形裁剪时建议设为false   true or false
                .showCropGrid(false)// 是否显示裁剪矩形网格 圆形裁剪时建议设为false    true or false
                .minimumCompressSize(100)// 小于100kb的图片不压缩
                .synOrAsy(true)//同步true或异步false 压缩 默认同步
                .scaleEnabled(true)// 裁剪是否可放大缩小图片 true or false
                //.cropWH(800,400)
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
                    if (selectList != null && selectList.size() > 0) {
                        LocalMedia media = selectList.get(0);
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
                        Glide.with(mActivity).load(path).into(actMoreBg);
                        //actMoreBg.setTag(path);
                        actMoreAddPic.setTag(path);
                        actMoreAddPic.setVisibility(View.GONE);
                    }
                    break;
            }
        }
    }

}
