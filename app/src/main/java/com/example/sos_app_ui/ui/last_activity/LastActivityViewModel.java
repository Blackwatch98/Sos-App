package com.example.sos_app_ui.ui.last_activity;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class LastActivityViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public LastActivityViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is yor last activity");
    }

    public LiveData<String> getText() {
        return mText;
    }
}