package com.colman.fit_me.firebase;

import com.colman.fit_me.model.Recipe;

public interface IDataSource {
    void insertRecipes(Recipe... recipes);
    void updateRecipes(Recipe... recipes);
    void deleteRecipes(Recipe... recipes);
    void deleteAllRecipes();
}
