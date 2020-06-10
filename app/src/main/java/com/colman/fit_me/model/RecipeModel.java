package com.colman.fit_me.model;
import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.colman.fit_me.MyApplication;
import com.colman.fit_me.sql.RecipeRoomDatabase;

import static android.content.Context.MODE_PRIVATE;

import java.util.Date;
import java.util.List;

public class RecipeModel {
    public static final RecipeModel instance = new RecipeModel();

    public interface Listener<T>{
        void onComplete(T data);
    }
    public interface CompListener{
        void onComplete();
    }
    private RecipeModel(){ }

    public void addRecipe(Recipe recipe,Listener<Boolean> listener) {
        RecipeFirebase.addRecipe(recipe,listener);
    }

    public LiveData<List<Recipe>> getAllRecipes(){
        LiveData<List<Recipe>> liveData = RecipeRoomDatabase.getDatabase(MyApplication.context).recipeDao().getAllRecipes();
        refreshRecipesList(null);
        return liveData;
    }


    public Recipe getRecipe(String id){
        return null;
    }

    public void update(Recipe recipe){

    }

    public void delete(Recipe recipe){

    }

    public void refreshRecipesList(final CompListener listener){
        String last = MyApplication.context.getSharedPreferences("TAG",MODE_PRIVATE).getString("RecipesLastUpdateDate","");
        Date d = new Date(Date.parse(last));
        RecipeFirebase.getAllRecipesSince(d,new Listener<List<Recipe>>() {
            @SuppressLint("StaticFieldLeak")
            @Override
            public void onComplete(final List<Recipe> data) {
                new AsyncTask<String,String,String>(){
                    @Override
                    protected String doInBackground(String... strings) {
                        long lastUpdated = 0;
                        Date d = new Date();
                        for(Recipe recipe : data)
                        {
                            RecipeRoomDatabase.getDatabase(MyApplication.context).recipeDao().insertAll(recipe);
                            //AppLocalDb.db.studentDao().insertAll(recipe);
                            if (recipe.timestamp.after(d))
                            {
                                d = recipe.timestamp;
                            }
                        }
                        SharedPreferences.Editor edit = MyApplication.context.getSharedPreferences("TAG", MODE_PRIVATE).edit();
                        //edit.putLong("RecipesLastUpdateDate",lastUpdated);
                        edit.putString("RecipesLastUpdateDate",d.toString());
                        edit.commit();
                        return "";
                    }
                    @Override
                    protected void onPostExecute(String s) {
                        super.onPostExecute(s);
                        if (listener!=null)  listener.onComplete();
                    }
                }.execute("");
            }
        });
    }
    
}
