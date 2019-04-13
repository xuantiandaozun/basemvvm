package com.system.mylibrary.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;

import com.alibaba.android.arouter.launcher.ARouter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jeremyliao.liveeventbus.LiveEventBus;
import com.system.mylibrary.BaseConfig;
import com.system.mylibrary.utils.SPUtils;


import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import butterknife.ButterKnife;
import me.yokeyword.fragmentation.SupportActivity;
import me.yokeyword.fragmentation.anim.DefaultHorizontalAnimator;
import me.yokeyword.fragmentation.anim.FragmentAnimator;

/**
 * 创建人： zhoudingwen
 * 创建时间：2018/4/2
 */

public abstract class BaseActivity<T> extends SupportActivity  {
    public String mtoken;
    public String mUser;
    public String mUserId;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init(savedInstanceState);
        setTheme(getThemeResId());
        setContentView(LayoutInflater.from(this).inflate(
                getLayoutResId(), null));
        //初始化数据
        ButterKnife.bind(this);
        //Arouter注入
        ARouter.getInstance().inject(this);
        initInfo();
        initDatas();
        LiveEventBus.get()
                .with("base_activity", String.class)
                .observe(this, new Observer<String>() {
                    @Override
                    public void onChanged(@Nullable String s) {
                        switch (s) {
                            case "initInfo":
                                initInfo();
                                break;
                        }
                    }
                });
    }

    /**
     * 初始化基本参数
     */
    private void initInfo() {
        String token = BaseConfig.getToken(getThisContext());
        mtoken = token;

        String user = (String) SPUtils.get(getThisContext(), BaseConfig.USER, "");
        mUser = user;

        String userid = (String) SPUtils.get(getThisContext(), BaseConfig.USERID, "");
        mUserId = userid;

    }

    /**
     * 设置主题
     *
     * @return
     */
    protected abstract int getThemeResId();

    /**
     * 返回布局资源ID
     *
     * @return
     */
    protected abstract int getLayoutResId();

    /**
     * 初始化
     */
    protected abstract void init(Bundle savedInstanceState);

    /**
     * 实现功能，填充数据
     */
    protected abstract void initDatas();
    /**
     * 初始化ViewModel
     */
    protected abstract void initViewModel();
    /**
     * 实现功能，填充数据
     * @param myProducts
     */
    protected abstract void dataCallBack(T myProducts);

    /**
     * 订阅数据
     * @param liveData
     */
    protected void subscribeUi(LiveData<T> liveData) {
        liveData.observe(this, new Observer<T>() {
            @Override
            public void onChanged(@Nullable T myProducts) {
                dataCallBack(myProducts);
            }
        });
    }
    /**
     * 获取当前Context
     *
     * @return
     */
    public Context getThisContext() {
        return this;
    }

    @Override
    public FragmentAnimator onCreateFragmentAnimator() {
        return new DefaultHorizontalAnimator();
    }
    /**
     * 根据String获取参数
     *
     * @param params
     * @return
     */
    public HashMap<String, Object> getHashMapByParams(String params) {
        if (TextUtils.isEmpty(params)) {
            return new HashMap<>();
        }
        Type type = new TypeToken<HashMap<String, Object>>() {
        }.getType();
        HashMap<String, Object> filter = new Gson().fromJson(params, type);
        return filter;
    }

    /**
     * 关闭Activity
     */
    public void CloseActivity() {
        finish();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


}
