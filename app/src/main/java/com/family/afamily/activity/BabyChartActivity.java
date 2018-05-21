package com.family.afamily.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.family.afamily.R;
import com.family.afamily.activity.base.BaseActivity;
import com.family.afamily.activity.mvp.interfaces.BabyChartView;
import com.family.afamily.activity.mvp.presents.BabyChartPresenter;
import com.family.afamily.entity.BabyChart;
import com.family.afamily.entity.BabyChartData;
import com.family.afamily.entity.ChartMax;
import com.family.afamily.utils.L;
import com.family.afamily.utils.SPUtils;
import com.family.afamily.utils.Utils;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by hp2015-7 on 2018/3/2.
 */

public class BabyChartActivity extends BaseActivity<BabyChartPresenter> implements BabyChartView {
    @BindView(R.id.base_title_right_tv)
    TextView baseTitleRightTv;
    @BindView(R.id.head_tab1_tv)
    TextView headTab1Tv;
    @BindView(R.id.head_tab1_l)
    TextView headTab1L;
    @BindView(R.id.head_tab2_tv)
    TextView headTab2Tv;
    @BindView(R.id.head_tab2_l)
    TextView headTab2L;
    @BindView(R.id.head_tab3_tv)
    TextView headTab3Tv;
    @BindView(R.id.head_tab3_l)
    TextView headTab3L;
    @BindView(R.id.line_chart1)
    LineChart lineChart1;
    @BindView(R.id.baby_tip_tv)
    TextView babyTipTv;
    @BindView(R.id.chart_y_dw)
    TextView chartDw;
    private String token;
    private int tab_index = 1;
    private BabyChartData babyChartData;
  //  private LineChartManager lineChartManager1;
    @Override
    protected void onCreateActivity(Bundle savedInstanceState) {
        setContentView(R.layout.activity_baby_chart);
        token = (String) SPUtils.get(mActivity,"token","");
    }

    @Override
    public void netWorkConnected() {

    }
    @OnClick(R.id.base_title_right_tv)
    public void clickTitle(){
        if(babyChartData!=null){

            String bir = babyChartData.getBirthday();
            if(TextUtils.isEmpty(bir)){
                toast("没有宝宝生日信息");
            }else if(!bir.contains("-")){
                toast("宝宝生日格式有误，请线下客服修改");
            }else{
                Intent intent = new Intent(mActivity,AddBabyGrowUpActivity.class);
                intent.putExtra("birthday",bir);
                startActivityForResult(intent,100);
            }
        }else{
            toast("未获取到宝宝信息，请稍后再试...");
        }

    }
    @OnClick(R.id.head_tab1_tv)
    public void clickTab1(){
        if(tab_index !=1){
            tab_index = 1;
            initChart();
            chartDw.setText("身高(cm)");
            initHeadView(headTab1Tv,headTab1L);
            presenter.getData(token,tab_index+"");
            babyTipTv.setText("宝宝还没有身高记录\n建议3个月-2岁宝宝每30天量一次身高");
        }
    }
    @OnClick(R.id.head_tab2_tv)
    public void clickTab2(){
        if(tab_index !=2){
            tab_index = 2;
            initChart();
            chartDw.setText("体重(kg)");
            initHeadView(headTab2Tv,headTab2L);
            presenter.getData(token,tab_index+"");
            babyTipTv.setText("宝宝还没有体重记录\n建议3个月-2岁宝宝每30天量一次体重");
        }
    }
    @OnClick(R.id.head_tab3_tv)
    public void clickTab3(){
        if(tab_index !=3){
            tab_index = 3;
            initChart();
            chartDw.setText("头围(cm)");
            initHeadView(headTab3Tv,headTab3L);
            presenter.getData(token,tab_index+"");
            babyTipTv.setText("宝宝还没有头围记录\n建议3个月-2岁宝宝每30天量一次头围");
        }
    }
    private void initHeadView(TextView tv,TextView line){
        headTab1Tv.setTextColor(Color.parseColor("#333333"));
        headTab2Tv.setTextColor(Color.parseColor("#333333"));
        headTab3Tv.setTextColor(Color.parseColor("#333333"));
        headTab1L.setVisibility(View.INVISIBLE);
        headTab2L.setVisibility(View.INVISIBLE);
        headTab3L.setVisibility(View.INVISIBLE);
        tv.setTextColor(ContextCompat.getColor(mActivity,R.color.color_yellow));
        line.setVisibility(View.VISIBLE);
    }

    @Override
    public void initDataSync() {
        super.initDataSync();
        presenter.getData(token,tab_index+"");
       // lineChartManager1 = new LineChartManager(lineChart1);
        initChart();
    }

