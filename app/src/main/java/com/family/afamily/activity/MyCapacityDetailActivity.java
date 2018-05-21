package com.family.afamily.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.family.afamily.R;
import com.family.afamily.activity.base.BaseActivity;
import com.family.afamily.activity.mvp.presents.BasePresent;
import com.family.afamily.utils.DownloadUtil;
import com.family.afamily.utils.FileUtile;
import com.family.afamily.utils.L;
import com.family.afamily.utils.PermissionUtils;
import com.joanzapata.pdfview.PDFView;
import com.joanzapata.pdfview.listener.OnDrawListener;
import com.joanzapata.pdfview.listener.OnLoadCompleteListener;
import com.joanzapata.pdfview.listener.OnPageChangeListener;
import com.joanzapata.pdfview.util.FileUtils;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;

public class MyCapacityDetailActivity extends BaseActivity implements OnPageChangeListener, OnLoadCompleteListener, OnDrawListener {
    @BindView(R.id.base_title_right_tv)
    TextView baseTitleRightTv;
    @BindView(R.id.pdfview)
    PDFView mPDFView;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.my_progress_rel)
    RelativeLayout myProgressRel;

    //资源id
    private String id;
    //下载链接
    private String url;
    //外部地址
    private  String fileDirName;

    Handler handler=new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                   /* Uri path = Uri.fromFile(new File(fileDirName));
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(path, "application/pdf");
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);*/


                    Intent intent = new Intent();
                    intent.setAction("android.intent.action.VIEW");
                    intent.addCategory("android.intent.category.DEFAULT");
                    intent.setDataAndType(Uri.fromFile(new File(fileDirName)),
                            "application/pdf");
                    try{
                        startActivity(intent);
                    }catch (Exception e){
                        toast("文件下载至"+fileDirName+",手机外部暂无支持打开此PDF文件的应用");
                    }

                    break;
            }
        }
    };

    public static void start(Context context, String title, String url, String id) {
        Intent intent = new Intent(context, MyCapacityDetailActivity.class);
        intent.putExtra("title", title);
        intent.putExtra("id", id);
        intent.putExtra("url", url);
        context.startActivity(intent);
    }

    @Override
    public void initDataSync() {
        super.initDataSync();

        baseTitleRightTv.setText("下载");

        String fileName = getExternalCacheDir().getPath() + "/" + id + ".pdf";
        File file = new File(fileName);
        //判断是否存在，如果存在直接打开
        if (!file.exists()) {
            PermissionUtils.requestPermission(mActivity, PermissionUtils.CODE_WRITE_EXTERNAL_STORAGE, grantListener);
        } else {
            displayfromfile(file);
        }
    }

    @Override
    public void netWorkConnected() {
    }

    @Override
    public BasePresent initPresenter() {
        return null;
    }

    @Override
    protected void onCreateActivity(Bundle savedInstanceState) {
        setContentView(R.layout.activity_my_capacity_detail);

        String title = getIntent().getStringExtra("title");
        url = getIntent().getStringExtra("url");
        id = getIntent().getStringExtra("id");

        setTitle(mActivity, title);
    }

    @Override
    public void initDataAsync() {
        super.initDataAsync();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        PermissionUtils.requestPermissionsResult(this, requestCode, permissions, grantResults, grantListener);
    }

    @OnClick({R.id.base_title_right_tv})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.base_title_right_tv:
                //下载
                File fileStr=FileUtile.createDir("pdf");

                fileDirName =fileStr.getAbsolutePath() + "/" + id + "_1.pdf";

                Log.i("tag","fileDirName-->"+fileDirName);
                File file = new File(fileDirName);
                //判断是否存在，如果存在直接打开
                if (file.exists()) {
                    Intent intent = new Intent();
                    intent.setAction("android.intent.action.VIEW");
                    intent.addCategory("android.intent.category.DEFAULT");
                    intent.setDataAndType(Uri.fromFile(file),
                            "application/pdf");
                    try {
                        startActivity(intent);
                    } catch (Exception e) {
                        toast("文件下载至"+fileDirName+",手机外部暂无支持打开此PDF文件的应用");
                    }

                    return;
                }

                myProgressRel.setVisibility(View.VISIBLE);
                DownloadUtil.get().download(url, fileStr.getAbsolutePath(), id + "_1.pdf", new DownloadUtil.OnDownloadListener() {
                    @Override
                    public void onDownloadSuccess() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                               handler.sendEmptyMessage(1);

                                myProgressRel.setVisibility(View.GONE);
                            }
                        });
                    }

                    @Override
                    public void onDownloading(int progress) {
                        L.d("progress:" + progress);
                        progressBar.setProgress(progress);
                    }

                    @Override
                    public void onDownloadFailed() {

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                myProgressRel.setVisibility(View.GONE);
                                toast("下载失败");
                            }
                        });

                        /*downloadDialog.dismiss();*/
                    }
                });
                break;
        }
    }

    private final PermissionUtils.PermissionGrantListener grantListener = new PermissionUtils.PermissionGrantListener() {
        @Override
        public void onPermissionGranted(int requestCode) {
            L.d("onPermissionGranted:" + requestCode);
            if (requestCode == PermissionUtils.CODE_WRITE_EXTERNAL_STORAGE) {
                myProgressRel.setVisibility(View.VISIBLE);

                final String fileName = getExternalCacheDir().getPath() + "/" + id + ".pdf";
                DownloadUtil.get().download(url, getExternalCacheDir().getPath(), id + ".pdf", new DownloadUtil.OnDownloadListener() {
                    @Override
                    public void onDownloadSuccess() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                String fileName = getExternalCacheDir().getPath() + "/" + id + ".pdf";
                                //打开下载成功的pdf文件
                                displayfromfile(new File(fileName));

                                myProgressRel.setVisibility(View.GONE);
                            }
                        });
                    }

                    @Override
                    public void onDownloading(int progress) {
                        L.d("progress:" + progress);
                        progressBar.setProgress(progress);
                    }

                    @Override
                    public void onDownloadFailed() {

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                myProgressRel.setVisibility(View.GONE);
                                toast("下载失败");
                            }
                        });

                        /*downloadDialog.dismiss();*/
                    }
                });
            }
        }

        @Override
        public void onPermissionDenied(int requestCode) {
            myProgressRel.setVisibility(View.GONE);
            //Toast.makeText(MainActivity.this,R.string.permission_denied_hint,Toast.LENGTH_SHORT).show();
            //PermissionUtils.openPermissionSettingActivity(MainActivity.this);
        }
    };


    private void displayfromfile(File file) {
        mPDFView.fromFile(file) //设置pdf文件地址
                .defaultPage(1)  //设置默认显示第1页
                .onPageChange(this) //设置翻页监听
                .onLoad(this)  //设置加载监听
                .onDraw(this)  //绘图监听
                .showMinimap(false) //pdf放大的时候，是否在屏幕的右上角生成小地图
                .swipeVertical(false) //pdf文档翻页是否是垂直翻页，默认是左右滑动翻页
                .enableSwipe(true) //是否允许翻页，默认是允许翻
                // .pages( 2 ，5 ) //把2 5 过滤掉
                .load();
    }


    /**
     * 翻页回调
     *
     * @param page
     * @param pagecount
     */

    @Override
    public void onPageChanged(int
                                      page, int
                                      pagecount) {

       /* toast.maketext(mainactivity.this
                , "page= " + page +

                        " pagecount= " + pagecount, toast.length_short).show();*/

    }


    /**
     * 加载完成回调
     *
     * @param nbpages 总共的页数
     */

    @Override
    public void loadComplete(int nbpages) {
       /* toast.maketext(mainactivity.this
                , "加载完成"
                        + nbpages, toast.length_short).show();*/
    }


    @Override
    public void onLayerDrawn(Canvas canvas, float pagewidth, float pageheight, int displayedpage) {

        // toast.maketext( mainactivity.this , "pagewidth= " + pagewidth + "

        // pageheight= " + pageheight + " displayedpage=" + displayedpage , toast.length_short).show();

    }

}
