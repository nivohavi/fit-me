package com.colman.fit_me.model;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.preference.PreferenceManager;

import androidx.lifecycle.LiveData;

import com.colman.fit_me.MainActivity;
import com.colman.fit_me.MyApplication;
import com.colman.fit_me.sql.RecipeRoomDatabase;

import static android.content.Context.MODE_PRIVATE;

import java.util.Date;
import java.util.List;

public class RecipeModel {
    public static final RecipeModel instance = new RecipeModel();
    SharedPreferences sharedPreferences;
    Context ctx;
    Long last;


    public interface Listener<T>{
        void onComplete(T data);
    }
    public interface CompListener{
        void onComplete();
    }
    private RecipeModel(){
        ctx = MainActivity.context;
        sharedPreferences = ctx.getSharedPreferences("TAG", MODE_PRIVATE);
    }

    public void newRecipeId(Listener<String> listener) {
        RecipeFirebase.newRecipeId(listener);
    }

    public void uploadImage(Uri uri, Listener<Uri> listener) {
        RecipeFirebase.uploadImage(uri,listener);
    }

    public void deleteRecipe(Recipe recipe,Listener<Boolean> listener) {
        RecipeFirebase.deleteRecipe(recipe,listener);
    }

    public void addRecipe(Recipe recipe,Listener<Boolean> listener) {
        RecipeFirebase.addRecipe(recipe,listener);
    }

    public void updateRecipe(Recipe recipe,Listener<Boolean> listener) {
        RecipeFirebase.updateRecipe(recipe,listener);
    }

    public LiveData<List<Recipe>> getAllRecipes(){
        LiveData<List<Recipe>> liveData = RecipeRoomDatabase.getDatabase(MainActivity.context).recipeDao().getAllRecipes();
        refreshRecipesList(null);
        return liveData;
    }

    public LiveData<List<Recipe>> getAllRecipesByUser(String email){
        LiveData<List<Recipe>> liveData = RecipeRoomDatabase.getDatabase(MainActivity.context).recipeDao().getAllRecipesByUser(email);
        refreshRecipesList(null);
        return liveData;
    }

    // TODO: ############## isDeleted ##############
    // TODO: add isDeleted property to Recipe


    public void delete(Recipe recipe,Listener<Boolean> listener){
        RecipeFirebase.deleteRecipe(recipe, new Listener<Boolean>() {
            @SuppressLint("StaticFieldLeak")
            @Override
            public void onComplete(Boolean data) {
                new AsyncTask<String,String,String>(){

                    @Override
                    protected String doInBackground(String... strings) {

                        // If to delete from local db or not
                        RecipeRoomDatabase.getDatabase(ctx).recipeDao().update(recipe);
                        return "";
                    }
                    @Override
                    protected void onPostExecute(String s) {
                        super.onPostExecute(s);
                        if (listener!=null)  listener.onComplete(true);
                    }
                }.execute("");
            }
        });
        //RecipeRoomDatabase.getDatabase(MainActivity.context).recipeDao().delete(recipe);

    }

    public void refreshRecipesList(final CompListener listener){

        last = sharedPreferences.getLong("RecipesLastUpdateDate",0);
        Date begining = new Date(0);
        RecipeFirebase.getAllRecipesSince(last,new Listener<List<Recipe>>() {
            @SuppressLint("StaticFieldLeak")
            @Override
            public void onComplete(final List<Recipe> data) {
                new AsyncTask<String,String,String>(){
                    @Override
                    protected String doInBackground(String... strings) {
                        Date d = new Date(0);
                        for(Recipe recipe : data)
                        {
                            if(!recipe.isDeleted())
                            {
                                RecipeRoomDatabase.getDatabase(ctx).recipeDao().insert(recipe);
                            }
                            else
                            {
                                RecipeRoomDatabase.getDatabase(ctx).recipeDao().delete(recipe.getId());
                                //RecipeRoomDatabase.getDatabase(ctx).recipeDao().updateIsDeleted(true,recipe.getId());

                                //RecipeRoomDatabase.getDatabase(MainActivity.context).recipeDao().update(recipe);
                            }
                            //RecipeRoomDatabase.getDatabase(ctx).recipeDao().insertAll(recipe);
                            //AppLocalDb.db.studentDao().insertAll(recipe);
                            //if (recipe.timestamp.getTime() > d.getTime())
                            //if ((recipe.timestamp.getTime() - d.getTime()) > Integer.parseInt(last))
                            if(recipe.timestamp.after(d) && !recipe.isDeleted())
                            {
                                last = recipe.timestamp.getTime();
                            }

                        }
                        SharedPreferences.Editor edit = sharedPreferences.edit();
                        //edit.putLong("RecipesLastUpdateDate",lastUpdated);
                        // edit.putLong("RecipesLastUpdateDate",d.getTime());
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
