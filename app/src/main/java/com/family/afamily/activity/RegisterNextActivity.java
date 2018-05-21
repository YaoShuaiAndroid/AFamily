package com.family.afamily.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.family.afamily.R;
import com.family.afamily.activity.base.BaseActivity;
import com.family.afamily.activity.mvp.interfaces.RegisterView;
import com.family.afamily.activity.mvp.presents.RegisterNextPresenter;
import com.family.afamily.entity.ConfigData;
import com.family.afamily.entity.ResponseBean;
import com.family.afamily.utils.FileUtile;
import com.family.afamily.utils.GlideCircleTransform;
import com.family.afamily.utils.Log;
import com.family.afamily.utils.OkHttpClientManager;
import com.family.afamily.utils.SPUtils;
import com.family.afamily.utils.UrlUtils;
import com.family.afamily.utils.Utils;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.tools.PictureFileUtils;
import com.squareup.okhttp.Request;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by hp2015-7 on 2017/11/29.
 */

public class RegisterNextActivity extends BaseActivity<RegisterNextPresenter> implements RegisterView {
    @BindView(R.id.img_head_register)
    ImageView imgHeadRegister;
    @BindView(R.id.edit_nick_register)
    EditText editNickRegister;
    @BindView(R.id.edit_pw_register)
    EditText editPwRegister;
    @BindView(R.id.edit_repw_register)
    EditText editRepwRegister;
    @BindView(R.id.check_register)
    CheckBox checkRegister;
    @BindView(R.id.agreement_register)
    TextView agreementRegister;
    @BindView(R.id.btn_submit)
    TextView btnSubmit;
    private  String path;
    private ConfigData configData;
    private String mobile, tgr;

    @Override
    protected void onCreateActivity(Bundle savedInstanceState) {
        setContentView(R.layout.activity_register_next);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            Utils.getStatusHeight(mActivity, findViewById(R.id.title_forget_pw_ll));
        }

        configData = (ConfigData) Utils.load(FileUtile.configPath(mActivity));
        if(configData == null){
            getConfigData(mActivity);
        }
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mobile = bundle.getString("mobile");
            tgr = bundle.getString("tgr");
        }


    }

    @Override
    public void initDataSync() {
        super.initDataSync();
        agreementRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (configData != null) {
                    Intent intent = new Intent(mActivity, AllWebViewActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("title", "注册协议");
                    bundle.putString("link", configData.getReg_message());
                    intent.putExtras(bundle);
                    startActivity(intent);
                }else{
                    toast("未获取到注册协议,正在重新获取...");
                    getConfigData(mActivity);
                }
            }
        });
    }

    @OnClick(R.id.img_head_register)
    public void clickHeadImg() {
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

    @OnClick(R.id.btn_submit)
    public void clickSubmit() {
        String nick = editNickRegister.getText().toString();
        String pw = editPwRegister.getText().toString();
        String rpw = editRepwRegister.getText().toString();
        boolean isCheck = checkRegister.isChecked();
        if (TextUtils.isEmpty(path)) {
            toast("请选择头像");
        } else if (TextUtils.isEmpty(nick)) {
            toast("请输入昵称");
        } else if (TextUtils.isEmpty(pw)) {
            toast("请输入密码");
        } else if (!Utils.isPassWord(pw)) {
            toast("密码必须为数组字母组合，并且长度为6-20位");
        } else if (TextUtils.isEmpty(rpw)) {
            toast("请输入确认密码");
        } else if (!pw.equals(rpw)) {
            toast("两次密码输入不一致");
            editRepwRegister.setText("");
        } else if (!isCheck) {
            toast("请勾选注册协议");
        } else {
            presenter.submitRegister(mobile, tgr, nick, pw, new File(path));
        }

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
       // imgHeadRegister.setTag("");
        PictureFileUtils.deleteCacheDirFile(mActivity);
    }

    @Override
    public void netWorkConnected() {

    }

    @Override
    public RegisterNextPresenter initPresenter() {
        return new RegisterNextPresenter(this);
    }

    @Override
    public void getCodeFail() {
    }

    @Override
    public void verifySuccess() {
    }

    @Override
    public void registerSuccess(String token) {
        if (TextUtils.isEmpty(token)) {
            toast("服务器异常");
        } else {
            SPUtils.put(mActivity, "token", token);
            startActivity(MainActivity.class);
            setResult(10);
            finish();
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

                    // 例如 LocalMedia 里面返回三种path
                    // 1.media.getPath(); 为原图path
                    // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true
                    // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true
                    // 如果裁剪并压缩了，以取压缩路径为准，因为是先裁剪后压缩的

                    break;
            }
        }
    }


    /**
     * 获取配置信息
     */
    public void getConfigData(final Activity mActivity) {
        Map<String, String> params = new HashMap<>();
        // params.put("token",token);
        params = Utils.getParams(params);
        OkHttpClientManager.postAsyn(UrlUtils.CONFIG_URL, new OkHttpClientManager.ResultCallback<ResponseBean<ConfigData>>() {
            @Override
            public void onError(Request request, Exception e) {
                toast("获取数据失败");
            }

            @Override
            public void onResponse(ResponseBean<ConfigData> response) {
                if (response == null || response.getRet_code() != 1 || response.getData() == null) {
                    toast("获取数据失败");
                } else {
                    toast("获取数据成功");
                    configData = response.getData();
                    Utils.save(response.getData(), FileUtile.configPath(mActivity));
                }
            }
        }, params);
    }

}
