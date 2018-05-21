package com.family.afamily.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.family.afamily.R;
import com.family.afamily.activity.base.BaseActivity;
import com.family.afamily.activity.detection.DetectListActivity;
import com.family.afamily.activity.mvp.interfaces.AddBodyView;
import com.family.afamily.activity.mvp.presents.AddBabyPresenter;
import com.family.afamily.activity.mvp.presents.AddbodyPresenter;
import com.family.afamily.activity.mvp.presents.BasePresent;
import com.family.afamily.entity.BodyModel;
import com.family.afamily.utils.AppUtil;
import com.family.afamily.utils.GlideCircleTransform;
import com.family.afamily.utils.SPUtils;
import com.family.afamily.view.DateDialog;
import com.family.afamily.view.GlideRoundImage;
import com.family.afamily.view.HeightDialog;
import com.family.afamily.view.WeightDialog;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.luck.picture.lib.config.PictureMimeType.ofImage;

public class AddBodyActivity extends BaseActivity<AddbodyPresenter> implements AddBodyView {
    private final static int STATUS_DATE=11;

    @BindView(R.id.base_title_tv)
    TextView baseTitleTv;
    @BindView(R.id.add_body_img)
    ImageView addBodyImg;
    @BindView(R.id.body_girl_img)
    ImageView bodyGirlImg;
    @BindView(R.id.body_boy_img)
    ImageView bodyBoyImg;
    @BindView(R.id.add_body_name)
    EditText addBodyName;
    @BindView(R.id.body_bir_text)
    TextView bodyBirText;

    private int selectSex=1;
    //头像信息
    private String path;
    //图片是否修改，为1表示修改
    private int type=0;

    private BodyModel bodyModel;

