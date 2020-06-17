package com.colman.fit_me.viewmodel;

import android.net.Uri;

import androidx.lifecycle.ViewModel;

import com.colman.fit_me.model.Recipe;
import com.colman.fit_me.model.RecipeModel;

public class EditRecipeViewModel extends ViewModel {

    public void updateRecipe(Recipe r, RecipeModel.Listener<Boolean> listener) {
        RecipeModel.instance.updateRecipe(r,listener);
    }

    public void deleteRecipe(Recipe r, RecipeModel.Listener<Boolean> listener) {
        RecipeModel.instance.delete(r);
        //RecipeModel.instance.deleteRecipe(r,listener);
    }




    public void uploadImage(Uri uri, RecipeModel.Listener<Uri> listener) {
        RecipeModel.instance.uploadImage(uri,listener);
    }

}
