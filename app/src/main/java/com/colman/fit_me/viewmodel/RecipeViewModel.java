package com.colman.fit_me.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.colman.fit_me.model.Recipe;
import com.colman.fit_me.sql.RecipeRepository;

import java.util.List;

public class RecipeViewModel extends AndroidViewModel {

    private RecipeRepository mRepository;
    private LiveData<List<Recipe>> mAllRecipes;

    public RecipeViewModel (){
        super(null);
    }

    public RecipeViewModel (Application application)
    {
        super(application);
        mRepository = new RecipeRepository(application);
        mAllRecipes = mRepository.getAllRecipes();
    }

    public LiveData<List<Recipe>> getAllRecipes()
    {
        return mAllRecipes;
    }

    public void insert(Recipe recipe)
    {
        mRepository.insert(recipe);
    }
}
