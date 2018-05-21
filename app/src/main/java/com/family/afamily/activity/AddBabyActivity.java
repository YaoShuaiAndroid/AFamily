package com.family.afamily.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.family.afamily.R;
import com.family.afamily.activity.base.BaseActivity;
import com.family.afamily.activity.mvp.interfaces.SubmitSuccessView;
import com.family.afamily.activity.mvp.presents.AddBabyPresenter;
import com.family.afamily.utils.GlideCircleTransform;
import com.family.afamily.utils.Log;
import com.family.afamily.utils.SPUtils;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;

import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by hp2015-7 on 2017/12/13.
 */

public class AddBabyActivity extends BaseActivity<AddBabyPresenter> implements SubmitSuccessView {
    @BindView(R.id.img_head_register)
    ImageView imgHeadRegister;
    @BindView(R.id.add_baby_edit_nick)
    EditText addBabyEditNick;
    @BindView(R.id.add_baby_data)
    TextView addBabyData;
    @BindView(R.id.btn_submit)
    TextView btnSubmit;
    private String path;
    private String token;
    private boolean isDetailsIn;
    private String headImage, birthday, nickname, id;

    @Override
    protected void onCreateActivity(Bundle savedInstanceState) {
        setContentView(R.layout.activity_add_baby);
        token = (String) SPUtils.get(mActivity, "token", "");
        isDetailsIn = getIntent().getBooleanExtra("isDetailsIn", false);
        headImage = getIntent().getStringExtra("headImage");
        birthday = getIntent().getStringExtra("birthday");
        nickname = getIntent().getStringExtra("nickname");
        id = getIntent().getStringExtra("id");
    }

    @Override
    public void netWorkConnected() {
    }

    @Override
    public AddBabyPresenter initPresenter() {
        return new AddBabyPresenter(this);
    }

    @Override
    public void initDataAsync() {
        super.initDataAsync();

        if (TextUtils.isEmpty(id)) {
            setTitle(this, "添加宝宝");
        } else {
            setTitle(this, "修改宝宝信息");
            RequestOptions options = new RequestOptions()
                    .centerCrop()
                    .error(R.mipmap.tx)
                    .transform(new GlideCircleTransform(mActivity));
            Glide.with(mActivity).load(headImage).apply(options).into(imgHeadRegister);
            addBabyEditNick.setText(nickname);
            addBabyData.setText(birthday);
        }
    }

    @OnClick(R.id.btn_submit)
    public void clickSubmit() {
        String nick = addBabyEditNick.getText().toString();
        String date = addBabyData.getText().toString();
        if (TextUtils.isEmpty(id)) {
            if (TextUtils.isEmpty(nick)) {
                toast("请输入昵称");
            } else if (TextUtils.isEmpty(date)) {
                toast("请选择时间");
            } else if (TextUtils.isEmpty(path)) {
                toast("请选择宝宝头像");
            } else {
                presenter.submitData(token, "", 2, nick, date, path);
            }
        } else {
            if (TextUtils.isEmpty(nick)) {
                toast("请输入昵称");
            } else if (TextUtils.isEmpty(date)) {
                toast("请选择时间");
            } else if (TextUtils.isEmpty(path)) {
                boolean isOK = false;
                if (!nick.equals(nickname)) {
                    isOK = true;
                }
                if (!date.equals(birthday)) {
                    isOK = true;
                }
                if (!isOK) {
                    toast("请修改后在提交");
                } else {
                    presenter.submitData(token, id, 1, nick, date, path);
                }
            } else {
                presenter.submitData(token, id, 2, nick, date, path);
            }
        }
    }

    @OnClick(R.id.img_head_register)
    public void clickHead() {
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
                .withAspectRatio(1, 1)// int 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
                // .hideBottomControls()// 是否显示uCrop工具栏，默认不显示 true or false
                // .isGif()// 是否显示gif图片 true or false
                // .compressSavePath(getPath())//压缩图片保存地址
                .freeStyleCropEnabled(true)// 裁剪框是否可拖拽 true or false
                .circleDimmedLayer(true)// 是否圆形裁剪 true or false
                .showCropFrame(false)// 是否显示裁剪矩形边框 圆形裁剪时建议设为false   true or false
                .showCropGrid(false)// 是否显示裁剪矩形网格 圆形裁剪时建议设为false    true or false
                .minimumCompressSize(100)// 小于100kb的图片不压缩
                .synOrAsy(true)//同步true或异步false 压缩 默认同步
                .scaleEnabled(true)// 裁剪是否可放大缩小图片 true or false
                .cropWH(150, 150)
                .forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code
    }

    @OnClick(R.id.add_baby_data)
    public void clickDate() {
        presenter.showDateDialog(addBabyData);
    }

    @Override
    public void submitSuccess(List<Map<String, String>> mapList) {
        setResult(100);
        finish();
        if (!isDetailsIn) {
           // startActivity(BabyDetailsActivity.class);
            startActivity(new Intent(mActivity, BabyChartActivity.class));
        }
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
                        RequestOptions options = new RequestOptions()
                                .centerCrop()
                                .transform(new GlideCircleTransform(mActivity));
                        Glide.with(mActivity).load(path).apply(options).into(imgHeadRegister);

                    }
                    break;
            }
        }
    }
}
