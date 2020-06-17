package com.colman.fit_me.viewmodel;

import android.net.Uri;

import androidx.lifecycle.ViewModel;

import com.colman.fit_me.model.Recipe;
import com.colman.fit_me.model.RecipeModel;

public class NewRecipeViewModel extends ViewModel {

    public void addRecipe(Recipe r, RecipeModel.Listener<Boolean> listener) {
        RecipeModel.instance.addRecipe(r,listener);
    }

    public void newRecipeId(RecipeModel.Listener<String> listener) {
        RecipeModel.instance.newRecipeId(listener);
    }

    public void uploadImage(Uri uri, RecipeModel.Listener<Uri> listener) {
        RecipeModel.instance.uploadImage(uri,listener);
    }

}
