package com.colman.fit_me.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.colman.fit_me.model.Recipe;
import com.colman.fit_me.model.RecipeModel;

import java.util.List;

public class RecipeListViewModel extends ViewModel {
    LiveData<List<Recipe>> liveData;

    public LiveData<List<Recipe>> getData() {
        if (liveData == null) {
            liveData = RecipeModel.instance.getAllRecipes();
        }
        return liveData;
    }

    public LiveData<List<Recipe>> getUserData(String email) {
        if (liveData == null) {
            liveData = RecipeModel.instance.getAllRecipesByUser(email);
        }
        return liveData;
    }


    public void refresh(RecipeModel.CompListener listener) {
        RecipeModel.instance.refreshRecipesList(listener);
    }
}