    @Override
    public BabyChartPresenter initPresenter() {
        return new BabyChartPresenter(this);
    }


    @Override
    public void successData(BabyChartData chartData) {
        if(chartData!=null){
            babyChartData = chartData;
            if(chartData.getMonth()!=null&&!chartData.getMonth().isEmpty()){
                //设置x轴的数据
                List<String> xValues = chartData.getMonth();
                ChartMax max = chartData.getMax();
                float y;
                String labels;
                if(tab_index == 1){
                    y = max.getHeight();
                    labels = "身高";
                    babyTipTv.setText("建议3个月-2岁宝宝每30天量一次身高");
                }else if (tab_index == 2){
                    y = max.getWeight();
                    labels = "体重";
                    babyTipTv.setText("建议3个月-2岁宝宝每30天量一次体重");
                }else{
                    y = max.getHead();
                    labels = "头围";
                    babyTipTv.setText("建议3个月-2岁宝宝每30天量一次头围");
                }
                y += (10-(y%10))+10 ;
                //自定义x轴显示
              //  MyXFormatter formatter = new MyXFormatter(xValues);

                List<BabyChart> yData = chartData.getUser_arr();
               // List<List<Float>> yValues = new ArrayList<>();
                List<Float> yValue = new ArrayList<>();
                for (int i = 0; i <yData.size() ; i++) {
                    if(tab_index == 1){
                        yValue.add(yData.get(i).getHeight());
                    }else if (tab_index == 2){
                        yValue.add(yData.get(i).getWeight());
                    }else{
                        yValue.add(yData.get(i).getHead());
                    }
                }

              //  yValues.add(yValue);
              //  lineChartManager1.showLineChart(yValues.get(0), formatter,labels, Color.CYAN);
                L.e("Tag","------------q----->"+y);

                YAxis leftAxis = lineChart1.getAxisLeft();
                leftAxis.removeAllLimitLines(); // reset all limit lines to avoid overlapping lines
                leftAxis.setAxisMaxValue(y);
                leftAxis.setDrawZeroLine(false);
                leftAxis.setAxisMinValue(0f);
                leftAxis.setLabelCount(5,false);
                leftAxis.setDrawLimitLinesBehindData(true);
                setData(xValues,yValue);
//                lineChartManager1.setYAxis(y, 0, 5);
//                lineChartManager1.setDescription("");
               // lineChart1.animateX(1000, Easing.EasingOption.EaseInOutQuart);
                lineChart1.animateY(1000, Easing.EasingOption.Linear);
                lineChart1.animateX(1000, Easing.EasingOption.Linear);
            }

        }
    }

    private void initChart(){
        setTitle(this,"成长记录");
        lineChart1.removeAllViews();
        lineChart1.setDrawGridBackground(false);
        lineChart1.setDrawBorders(true);
        lineChart1.setDescription("");
        lineChart1.setTouchEnabled(true);
        lineChart1.setDragEnabled(true);
        lineChart1.setScaleEnabled(false);
        lineChart1.setBorderColor(Color.parseColor("#cccccc"));
        lineChart1.setBorderWidth(0.8f);
        lineChart1.setPinchZoom(true);
        XAxis xAxis = lineChart1.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        lineChart1.setViewPortOffsets(Utils.dp2px(23), Utils.dp2px(10),Utils.dp2px(15), Utils.dp2px(30));
        lineChart1.getAxisRight().setEnabled(false);
        lineChart1.fitScreen();
    }


    private void setData(List<String> xVals,List<Float> xValue){

        ArrayList<Entry> yVals = new ArrayList<Entry>();
        for (int i = 0; i < xValue.size(); i++) {
            yVals.add(new Entry(xValue.get(i), i));
        }
        LineDataSet set1 = new LineDataSet(yVals, "");
        set1.setDrawCubic(true);
        set1.setColor(Color.CYAN);
        set1.setCircleColor(Color.CYAN);
        set1.setLineWidth(1f);
        set1.setCircleRadius(3f);
        set1.setDrawCircleHole(false);
        set1.setValueTextSize(9f);
        set1.setDrawFilled(false);

        ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
        dataSets.add(set1); // add the datasets

        LineData data = new LineData(xVals, dataSets);
        float ratio = (float) xVals.size()/(float) 7;
        //显示的时候是按照多大的比率缩放显示,1f表示不放大缩小
        lineChart1.zoom(ratio,1f,0,0);
        // set data
        lineChart1.setData(data);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 100 && resultCode == 100){
            presenter.getData(token,tab_index+"");
        }
    }

}
