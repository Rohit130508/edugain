package com.edugainnow.edugain.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HomeViewModel extends ViewModel {

    private MutableLiveData<String> mText,mText1,mText2;

    public HomeViewModel() {
        mText = new MutableLiveData<>();
        mText1 = new MutableLiveData<>();
        mText2 = new MutableLiveData<>();

        mText.setValue("All Registration List");
        mText1.setValue("New Registration");
        mText2.setValue("Today Registration List");
    }

    public LiveData<String> getText() {
        return mText;
    }
    public LiveData<String> getText1()
    {
        return mText1;
    }public LiveData<String> getText2()
    {
        return mText2;
    }


}