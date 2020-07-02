package com.colman.fit_me.sql;
import android.os.Build;
import android.app.Application;

import androidx.lifecycle.LiveData;

import com.colman.fit_me.model.Recipe;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class RecipeRepository {

    private RecipeDao mRecipeDao;
    private LiveData<List<Recipe>> mAllRecipes;
    public static Date lud;
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();


    // Note that in order to unit test the RecipeRepository, you have to remove the Application
    // dependency. This adds complexity and much more code, and this sample is not about testing.
    // See the BasicSample in the android-architecture-components repository at
    // https://github.com/googlesamples
    public RecipeRepository(Application application) {
        lud = new Date();
        lud = new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime();
        RecipeRoomDatabase db = RecipeRoomDatabase.getDatabase(application);
        mRecipeDao = db.recipeDao();
        mAllRecipes = mRecipeDao.getAllRecipes();
    }

    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    public LiveData<List<Recipe>> getAllRecipes()
    {
        return mAllRecipes;
    }

    // You must call this on a non-UI thread or your app will throw an exception. Room ensures
    // that you're not doing any long running operations on the main thread, blocking the UI.
    public void insert(final Recipe recipe) {
        RecipeRoomDatabase.databaseWriteExecutor.execute(() -> {
            mRecipeDao.insert(recipe);
        });
    }

    public void delete(final Recipe recipe) {
        RecipeRoomDatabase.databaseWriteExecutor.execute(() -> {
            mRecipeDao.delete(recipe);
        });
    }

    public void update(final Recipe recipe) {
        RecipeRoomDatabase.databaseWriteExecutor.execute(() -> {
            mRecipeDao.update(recipe);
        });
    }

    public void delete(final String id) {
        RecipeRoomDatabase.databaseWriteExecutor.execute(() -> {
            mRecipeDao.delete(id);
        });
    }

    public void deleteAllRecipes() {
        RecipeRoomDatabase.databaseWriteExecutor.execute(() -> {
            mRecipeDao.deleteAll();
        });
    }

}
