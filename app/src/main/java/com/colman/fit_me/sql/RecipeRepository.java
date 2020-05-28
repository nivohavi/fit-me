package com.colman.fit_me.sql;

import android.app.Application;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.LiveData;

import com.colman.fit_me.firebase.FirestoreManager;
import com.colman.fit_me.model.Recipe;
import com.colman.fit_me.utils.Converters;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class RecipeRepository {

    private RecipeDao mRecipeDao;
    private LiveData<List<Recipe>> mAllRecipes;
    public static Date lud;
    private FirestoreManager firestoreManager;


    // Note that in order to unit test the RecipeRepository, you have to remove the Application
    // dependency. This adds complexity and much more code, and this sample is not about testing.
    // See the BasicSample in the android-architecture-components repository at
    // https://github.com/googlesamples
    public RecipeRepository(Application application) {
        lud = new Date();
        firestoreManager = FirestoreManager.newInstance();
/*        firestoreManager.getLastUpdateFirebase(new OnCompleteListener<DocumentSnapshot>() {
            int count =0;
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    // Document found in the offline cache
                    DocumentSnapshot document = task.getResult();
                    lud = Converters.fromTimestamp((Timestamp) document.get("timestamp"));
                } else {
                    Log.d("Niv", "Cached get failed: ", task.getException());
                }
            }
        });*/
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

    public LiveData<List<Recipe>> getAllRecipesByCategory()
    {
        return mAllRecipes;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public boolean recipeExists(String id)
    {
        AtomicBoolean returnValue = new AtomicBoolean(false);
        mAllRecipes.getValue().forEach(recipe -> {
            // TODO: fix bugs
            if(recipe.getId() == id)
            {
                returnValue.set(true);
            }
        });

        return returnValue.get();
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
