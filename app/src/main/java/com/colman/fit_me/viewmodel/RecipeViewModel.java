package com.colman.fit_me.viewmodel;

import android.app.Application;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.RoomDatabase;

import com.colman.fit_me.firebase.FirebaseQueryLiveData;
import com.colman.fit_me.firebase.FirestoreManager;
import com.colman.fit_me.model.Recipe;
import com.colman.fit_me.sql.RecipeRepository;
import com.colman.fit_me.sql.RecipeRoomDatabase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RecipeViewModel extends AndroidViewModel {

    private FirebaseFirestore fb;
    private RecipeRepository mRepository;
    private LiveData<List<Recipe>> mAllRecipes;
    private MutableLiveData<List<Recipe>> recipes;
    private FirestoreManager firestoreManager;

    private static final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("recipes");
    private final FirebaseQueryLiveData liveData = new FirebaseQueryLiveData(mDatabase);

    public RecipeViewModel (Application application)
    {
        super(application);
        fb = FirebaseFirestore.getInstance();
        mRepository = new RecipeRepository(application);
        mAllRecipes = mRepository.getAllRecipes();
        firestoreManager = FirestoreManager.newInstance();
    }

    public RecipeViewModel (){
        super(null);
    }


    @NonNull
    public LiveData<DataSnapshot> getdataSnapshotLiveData(){
        return liveData;
    }


    public LiveData<List<Recipe>> getAllRecipes()
    {
        return mAllRecipes;
    }

    public void insert(Recipe recipe)
    {
        //
        // Add Recipe is only to FireBase - not to Room(SQL)
        //
        fb.collection("recipes").document().set(recipe).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("Niv", "XXXXXXXXXXXXXX -----  Success ----------- XXXXXXXXX");
                RecipeRepository.lud = new Date();
            }
        });

        //mRepository.insert(recipe);
    }

    public void delete(String id)
    {
        //
        // Add Recipe is only to FireBase - not to Room(SQL)
        //
        fb.collection("recipes").document(id).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("Niv", "XXXXXXXXXXXXXX -----  Success ----------- XXXXXXXXX");
                RecipeRepository.lud = new Date();
            }
        });

        //mRepository.insert(recipe);
    }


    public LiveData<List<Recipe>> getRecipes() {
        if (recipes == null) {
            recipes = new MutableLiveData<List<Recipe>>();
            loadRecipes();
        }
        //return recipes;
        return mAllRecipes;
    }

    public LiveData<List<Recipe>> getRecipesByCategory(String category) {
        if (recipes == null) {
            recipes = new MutableLiveData<List<Recipe>>();
            //loadRecipesByCategory(category);
            loadRecipes();
        }
        //return recipes;
        //TODO: return query
        //recipes.getValue().removeIf(recipe -> (!recipe.getCategory().equals(category)));

        return mAllRecipes;
    }

    private void loadRecipes() {

        // 1. Get from local DB the last update DateTime of specific (each Category is row from local DB)
        Date d = RecipeRepository.lud;
        // 2. Get all recipes By Category from Firebase newer than 'lud' timestamp
        // 3. If data != null
        // 3.1 push all recipes to SQL
        // 3.2 lud = newestRecipe.timestamp
        // 3.3 update SQL with new timestamp
        // 4. getAllRecipes() from SQL
        // 5. return getAllRecipes()

        // Do an asynchronous operation to fetch users.
        firestoreManager.getAllRecipesFirebase(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<Recipe> list = new ArrayList<>();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Recipe r = new Recipe(document.getData());
                        if(r.getTimestamp().after(d))
                        {
                            // Insert to SQL (Room)
                            mRepository.insert(r);
                        }
                        list.add(r);
                    }
                    recipes.setValue(list);
                    Log.d("Niv", list.toString());
                } else {
                    Log.d("Niv", "Error getting documents: ", task.getException());
                }
            }
        });
    }
}
