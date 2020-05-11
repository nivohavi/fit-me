package com.colman.fit_me.sql;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import com.colman.fit_me.firebase.FirebaseQueryLiveData;
import com.colman.fit_me.model.Recipe;
import com.colman.fit_me.ui.recipes.RecipeListFragment;
import com.colman.fit_me.viewmodel.RecipeViewModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.List;

public class RecipeRepository {

    private RecipeDao mRecipeDao;
    private RecipeRoomDatabase recipeRoomDatabase;
    private LiveData<List<Recipe>> mAllRecipes;
    CollectionReference collectionReference;
    private Date lud;
    private FirebaseQueryLiveData firebaseQueryLiveData;
    private RecipeViewModel mRecipeViewModel;
    private FirebaseFirestore mDatabase;



    // Note that in order to unit test the RecipeRepository, you have to remove the Application
    // dependency. This adds complexity and much more code, and this sample is not about testing.
    // See the BasicSample in the android-architecture-components repository at
    // https://github.com/googlesamples
    public RecipeRepository(Application application) {
        lud = new Date();
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
        // 1. Get from local DB the last update DateTime of specific (each Category is row from local DB)
        lud = RecipeRoomDatabase.RoomLastUpdate;
        // 2. Get all recipes By Category from Firebase newer than 'lud' timestamp
        // 3. If data != null
        // 3.1 push all recipes to SQL
        // 3.2 lud = newestRecipe.timestamp
        // 3.3 update SQL with new timestamp
        // 4. getAllRecipes() from SQL
        // 5. return getAllRecipes()
        return mAllRecipes;
    }

    // You must call this on a non-UI thread or your app will throw an exception. Room ensures
    // that you're not doing any long running operations on the main thread, blocking the UI.
    public void insert(final Recipe recipe) {

        // Add Recipe is only to FireBase - not to Room(SQL)
        //

        RecipeRoomDatabase.databaseWriteExecutor.execute(() -> {
            mRecipeDao.insert(recipe);
        });

    }
}
