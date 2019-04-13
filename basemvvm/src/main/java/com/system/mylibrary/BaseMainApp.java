package com.system.mylibrary;

import android.content.Context;


import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;

/**
 * 创建人： zhoudingwen
 * 创建时间：2018/4/2
 */

public class BaseMainApp extends MultiDexApplication {
    private static  BaseMainApp mainApp;
    @Override
    public void onCreate() {
        super.onCreate();
        mainApp=this;
    }

    public static Context getContext(){
        return mainApp;
    }
}
