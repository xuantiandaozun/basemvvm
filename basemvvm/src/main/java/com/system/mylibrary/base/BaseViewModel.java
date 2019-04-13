package com.system.mylibrary.base;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MediatorLiveData;

public abstract class BaseViewModel<T,K extends BaseRepository> extends AndroidViewModel {
    public   K mRepository;

    public  MediatorLiveData<T> liveData;


    public BaseViewModel(@NonNull Application application) {
        super(application);
        liveData = new MediatorLiveData<>();

        mRepository = getResitory();

    }

    public MediatorLiveData<T> getLiveData() {
        return liveData;
    }

    protected abstract K getResitory() ;

}
