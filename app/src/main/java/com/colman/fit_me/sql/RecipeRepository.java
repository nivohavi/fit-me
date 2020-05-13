package com.colman.fit_me.sql;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import com.colman.fit_me.LoginActivity;
import com.colman.fit_me.MainActivity;
import com.colman.fit_me.firebase.FirebaseQueryLiveData;
import com.colman.fit_me.model.Recipe;
import com.colman.fit_me.ui.recipes.NewRecipeFragment;
import com.colman.fit_me.ui.recipes.RecipeListFragment;
import com.colman.fit_me.viewmodel.RecipeViewModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class RecipeRepository {

    private RecipeDao mRecipeDao;
    private RecipeRoomDatabase recipeRoomDatabase;
    private LiveData<List<Recipe>> mAllRecipes;
    CollectionReference collectionReference;
    private FirebaseQueryLiveData firebaseQueryLiveData;
    private RecipeViewModel mRecipeViewModel;
    public static Date lud;
    DocumentReference newRecipe;



    // Note that in order to unit test the RecipeRepository, you have to remove the Application
    // dependency. This adds complexity and much more code, and this sample is not about testing.
    // See the BasicSample in the android-architecture-components repository at
    // https://github.com/googlesamples
    public RecipeRepository(Application application) {
        //lud = new Date();
        lud = new GregorianCalendar(2014, Calendar.FEBRUARY, 11).getTime();
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

    public LiveData<List<Recipe>> getAllRecipesByCategory()
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
}
