package com.colman.fit_me.firebase;

import android.app.Application;
import android.os.Build;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.colman.fit_me.model.Recipe;
import com.colman.fit_me.sql.RecipeRepository;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class RecipeListLiveData extends LiveData<List<Recipe>> implements EventListener<DocumentSnapshot> {

    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private List<Recipe> recipeListTemp = new ArrayList<>();
    public MutableLiveData<List<Recipe>> recipesList = new MutableLiveData<>();
    private RecipeRepository mRepository;

    public RecipeListLiveData(Application application)
    {
        mRepository = new RecipeRepository(application);
    }


   /* @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException e) {
        recipeListTemp.clear();
        for (DocumentChange dc : value.getDocumentChanges())
        {
            Recipe r = new Recipe(dc.getDocument().getData());
            switch (dc.getType())
            {
                case ADDED:
                    mRepository.insert(r);
                    recipeListTemp.add(r);
                    break;
                case MODIFIED:
                    mRepository.update(r);
                    recipeListTemp.forEach(recipe -> {
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
        recipesList.setValue(recipeListTemp);
    }
*/






    @Override
    public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {

    }
}
