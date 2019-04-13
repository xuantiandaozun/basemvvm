package com.system.mylibrary.base;


import com.system.mylibrary.requestlistener.BaseHttpSubscriber;

import androidx.lifecycle.MediatorLiveData;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Repository基类
 *
 * @author weishixiong
 * @Time 2018-03-30
 */

public class BaseRepository {
    public MediatorLiveData liveData;
    /**
     * RxJava Subscriber回调
     */
    private BaseHttpSubscriber baseHttpSubscriber;
    /**
     * 初始化
     * commonHttpSubscriber = new Common
     */
    public BaseRepository() {
        liveData = new MediatorLiveData<>();
    }

    public MediatorLiveData getLiveData() {
        return liveData;
    }

}
