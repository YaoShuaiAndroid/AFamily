package com.family.afamily.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.family.afamily.R;
import com.family.afamily.activity.base.BaseActivity;
import com.family.afamily.activity.mvp.interfaces.SearchView;
import com.family.afamily.activity.mvp.presents.SearchPresenter;
import com.family.afamily.adapters.SearchAdapter;
import com.family.afamily.adapters.StudyListAdapter;
import com.family.afamily.entity.BasePageBean;
import com.family.afamily.entity.ResponsePageBean;
import com.family.afamily.entity.SearchData;
import com.family.afamily.utils.L;
import com.family.afamily.utils.SPUtils;
import com.family.afamily.utils.Utils;
import com.family.afamily.view.TagLayout;
import com.superrecycleview.superlibrary.adapter.SuperBaseAdapter;
import com.superrecycleview.superlibrary.recycleview.ProgressStyle;
import com.superrecycleview.superlibrary.recycleview.SuperRecyclerView;
import com.superrecycleview.superlibrary.utils.SuperDivider;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by hp2015-7 on 2018/1/15.
 */

public class SearchActivity extends BaseActivity<SearchPresenter> implements SearchView ,SuperRecyclerView.LoadingListener{
    @BindView(R.id.search_img)
    ImageView searchImg;
    @BindView(R.id.search_edit)
    EditText searchEdit;
    @BindView(R.id.frag2_title_search)
    RelativeLayout frag2TitleSearch;
    @BindView(R.id.search_list_rv)
    SuperRecyclerView searchListRv;

    View headView;
    TagLayout head_hot_tag;
    TagLayout head_cy_tag;

    private String token;

    private List<Map<String, String>> list = new ArrayList<>();
    private SearchAdapter adapter;
    private StudyListAdapter studyListAdapter;
    private boolean isZaoJ = true;
    private BasePageBean<Map<String, String>> basePageBean;
    String str = "";
    @Override
    protected void onCreateActivity(Bundle savedInstanceState) {
        setContentView(R.layout.activity_seach);
        token = (String) SPUtils.get(mActivity, "token", "");
        isZaoJ = getIntent().getBooleanExtra("isZaoj",true);
    }

    @Override
    public void netWorkConnected() {

    }

    @Override
    public SearchPresenter initPresenter() {
        return new SearchPresenter(this);
    }

    @OnClick(R.id.search_img)
    public void clickSearch() {
        str = searchEdit.getText().toString();
        if (!TextUtils.isEmpty(str)) {
            presenter.submitData(token, str,isZaoJ,1,1);
        }
    }

