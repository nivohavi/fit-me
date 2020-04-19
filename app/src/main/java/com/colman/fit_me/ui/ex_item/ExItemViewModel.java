package com.colman.fit_me.ui.ex_item;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ExItemViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public ExItemViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is exItem fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}