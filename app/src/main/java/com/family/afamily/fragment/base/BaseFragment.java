package com.family.afamily.fragment.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.family.afamily.activity.mvp.interfaces.BaseView;
import com.family.afamily.activity.mvp.presents.BasePresent;
import com.family.afamily.utils.DialogLoading;
import com.family.afamily.utils.Utils;

/**
 * Created by hp2015-7 on 2017/12/18.
 */

public abstract class BaseFragment<P extends BasePresent> extends Fragment implements BaseView {
    private DialogLoading dialogLoading;
    private Activity mActivity;
    protected Bundle mBundle;
    protected P presenter;

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mBundle != null) {
            outState.putBundle("bundle", mBundle);
        }
    }

    /**
     * 绑定activity
     *
     * @param context
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    /**
     * 运行在onAttach之后
     * 可以接受别人传递过来的参数,实例化对象.
     *
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //获取bundle,并保存起来
        if (savedInstanceState != null) {
            mBundle = savedInstanceState.getBundle("bundle");
        } else {
            mBundle = getArguments() == null ? new Bundle() : getArguments();
        }
        dialogLoading = new DialogLoading(getActivity());
        mActivity = getActivity();
        //创建presenter
        presenter = initPresenter();
    }

    /**
     * 运行在onCreate之后
     * 生成view视图
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return initView(inflater, container, savedInstanceState);
    }

    /**
     * 运行在onCreateView之后
     * 加载数据
     */
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //由于fragment生命周期比较复杂,所以Presenter在onCreateView创建视图之后再进行绑定,不然会报空指针异常
//        if(presenter!=null){
//            presenter.attach(this);
//        }

    }

    @Override
    public void onDestroyView() {
        if (presenter != null) {
            presenter.detach();
        }
        super.onDestroyView();
    }


    /**
     * @param tofragment 跳转的fragment
     * @param tag        fragment的标签
     */
    public void startFragment(Fragment tofragment, String tag) {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.hide(this).add(android.R.id.content, tofragment, tag);
        fragmentTransaction.addToBackStack(tag);
        fragmentTransaction.commitAllowingStateLoss();
    }

    /**
     * 类似Activity的OnBackgress
     * fragment进行回退
     */
    public void onBack() {
        getFragmentManager().popBackStack();
    }

    /**
     * 初始化Fragment应有的视图
     *
     * @return
     */
    public abstract View initView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState);

    /**
     * 创建prensenter
     *
     * @return <T extends BasePresenter> 必须是BasePresenter的子类
     */
    public abstract P initPresenter();


    @Override
    public void showProgress(int showType) {
        if (dialogLoading != null) {
            if (showType == 1) {
                dialogLoading.setDialogContext("正在登录中....");
            } else if (showType == 2) {
                dialogLoading.setDialogContext("正在提交....");
            } else {
                dialogLoading.setDialogContext("正在获取....");
            }
        }
    }

    @Override
    public void hideProgress() {
        if (dialogLoading != null) {
            dialogLoading.closeDialog();
        }
    }

    @Override
    public void toast(CharSequence s) {
        Utils.showMToast(mActivity, s);
    }
}
