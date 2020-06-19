package com.colman.fit_me.viewmodel;

import com.colman.fit_me.model.Recipe;
import com.colman.fit_me.model.RecipeModel;

public class UserViewModel {

    public void updateRecipe(Recipe r, RecipeModel.Listener<Boolean> listener) {
        RecipeModel.instance.updateRecipe(r,listener);
    }
}
