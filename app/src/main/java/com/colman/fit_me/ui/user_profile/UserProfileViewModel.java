package com.colman.fit_me.ui.user_profile;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.colman.fit_me.model.Recipe;
import com.colman.fit_me.model.RecipeModel;
import com.colman.fit_me.model.UserModel;
import com.google.firebase.firestore.auth.User;

import java.util.List;

public class UserProfileViewModel extends ViewModel {


    public void signUp(String email, String password, UserModel.Listener<Boolean> listener) {
        UserModel.instance.signUp(email, password,listener);
    }

    public void login(String email, String password, UserModel.Listener<Boolean> listener) {
        UserModel.instance.login(email, password,listener);
    }

    public void logout() {
        UserModel.instance.logout();
    }


    public void getCurrentUserEmail(UserModel.Listener<String> listener) {
        UserModel.instance.getCurrentUserEmail(listener);
    }

    public void isUserLoggedIn(UserModel.Listener<Boolean> listener) {
        UserModel.instance.isUserLoggedIn(listener);
    }

}