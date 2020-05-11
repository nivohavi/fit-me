package com.colman.fit_me.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.colman.fit_me.firebase.FirebaseQueryLiveData;
import com.colman.fit_me.firebase.FirestoreManager;
import com.colman.fit_me.model.Recipe;
import com.colman.fit_me.sql.RecipeRepository;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class RecipeViewModel extends AndroidViewModel {

    private RecipeRepository mRepository;
    private LiveData<List<Recipe>> mAllRecipes;
    private MutableLiveData<List<Recipe>> recipes;
    private FirestoreManager firestoreManager;

    private static final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("recipes");
    private final FirebaseQueryLiveData liveData = new FirebaseQueryLiveData(mDatabase);

    public RecipeViewModel (Application application)
    {
        super(application);
        mRepository = new RecipeRepository(application);
        mAllRecipes = mRepository.getAllRecipes();
        firestoreManager = FirestoreManager.newInstance();
    }

    public RecipeViewModel (){
        super(null);
    }

    public LiveData<List<Recipe>> getRecipes() {
        if (recipes == null) {
            recipes = new MutableLiveData<List<Recipe>>();
            loadRecipes();
        }
        return recipes;
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
        mRepository.insert(recipe);
    }

    private void loadRecipes() {
        // Do an asynchronous operation to fetch users.
        firestoreManager.getAllRecipesFirebase(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<String> list = new ArrayList<>();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        list.add(document.getId());
                    }
                    Log.d("Niv", list.toString());
                } else {
                    Log.d("Niv", "Error getting documents: ", task.getException());
                }
            }
        });

    }
}
