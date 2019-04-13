package com.system.mylibrary.base;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.android.arouter.launcher.ARouter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jeremyliao.liveeventbus.LiveEventBus;
import com.system.mylibrary.BaseConfig;
import com.system.mylibrary.utils.SPUtils;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import me.yokeyword.fragmentation.SupportFragment;

/**
 * 创建人： zhoudingwen
 * 创建时间：2018/4/2
 */

public abstract class BaseFragment<T> extends SupportFragment {
    /*分页相关*/
    /* 每页最大分页数量 */
    public int mPageSize = 20;
    public static final int mMaxPageSize = Integer.MAX_VALUE;
    public static final int mNormalPageSize = 20;
    /* 当前页码 */
    public int mPageIndex = 1;
    public View rootView;

    public String mtoken;
    public String mUser;
    public String mUserId;
    private Unbinder unbinder;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Arouter注入
        ARouter.getInstance().inject(this);
        init();
        initInfo();
        LiveEventBus.get()
                .with("base_fragment", String.class)
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
        String token = BaseConfig.getToken(getContext());

        mtoken = token;

        String user = (String) SPUtils.get(getThisContext(), BaseConfig.USER, "");

        mUser = user;

        String userid = (String) SPUtils.get(getThisContext(), BaseConfig.USERID, "");

        mUserId = userid;

    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(getLayoutResId(), container, false);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initViewModel();
        initDatas();
    }
    /**
     * 初始化
     */
    protected abstract void init();

    /**
     * 返回布局资源ID
     *
     * @return
     */
    protected abstract int getLayoutResId();
    /**
     * 初始化ViewModel
     */
    protected abstract void initViewModel();

    /**
     * 实现功能，填充数据
     */
    protected abstract void initDatas();
    /**
     * 实现功能，填充数据
     * @param myProducts
     */
    protected abstract void dataCallBack(T myProducts);

    /**
     * 获取context
     *
     * @return
     */
    public Context getThisContext() {
        return getActivity();
    }

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
     * 关闭Fragment
     */
    public void closeFragment() {
        Bundle bundle = new Bundle();
        setFragmentResult(RESULT_OK, bundle);
        pop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
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
}
