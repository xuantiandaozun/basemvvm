package com.system.mylibrary.requestlistener;


import android.content.Intent;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import com.jakewharton.retrofit2.adapter.rxjava2.HttpException;
import com.orhanobut.logger.Logger;
import com.system.mylibrary.BaseMainApp;

import org.json.JSONException;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;

import androidx.lifecycle.MediatorLiveData;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * 创建人： zhoudingwen
 * 创建时间：2018/5/3
 * @author 18081
 */

public  class BaseHttpSubscriber implements Observer {
    private final String type;
    private MediatorLiveData liveData;

    public BaseHttpSubscriber(MediatorLiveData liveData, String type) {
        this.liveData = liveData;
        this.type=type;
    }


    @Override
    public void onSubscribe(Disposable d) {

    }


    @Override
    public void onNext(Object t) {
        Gson gson = new Gson();
        String json = gson.toJson(t);
        if(json.contains("登录过期")){
            Intent intent = new Intent("com.system.main");
            intent.putExtra("name","logout");
            BaseMainApp.getContext().sendBroadcast(intent);
        }else {
            JsonObject jsonObject = gson.fromJson(json, JsonObject.class);
            jsonObject.addProperty("key",type);
            liveData.setValue(jsonObject);
        }

    }

    @Override
    public void onError(Throwable e) {
        String erroContent =null;
        if (e instanceof HttpException) {
            HttpException httpException = (HttpException) e;
            int code = httpException.code();
            try {
                String string = httpException.response().errorBody().string();
                Logger.e(string);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            if (code == 500 || code == 404) {
                erroContent = "服务器出错";
            }else {
                erroContent = "网络异常";
            }
        }else if(e instanceof JsonSyntaxException |e instanceof JsonParseException |e instanceof JSONException){
            erroContent = "json解析异常";
        } else if (e instanceof ConnectException) {
            erroContent = "网络断开,请打开网络!";
        } else if (e instanceof SocketTimeoutException) {
            erroContent = "网络连接超时!!";
        } else {
            erroContent = "发生未知错误";
        }
        Logger.e(erroContent);


    }

    @Override
    public void onComplete() {

    }
}
