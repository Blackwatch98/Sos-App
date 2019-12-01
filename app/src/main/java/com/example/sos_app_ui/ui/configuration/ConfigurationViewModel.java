package com.example.sos_app_ui.ui.configuration;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ConfigurationViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public ConfigurationViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Configuration");
    }

    public LiveData<String> getText() {
        return mText;
    }
}