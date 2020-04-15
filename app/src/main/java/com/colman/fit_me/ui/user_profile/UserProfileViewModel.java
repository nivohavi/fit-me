package com.colman.fit_me.ui.user_profile;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class UserProfileViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public UserProfileViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is User Profile fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}