    public static void start(Context context) {
        Intent intent = new Intent(context, AddBodyActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void netWorkConnected() {
    }
    /**
     * 添加宝宝信息
     */
    public void submitData(String nickname,String birthday) {
        String token = (String) SPUtils.get(mActivity, "token", "");

        if (TextUtils.isEmpty(token)) {
            Intent intent = new Intent(mActivity, LoginActivity.class);
            startActivity(intent);
        }

        if (AppUtil.checkNetWork(mActivity)) {
            presenter.submitData(token,  ""+selectSex,  nickname,  birthday, path);
        } else {
            toast("网络异常");
        }
    }

    /**
     * 修改宝宝信息
     */
    public void submitData(String nickname,String birthday,String bid) {
        String token = (String) SPUtils.get(mActivity, "token", "");

        if (TextUtils.isEmpty(token)) {
            Intent intent = new Intent(mActivity, LoginActivity.class);
            startActivity(intent);
        }

        bodyModel.setBirthday(birthday);
        bodyModel.setNickname(nickname);
        bodyModel.setSex(""+selectSex);

        if (AppUtil.checkNetWork(mActivity)) {
            presenter.submitEditData(token,  bodyModel, path,type);
        } else {
            toast("网络异常");
        }
    }

    @Override
    public AddbodyPresenter initPresenter() {
        return new AddbodyPresenter(this);
    }

    @Override
    protected void onCreateActivity(Bundle savedInstanceState) {
        setContentView(R.layout.activity_add_body);

        bodyModel=getIntent().getParcelableExtra("bodyModel");
    }

    @Override
    public void initDataAsync() {
        super.initDataAsync();
        if(bodyModel!=null){
            setTitle(mActivity,"修改孩子信息");
            setData();
        }else{
            setTitle(mActivity,"填写孩子信息");
        }
    }

    @OnClick({R.id.body_bir_text,R.id.add_body_img,
            R.id.body_boy_lin,R.id.body_girl_lin,R.id.add_body_commit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
           /* case R.id.body_height_text:
                new HeightDialog(mActivity, bodyHeightText, handler, bodyHeightText.getText().toString());
                break;
            case R.id.body_weight_text:
                new WeightDialog(mActivity, bodyHeightText, handler, bodyWeightText.getText().toString());
                break;*/
            case R.id.body_bir_text:
                String date=bodyBirText.getText().toString();
                if(TextUtils.isEmpty(date)){
                    date=AppUtil.getStrTime();
                }

                new DateDialog((Activity) mActivity,addBodyName,date,handler,"选择宝宝生日");
                break;
            case R.id.add_body_img:
                clickHead();
                break;
            case R.id.body_boy_lin:
                //点击小王子
                selectSex=1;
                bodyGirlImg.setBackgroundResource(R.mipmap.select_false);
                bodyBoyImg.setBackgroundResource(R.mipmap.select_true);
                break;
            case R.id.body_girl_lin:
                //点击小公举
                selectSex=0;
                bodyGirlImg.setBackgroundResource(R.mipmap.select_true);
                bodyBoyImg.setBackgroundResource(R.mipmap.select_false);
                break;
            case R.id.add_body_commit:
                String name=addBodyName.getText().toString();
                String bir=bodyBirText.getText().toString();

                if(TextUtils.isEmpty(name)){
                    toast("请填写宝宝姓名");
                    return;
                }
                if(TextUtils.isEmpty(bir)){
                    toast("请填写宝宝生日");
                    return;
                }

                if(TextUtils.isEmpty(path)){
                    toast("请选择头像");
                    return;
                }
                if(bodyModel!=null){
                    submitData(  name, bir,bodyModel.getId());
                }else{
                    submitData(name, bir);
                }
                break;
        }
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
               /* case 1:
                    String weigth = (String) msg.obj;
                    bodyWeightText.setText(weigth);
                    break;
                case 2:
                    String height = (String) msg.obj;
                    bodyHeightText.setText(height);
                    break;*/
                case STATUS_DATE:
                    String date = (String) msg.obj;
                    bodyBirText.setText(date);
                    break;
            }
        }
    };


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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case PictureConfig.CHOOSE_REQUEST:
                if(resultCode!=RESULT_OK){
                    return;
                }
                // 图片选择结果回调
                List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
                if (selectList != null && selectList.size() > 0) {
                    LocalMedia media = selectList.get(0);
                    if (media.isCompressed()) {
                        path = media.getCompressPath();
                    } else if (media.isCut()) {
                        path = media.getCutPath();
                    } else {
                        path = media.getPath();
                    }

                    type=1;

                    RequestOptions options = new RequestOptions()
                            .centerCrop()
                            .placeholder(R.mipmap.no_img)
                            .transform(new GlideCircleTransform(mActivity));

                    Glide.with(mActivity)
                            .load(path)
                            .apply(options)
                            .into(addBodyImg);
                }
                break;
        }
    }

    @Override
    public void submitSuccess(BodyModel bodyModel) {
        Bundle bundle=new Bundle();
        bundle.putParcelable("body", (Parcelable) bodyModel);
        Intent intent=new Intent(mActivity, DetectListActivity.class);
        intent.putExtras(bundle);
        setResult(100,intent);

        finish();
    }

    public void setData(){
        addBodyName.setText(bodyModel.getNickname());
        if(bodyModel.getNickname().length()>5){
            addBodyName.setSelection(5);
        }else{
            addBodyName.setSelection(bodyModel.getNickname().length());
        }
        bodyBirText.setText(bodyModel.getBirthday().contains("-")?bodyModel.getBirthday():AppUtil.getStrTime(bodyModel.getBirthday()));
        if(bodyModel.getSex()!=null&&(bodyModel.getSex().equals("男")||bodyModel.getSex().equals("1"))){
            selectSex=1;

            bodyGirlImg.setBackgroundResource(R.mipmap.select_false);
            bodyBoyImg.setBackgroundResource(R.mipmap.select_true);
        }else{
            selectSex=0;

            bodyGirlImg.setBackgroundResource(R.mipmap.select_true);
            bodyBoyImg.setBackgroundResource(R.mipmap.select_false);
        }

        path=bodyModel.getIcon();

        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.mipmap.no_img)
                .transform(new GlideCircleTransform(mActivity));

        Glide.with(mActivity)
                .load(path)
                .apply(options)
                .into(addBodyImg);
    }
}
