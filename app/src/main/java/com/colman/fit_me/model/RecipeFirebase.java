package com.colman.fit_me.model;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class RecipeFirebase {
    final static String RECIPE_COLLECTION = "recipes";

    public static void addRecipe(Recipe recipe, final RecipeModel.Listener<Boolean> listener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(RECIPE_COLLECTION).document(recipe.getId()).set(toJson(recipe)).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (listener!=null){
                    listener.onComplete(task.isSuccessful());
                }
            }
        });
    }

    public static void getAllRecipesSince(Date d, final RecipeModel.Listener<List<Recipe>> listener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Timestamp ts = new Timestamp(d);
        db.collection(RECIPE_COLLECTION).whereGreaterThanOrEqualTo("timestamp", ts)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                List<Recipe> recipesData = null;
                if (task.isSuccessful()){
                    recipesData = new LinkedList<Recipe>();
                    for(QueryDocumentSnapshot doc : task.getResult()){
                        Map<String,Object> json = doc.getData();
                        Recipe recipe = factory(json);
                        recipesData.add(recipe);
                    }
                }
                listener.onComplete(recipesData);
                Log.d("TAG","refresh " + recipesData.size());
            }
        });
    }


    public static void getAllRecipes(final RecipeModel.Listener<List<Recipe>> listener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(RECIPE_COLLECTION).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                List<Recipe> recipesData = null;
                if (task.isSuccessful()){
                    recipesData = new LinkedList<Recipe>();
                    for(QueryDocumentSnapshot doc : task.getResult()){
                        Recipe recipe = doc.toObject(Recipe.class);
                        recipesData.add(recipe);
                    }
                }
                listener.onComplete(recipesData);
            }
        });
    }
    


    private static Map<String, Object> toJson(Recipe recipe){
        HashMap<String, Object> result = new HashMap<>();


        result.put("id",recipe.getId());
        result.put("name",recipe.getName());
        result.put("category",recipe.getCategory());
        result.put("description",recipe.getDescription());
        result.put("directions", recipe.getDirections());
        result.put("ingredientsJson", recipe.getIngredientsJson());
        result.put("imgURL",recipe.getImgURL());
        result.put("timestamp",new Timestamp(new Date()));

        return result;
    }


    private static Recipe factory(Map<String, Object> json){
        Recipe recipe = new Recipe();
        recipe.id = (String)json.get("id");
        recipe.name = (String)json.get("name");
        recipe.imgURL = (String)json.get("imgUrl");

        recipe.category = (String)json.get("category");
        recipe.description = (String)json.get("description");
        recipe.ingredientsJson = (String)json.get("ingredientsJson");
        Timestamp ts = (Timestamp)json.get("timestamp");

        if (ts != null) recipe.timestamp = new Date();
        return recipe;
    }
}
