package com.colman.fit_me.firebase;

import com.colman.fit_me.model.Recipe;
import com.google.android.gms.tasks.OnCompleteListener;
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

    public void createDocument(Recipe recipe) {
        Map<String, Object> recipeMap = new HashMap<>();
        recipeMap.put("id", recipe.id );
        recipeMap.put("name", recipe.name);
        recipeMap.put("createdBy", "createdBy");
        recipeMap.put("category", "category");
        recipeMap.put("description", "description");
        recipeMap.put("ingredientsJson", "ingredientsJson");
        recipeMap.put("directions", "directions");
        recipeMap.put("imgURL", "imgURL");
        recipeMap.put("timestamp", new Date());
        recipesCollectionReference.add(recipeMap);
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
