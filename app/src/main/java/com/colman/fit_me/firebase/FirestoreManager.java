package com.colman.fit_me.firebase;

import android.util.Log;

import com.colman.fit_me.model.Recipe;
import com.colman.fit_me.sql.RecipeRepository;
import com.colman.fit_me.viewmodel.RecipeViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class FirestoreManager {

    private static FirestoreManager recipesFirestoreManager;
    private CollectionReference recipesCollectionReference;
    private CollectionReference lastupdateCollectionReference;
    private RecipeRepository mRepository;


    public static FirestoreManager newInstance() {
        if (recipesFirestoreManager == null) {
            recipesFirestoreManager = new FirestoreManager();
        }
        return recipesFirestoreManager;
    }

    private FirestoreManager() {
        recipesCollectionReference = FirebaseFirestore.getInstance().collection("recipes");
        lastupdateCollectionReference = FirebaseFirestore.getInstance().collection("lastupdate");
    }


    public void getAllRecipesFirebase(OnCompleteListener<QuerySnapshot> onCompleteListener)
    {
        recipesCollectionReference.get().addOnCompleteListener(onCompleteListener);


    }

    public void getAllRecipesFirebase(EventListener<QuerySnapshot> eventListener)
    {
        recipesCollectionReference.orderBy("name", Query.Direction.DESCENDING).addSnapshotListener(eventListener);
    }

    public void getLastUpdateFirebase(OnCompleteListener<DocumentSnapshot> onCompleteListener)
    {
        lastupdateCollectionReference.document("lud").get().addOnCompleteListener(onCompleteListener);
    }

    public void delete(String id, RecipeViewModel.MyCallback callback)
    {

        recipesCollectionReference.document(id).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                mRepository.deleteAllRecipes();
                callback.onDataGot("Deleted");
            }
        });
    }

/*    public void updateRecipeFirebase(Recipe recipe) {
        String documentId = recipe.getId();
        DocumentReference documentReference = recipesCollectionReference.document(documentId);
        documentReference.set(recipe);
    }

    public void deleteRecipe(String documentId) {
        DocumentReference documentReference = recipesCollectionReference.document(documentId);
        documentReference.delete();
    }*/
}
