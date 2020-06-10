package com.colman.fit_me.firebase;

import android.os.Build;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.colman.fit_me.model.Recipe;
import com.colman.fit_me.sql.RecipeRepository;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ModelFirebase {

    private FirebaseFirestore fb = FirebaseFirestore.getInstance();
    private MutableLiveData<List<Recipe>> recipes;
    private FirestoreManager firestoreManager;
    private RecipeRepository mRepository;
    private CollectionReference recipesCollectionReference;


    private ModelFirebase()
    {
        recipesCollectionReference = FirebaseFirestore.getInstance().collection("recipes");

    }


    public interface GetAllRecipesListener{
        void onComplete(LiveData<List<Recipe>> data);
    }


    public void getAllRecipes(GetAllRecipesListener listener)
    {
        //recipesCollectionReference.orderBy("name", Query.Direction.DESCENDING).addSnapshotListener(listener);

    }




    private void loadRecipes() {
        Date d = RecipeRepository.lud;


        firestoreManager.getAllRecipesFirebase(new EventListener<QuerySnapshot>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException e) {
                if (e != null)
                {
                    Log.w("Niv", "Listen failed.", e);
                    return;
                }
                List<Recipe> list = new ArrayList<>();
                for (DocumentChange dc : value.getDocumentChanges())
                {
                    Recipe r = new Recipe(dc.getDocument().getData());
                    switch (dc.getType())
                    {
                        case ADDED:
                            mRepository.insert(r);
                            list.add(r);
                            break;
                        case MODIFIED:
                            mRepository.update(r);
                            list.forEach(recipe -> {
                                if(recipe.getId() == r.getId())
                                {
                                    recipe.setName(r.getName());
                                    recipe.setDescription(r.getDescription());
                                    recipe.setDirections(r.getDirections());
                                    recipe.setImgURL(r.getImgURL());
                                    recipe.setIngredientsJson(r.getIngredientsJson());
                                }
                            });
                            break;
                        case REMOVED:
                            mRepository.delete(r);
                            break;
                    }
                }

/*                for (QueryDocumentSnapshot document : value)
                {

                    Recipe r = new Recipe(document.getData());
                    //mRepository.recipeExists(r.getId());
                    if(r.getTimestamp().after(d))
                    {
                        // Insert to SQL (Room)
                        //mRepository.insert(r);
                        mRepository.insert(r);
                    }
                    list.add(r);
                }*/
                recipes.setValue(list);
            }
        });
    }
}
