package com.example.go4lunchAlx.ui.mates;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MatesViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public MatesViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}