package com.example.sos_app_ui.ui.current_activity;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CurrentActivityViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public CurrentActivityViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Check your activity!");
    }

    public LiveData<String> getText() {
        return mText;
    }
}