    @Override
    public void initDataAsync() {
        super.initDataAsync();
        searchListRv.setLayoutManager(new LinearLayoutManager(this));
        searchListRv.setRefreshEnabled(false);
        searchListRv.setLoadMoreEnabled(false);
        searchListRv.setLoadingListener(this);// 下拉刷新，上拉加载的监听
        searchListRv.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);// 下拉刷新的样式
        searchListRv.setLoadingMoreProgressStyle(ProgressStyle.BallClipRotate);// 上拉加载的样式
        searchListRv.setArrowImageView(R.mipmap.iconfont_downgrey);//
        if (Utils.isConnected(mActivity)) {
            presenter.getData(token,isZaoJ);
        }
        if(isZaoJ) {
            adapter = new SearchAdapter(mActivity, list);
            searchListRv.setAdapter(adapter);
            addHead();
            adapter.setOnItemClickListener(new SuperBaseAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    Map<String, String> map = list.get(position - 1);
                    Intent intent = new Intent(mActivity, ZaoJaoDetailsActivity.class);
                    intent.putExtra("id", map.get("id"));
                    intent.putExtra("study", map.get("look"));
                    startActivity(intent);
                }
            });

        }else{
            searchListRv.addItemDecoration(SuperDivider.newShapeDivider().setColor(R.color.divider_yu_yue).setStartSkipCount(2).setEndSkipCount(1).setSizeDp(10));
            studyListAdapter = new StudyListAdapter(mActivity,list);
            searchListRv.setAdapter(studyListAdapter);
            addHead();

            studyListAdapter.setOnItemClickListener(new SuperBaseAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    Map<String, String> map = list.get(position - 1);
                    String leixing = map.get("leixing");
                    if("玩中学".equals(leixing)){
                        Intent intent = new Intent(mActivity, ProductDetailsActivity.class);
                        intent.putExtra("goods_id", map.get("goods_id"));
                        startActivity(intent);
                    }else{
                        //书房
                        Intent intent = new Intent(mActivity, ProductDetailsActivity.class);
                        intent.putExtra("goods_id", map.get("goods_id"));
                        startActivity(intent);
                    }
                }
            });

        }
        searchEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == KeyEvent.KEYCODE_SEARCH || actionId == EditorInfo.IME_ACTION_SEARCH) {
                    str = searchEdit.getText().toString();
                    if (!TextUtils.isEmpty(str)) {
                        presenter.submitData(token, str,isZaoJ,1,1);
                    }
                }
                return true;
            }
        });

        searchEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    headView.setVisibility(View.VISIBLE);
                    searchListRv.setRefreshEnabled(false);
                    searchListRv.setLoadMoreEnabled(false);
                    list.clear();
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }

    private void addHead() {
        headView = getLayoutInflater().inflate(R.layout.head_search_layout, (ViewGroup) searchListRv.getParent(), false);
        head_hot_tag = headView.findViewById(R.id.head_hot_tag);
        head_cy_tag = headView.findViewById(R.id.head_cy_tag);
        if(isZaoJ) {
            adapter.addHeaderView(headView);
        }else{
            studyListAdapter.addHeaderView(headView);
        }
    }

    @Override
    public void successData(SearchData data) {
        if (data != null) {
            if (data.getHot_word() != null) {
                head_hot_tag.setVisibility(View.VISIBLE);
                head_hot_tag.removeAllViews();
                for (int i = 0; i < data.getHot_word().size(); i++) {
                    final TextView tv = new TextView(this);
                    tv.setText(data.getHot_word().get(i));
                    tv.setTextColor(Color.parseColor("#666666"));
                    tv.setPadding(20, 6, 20, 6);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    params.setMargins(10, 10, 10, 10);
                    tv.setLayoutParams(params);
                    tv.setBackgroundResource(R.drawable.shape_item_lable_bg);
                    head_hot_tag.addView(tv);

                    tv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            str = tv.getText().toString();
                            searchEdit.setText(str);
                            presenter.submitData(token, str,isZaoJ,1,1);
                        }
                    });
                }
                if(isZaoJ) {
                    adapter.notifyDataSetChanged();
                }else{
                    studyListAdapter.notifyDataSetChanged();
                }
            } else {
                head_hot_tag.setVisibility(View.GONE);
            }

            if (data.getSearch_word() != null) {
                head_cy_tag.setVisibility(View.VISIBLE);
                head_cy_tag.removeAllViews();
                for (int i = 0; i < data.getSearch_word().size(); i++) {
                    final TextView tv = new TextView(this);
                    tv.setText(data.getSearch_word().get(i));
                    tv.setTextColor(Color.parseColor("#666666"));
                    tv.setPadding(20, 6, 20, 6);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    params.setMargins(10, 10, 10, 10);
                    tv.setLayoutParams(params);
                    tv.setBackgroundResource(R.drawable.shape_item_lable_bg);
                    head_cy_tag.addView(tv);
                    tv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            str = tv.getText().toString();
                            searchEdit.setText(str);
                            L.e("tag", "------------------------>" + str);
                            presenter.submitData(token, str,isZaoJ,1,1);
                        }
                    });
                }
                if(isZaoJ) {
                    adapter.notifyDataSetChanged();
                }else{
                    studyListAdapter.notifyDataSetChanged();
                }
            } else {
                head_cy_tag.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void searchSuccess(BasePageBean<Map<String, String>> data, int getType) {
        if (data != null) {
//            headView.setVisibility(View.GONE);
//            list.clear();
//            list.addAll(data);
//            adapter.notifyDataSetChanged();
//        } else {
//            toast("无搜索内容");
//        }
            this.basePageBean = data;
            if(basePageBean.getList_data()!=null&&!basePageBean.getList_data().isEmpty()){
                headView.setVisibility(View.GONE);
                searchListRv.setRefreshEnabled(true);
                searchListRv.setLoadMoreEnabled(true);
                if(getType == 1){
                    list.clear();
                }else if(getType == 2){
                    list.clear();
                    searchListRv.completeRefresh();
                }else{
                    searchListRv.completeLoadMore();
                }
                list.addAll(basePageBean.getList_data());
                if(isZaoJ) {
                    adapter.notifyDataSetChanged();
                }else{
                    studyListAdapter.notifyDataSetChanged();
                }
            }else{
                if(getType == 2){
                    searchListRv.completeRefresh();
                }else if(getType == 3){
                    searchListRv.completeLoadMore();
                }
            }
        }else{
            if(getType == 2){
                searchListRv.completeRefresh();
            }else if(getType == 3){
                searchListRv.completeLoadMore();
            }
            toast("无搜索内容");
        }
    }


    @Override
    public void onRefresh() {
        if (Utils.isConnected(mActivity)) {
            presenter.submitData(token, str,isZaoJ,1,2);
        } else {
            searchListRv.completeRefresh();
        }
    }

    @Override
    public void onLoadMore() {
        if (Utils.isConnected(mActivity)) {
            if (basePageBean != null) {
                if (basePageBean.getPage() < basePageBean.getTotle_page()) {
                    presenter.submitData(token, str,isZaoJ,1,3);
                } else {
                    if (basePageBean.getTotle_page() == basePageBean.getPage()) {
                        searchListRv.setNoMore(true);
                    } else {
                        searchListRv.completeLoadMore();
                    }
                }
            }
        } else {
            searchListRv.completeLoadMore();
        }
    }

}
